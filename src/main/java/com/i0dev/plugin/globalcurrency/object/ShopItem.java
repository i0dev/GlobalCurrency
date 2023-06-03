package com.i0dev.plugin.globalcurrency.object;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
@FieldDefaults(level = AccessLevel.PUBLIC)
@AllArgsConstructor
public class ShopItem {

    public String id;
    public int slot;
    public String material;
    public int data;
    public int amount;

    public List<String> lore;
    public String displayName;
    public int cost;
    public boolean glow;
    List<String> commands;

    public ShopItem(Map<String, Object> map) {
        id = (String) map.get("id");
        slot = (int) map.get("slot");
        material = (String) map.get("material");
        amount = (int) map.get("amount");
        lore = (List<String>) map.get("lore");
        displayName = (String) map.get("displayName");
        cost = (int) map.get("cost");
        glow = (boolean) map.get("glow");
        commands = (List<String>) map.get("commands");
    }

}
