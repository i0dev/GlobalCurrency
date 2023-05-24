package com.i0dev.globalcurrency.entity.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@AllArgsConstructor
@Getter
public class ShopCategory {
    public String id;
    public String inventoryTitle;
    public short inventorySize;
    public short slot;
    public short amount;
    public Material material;
    public String displayName;
    public List<String> lore;
    public boolean glow;
    public List<String> items;
}
