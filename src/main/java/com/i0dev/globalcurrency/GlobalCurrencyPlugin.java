package com.i0dev.globalcurrency;

import com.i0dev.globalcurrency.engine.EngineLog;
import com.i0dev.globalcurrency.engine.EngineSQL;
import com.i0dev.globalcurrency.entity.MConfColl;
import com.i0dev.globalcurrency.entity.MLangColl;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.collections.MassiveList;

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
        EngineSQL.get().makeTable();
        EngineLog.get().initialize();
    }


    @Override
    public List<Class<?>> getClassesActiveColls() {
        return new MassiveList<>(
                MConfColl.class,
                MLangColl.class
        );
    }

}