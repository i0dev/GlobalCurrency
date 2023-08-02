package com.i0dev.globalcurrency.action;

import com.i0dev.globalcurrency.engine.EngineInventory;
import com.i0dev.globalcurrency.engine.EngineLog;
import com.i0dev.globalcurrency.engine.EngineSQL;
import com.i0dev.globalcurrency.entity.MConf;
import com.i0dev.globalcurrency.entity.MLang;
import com.i0dev.globalcurrency.entity.object.ShopItem;
import com.i0dev.globalcurrency.util.Pair;
import com.i0dev.globalcurrency.util.Utils;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionConfirmPurchaseItem extends ChestActionAbstract {

    private ShopItem shopItem;
    private String categoryId;

    public ActionConfirmPurchaseItem(ShopItem shopItem, String categoryId) {
        this.shopItem = shopItem;
        this.categoryId = categoryId;
    }

    @Override
    @SneakyThrows
    public boolean onClick(InventoryClickEvent event, Player player) {
        if (MConf.get().closeBuyWindowOnPurchase) player.closeInventory();

        // Balance Check
        long balance = EngineSQL.get().getAmount(player.getUniqueId());
        if (balance < shopItem.getPrice()) {
            player.sendMessage(Utils.prefixAndColor(MLang.get().notEnoughMoney,
                            new Pair<>("%amount%", String.valueOf(balance))
                    )
            );
            return false;
        }


        // Perm Check
        if (!shopItem.getRequiredPermissionToBuy().equals("") && !player.hasPermission(shopItem.getRequiredPermissionToBuy())) {
            player.sendMessage(Utils.prefixAndColor(MLang.get().noPermissionToBuy));
            return false;
        }

        if (shopItem.getPreventBuyIfHasPermission() != null && !shopItem.getPreventBuyIfHasPermission().equals("") && player.hasPermission(shopItem.getPreventBuyIfHasPermission())) {
            player.sendMessage(Utils.prefixAndColor(MLang.get().alreadyHasPermission));
            return false;
        }

        // Limit Per Player Check
        if (shopItem.getLimitPerPlayer() != -1 && EngineSQL.get().getPurchases(player.getUniqueId(), shopItem.getId(), shopItem.getLimitCheckBackMillis()) >= shopItem.getLimitPerPlayer()) {
            player.sendMessage(Utils.prefixAndColor(MLang.get().limitPerPlayerReached,
                    new Pair<>("%limit%", String.valueOf(shopItem.getLimitPerPlayer()))
            ));
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

        EngineLog.get().log(player.getName() + " has purchased " + shopItem.getId() + " for " + shopItem.price + " currency, on the server " + MConf.get().serverID + ".");
        EngineSQL.get().logPurchase(player.getUniqueId(), shopItem.getId(), shopItem.getPrice());

        if (!MConf.get().closeBuyWindowOnPurchase)
            player.openInventory(EngineInventory.get().getCategoryInventoryById(categoryId));

        return false;
    }

}