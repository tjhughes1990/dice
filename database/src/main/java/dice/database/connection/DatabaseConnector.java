package dice.database.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import dice.common.DiceException;

/**
 * Infer database connection parameters, and connect to the PostgreSQL database.
 */
public class DatabaseConnector {

    private static final String POSTGRES_PREFIX = "POSTGRES_";
    private static final String PORT_PROP_NAME = POSTGRES_PREFIX + "PORT";
    private static final String USER_PROP_NAME = POSTGRES_PREFIX + "USER";
    private static final String PASS_PROP_NAME = POSTGRES_PREFIX + "PASSWORD";
    private static final String DB_PROP_NAME = POSTGRES_PREFIX + "DB";

    private static final String DRIVER_NAME = "org.postgresql.Driver";

    private final String propsPath;

    /**
     * Constructor.
     *
     * @param propsPath
     *            the path to the properties file to load.
     */
    public DatabaseConnector(final String propsPath) {
        this.propsPath = propsPath;
    }

    /**
     * Connect to the database using properties specified in the resources file.
     *
     * @return a connection to the database.
     *
     * @throws DiceException
     *             if the connection to the database could not be established.
     */
    public Connection connect() throws DiceException {
        // Check that the driver is on the classpath.
        try {
            Class.forName(DRIVER_NAME);
        } catch (final ClassNotFoundException e) {
            throw new DiceException("No database driver found");
        }

        // Load the properties file.
        final ClassLoader cl = DatabaseConnector.class.getClassLoader();
        final InputStream is = cl.getResourceAsStream(propsPath);

        final Properties props = new Properties();
        try {
            props.load(is);
        } catch (final IOException e) {
            throw new DiceException("Failed to load properties from resources", e);
        }

        // Make the connection.
        final String url = "jdbc:postgresql://postgres:" + props.getProperty(PORT_PROP_NAME) + "/"
                + props.getProperty(DB_PROP_NAME);
        final String user = props.getProperty(USER_PROP_NAME);
        final String password = props.getProperty(PASS_PROP_NAME);
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (final SQLException e) {
            throw new DiceException("Failed to connect to the database", e);
        }
    }
}
