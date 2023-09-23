package me.samsey.ffa.database.api.type;

import me.samsey.ffa.database.api.Database;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL extends Database {

    private static final int TIMEOUT = 8;
    private String host;
    private int port;
    private String database;
    private String user;
    private String password;


    /**
     * Instantiate the object, doesn't connect yet.
     */
    public MySQL(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Connects to the database.
     */
    public void connect() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Could not load driver class com.mysql.jdbc.Driver", e);
        }

        connection = DriverManager.getConnection(
                "jdbc:mysql://" + host + ":" + port + "/" + database +							// URI
                        "?useSSL=false",																// Disable warning
//        		"&connectTimeout=" + (TIMEOUT * 1000) + "&socketTimeout=" + (TIMEOUT * 1000), 	// Query
                user, password); 																// Authentication
    }

    /**
     *  Checks if the connection is still valid. Useful for refreshing.
     */
    public boolean isConnectionValid() {
        if (connection == null) {
            return false;
        }

        try {
            return connection.isValid(TIMEOUT);
        } catch (SQLException e) {
            return false;
        }
    }
}
