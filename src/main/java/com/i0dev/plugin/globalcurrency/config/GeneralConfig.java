package com.i0dev.plugin.globalcurrency.config;

import com.i0dev.plugin.globalcurrency.object.ConfigItemStack;
import com.i0dev.plugin.globalcurrency.object.ShopItem;
import com.i0dev.plugin.globalcurrency.template.AbstractConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
@Setter
public class GeneralConfig extends AbstractConfiguration {
    public GeneralConfig(String path, String... header) {
        super(path, header);
    }

    protected void setValues() {
        config.set("databaseAddress", "localhost", "Database login information.");
        config.set("databasePort", 0);
        config.set("databaseDatabase", "globalCurrency");
        config.set("databaseUsername", "username");
        config.set("databasePassword", "password");

        config.set("shopInventorySize", 27);
        config.set("shopInventoryBorderGlass", new ConfigItemStack(
                Material.STAINED_GLASS_PANE,
                "&f",
                new ArrayList<>(),
                15,
                true

        ).serialize());
        config.set("shopInventoryTitle", "&c&lRuby Shop");
        config.set("shopItems", Arrays.asList(new ShopItem("champions-lootbox",
                                0,
                                Material.ENDER_CHEST.toString(),
                                0,
                                1,
                                Arrays.asList("&f", "&7Champions Lootbox", "&f", "&fCosts: &c10 rubies"),
                                "&c&lChampions Lootbox",
                                10,
                                true,
                                Arrays.asList("lootbox give {player} champions 1", "msg {player} you claimed champion lootbox")
                        ).serialize()
                )
        );
    }
}
