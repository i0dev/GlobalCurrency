package com.i0dev.globalcurrency.entity;

import com.i0dev.globalcurrency.entity.object.*;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;

@EditorName("config")
public class MConf extends Entity<MConf> {

    protected static transient MConf i;

    public static MConf get() {
        return i;
    }

    public List<String> aliasesGlobalCurrency = MUtil.list("medals", "globalcurrency");
    public boolean makeExample = true;
    public String serverID = "server1";
    public DatabaseInformation databaseInformation = new DatabaseInformation();
    public boolean closeBuyWindowOnPurchase = false;
    public ShopInventory shopInventory = new ShopInventory();
    public ConfirmationInventory confirmationInventory = new ConfirmationInventory();
    public BackToCategoriesItem backToCategoriesItem = new BackToCategoriesItem();

    public String blackMarketShopTitle = "Black Market";
    public int blackMarketShopSize = 27;
    public Material blackMarketShopBorderMaterial = Material.BLACK_STAINED_GLASS_PANE;
    public String blackMarketShopBorderName = "&f";
    public List<String> blackMarketShopBorderLore = MUtil.list();
    public boolean blackMarketShopBorderGlow = false;

    public int blackMarketShopBalanceItemSlot = 4;
    public ItemConfig blackMarketShopBalanceItem = new ItemConfig(
            Material.DIAMOND,
            "Rubies: %balance%",
            MUtil.list(),
            true
    );

    public int getLimit(String itemID) {
        for (BlackMarketShopItem item : blackMarketShopItems) {
            if (item.getId().equals(itemID)) {
                return item.getLimit();
            }
        }
        return 0;
    }

    public BlackMarketShopItem getShopItem(String id) {
        for (BlackMarketShopItem item : blackMarketShopItems) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public long refreshShopEveryXMillis = 21600000L; // 6 hours
    public boolean broadcastWhenShopRefreshes = true;

    public List<String> blackMarketActiveTimes = MUtil.list(
            "00:00-24:00"
    );

    public boolean isActiveTime() {
        Instant instant = Instant.ofEpochMilli(System.currentTimeMillis());
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant,  TimeZone.getTimeZone("EST").toZoneId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String time = localDateTime.format(formatter);
        for (String activeTime : blackMarketActiveTimes) {
            String[] split = activeTime.split("-");
            String start = split[0];
            String end = split[1];
            if (time.compareTo(start) >= 0 && time.compareTo(end) <= 0) {
                return true;
            }
        }
        return false;
    }


    public List<Integer> blackMarketShopItemSlots = MUtil.list(12, 13, 14);

    public List<BlackMarketShopItem> blackMarketShopItems = MUtil.list(
            new BlackMarketShopItem(
                    "diamond",
                    Material.DIAMOND,
                    "Diamond",
                    MUtil.list(
                            "&7Diamonds are forever!",
                            "",
                            "&cCosts: &a%cost% rubies",
                            "&cGlobal Limit remaining: &a%limit% purchases"
                    ),
                    100,
                    1,
                    true,
                    MUtil.list("give %player% diamond 1"),
                    5,
                    100
            ),
            new BlackMarketShopItem(
                    "emerald",
                    Material.EMERALD,
                    "Emerald",
                    MUtil.list(
                            "&7Diamonds are forever!",
                            "",
                            "&cCosts: &a%cost% rubies",
                            "&cGlobal Limit remaining: &a%limit% purchases"
                    ), 100,
                    1,
                    true,
                    MUtil.list("give %player% emerald 1"),
                    5,
                    100
            ),
            new BlackMarketShopItem(
                    "iron_ingot",
                    Material.IRON_INGOT,
                    "Iron Ingot",
                    MUtil.list(
                            "&7Diamonds are forever!",
                            "",
                            "&cCosts: &a%cost% rubies",
                            "&cGlobal Limit remaining: &a%limit% purchases"
                    ), 100,
                    1,
                    true,
                    MUtil.list("give %player% iron_ingot 1"),
                    5,
                    100
            ),
            new BlackMarketShopItem(
                    "gold_ingot",
                    Material.GOLD_INGOT,
                    "Gold Ingot",
                    MUtil.list(
                            "&7Diamonds are forever!",
                            "",
                            "&cCosts: &a%cost% rubies",
                            "&cGlobal Limit remaining: &a%limit% purchases"
                    ), 100,
                    1,
                    true,
                    MUtil.list("give %player% gold_ingot 1"),
                    5,
                    100
            )
    );

    @Override
    public MConf load(MConf that) {
        super.load(that);
        return this;
    }

}
