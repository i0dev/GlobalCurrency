package com.i0dev.globalcurrency.cmd;

import com.i0dev.globalcurrency.GlobalCurrencyPlugin;
import com.i0dev.globalcurrency.Perm;
import com.i0dev.globalcurrency.entity.MConf;
import com.massivecraft.massivecore.command.MassiveCommandVersion;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

import java.util.List;

public class CmdGlobalcurrency extends GlobalcurrencyCommand {

    private static CmdGlobalcurrency i = new CmdGlobalcurrency();

    public CmdGlobalcurrencyAdd cmdGlobalcurrencyAdd = new CmdGlobalcurrencyAdd();
    public CmdGlobalcurrencyBalance cmdGlobalcurrencyBalance = new CmdGlobalcurrencyBalance();
    public CmdGlobalcurrencyRemove cmdGlobalcurrencyRemove = new CmdGlobalcurrencyRemove();
    public CmdGlobalcurrencySet cmdGlobalcurrencySet = new CmdGlobalcurrencySet();
    public CmdGlobalcurrencyShop cmdGlobalcurrencyShop = new CmdGlobalcurrencyShop();
    public CmdGlobalcurrencyImport cmdGlobalcurrencyImport = new CmdGlobalcurrencyImport();
    public CmdGlobalcurrencyBlackMarket cmdGlobalcurrencyBlackMarket = new CmdGlobalcurrencyBlackMarket();
    public MassiveCommandVersion cmdGlobalcurrencyVersion = new MassiveCommandVersion(GlobalCurrencyPlugin.get()).setAliases("v", "version").addRequirements(RequirementHasPerm.get(Perm.VERSION));

    public static CmdGlobalcurrency get() {
        return i;
    }

    @Override
    public List<String> getAliases() {
        return MConf.get().aliasesGlobalCurrency;
    }

}
