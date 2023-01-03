package com.i0dev.plugin.globalcurrency.manager;

import com.i0dev.plugin.globalcurrency.GlobalCurrencyPlugin;
import com.i0dev.plugin.globalcurrency.config.GeneralConfig;
import com.i0dev.plugin.globalcurrency.object.*;
import com.i0dev.plugin.globalcurrency.template.AbstractManager;
import com.i0dev.plugin.globalcurrency.utility.MsgUtil;
import com.i0dev.plugin.globalcurrency.utility.Utility;
import de.tr7zw.nbtapi.NBTItem;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
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
    public Inventory shopInventory;

    @Override
    public void initialize() {
        setListener(true);
    }

    public List<ShopItem> getShopItems() {
        SimpleConfig cnf = GlobalCurrencyPlugin.getPlugin().cnf();
        List<ShopItem> shopItemList = new ArrayList<>();
        for (Object obj : cnf.getList("shopItems")) {
            shopItemList.add(new ShopItem((Map<String, Object>) obj));
        }
        return shopItemList;
    }

    public void setupInventory() {
        SimpleConfig cnf = GlobalCurrencyPlugin.getPlugin().cnf();
        shopInventory = Bukkit.createInventory(new ShopInventoryHolder(), cnf.getInt("shopInventorySize"), Utility.color(cnf.getString("shopInventoryTitle")));

        List<ShopItem> shopItemList = getShopItems();

        for (ShopItem shopItem : shopItemList) {
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

            shopInventory.setItem(shopItem.getSlot(), item);
        }

        for (int i = 0; i < shopInventory.getSize(); i++) {
            if (shopInventory.getItem(i) == null || shopInventory.getItem((i)).getType() == null || shopInventory.getItem(i).getType().equals(Material.AIR))
                shopInventory.setItem(i,
                        new ConfigItemStack(
                                cnf.getConfigurationSection("shopInventoryBorderGlass")
                                        .getValues(true))
                                .toItemStack());
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory() == null) return;
        if (e.getInventory().getHolder() instanceof ShopInventoryHolder) {
            e.setCancelled(true);
        }

        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        if (Material.AIR.equals(item.getType())) return;

        NBTItem nbtItem = new NBTItem(item);
        if (!nbtItem.hasTag("globalcurrency-id")) return;
        String id = nbtItem.getString("globalcurrency-id");

        GlobalCurrencyPlugin pl = GlobalCurrencyPlugin.getPlugin();

        List<ShopItem> shopItemList = getShopItems();

        ShopItem shopItem = shopItemList.stream()
                .filter(o -> o.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);

        int cost = shopItem.getCost();
        long balance = SQLManager.getInstance().getAmount(e.getWhoClicked().getUniqueId());
        if (balance < cost) {
            MsgUtil.msg(e.getWhoClicked(), pl.msg().getString("insufficientBalance"), new Pair<>("{amount}", balance + ""));
            return;
        }

        for (String command : shopItem.getCommands()) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", e.getWhoClicked().getName()));
        }

        MsgUtil.msg(e.getWhoClicked(), pl.msg().getString("youBought"),
                new Pair<>("{item}", shopItem.getDisplayName()),
                new Pair<>("{cost}", balance + "")
        );

        SQLManager.getInstance().removeAmount(e.getWhoClicked().getUniqueId(), cost);
        LogManager.getInstance().log(e.getWhoClicked().getName() + " has purchased " + shopItem.getId() + " for " + cost + " currency");
    }


}
