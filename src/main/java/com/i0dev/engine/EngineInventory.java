package com.i0dev.engine;

import com.i0dev.GlobalCurrencyPlugin;
import com.i0dev.action.ActionChoseCategory;
import com.i0dev.action.ActionSelectItem;
import com.i0dev.entity.MConf;
import com.i0dev.entity.object.ShopCategory;
import com.i0dev.entity.object.ShopInventory;
import com.i0dev.entity.object.ShopItem;
import com.i0dev.util.Glow;
import com.i0dev.util.ItemBuilder;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.logging.Level;
import java.util.stream.IntStream;

public class EngineInventory extends Engine {

    private static final EngineInventory i = new EngineInventory();

    public static EngineInventory get() {
        return i;
    }

    private ChestGui getBasicChestGui(String title, int size) {
        ShopInventory invConf = MConf.get().shopInventory;

        Inventory inventory = Bukkit.createInventory(null, size, Txt.parse(title));
        ChestGui chestGui = ChestGui.getCreative(inventory);

        chestGui.setAutoclosing(false);
        chestGui.setAutoremoving(true);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);

        IntStream.range(0, chestGui.getInventory().getSize()).forEach(i -> chestGui.getInventory().setItem(i, new ItemBuilder(invConf.borderMaterial)
                .amount(1)
                .name(invConf.borderName)
                .lore(invConf.borderLore)
                .enchantment(invConf.borderGlow ? Glow.getGlow() : null)
        ));

        return chestGui;
    }


    public Inventory getMainMenu() {
        ShopInventory invConf = MConf.get().shopInventory;

        ChestGui chestGui = getBasicChestGui(invConf.title, invConf.size);

        chestGui.getInventory().setItem(invConf.informationItemSlot, new ItemBuilder(invConf.informationItemMaterial)
                .amount(1)
                .name(invConf.informationItemName)
                .lore(invConf.informationItemLore)
                .enchantment(invConf.informationItemGlow ? Glow.getGlow() : null)
        );

        for (ShopCategory category : MConf.get().shopCategories) {
            chestGui.getInventory().setItem(category.slot, new ItemBuilder(category.material)
                    .amount(1)
                    .name(category.displayName)
                    .lore(category.lore)
                    .enchantment(category.glow ? Glow.getGlow() : null)
            );
            chestGui.setAction(category.slot, new ActionChoseCategory(category.getId()));
        }

        return chestGui.getInventory();
    }

    private ShopCategory getCategoryById(String categoryId) {
        return MConf.get().shopCategories.stream().filter(shopCategory -> shopCategory.getId().equals(categoryId)).findFirst().orElse(null);
    }

    private ShopItem getItemById(String itemId) {
        return MConf.get().shopItems.stream().filter(shopItem -> shopItem.getId().equals(itemId)).findFirst().orElse(null);
    }

    public Inventory getCategoryInventoryById(String categoryId) throws Exception {
        ShopCategory category = getCategoryById(categoryId);
        if (category == null) throw new Exception("Category not found");

        ChestGui chestGui = getBasicChestGui(category.displayName, category.inventorySize);

        for (String itemString : category.getItems()) {
            ShopItem item = getItemById(itemString);
            if (item == null) {
                GlobalCurrencyPlugin.get().log(Level.SEVERE, "Item " + itemString + " not found while opening category " + category.getId() + " inventory");
                continue;
            }
            chestGui.getInventory().setItem(item.slot, new ItemBuilder(item.material)
                    .amount(1)
                    .name(item.displayName)
                    .lore(item.lore)
                    .enchantment(item.glow ? Glow.getGlow() : null)
            );
            chestGui.setAction(item.slot, new ActionSelectItem(item));
        }

        return chestGui.getInventory();
    }
}