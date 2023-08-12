package com.i0dev.globalcurrency.entity;

import com.massivecraft.massivecore.store.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class BlackMarketData extends Entity<BlackMarketData> {

    protected static BlackMarketData i;

    public static BlackMarketData get() {
        return i;
    }

    long lastRefreshTimeMillis = 0;
    List<String> items = new ArrayList<>();
    Map<String, Integer> purchaseAmounts = new HashMap<>();

    public void clearLogs() {
        purchaseAmounts.clear();
        this.changed();
    }

    public int getPurchaseAmounts(String shopItemId) {
        return purchaseAmounts.getOrDefault(shopItemId, 0);
    }

    public boolean isItemOnLimit(String shopItemId) {
        return getPurchaseAmounts(shopItemId) >= MConf.get().getLimit(shopItemId);
    }

    public void logPurchase(String shopItemId) {
        purchaseAmounts.put(shopItemId, getPurchaseAmounts(shopItemId) + 1);
        this.changed();
    }

    @Override
    public BlackMarketData load(BlackMarketData that) {
        super.load(that);
        return this;
    }

}


