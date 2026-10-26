
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.*;

public class DBConnection {

    private static final Logger logger = Logger.getLogger(DBConnection.class.getName());
    private static Connection connection = null;

    static {
        try {
            Properties props = new Properties();
            InputStream input = new FileInputStream("src/db.properties");
            props.load(input);

            String driver = props.getProperty("db.driver");
            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);

            logger.info("Database connection established successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Database connection failed", e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
