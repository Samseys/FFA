package me.samsey.ffa.database.api.type;

import me.samsey.ffa.FFA;
import me.samsey.ffa.database.api.Database;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLite extends Database {

    public void connect() throws SQLException {
        File dataFolder = new File(FFA.getInstance().getDataFolder(), "database.db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                Logger.getLogger("SQL").log(Level.SEVERE, "File write error: database.db");
            }
        }
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Could not load driver class com.mysql.jdbc.Driver", e);
        }

        connection = DriverManager.getConnection("jdbc:sqlite:"+ dataFolder);
    }

    @Override
    public boolean isConnectionValid() {
        return true;
    }
}
