package com.i0dev.action;

import com.i0dev.engine.EngineLog;
import com.i0dev.engine.EngineSQL;
import com.i0dev.entity.MLang;
import com.i0dev.entity.object.ShopItem;
import com.i0dev.util.Pair;
import com.i0dev.util.Utils;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionSelectItem extends ChestActionAbstract {

    private ShopItem shopItem;

    public ActionSelectItem(ShopItem shopItem) {
        this.shopItem = shopItem;
    }

    @Override
    @SneakyThrows
    public boolean onClick(InventoryClickEvent event, Player player) {
        player.closeInventory();

        long balance = EngineSQL.get().getAmount(player.getUniqueId());
        if (balance < shopItem.getPrice()) {
            player.sendMessage(Utils.prefixAndColor(MLang.get().notEnoughMoney,
                            new Pair<>("%amount%", String.valueOf(balance))
                    )
            );
            return false;
        }

        EngineSQL.get().removeAmount(player.getUniqueId(), shopItem.getPrice());

        for (String command : shopItem.getCommands()) {
            command = command.replace("%player%", player.getName());
            player.getServer().dispatchCommand(player.getServer().getConsoleSender(), command);
        }

        player.sendMessage(Utils.prefixAndColor(MLang.get().youBought,
                        new Pair<>("%item%", shopItem.getDisplayName()),
                        new Pair<>("%amount%", String.valueOf(shopItem.getPrice()))
                )
        );


        EngineLog.get().log(player.getName() + " has purchased " + shopItem.getId() + " for " + shopItem.price + " currency");

        return false;
    }

}