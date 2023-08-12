package com.i0dev.globalcurrency;

import com.i0dev.globalcurrency.engine.EngineLog;
import com.i0dev.globalcurrency.engine.EngineSQL;
import com.i0dev.globalcurrency.entity.*;
import com.i0dev.globalcurrency.integration.PlaceholderAPI;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.collections.MassiveList;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.logging.Level;

public class GlobalCurrencyPlugin extends MassivePlugin {

    private static GlobalCurrencyPlugin i;

    public GlobalCurrencyPlugin() {
        GlobalCurrencyPlugin.i = this;
    }

    public static GlobalCurrencyPlugin get() {
        return i;
    }

    @Override
    public void onEnableInner() {
        this.activateAuto();
        if (!EngineSQL.get().connect()) {
            this.log(Level.SEVERE, "Could not connect to the database. Plugin Disabling.");
            this.getServer().getPluginManager().disablePlugin(this);
            this.setEnabled(false);
            return;
        }
        EngineSQL.get().makeTables();
        EngineLog.get().initialize();
    }


    @Override
    public void onEnable() {
        super.onEnable();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI(this).register();
        } else {
            getLogger().warning("Could not find PlaceholderAPI! This plugin is required.");
        }
    }

    @Override
    public void onEnablePost() {
        super.onEnablePost();
        if (MConf.get().makeExample)
            Category.example();
    }

    @Override
    public List<Class<?>> getClassesActiveColls() {
        return new MassiveList<>(
                MConfColl.class,
                MLangColl.class,
                CategoryColl.class,
                BlackMarketDataColl.class
        );
    }

}