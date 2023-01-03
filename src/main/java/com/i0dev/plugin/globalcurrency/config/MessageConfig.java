package com.i0dev.plugin.globalcurrency.config;

import com.i0dev.plugin.globalcurrency.template.AbstractConfiguration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageConfig extends AbstractConfiguration {

    public MessageConfig(String path, String... header) {
        super(path, header);
    }

    protected void setValues() {
        config.set("reloadedConfig", "&7You have&a reloaded&7 the configuration.");
        config.set("noPermission", "&cYou don not have permission to run that command.");
        config.set("cantFindPlayer", "&cThe player: &f{player}&c cannot be found!");
        config.set("invalidNumber", "&cThe number &f{num} &cis invalid! Try again.");
        config.set("cantUseAsConsole", "&cYou can only run this command as a player.");

        config.set("helpPageTitle", "&8_______&r&8[&r &c&lGlobal Currency &8]_______");
        config.set("helpPageFormat", " &c* &7/{cmd}");

        config.set("addedToPlayer", "&7You have added &f{amount} &7rubies to &c{player}");
        config.set("removedFromPlayer", "&7You have removed &f{amount} &7rubies from &c{player}");
        config.set("setPlayer", "&7You have set &c{player}'s &7balance to &f{amount}");
        config.set("yourBalance", "&7Your balance is &f{amount} &7rubies.");
        config.set("playersBalance", "&c{player}'s &7balance is &f{amount} &7rubies.");
        config.set("insufficientBalance", "&cInsufficient Balance. You only have &f{amount} rubies");
        config.set("youBought", "&7You have purchased {item} &7for &f{cost} rubies");
    }
}
