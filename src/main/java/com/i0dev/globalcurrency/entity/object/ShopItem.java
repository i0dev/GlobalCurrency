package com.i0dev.globalcurrency.entity.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;


@AllArgsConstructor
@Getter
public class ShopItem {
    public String id;
    public String displayName;
    public Material material;
    public short slot;
    public short amount;
    public long price;
    public List<String> lore;
    public boolean glow;
    public List<String> commands;
    public String requiredPermissionToBuy;
    public String preventBuyIfHasPermission;
    public int limitPerPlayer;
    public long limitCheckBackMillis;
}
