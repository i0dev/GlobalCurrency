package com.i0dev.plugin.globalcurrency.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;

@Data
@AllArgsConstructor
public class ConfigFile {

    String fileName;
    FileConfiguration config;

}
