package com.i0dev.globalcurrency.cmd;

import com.massivecraft.massivecore.util.MUtil;

import java.util.List;

public class CmdGlobalcurrencyBlackMarket extends GlobalcurrencyCommand {

    private static CmdGlobalcurrencyBlackMarket i = new CmdGlobalcurrencyBlackMarket();

    public static CmdGlobalcurrencyBlackMarket get() {
        return i;
    }

    public CmdGlobalcurrencyBlackMarketShop cmdGlobalCurrencyBlackMarketShop = new CmdGlobalcurrencyBlackMarketShop();
    public CmdGlobalcurrencyBlackMarketRefreshShop cmdGlobalCurrencyBlackMarketRefreshShop = new CmdGlobalcurrencyBlackMarketRefreshShop();


    @Override
    public List<String> getAliases() {
        return MUtil.list("blackmarket");
    }

}
