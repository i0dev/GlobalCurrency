package com.i0dev.globalcurrency.cmd;

import com.i0dev.globalcurrency.Perm;
import com.i0dev.globalcurrency.engine.EngineInventory;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

public class CmdGlobalcurrencyShop extends GlobalcurrencyCommand {

    public CmdGlobalcurrencyShop() {
        this.addRequirements(RequirementHasPerm.get(Perm.SHOP));
        this.addRequirements(RequirementIsPlayer.get());
    }

    @Override
    public void perform() {
        me.openInventory(EngineInventory.get().getMainMenu());
    }
}
