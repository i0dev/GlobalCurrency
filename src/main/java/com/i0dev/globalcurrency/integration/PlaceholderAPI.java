package com.i0dev.globalcurrency.integration;

import com.i0dev.globalcurrency.GlobalCurrencyPlugin;
import com.i0dev.globalcurrency.engine.EngineSQL;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.text.NumberFormat;

public class PlaceholderAPI extends PlaceholderExpansion {

    public PlaceholderAPI(GlobalCurrencyPlugin plugin) {
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return "i0dev";
    }

    @Override
    public String getIdentifier() {
        return "globalcurrency";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    NumberFormat format = NumberFormat.getInstance();

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        if (identifier.equals("balance")) {
            return format.format(EngineSQL.get().getAmount(player.getUniqueId()));
        }


        return null;
    }

}