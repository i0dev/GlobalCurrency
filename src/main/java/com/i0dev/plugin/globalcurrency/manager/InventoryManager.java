package com.i0dev.plugin.globalcurrency.manager;

import com.i0dev.plugin.globalcurrency.GlobalCurrencyPlugin;
import com.i0dev.plugin.globalcurrency.object.*;
import com.i0dev.plugin.globalcurrency.template.AbstractManager;
import com.i0dev.plugin.globalcurrency.utility.MsgUtil;
import com.i0dev.plugin.globalcurrency.utility.Utility;
import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InventoryManager extends AbstractManager {

    @Getter
    private static final InventoryManager instance = new InventoryManager();
    @Getter
    public Inventory categoryInventory;

    @Override
    public void initialize() {
        setListener(true);
    }

    public List<Category> getCategories() {
        FileConfiguration cnf = GlobalCurrencyPlugin.getPlugin().cnf();
        List<Category> list = new ArrayList<>();
        for (Object obj : cnf.getList("shop.categories")) {
            list.add(new Category((Map<String, Object>) obj));
        }
        return list;
    }

    public List<ShopItem> getShopItems() {
        FileConfiguration cnf = GlobalCurrencyPlugin.getPlugin().cnf();
        List<ShopItem> list = new ArrayList<>();
        for (Object obj : cnf.getList("shop.items")) {
            list.add(new ShopItem((Map<String, Object>) obj));
        }
        return list;
    }

    public ShopItem getShopItem(String shopItemID) {
        for (ShopItem shopItem : getShopItems()) {
            if (shopItem.getId().equalsIgnoreCase(shopItemID)) return shopItem;
        }
        return null;
    }

    public Category getCategory(String categoryID) {
        for (Category category : getCategories()) {
            if (category.getId().equalsIgnoreCase(categoryID)) return category;
        }
        return null;
    }


    public void setupInventory() {
        FileConfiguration cnf = GlobalCurrencyPlugin.getPlugin().cnf();
        this.categoryInventory = Bukkit.createInventory(new ShopInventoryHolder(), cnf.getInt("shop.inventory.size"), Utility.color(cnf.getString("shop.inventory.title")));

        for (Category category : getCategories()) {
            ItemStack item = new ItemStack(Material.valueOf(category.getMaterial()), category.getAmount(), ((short) category.getData()));
            ItemMeta meta = item.getItemMeta();
            meta.setLore(Utility.color(category.getLore()));
            meta.setDisplayName(Utility.color(category.getDisplayName()));
            if (category.isGlow()) {
                meta.addEnchant(EnchantGlow.getGlow(), 1, true);
            }
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);

            NBTItem nbtItem = new NBTItem(item);
            nbtItem.setString("category-id", category.getId());

            item = nbtItem.getItem();

            categoryInventory.setItem(category.getSlot(), item);
        }

        for (int i = 0; i < categoryInventory.getSize(); i++) {
            if (categoryInventory.getItem(i) == null || categoryInventory.getItem((i)).getType() == null || categoryInventory.getItem(i).getType().equals(Material.AIR))
                categoryInventory.setItem(i,
                        new ConfigItemStack(
                                cnf.getConfigurationSection("shop.inventory.border_glass")
                                        .getValues(true))
                                .toItemStack());
        }
    }

    public Inventory getCategoryInventory(String categoryId) {

        Category category = getCategory(categoryId);
        Inventory categoricalInventory = Bukkit.createInventory(new ShopInventoryHolder(), category.getInventorySize(), Utility.color(category.getInventoryTitle()));

        List<ShopItem> shopItems = new ArrayList<>();
        for (String shopItemId : category.getShopItemIds()) {
            shopItems.add(getShopItem(shopItemId));
        }

        for (ShopItem shopItem : shopItems) {
            ItemStack item = new ItemStack(Material.valueOf(shopItem.getMaterial()), shopItem.getAmount(), ((short) shopItem.getData()));
            ItemMeta meta = item.getItemMeta();
            meta.setLore(Utility.color(shopItem.getLore()));
            meta.setDisplayName(Utility.color(shopItem.getDisplayName()));
            if (shopItem.isGlow()) {
                meta.addEnchant(EnchantGlow.getGlow(), 1, true);
            }
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);

            NBTItem nbtItem = new NBTItem(item);
            nbtItem.setString("globalcurrency-id", shopItem.getId());

            item = nbtItem.getItem();

            categoricalInventory.setItem(shopItem.getSlot(), item);
        }
        FileConfiguration cnf = GlobalCurrencyPlugin.getPlugin().cnf();

        for (int i = 0; i < categoricalInventory.getSize(); i++) {
            if (categoricalInventory.getItem(i) == null || categoricalInventory.getItem((i)).getType() == null || categoricalInventory.getItem(i).getType().equals(Material.AIR))
                categoricalInventory.setItem(i,
                        new ConfigItemStack(
                                cnf.getConfigurationSection("shop.inventory.border_glass")
                                        .getValues(true))
                                .toItemStack());
        }
        return categoricalInventory;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof ShopInventoryHolder) {
            e.setCancelled(true);
        }

        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        if (Material.AIR.equals(item.getType())) return;

        NBTItem nbtItem = new NBTItem(item);
        GlobalCurrencyPlugin pl = GlobalCurrencyPlugin.getPlugin();

        if (nbtItem.hasTag("category-id")) {
            String id = nbtItem.getString("category-id");
            e.getWhoClicked().closeInventory();
            e.getWhoClicked().openInventory(getCategoryInventory(id));

        } else if (nbtItem.hasTag("globalcurrency-id")) {

            String id = nbtItem.getString("globalcurrency-id");

            ShopItem shopItem = getShopItem(id);

            int cost = shopItem.getCost();
            long balance = SQLManager.getInstance().getAmount(e.getWhoClicked().getUniqueId());
            if (balance < cost) {
                MsgUtil.msg(e.getWhoClicked(), pl.msg().getString("insufficientBalance"), new Pair<>("{amount}", String.valueOf(balance)));
                return;
            }

            for (String command : shopItem.getCommands()) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", e.getWhoClicked().getName()));
            }

            MsgUtil.msg(e.getWhoClicked(), pl.msg().getString("youBought"),
                    new Pair<>("{item}", shopItem.getDisplayName()),
                    new Pair<>("{cost}", String.valueOf(cost))
            );

            SQLManager.getInstance().removeAmount(e.getWhoClicked().getUniqueId(), cost);
            LogManager.getInstance().log(e.getWhoClicked().getName() + " has purchased " + shopItem.getId() + " for " + cost + " currency");
        }
    }


}
