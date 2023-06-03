package com.i0dev.plugin.globalcurrency.object;

import com.i0dev.plugin.globalcurrency.utility.Utility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ConfigItemStack {

    protected Material material;
    protected String displayName;
    protected List<String> lore;

    protected int data;
    protected boolean glow;

    public ItemStack toItemStack() {
        ItemStack stack = new ItemStack(material, 1, (short) data);
        if (glow) stack.addUnsafeEnchantment(EnchantGlow.getGlow(), 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(Utility.color(displayName));
        meta.setLore(Utility.color(lore));
        stack.setItemMeta(meta);
        return stack;
    }

    public ConfigItemStack(Map<String, Object> map) {
        this.material = Material.valueOf((String) map.get("material"));
        this.displayName = (String) map.get("displayName");
        this.lore = (List<String>) map.get("lore");
        this.data = (int) map.get("data");
        this.glow = (boolean) map.get("glow");
    }

}
