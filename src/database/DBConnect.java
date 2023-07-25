package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**Connection to the database*/
public class DBConnect {
    private static PreparedStatement preparedStatement;
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection connection;  // Connection Interface

    /**
     * Opens database connection.
     * @return
     */
    public static Connection openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
        return connection;
    }

    /**
     * Closes database connection.
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     * Maintains/gets the connection.
     * @return connection
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * @param connection sets connection
     * @param sqlCommand sets sqlCommand
     * @throws SQLException
     */
    public static void setPreparedStatement(Connection connection, String sqlCommand) throws SQLException {
        preparedStatement = connection.prepareStatement(sqlCommand);
    }

    /**
     * @return preparedStatement
     */
    public static PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }
}
