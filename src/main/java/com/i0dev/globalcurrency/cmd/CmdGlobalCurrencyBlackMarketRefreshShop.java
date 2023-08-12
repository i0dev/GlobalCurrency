package com.i0dev.globalcurrency.cmd;

import com.i0dev.globalcurrency.task.TaskRefreshExtractShop;

public class CmdGlobalCurrencyBlackMarketRefreshShop extends GlobalCurrencyCommand {

    @Override
    public void perform() {
        TaskRefreshExtractShop.get().refreshShop();
    }

}
