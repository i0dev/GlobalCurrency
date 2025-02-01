package com.i0dev.globalcurrency.cmd;

import com.i0dev.globalcurrency.task.TaskRefreshExtractShop;

public class CmdGlobalcurrencyBlackMarketRefreshShop extends GlobalcurrencyCommand {

    @Override
    public void perform() {
        TaskRefreshExtractShop.get().refreshShop();
    }

}
