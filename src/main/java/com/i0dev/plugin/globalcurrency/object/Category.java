package com.i0dev.plugin.globalcurrency.object;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC)
@NoArgsConstructor
public class Category implements SerializableConfig {


    public String id;
    public String inventoryTitle;
    public int slot;
    public String material;
    public int data;
    public boolean glow;
    public int amount;

    public int inventorySize;

    public List<String> lore;
    public String displayName;

    List<String> shopItemIds;

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> ret = new TreeMap<>();
        ret.put("id", id);
        ret.put("inventoryTitle", inventoryTitle);
        ret.put("slot", slot);
        ret.put("material", material);
        ret.put("data", data);
        ret.put("glow", glow);
        ret.put("amount", amount);
        ret.put("inventorySize", inventorySize);
        ret.put("lore", lore);
        ret.put("displayName", displayName);
        ret.put("shopItemIds", shopItemIds);
        return ret;
    }

    public Category(Map<String, Object> map) {
        id = (String) map.get("id");
        inventorySize = (int) map.get("inventorySize");
        slot = (int) map.get("slot");
        material = (String) map.get("material");
        data = (int) map.get("data");
        glow = (boolean) map.get("glow");
        amount = (int) map.get("amount");
        inventorySize = (int) map.get("inventorySize");
        lore = (List<String>) map.get("lore");
        displayName = (String) map.get("displayName");
        inventoryTitle = (String) map.get("inventoryTitle");
        shopItemIds = (List<String>) map.get("shopItemIds");
    }
}
