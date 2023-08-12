package com.i0dev.globalcurrency.task;

import com.i0dev.globalcurrency.entity.BlackMarketData;
import com.i0dev.globalcurrency.entity.MConf;
import com.i0dev.globalcurrency.entity.MLang;
import com.i0dev.globalcurrency.entity.object.BlackMarketShopItem;
import com.i0dev.globalcurrency.util.RandomCollection;
import com.i0dev.globalcurrency.util.Utils;
import com.massivecraft.massivecore.ModuloRepeatTask;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskRefreshExtractShop extends ModuloRepeatTask {

    private static TaskRefreshExtractShop i = new TaskRefreshExtractShop();

    public static TaskRefreshExtractShop get() {
        return i;
    }


    @Override
    public long getDelayMillis() {
        return 10000L; // 10 second
    }

    @Override
    public void invoke(long l) {
        long refreshShopEveryXMillis = MConf.get().refreshShopEveryXMillis;
        if (BlackMarketData.get().getLastRefreshTimeMillis() + refreshShopEveryXMillis < System.currentTimeMillis()) {
            BlackMarketData.get().setLastRefreshTimeMillis(System.currentTimeMillis());
            refreshShop();
        }
    }


    public void refreshShop() {
        BlackMarketData data = BlackMarketData.get();
        data.clearLogs();
        data.setLastRefreshTimeMillis(System.currentTimeMillis());
        List<String> newItems = getNewRewards(Math.min(
                         MConf.get().blackMarketShopItemSlots.size(),
                        MConf.get().blackMarketShopItems.size())
                ).stream()
                        .map(BlackMarketShopItem::getId)
                        .collect(Collectors.toList());
        data.setItems(newItems);
        data.changed();
        if (MConf.get().broadcastWhenShopRefreshes)
            Bukkit.broadcastMessage(Utils.prefixAndColor(MLang.get().blackMarketShopRefreshedAnnouncement));
    }


    public List<BlackMarketShopItem> getNewRewards(int amount) {
        List<BlackMarketShopItem> ret = new ArrayList<>();
        List<BlackMarketShopItem> pool = new ArrayList<>(MConf.get().blackMarketShopItems);
        for (int i = 0; i < amount; i++) {
            RandomCollection<BlackMarketShopItem> randomCollection = RandomCollection.buildFromExtractShopConfig(pool);
            BlackMarketShopItem advancedItemConfig = randomCollection.next();
            ret.add(advancedItemConfig);
            pool.remove(advancedItemConfig);
        }
        return ret;
    }


}
