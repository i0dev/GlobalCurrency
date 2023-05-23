package com.i0dev.entity.object;

import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

public class ShopInventory {
    public short size = 27;
    public String title = "&c&lMedal Shop";
    public Material borderMaterial = Material.BLACK_STAINED_GLASS_PANE;
    public String borderName = "&f";
    public List<String> borderLore = MUtil.list();
    public boolean borderGlow = true;
    public Material informationItemMaterial = Material.BOOK;
    public int informationItemSlot = 22;
    public boolean informationItemGlow = true;
    public String informationItemName = "&c&lInformation";
    public List<String> informationItemLore = MUtil.list("&7&lInformation about the shop", "", "&7...");
}
