package com.i0dev.globalcurrency.engine;

import com.i0dev.globalcurrency.GlobalCurrencyPlugin;
import com.massivecraft.massivecore.Engine;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class EngineLog extends Engine {

    private static EngineLog i = new EngineLog();

    public static EngineLog get() {
        return i;
    }

    String path;

    @SneakyThrows
    public void initialize() {
        File dataFolder = GlobalCurrencyPlugin.get().getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        path = dataFolder + "/logs.txt";
        if (!new File(path).exists()) {
            new File(path).createNewFile();
        }
    }

    @SneakyThrows
    public void log(String string) {
        File file = new File(path);
        FileWriter fr = new FileWriter(file, true);
        Instant instant = Instant.ofEpochMilli(System.currentTimeMillis());
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, TimeZone.getTimeZone("EST").toZoneId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateString = localDateTime.format(formatter);
        System.out.println(string);
        fr.write("[" + dateString + "] " + string + "\n");
        fr.close();
    }

}
