package com.i0dev.engine;

import com.i0dev.entity.MConf;
import com.i0dev.entity.object.DatabaseInformation;
import com.massivecraft.massivecore.Engine;
import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.UUID;

public class EngineSQL extends Engine {
    private static EngineSQL i = new EngineSQL();

    public static EngineSQL get() {
        return i;
    }

    @Getter
    private Connection connection;

    public boolean connect() {
        DatabaseInformation info = MConf.get().databaseInformation;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + info.getAddress() + ":" + info.getPort() + "/" + info.getDatabase(), info.getUsername(), info.getPassword());
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
