package com.i0dev.plugin.globalcurrency.utility;

import com.i0dev.plugin.globalcurrency.GlobalCurrencyPlugin;
import com.i0dev.plugin.globalcurrency.object.ConfigFile;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Set;

public class ConfigUtil {

    @SneakyThrows
    public static void loadConfig(String fileName) {
        GlobalCurrencyPlugin plugin = GlobalCurrencyPlugin.getPlugin();
        if (!plugin.getDataFolder().exists() && !plugin.getDataFolder().mkdir()) {
            throw new IOException("Failed to create data folder: " + plugin.getDataFolder().getName());
        }

        if (!new File(plugin.getDataFolder(), fileName).exists()) {
            plugin.saveResource(fileName, false);
            return;
        }

        File file = File.createTempFile(fileName.split("\\.")[0], ".tmp", plugin.getDataFolder());
        OutputStream outputStream = new FileOutputStream(file);
        if (plugin.getResource(fileName) == null) throw new IOException("Internal resource not found: " + fileName);
        IOUtils.copy(Objects.requireNonNull(plugin.getResource(fileName)), outputStream);

        FileConfiguration config = new YamlConfiguration();
        config.load(file);

        File dataFolderFile = new File(plugin.getDataFolder(), fileName);
        FileConfiguration dataFolderConfig = new YamlConfiguration();
        dataFolderConfig.load(dataFolderFile);

        Set<String> dataFolderKeys = dataFolderConfig.getKeys(true);
        Set<String> internalKeys = config.getKeys(true);

        if (internalKeys.isEmpty()) return;

        internalKeys.forEach(s -> {
          //  System.out.println("Checking key: " + s);
            if (!dataFolderKeys.contains(s)) {
                plugin.getConfig().set(s, config.get(s));
                System.out.println("Adding key: " + s);
            }
        });

        config.save(new File(plugin.getDataFolder(), fileName));
        if (!file.delete()) System.out.println("Failed to delete temp file: " + file.getName());
    }

    public static void reloadConfig(ConfigFile config) {
        GlobalCurrencyPlugin plugin = GlobalCurrencyPlugin.getPlugin();
        File file = new File(plugin.getDataFolder(), config.getFileName());
        try {
            config.getConfig().load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
