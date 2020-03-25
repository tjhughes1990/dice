package dice.database.application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import dice.common.DiceException;
import dice.common.types.DiceRollCollection;
import dice.common.types.DiceRollType;
import dice.common.types.IdName;
import dice.database.connection.DatabaseConnector;

@RestController
public class DatabaseController {

    private static final Log LOG = LogFactory.getLog(DatabaseController.class);

    private static final String DB_PROPS_PATH = "database.env";
    private static final DatabaseConnector DB_CONNECTOR = new DatabaseConnector(DB_PROPS_PATH);

    private static final String COLLECTION_TABLE_NAME = "dice.dice_collection";
    private static final String DICE_TABLE_NAME = "dice.dice";
    private static final String COLLECTION_ID_COL = "id";
    private static final String COLLECTION_NAME_COL = "name";
    private static final List<String> DICE_COL_NAMES = List.of("id", "collection_id", "name", "min_result",
            "max_result", "roll_number");

    private static final String DELIM = ",";
    private static final Set<String> RESTRICTED_CHARS = Set.of(";", "*", "\\", "#");

    private final Connection connection;

    /**
     * Constructor.
     *
     * @throws DiceException
     *             if a connection to the database could not be established.
     */
    public DatabaseController() throws DiceException {
        connection = DB_CONNECTOR.connect();
    }

    /**
     * Get a list of dice collections available in the database.
     *
     * @return a set of {@link IdName} objects corresponding to collections in the database.
     */
    @GetMapping(value = "getCollections", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<IdName> getCollections() {
        final String sql = "SELECT " + String.join(DELIM, List.of(COLLECTION_ID_COL, COLLECTION_NAME_COL)) + " FROM "
                + COLLECTION_TABLE_NAME;
        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            final ResultSet rs = statement.executeQuery();
            final Set<IdName> idNameList = new HashSet<>();
            while (rs.next()) {
                idNameList.add(new IdName(rs.getLong(1), rs.getString(2)));
            }

            return idNameList;
        } catch (final SQLException e) {
            final String errMsg = "Failed to read dice collections from database";
            LOG.error(errMsg, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, e);
        }
    }

    /**
     * Save a new dice collection to the database.
     *
     * @param diceRollCollection
     *            the dice collection to save.
     */
    @PostMapping(value = "saveDice", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveDice(@RequestBody(required = true) final DiceRollCollection diceRollCollection) {
        validateDiceCollection(diceRollCollection);

        final String collectionSql = "INSERT INTO " + COLLECTION_TABLE_NAME + " (" + COLLECTION_NAME_COL
                + ") VALUES (?);";
        final long id;
        try (final PreparedStatement statement = connection.prepareStatement(collectionSql,
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, diceRollCollection.getName());
            statement.executeUpdate();
            final ResultSet keyResultSet = statement.getGeneratedKeys();
            if (!keyResultSet.next()) {
                throw new SQLException("Failed to insert new dice collection to database");
            }

            id = keyResultSet.getLong(1);
        } catch (final SQLException e) {
            final String errMsg = "Failed to save dice collection to database";
            LOG.error(errMsg, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, e);
        }

        // Insert individual dice.
        final String diceSql = "INSERT INTO " + DICE_TABLE_NAME + " ("
                + String.join(DELIM, DICE_COL_NAMES.subList(1, DICE_COL_NAMES.size())) + ") VALUES (?,?,?,?,?);";
        try (final PreparedStatement statement = connection.prepareStatement(diceSql)) {
            for (final DiceRollType diceRoll : diceRollCollection.getDiceRolls()) {
                statement.setLong(1, id);
                statement.setString(2, diceRoll.getName());
                statement.setInt(3, diceRoll.getMinResult());
                statement.setInt(4, diceRoll.getMaxResult());
                statement.setInt(5, diceRoll.getRollNumber());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (final SQLException e) {
            final String errMsg = "Failed to save dice collection to database";
            LOG.error(errMsg, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, e);
        }
    }

    /**
     * Check that the dice collection being added is valid.
     *
     * @param diceRollCollection
     *            the dice collection to validate.
     *
     * @throws ResponseStatusException
     *             if the collection is invalid.
     */
    private static void validateDiceCollection(final DiceRollCollection diceRollCollection)
            throws ResponseStatusException {
        if (diceRollCollection == null) {
            final String errMsg = "Collection was null";
            LOG.error(errMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
        }

        final String name = diceRollCollection.getName();
        if (name == null || name.isEmpty() || name.isBlank()) {
            final String errMsg = "Collection name was empty or null";
            LOG.error(errMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
        } else if (RESTRICTED_CHARS.parallelStream().anyMatch(c -> name.contains(c))) {
            final String errMsg = "Collection contained illegal characters";
            LOG.error(errMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
        }

        final List<DiceRollType> diceRolls = diceRollCollection.getDiceRolls();
        if (diceRolls == null || diceRolls.isEmpty()) {
            final String errMsg = "Collection contained no rolls";
            LOG.error(errMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
        }
    }

    /**
     * Delete a dice collection from the database.
     *
     * @param id
     *            the collection ID to remove from the database.
     *
     * @throws DiceException
     *             if the collection could not be removed.
     */
    @GetMapping(value = "/deleteDice")
    public void deleteDice(@RequestParam(value = COLLECTION_ID_COL, required = true) final long id)
            throws DiceException {
        final String sql = "DELETE FROM " + COLLECTION_TABLE_NAME + " WHERE " + COLLECTION_ID_COL + " = " + id;
        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (final SQLException e) {
            throw new DiceException("Failed to delete collection", e);
        }
    }

    /**
     * Load a set of dice from the database.
     *
     * @param id
     *            the ID of the dice collection to load.
     *
     * @return the collection of dice.
     */
    @GetMapping(value = "loadDice", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DiceRollType> loadDice(@RequestParam(value = COLLECTION_ID_COL, required = true) final long id) {
        final String sql = "SELECT " + String.join(DELIM, DICE_COL_NAMES.subList(2, DICE_COL_NAMES.size())) + " FROM "
                + DICE_TABLE_NAME + " WHERE " + DICE_COL_NAMES.get(1) + " = " + id;

        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            final ResultSet rs = statement.executeQuery();
            final List<DiceRollType> diceRolls = new ArrayList<>();
            long i = 0;
            while (rs.next()) {
                final String name = rs.getString(1);
                final int minResult = rs.getInt(2);
                final int maxResult = rs.getInt(3);
                final int rollNumber = rs.getInt(4);

                final String diceName = name == null ? Long.valueOf(i).toString() : name;
                final DiceRollType drt = new DiceRollType(i, diceName, minResult, maxResult, rollNumber);
                diceRolls.add(drt);
                i++;
            }

            return diceRolls;
        } catch (final SQLException | DiceException e) {
            final String errMsg = "Failed to load dice collection from database";
            LOG.error(errMsg, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, e);
        }
    }
}
