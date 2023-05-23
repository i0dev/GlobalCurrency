package com.i0dev.cmd;

import com.i0dev.Perm;
import com.i0dev.engine.EngineInventory;
import com.i0dev.entity.MConf;
import com.i0dev.entity.object.ShopCategory;
import com.i0dev.entity.object.ShopInventory;
import com.i0dev.util.Glow;
import com.i0dev.util.ItemBuilder;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.stream.IntStream;

public class CmdGlobalCurrencyShop extends GlobalCurrencyCommand {

    public CmdGlobalCurrencyShop() {
        this.addRequirements(RequirementHasPerm.get(Perm.SHOP));
        this.addRequirements(RequirementIsPlayer.get());
    }

    @Override
    public void perform() throws MassiveException {
        me.openInventory(EngineInventory.get().getMainMenu());
    }
}
