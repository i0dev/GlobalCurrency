package com.i0dev.plugin.globalcurrency.manager;

import com.i0dev.plugin.globalcurrency.GlobalCurrencyPlugin;
import com.i0dev.plugin.globalcurrency.template.AbstractManager;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.UUID;

public class SQLManager extends AbstractManager {

    @Getter
    private static final SQLManager instance = new SQLManager();

    @Getter
    private Connection connection;

    @SneakyThrows
    @Override
    public void deinitialize() {
        if (connection != null) connection.close();
    }


    public boolean connect() {
        FileConfiguration cnf = GlobalCurrencyPlugin.getPlugin().cnf();

        String address = cnf.getString("database.address");
        int port = cnf.getInt("database.port");
        String database = cnf.getString("database.database");
        String username = cnf.getString("database.username");
        String password = cnf.getString("database.password");

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/" + database, username, password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SneakyThrows
    public void makeTable() {
        String SQL = "CREATE TABLE IF NOT EXISTS currency ("
                + "`uuid`   VARCHAR(36) NOT NULL,"
                + "`amount` BIGINT      NOT NULL,"
                + "PRIMARY KEY (`uuid`)"
                + ")";
        connection.createStatement().execute(SQL);
    }

    @SneakyThrows
    public boolean isInTable(UUID uuid) {
        ResultSet result = connection.createStatement().executeQuery("SELECT * FROM currency WHERE uuid='" + uuid + "'");
        return result.next();
    }

    @SneakyThrows
    public long getAmount(UUID uuid) {
        ResultSet result = connection.createStatement().executeQuery("SELECT * FROM currency WHERE uuid='" + uuid + "'");
        if (!result.next()) return 0;
        return result.getLong("amount");
    }

    @SneakyThrows
    public void addAmount(UUID uuid, long amount) {
        long currentBalance = getAmount(uuid);
        long newBalance = currentBalance + amount;
        setAmount(uuid, newBalance);
    }

    @SneakyThrows
    public void removeAmount(UUID uuid, long amount) {
        long currentBalance = getAmount(uuid);
        long newBalance = currentBalance - amount;
        setAmount(uuid, newBalance);
    }

    @SneakyThrows
    public void setAmount(UUID uuid, long amount) {
        if (!isInTable(uuid))
            connection.createStatement().execute("INSERT INTO currency VALUES ('" + uuid + "', " + amount + ")");
        else
            connection.createStatement().execute("UPDATE currency SET amount =" + amount + " WHERE uuid='" + uuid + "'");
    }

}
