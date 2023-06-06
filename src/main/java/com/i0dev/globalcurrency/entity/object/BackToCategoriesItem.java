package com.i0dev.globalcurrency.entity.object;

import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;


public class BackToCategoriesItem {
    public Material material = Material.BARRIER;
    public String displayName = "&c&lBack";
    public List<String> lore = MUtil.list("", "&7Click to go back to categories");
    public boolean glow = true;
}
