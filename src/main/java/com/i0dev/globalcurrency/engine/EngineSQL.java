package com.i0dev.globalcurrency.engine;

import com.i0dev.globalcurrency.entity.MConf;
import com.i0dev.globalcurrency.entity.object.DatabaseInformation;
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
            lastConnectionTime = System.currentTimeMillis();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SneakyThrows
    public void makeTables() {
        String SQL = "CREATE TABLE IF NOT EXISTS currency ("
                + "`uuid`   VARCHAR(36) NOT NULL,"
                + "`amount` BIGINT      NOT NULL,"
                + "PRIMARY KEY (`uuid`)"
                + ")";
        connection.createStatement().execute(SQL);
        SQL = "CREATE TABLE IF NOT EXISTS logs (" +
                "`id` INT NOT NULL AUTO_INCREMENT," +
                "`uuid` VARCHAR(36) NOT NULL," +
                "`time` BIGINT NOT NULL," +
                "`item-id` VARCHAR(36) NOT NULL," +
                "`server-id` VARCHAR(36) NOT NULL," +
                "`price` BIGINT NOT NULL," +
                "PRIMARY KEY (`id`)" +
                ")";
        connection.createStatement().execute(SQL);
    }

    private long lastConnectionTime = 0;

    private void reconnect() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
                return;
            }
        } catch (Exception ignored) {
            connect();
            return;
        }

        long reconnectToDatabaseEveryMillis = MConf.get().databaseInformation.getReconnectToDatabaseEveryMillis();
        if (System.currentTimeMillis() - lastConnectionTime < reconnectToDatabaseEveryMillis) return;
        lastConnectionTime = System.currentTimeMillis();
    }

    @SneakyThrows
    public boolean isInTable(UUID uuid) {
        reconnect();
        ResultSet result = connection.createStatement().executeQuery("SELECT * FROM currency WHERE uuid='" + uuid + "'");
        return result.next();
    }

    @SneakyThrows
    public long getAmount(UUID uuid) {
        reconnect();
        ResultSet result = connection.createStatement().executeQuery("SELECT * FROM currency WHERE uuid='" + uuid + "'");
        if (!result.next()) return 0;
        return result.getLong("amount");
    }

    @SneakyThrows
    public void addAmount(UUID uuid, long amount) {
        reconnect();

        if (uuid == null) {
            return;
        }

        long currentBalance = getAmount(uuid);
        long newBalance = currentBalance + amount;
        setAmount(uuid, newBalance);
    }

    @SneakyThrows
    public void removeAmount(UUID uuid, long amount) {
        reconnect();
        long currentBalance = getAmount(uuid);
        long newBalance = currentBalance - amount;
        setAmount(uuid, newBalance);
    }

    @SneakyThrows
    public void setAmount(UUID uuid, long amount) {
        reconnect();
        if (!isInTable(uuid))
            connection.createStatement().execute("INSERT INTO currency VALUES ('" + uuid + "', " + amount + ")");
        else
            connection.createStatement().execute("UPDATE currency SET amount =" + amount + " WHERE uuid='" + uuid + "'");
    }

    @SneakyThrows
    public void logPurchase(UUID uuid, String itemID, long price) {
        reconnect();
        long time = System.currentTimeMillis();
        String serverID = MConf.get().serverID;
        connection.createStatement().execute("INSERT INTO logs (uuid, time, `item-id`, `server-id`, price) VALUES ('" + uuid + "', " + time + ", '" + itemID + "', '" + serverID + "', " + price + ")");
    }

    @SneakyThrows
    public int getPurchases(UUID uuid, String itemID, long millisAgoToCheck) {
        reconnect();
        long time = System.currentTimeMillis() - millisAgoToCheck;
        ResultSet result = connection.createStatement().executeQuery("SELECT * FROM logs WHERE uuid='" + uuid + "' AND `item-id`='" + itemID + "' AND time > " + time);
        int purchases = 0;
        while (result.next()) purchases++;
        return purchases;
    }

}
