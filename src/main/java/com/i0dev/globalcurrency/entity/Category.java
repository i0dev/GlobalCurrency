package com.i0dev.globalcurrency.entity;


import com.i0dev.globalcurrency.entity.object.ShopItem;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.List;

@Setter
@Getter
@EditorName("config")
public class Category extends Entity<Category> {

    public static Category get(Object oid) {
        return CategoryColl.get().get(oid);
    }

    public int backButtonSlot;
    public String inventoryTitle;
    public short inventorySize;
    public short slot;
    public short amount;
    public Material material;
    public String displayName;
    public List<String> lore;
    public boolean glow;
    public List<ShopItem> items;

    public ShopItem getItemById(String id) {
        return items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);
    }


    public static void example() {
        if (CategoryColl.get().containsId("example")) return;
        Category category = CategoryColl.get().create("example");
        category.setBackButtonSlot(22);
        category.setInventoryTitle("&c&lMedal Shop - Ores");
        category.setInventorySize((short) 27);
        category.setSlot((short) 5);
        category.setAmount((short) 1);
        category.setMaterial(Material.DIAMOND_ORE);
        category.setDisplayName("&b&lOres");
        category.setLore(MUtil.list("&7Valuable ores!"));
        category.setGlow(true);
        category.setItems(MUtil.list(
                new ShopItem(
                        "diamond",
                        "&b&lDiamond",
                        Material.DIAMOND,
                        (short) 12,
                        (short) 1,
                        1000,
                        MUtil.list("&7Price: &f1000", "&b1 Diamond"),
                        true,
                        MUtil.list("give %player% diamond 1"),
                        "",
                        "",
                        -1,
                        -1
                ),
                new ShopItem(
                        "gold_ingot",
                        "&6&lGold Ingot",
                        Material.GOLD_INGOT,
                        (short) 13,
                        (short) 1,
                        100,
                        MUtil.list("&7Price: &f100", "&61 Gold Ingot"),
                        true,
                        MUtil.list("give %player% gold_ingot 1"),
                        "permission.example",
                        "deny.permission.example",
                        5,
                        259_200_000 // 3 days
                )
        ));
    }


    @Override
    public Category load(Category that) {
        super.load(that);
        return this;
    }

}
