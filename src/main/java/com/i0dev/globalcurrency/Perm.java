package com.i0dev.globalcurrency;

import com.massivecraft.massivecore.Identified;
import com.massivecraft.massivecore.util.PermissionUtil;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.Permissible;

public enum Perm implements Identified {

    BASECOMMAND("basecommand"),

    BLACKMARKET("blackmarket"),
    BLACK_MARKET("blackmarket"),
    BLACK_MARKET_SHOP("blackmarketshop"),
    REFRESH_SHOP("refreshshop"),
    BLACK_MARKET_REFRESH_SHOP("blackmarketrefreshshop"),

    ADD("add"),
    BALANCE("balance"),
    BALANCEOTHERS("balanceothers"),
    REMOVE("remove"),
    SET("set"),
    SHOP("shop"),

    IMPORT("import"),

    VERSION("version");

    private final String id;

    Perm(String id) {
        this.id = "globalcurrency." + id;
    }

    @Override
    public String getId() {
        return id;
    }

    public boolean has(Permissible permissible, boolean verboose) {

        if (permissible instanceof ConsoleCommandSender) {
            return true;
        }

        return PermissionUtil.hasPermission(permissible, this, verboose);
    }

    public boolean has(Permissible permissible) {

        if (permissible instanceof ConsoleCommandSender) {
            return true;
        }

        return PermissionUtil.hasPermission(permissible, this);
    }
}
