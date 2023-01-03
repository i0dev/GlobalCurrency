package com.i0dev.plugin.globalcurrency.manager;

import com.i0dev.plugin.globalcurrency.GlobalCurrencyPlugin;
import com.i0dev.plugin.globalcurrency.template.AbstractManager;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LogManager extends AbstractManager {

    @Getter
    private static final LogManager instance = new LogManager();

    String path;

    @SneakyThrows
    @Override
    public void initialize() {
        path = GlobalCurrencyPlugin.getPlugin().getDataFolder() + "/logs.txt";
        if (!new File(path).exists()) new File(path).createNewFile();
    }

    @SneakyThrows
    public void log(String string) {
        File file = new File(path);
        FileWriter fr = new FileWriter(file, true);
        Instant instant = Instant.ofEpochMilli(System.currentTimeMillis());
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateString = localDateTime.format(formatter);
        System.out.println(string);
        fr.write("[" + dateString + "] " + string + "\n");
        fr.close();
    }


}
