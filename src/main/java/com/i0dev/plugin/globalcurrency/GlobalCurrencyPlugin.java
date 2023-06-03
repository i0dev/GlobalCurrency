package com.i0dev.plugin.globalcurrency;


import com.i0dev.plugin.globalcurrency.command.CmdGlobalCurrency;
import com.i0dev.plugin.globalcurrency.hook.PlaceholderAPIHook;
import com.i0dev.plugin.globalcurrency.manager.InventoryManager;
import com.i0dev.plugin.globalcurrency.manager.LogManager;
import com.i0dev.plugin.globalcurrency.manager.SQLManager;
import com.i0dev.plugin.globalcurrency.object.CorePlugin;
import lombok.Getter;

public class GlobalCurrencyPlugin extends CorePlugin {

    @Getter
    private static GlobalCurrencyPlugin plugin;
    public static final String PERMISSION_PREFIX = "globalcurrency";

    @Override
    public void startup() {
        plugin = this;

        // Managers
        registerManager(SQLManager.getInstance());
        registerManager(InventoryManager.getInstance());
        registerManager(LogManager.getInstance());

        // Hooks
        if (isPluginEnabled("PlaceholderAPI"))
            registerHook(new PlaceholderAPIHook(), "papi");

        // Configs
        registerConfig("config.yml");
        registerConfig("messages.yml");

        // Commands
        registerCommand(CmdGlobalCurrency.getInstance(), "globalcurrency");

        // Extra startup stuff
        if (!SQLManager.getInstance().connect()) {
            getLogger().severe("\u001B[31mDatabase failed to connect. Shutting down.");
            shutdown();
        }
        SQLManager.getInstance().makeTable();
        InventoryManager.getInstance().setupInventory();
    }
}
