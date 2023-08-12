package com.i0dev.globalcurrency.entity.object;

import com.i0dev.globalcurrency.entity.BlackMarketData;
import com.i0dev.globalcurrency.util.ItemBuilder;
import com.massivecraft.massivecore.util.MUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
@Data
public class BlackMarketShopItem {

    String id;
    Material material;
    String displayName;
    List<String> lore;
    int amount;
    int weight;
    boolean glow;
    List<String> commands;
    int limit;
    long price;

    public ItemStack getItemStack(Player observer) {
        List<String> newLore = MUtil.list();
        ItemBuilder itemBuilder = new ItemBuilder(material)
                .name(displayName)
                .addGlow(glow)
                .hideAllAttributes()
                .amount(amount);

        for (String line : lore) {
            newLore.add(line
                    .replace("%cost%", String.valueOf(price))
                    .replace("%limit%", String.valueOf(limit - BlackMarketData.get().getPurchaseAmounts(id))));
        }
        itemBuilder.lore(newLore);
        return itemBuilder;
    }


}
