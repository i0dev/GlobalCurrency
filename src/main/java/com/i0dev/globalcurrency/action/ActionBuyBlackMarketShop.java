package com.i0dev.globalcurrency.action;

import com.i0dev.globalcurrency.cmd.CmdGlobalCurrencyBlackMarketShop;
import com.i0dev.globalcurrency.engine.EngineLog;
import com.i0dev.globalcurrency.engine.EngineSQL;
import com.i0dev.globalcurrency.entity.BlackMarketData;
import com.i0dev.globalcurrency.entity.MConf;
import com.i0dev.globalcurrency.entity.MLang;
import com.i0dev.globalcurrency.entity.object.BlackMarketShopItem;
import com.i0dev.globalcurrency.util.Pair;
import com.i0dev.globalcurrency.util.Utils;
import com.massivecraft.massivecore.chestgui.ChestAction;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

@AllArgsConstructor
public class ActionBuyBlackMarketShop implements ChestAction {

    BlackMarketShopItem item;

    @Override
    public boolean onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (!hasRequiredBalances(player)) {
            Utils.msg(player, MLang.get().notEnoughMoney);
            return false;
        }

        // check limits
        if (item.getLimit() != -1 && BlackMarketData.get().isItemOnLimit(item.getId())) {
            Utils.msg(player, MLang.get().reachedPurchaseLimit);
            return false;
        }

        if (!MConf.get().isActiveTime()) {
            Utils.msg(player, Utils.prefixAndColor(MLang.get().blackMarketShopDisabled));
            return false;
        }

        long currentBalance = EngineSQL.get().getAmount(player.getUniqueId());
        EngineSQL.get().setAmount(player.getUniqueId(), currentBalance - item.getPrice());

        Utils.runCommands(item.getCommands(), player);

        Utils.msg(player, MLang.get().boughtBlackMarketItem,
                new Pair<>("%price%", String.valueOf(item.getPrice())),
                new Pair<>("%item%", item.getDisplayName())
        );

        EngineLog.get().log(player.getName() + " has bought from the black market shop " + item.getId() + " for " + item.getPrice() + " currency");
        BlackMarketData.get().logPurchase(item.getId());
        CmdGlobalCurrencyBlackMarketShop.reopenInventory(player);
        return true;
    }


    private boolean hasRequiredBalances(Player player) {
        long currentBalance = EngineSQL.get().getAmount(player.getUniqueId());
        return currentBalance >= item.getPrice();
    }

}
