package com.i0dev.globalcurrency.util;

import com.i0dev.globalcurrency.entity.object.BlackMarketShopItem;

import java.util.List;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<>();
    private final Random random;
    private double total = 0;

    public RandomCollection() {
        this(new Random());
    }

    public RandomCollection(Random random) {
        this.random = random;
    }

    public RandomCollection<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, result);
        return this;
    }

    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }

    public static RandomCollection<BlackMarketShopItem> buildFromExtractShopConfig(List<BlackMarketShopItem> pool) {
        RandomCollection<BlackMarketShopItem> rc = new RandomCollection<>();

        pool.forEach(extractShopItem -> {
            int weight = extractShopItem.getWeight();
            rc.add(weight, extractShopItem);
        });

        return rc;
    }


}