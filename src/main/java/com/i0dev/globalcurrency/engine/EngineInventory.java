package com.i0dev.globalcurrency.engine;

import com.i0dev.globalcurrency.action.ActionChoseCategory;
import com.i0dev.globalcurrency.action.ActionConfirmPurchaseItem;
import com.i0dev.globalcurrency.entity.Category;
import com.i0dev.globalcurrency.entity.CategoryColl;
import com.i0dev.globalcurrency.entity.MConf;
import com.i0dev.globalcurrency.entity.object.ConfirmationInventory;
import com.i0dev.globalcurrency.entity.object.ShopInventory;
import com.i0dev.globalcurrency.entity.object.ShopItem;
import com.i0dev.globalcurrency.util.Glow;
import com.i0dev.globalcurrency.util.ItemBuilder;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.Txt;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
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
                .addGlow(invConf.borderGlow)
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

        for (Category category : CategoryColl.get().getAll()) {
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


    public Inventory getConfirmationGUI(String categoryId, String itemId) {
        ConfirmationInventory cnf = MConf.get().confirmationInventory;
        Category category = Category.get(categoryId);
        ShopItem item = category.getItemById(itemId);
        ChestGui chestGui = getBasicChestGui(cnf.title.replace("%item_name%", item.displayName), cnf.size);

        chestGui.getInventory().setItem(cnf.itemSlot, new ItemBuilder(item.material)
                .amount(1)
                .name(item.displayName)
                .lore(item.lore)
                .enchantment(item.glow ? Glow.getGlow() : null));

        List<String> confirmLore = new ArrayList<>();
        cnf.confirmItemLore.forEach(s -> confirmLore.add(s
                .replace("%item_price%", String.valueOf(item.price))
                .replace("%s%", item.price > 1 ? "s" : "")
                .replace("%item_name%", item.displayName)));

        chestGui.getInventory().setItem(cnf.confirmItemSlot, new ItemBuilder(cnf.confirmItemMaterial)
                .amount(1)
                .name(cnf.confirmItemName)
                .lore(confirmLore)
                .enchantment(cnf.confirmItemGlow ? Glow.getGlow() : null)
        );
        chestGui.setAction(cnf.confirmItemSlot, new ActionConfirmPurchaseItem(item, categoryId));

        chestGui.getInventory().setItem(cnf.cancelItemSlot, new ItemBuilder(cnf.cancelItemMaterial)
                .amount(1)
                .name(cnf.cancelItemName)
                .lore(cnf.cancelItemLore)
                .enchantment(cnf.cancelItemGlow ? Glow.getGlow() : null)
        );
        chestGui.setAction(cnf.cancelItemSlot, inventoryClickEvent -> {
            inventoryClickEvent.getWhoClicked().openInventory(getCategoryInventoryById(categoryId));
            return true;
        });

        return chestGui.getInventory();
    }


    @SneakyThrows
    public Inventory getCategoryInventoryById(String categoryId) {
        Category category = Category.get(categoryId);
        if (category == null) {
            return null;
        }

        ChestGui chestGui = getBasicChestGui(category.displayName, category.inventorySize);


        for (ShopItem item : category.getItems()) {
            chestGui.getInventory().setItem(item.slot, new ItemBuilder(item.material)
                    .amount(1)
                    .name(item.displayName)
                    .lore(item.lore)
                    .enchantment(item.glow ? Glow.getGlow() : null)
            );
            chestGui.setAction(item.slot, inventoryClickEvent -> {
                inventoryClickEvent.getWhoClicked().openInventory(getConfirmationGUI(categoryId, item.getId()));
                return false;
            });
        }

        chestGui.getInventory().setItem(category.backButtonSlot, new ItemBuilder(MConf.get().backToCategoriesItem.material)
                .amount(1)
                .name(MConf.get().backToCategoriesItem.displayName)
                .lore(MConf.get().backToCategoriesItem.lore)
                .enchantment(MConf.get().backToCategoriesItem.glow ? Glow.getGlow() : null)
        );

        chestGui.setAction(category.backButtonSlot, inventoryClickEvent -> {
            inventoryClickEvent.getWhoClicked().openInventory(getMainMenu());
            return true;
        });

        return chestGui.getInventory();
    }
}