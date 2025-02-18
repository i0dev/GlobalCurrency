package com.i0dev.globalcurrency.action;

import com.i0dev.globalcurrency.engine.EngineInventory;
import com.i0dev.globalcurrency.entity.MConf;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionChoseCategory extends ChestActionAbstract {

    private String categoryId;

    public ActionChoseCategory(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    @SneakyThrows
    public boolean onClick(InventoryClickEvent event, Player player) {
        player.openInventory(EngineInventory.get().getCategoryInventoryById(categoryId));
        return false;
    }

}