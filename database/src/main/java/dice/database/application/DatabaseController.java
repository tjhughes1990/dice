package dice.database.application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        final String sql = "SELECT id, name FROM dice.dice_collection";
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
    public void saveDice(@RequestBody final DiceRollCollection diceRollCollection) {
        // TODO
        final String sql = "";
        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            System.out.println("SAVE DICE");
        } catch (final SQLException e) {
            final String errMsg = "Failed to save dice collection to database";
            LOG.error(errMsg, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, e);
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
    public void deleteDice(@RequestParam(value = "id", required = true) final long id) throws DiceException {
        // TODO
        final String sql = "DELETE FROM dice.dice_collection WHERE id = " + id;
        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeQuery();
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
    public List<DiceRollType> loadDice(@RequestParam(value = "id", required = true) final long id) {
        final String sql = "SELECT min_result, max_result, roll_number FROM dice.dice WHERE collection_id = " + id;
        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            final ResultSet rs = statement.executeQuery();
            final List<DiceRollType> diceRolls = new ArrayList<>();
            while (rs.next()) {
                final DiceRollType drt = new DiceRollType();
                drt.setMinResult(rs.getInt(1));
                drt.setMaxResult(rs.getInt(2));
                drt.setRollNumber(rs.getInt(3));

                diceRolls.add(drt);
            }

            return diceRolls;
        } catch (final SQLException e) {
            final String errMsg = "Failed to load dice collection from database";
            LOG.error(errMsg, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, e);
        }
    }
}
