package dice.database.application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import dice.common.DiceException;
import dice.common.types.DiceRollType;
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

    @PostMapping(value = "saveDice",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveDice(final List<DiceRollType> diceList) {
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

    @GetMapping(value = "loadDice", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DiceRollType> loadDice(@RequestParam(value = "id", required = true) final long id) {
        // TODO
        final String sql = "";
        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            // TODO
            System.out.println("LOAD DICE");
        } catch (final SQLException e) {
            final String errMsg = "Failed to load dice collection from database";
            LOG.error(errMsg, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errMsg, e);
        }
        return Collections.emptyList();
    }
}
