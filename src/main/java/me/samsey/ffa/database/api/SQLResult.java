package me.samsey.ffa.database.api;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLResult implements AutoCloseable {

    private final Statement statement;
    private final ResultSet resultSet;

    public SQLResult(Statement statement, ResultSet resultSet) {
        this.statement = statement;
        this.resultSet = resultSet;
    }

    @Override
    public void close() throws SQLException {
        statement.close();
    }

    public boolean absolute(int row) throws SQLException {
        return resultSet.absolute(row);
    }

    public void afterLast() throws SQLException {
        resultSet.afterLast();
    }

    public void beforeFirst() throws SQLException {
        resultSet.beforeFirst();
    }

    public int findColumn(String columnLabel) throws SQLException {
        return resultSet.findColumn(columnLabel);
    }

    public boolean first() throws SQLException {
        return resultSet.first();
    }

    public Array getArray(int columnIndex) throws SQLException {
        return resultSet.getArray(columnIndex);
    }

    public Array getArray(String columnLabel) throws SQLException {
        return resultSet.getArray(columnLabel);
    }

    public boolean getBoolean(int columnIndex) throws SQLException {
        return resultSet.getBoolean(columnIndex);
    }

    public boolean getBoolean(String columnLabel) throws SQLException {
        return resultSet.getBoolean(columnLabel);
    }

    public double getDouble(int columnIndex) throws SQLException {
        return resultSet.getDouble(columnIndex);
    }

    public double getDouble(String columnLabel) throws SQLException {
        return resultSet.getDouble(columnLabel);
    }

    public float getFloat(int columnIndex) throws SQLException {
        return resultSet.getFloat(columnIndex);
    }

    public float getFloat(String columnLabel) throws SQLException {
        return resultSet.getFloat(columnLabel);
    }

    public int getInt(int columnIndex) throws SQLException {
        return resultSet.getInt(columnIndex);
    }

    public int getInt(String columnLabel) throws SQLException {
        return resultSet.getInt(columnLabel);
    }

    public long getLong(int columnIndex) throws SQLException {
        return resultSet.getLong(columnIndex);
    }

    public long getLong(String columnLabel) throws SQLException {
        return resultSet.getLong(columnLabel);
    }

    public short getShort(int columnIndex) throws SQLException {
        return resultSet.getShort(columnIndex);
    }

    public short getShort(String columnLabel) throws SQLException {
        return resultSet.getShort(columnLabel);
    }

    public String getString(int columnIndex) throws SQLException {
        return resultSet.getString(columnIndex);
    }

    public String getString(String columnLabel) throws SQLException {
        return resultSet.getString(columnLabel);
    }

    public int getRow() throws SQLException {
        return resultSet.getRow();
    }

    public void insertRow() throws SQLException {
        resultSet.insertRow();
    }

    public boolean isAfterLast() throws SQLException {
        return resultSet.isAfterLast();
    }

    public boolean isBeforeFirst() throws SQLException {
        return resultSet.isBeforeFirst();
    }

    public boolean isFirst() throws SQLException {
        return resultSet.isFirst();
    }

    public boolean isLast() throws SQLException {
        return resultSet.isLast();
    }

    public boolean last() throws SQLException {
        return resultSet.last();
    }

    public boolean next() throws SQLException {
        return resultSet.next();
    }

    public boolean previous() throws SQLException {
        return resultSet.previous();
    }

    public boolean wasNull() throws SQLException {
        return resultSet.wasNull();
    }



}
