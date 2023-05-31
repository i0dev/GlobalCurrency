package com.i0dev.globalcurrency.entity.object;

import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

public class ConfirmationInventory {
    public short size = 27;
    public String title = "%item_name%";

    public int itemSlot = 13;

    public Material confirmItemMaterial = Material.GREEN_CONCRETE;
    public int confirmItemSlot = 15;
    public String confirmItemName = "&a&lConfirm Purchase";
    public List<String> confirmItemLore = MUtil.list(
            "&7You are about to purchase %item_name%",
            "&7for %item_price% Medal%s%!",
            "",
            "&7This action is irreversible!",
            "&7So make sure you want to buy this item!",
            "",
            "&aClick to confirm your purchase!"
    );
    public boolean confirmItemGlow = true;

    public Material cancelItemMaterial = Material.RED_CONCRETE;
    public int cancelItemSlot = 11;
    public String cancelItemName = "&c&lCancel Purchase";
    public List<String> cancelItemLore = MUtil.list(
            "",
            "&cClick to cancel your purchase!"
    );
    public boolean cancelItemGlow = true;
}
