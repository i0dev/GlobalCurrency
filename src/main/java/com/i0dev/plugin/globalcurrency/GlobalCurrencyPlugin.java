package com.i0dev.plugin.globalcurrency;

import com.i0dev.plugin.globalcurrency.command.CmdGlobalCurrency;
import com.i0dev.plugin.globalcurrency.config.GeneralConfig;
import com.i0dev.plugin.globalcurrency.config.MessageConfig;
import com.i0dev.plugin.globalcurrency.hook.*;
import com.i0dev.plugin.globalcurrency.manager.ConfigManager;
import com.i0dev.plugin.globalcurrency.manager.InventoryManager;
import com.i0dev.plugin.globalcurrency.manager.LogManager;
import com.i0dev.plugin.globalcurrency.manager.SQLManager;
import com.i0dev.plugin.globalcurrency.object.CorePlugin;
import lombok.Getter;

@Getter
public class GlobalCurrencyPlugin extends CorePlugin {
    @Getter
    private static GlobalCurrencyPlugin plugin;
    public static final String PERMISSION_PREFIX = "globalcurrency";

    @Override
    public void startup() {
        plugin = this;

        // Managers
        registerManager(ConfigManager.getInstance());
        registerManager(SQLManager.getInstance());
        registerManager(InventoryManager.getInstance());
        registerManager(LogManager.getInstance());

        // Hooks
        if (isPluginEnabled("PlaceholderAPI"))
            registerHook(new PlaceholderAPIHook(), "papi");

        // Configs
        registerConfig(new GeneralConfig("config.yml", "Main configuration file", "Plugin created by i0dev"));
        registerConfig(new MessageConfig("messages.yml", "Main messaging configuration"));

        // Commands
        registerCommand(CmdGlobalCurrency.getInstance(), "globalcurrency");

        if (!SQLManager.getInstance().connect()) {
            System.out.println("\u001B[31mDatabase failed to connect. Shutting down.");
            shutdown();
        }
        SQLManager.getInstance().makeTable();
        InventoryManager.getInstance().setupInventory();
    }

    @Override
    public void shutdown() {

    }

}
