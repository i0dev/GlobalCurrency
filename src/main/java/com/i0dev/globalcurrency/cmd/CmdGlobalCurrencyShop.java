package com.i0dev.globalcurrency.cmd;

import com.i0dev.globalcurrency.Perm;
import com.i0dev.globalcurrency.engine.EngineInventory;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

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
