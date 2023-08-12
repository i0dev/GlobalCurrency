package com.i0dev.globalcurrency.cmd;

import com.i0dev.globalcurrency.GlobalCurrencyPlugin;
import com.i0dev.globalcurrency.Perm;
import com.i0dev.globalcurrency.entity.MConf;
import com.massivecraft.massivecore.command.MassiveCommandVersion;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.util.MUtil;

import java.util.List;

public class CmdGlobalCurrencyBlackMarket extends GlobalCurrencyCommand {

    private static CmdGlobalCurrencyBlackMarket i = new CmdGlobalCurrencyBlackMarket();

    public static CmdGlobalCurrencyBlackMarket get() {
        return i;
    }

    public CmdGlobalCurrencyBlackMarketShop cmdGlobalCurrencyBlackMarketShop = new CmdGlobalCurrencyBlackMarketShop();
    public CmdGlobalCurrencyBlackMarketRefreshShop cmdGlobalCurrencyBlackMarketRefreshShop = new CmdGlobalCurrencyBlackMarketRefreshShop();


    @Override
    public List<String> getAliases() {
        return MUtil.list("blackmarket");
    }

}
