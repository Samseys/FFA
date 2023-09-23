package me.samsey.ffa.database.api;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Database {

    protected Connection connection;

    /**
     * Instantiate the object, doesn't connect yet.
     */
    public Database() {}

    public Connection getConnection() {
        return connection;
    }

    /**
     * Connects to the database.
     */
    public abstract void connect() throws SQLException;


    /**
     * Closes the active connection.
     */
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            }
            catch (SQLException ex) { }
        }
    }

    public abstract boolean isConnectionValid();

    /**
     * Prepares a query.
     */
    public SQLResult preparedQuery(String sql, Object... parameters) throws SQLException {
        PreparedStatement statement = null;

        try {
            checkNullConnection();
            statement = prepareWithParameters(connection, sql, false, parameters);
            ResultSet resultSet = statement.executeQuery();
            return new SQLResult(statement, resultSet);

        } catch (SQLException e) {
            // statement va chiuso solo in caso di errore, altrimenti deve chiuderlo l'utente
            handleSQLException(statement, sql, e);
            throw e;
        }
    }


    /**
     * Prepares an update.
     */
    public int preparedUpdate(String sql, Object... parameters) throws SQLException {
        try {
            checkNullConnection();
            try (PreparedStatement statement = prepareWithParameters(connection, sql, false, parameters)) {
                int rowCount = statement.executeUpdate();
                return rowCount;
            }
            // Qui va sempre chiuso

        } catch (SQLException e) {
            handleSQLException(null, sql, e);
            throw e;
        }
    }


    /**
     * Prepares an update and returns auto generated keys.
     */
    public SQLResult preparedUpdateAndKeys(String sql, Object... parameters) throws SQLException {
        PreparedStatement statement = null;

        try {
            checkNullConnection();
            statement = prepareWithParameters(connection, sql, true, parameters);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            return new SQLResult(statement, resultSet);

        } catch (SQLException e) {
            handleSQLException(statement, sql, e);
            throw e;
        }
    }


    /**
     * Executes an update.<br>
     * <b>Warning!</b> Use a prepared statement if there are user inputs, to avoid SQL injection.
     */
    public int update(String sql) throws SQLException {
        try {
            checkNullConnection();
            try (Statement statement = connection.createStatement()) {
                int rowCount = statement.executeUpdate(sql);
                return rowCount;
            }
            // Qui va sempre chiuso

        } catch (SQLException e) {
            handleSQLException(null, sql, e);
            throw e;
        }
    }


    /**
     * Executes an update and auto generated keys.<br>
     * <b>Warning!</b> Use a prepared statement if there are user inputs, to avoid SQL injection.
     */
    public SQLResult updateAndKeys(String sql) throws SQLException {
        Statement statement = null;

        try {
            checkNullConnection();
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            ResultSet resultSet = statement.getGeneratedKeys();
            return new SQLResult(statement, resultSet);

        } catch (SQLException e) {
            handleSQLException(statement, sql, e);
            throw e;
        }
    }


    /**
     * Queries the database.<br>
     * <b>Warning!</b> Use a prepared statement if there are user inputs, to avoid SQL injection.
     */
    public SQLResult query(final String sql) throws SQLException {
        Statement statement = null;

        try {
            checkNullConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            return new SQLResult(statement, resultSet);

        } catch (SQLException e) {
            handleSQLException(statement, sql, e);
            throw e;
        }
    }


    public static String escapeLikeParameter(String s) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' || c == '%' || c == '_') {
                result.append('\\');
            }
            result.append(c);
        }

        return result.toString();
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Prepare a statement with parameters already set, returning auto generated keys.
     * Internal use, the MOST COMPLETE method.
     */
    private PreparedStatement prepareWithParameters(Connection connection, String sql, boolean returnGeneratedKeys, Object... parameters) throws SQLException {
        int parametersAmount = parameters != null ? parameters.length : 0;

        if (countOccurrences(sql, '?') != parametersAmount) {
            throw new SQLException("Amount of parameters doesn't match amount of values (question marks)");
        }

        PreparedStatement statement;
        if (returnGeneratedKeys) {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } else {
            statement = connection.prepareStatement(sql);
        }

        if (parametersAmount > 0) {
            int index = 1;
            for (Object o : parameters) {
                setParameter(statement, index, o);
                index++;
            }
        }

        return statement;
    }

    private void setParameter(PreparedStatement statement, int index, Object param) throws SQLException {
        if (param == null) {
            throw new SQLException("Parameter " + index + " for prepared statement cannot be null");
        }

        Class<?> clazz;
        Object value;

        if (param instanceof Nullable) {
            Nullable nullableParam = (Nullable) param;
            clazz = nullableParam.getNullType();
            value = nullableParam.getValue();

        } else {
            clazz = param.getClass();
            value = param;
        }

        if 		(clazz == int.class) 		statement.setInt(index, (int) value);
        else if (clazz == Integer.class)	statement.setInt(index, (Integer) value);
        else if (clazz == long.class) 		statement.setLong(index, (long) value);
        else if (clazz == Long.class) 		statement.setLong(index, (Long) value);
        else if (clazz == String.class) 	statement.setString(index, (String) value);
        else if (clazz == boolean.class) 	statement.setBoolean(index, (boolean) value);
        else if (clazz == Boolean.class) 	statement.setBoolean(index, (Boolean) value);
        else if (clazz == double.class) 	statement.setDouble(index, (double) value);
        else if (clazz == Double.class) 	statement.setDouble(index, (Double) value);
        else if (clazz == float.class) 		statement.setFloat(index, (float) value);
        else if (clazz == Float.class) 		statement.setFloat(index, (Float) value);
        else if (clazz == short.class) 		statement.setShort(index, (short) value);
        else if (clazz == Short.class) 		statement.setShort(index, (Short) value);
            // Add here Date, Timestamp, etc
        else throw new SQLException("Unknown or unsupported parameter type: " + clazz.getSimpleName());
    }


    private void handleSQLException(AutoCloseable closeable, String sql, SQLException sqlException) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ex) { }
        }
        Logger.getLogger("Database").log(Level.SEVERE, "SQL error (query: " + sql + "): " + sqlException.toString().replaceAll("\n{2,}", "\n"));
        if (!isConnectionValid()) {
            // Se è un errore di connessione (e non per esempio di query) proviamo a riconnettere
            try {
                connect();
                Logger.getLogger("SQL").log(Level.INFO, "Reconnection attempt succeeded!");
            } catch (SQLException e) {
                Logger.getLogger("SQL").log(Level.SEVERE, "Reconnection attempt failed.");
            }
        }
    }


    private void checkNullConnection() throws SQLException {
        if (connection == null) {
            throw new SQLException("Connection was null (either connect() was not called or failed)");
        }
    }


    private int countOccurrences(String haystack, char needle) {
        int count = 0;

        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needle) {
                count++;
            }
        }

        return count;
    }
}
