package com.i0dev.globalcurrency.cmd;

import com.i0dev.globalcurrency.Perm;
import com.i0dev.globalcurrency.action.ActionBuyBlackMarketShop;
import com.i0dev.globalcurrency.engine.EngineLog;
import com.i0dev.globalcurrency.engine.EngineSQL;
import com.i0dev.globalcurrency.entity.BlackMarketData;
import com.i0dev.globalcurrency.entity.MConf;
import com.i0dev.globalcurrency.entity.MLang;
import com.i0dev.globalcurrency.entity.object.BlackMarketShopItem;
import com.i0dev.globalcurrency.util.ItemBuilder;
import com.i0dev.globalcurrency.util.Pair;
import com.i0dev.globalcurrency.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.chestgui.ChestAction;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class CmdGlobalCurrencyBlackMarketShop extends GlobalCurrencyCommand {

    public CmdGlobalCurrencyBlackMarketShop() {
        this.addRequirements(RequirementIsPlayer.get());
    }

    @Override
    public void perform() throws MassiveException {

        if (!MConf.get().isActiveTime()) {
            msg(Utils.prefixAndColor(MLang.get().blackMarketShopDisabled));
            return;
        }


        reopenInventory(me);
    }


    public static Inventory getInventory(Player player) {
        ChestGui chestGui = getBasicChestGui(MConf.get().blackMarketShopTitle, MConf.get().blackMarketShopSize);

        ItemStack balanceInfoItem = MConf.get().blackMarketShopBalanceItem.getItemStack();
        ItemMeta meta = balanceInfoItem.getItemMeta();
        meta.setDisplayName(meta.getDisplayName().replace("%balance%", String.valueOf(EngineSQL.get().getAmount(player.getUniqueId()))));
        balanceInfoItem.setItemMeta(meta);
        chestGui.getInventory().setItem(MConf.get().blackMarketShopBalanceItemSlot, balanceInfoItem);

        int i = 0;
        for (Integer slot : MConf.get().blackMarketShopItemSlots) {
            BlackMarketShopItem item = MConf.get().getShopItem(BlackMarketData.get().getItems().get(i));
            chestGui.getInventory().setItem(slot, item.getItemStack(player));
            chestGui.setAction(slot, new ActionBuyBlackMarketShop(item));
            i++;
        }

        return chestGui.getInventory();
    }

    public static void reopenInventory(Player player) {
        player.openInventory(getInventory(player));
    }

    public static ChestGui getBasicChestGui(String title, int size) {
        Inventory inventory = Bukkit.createInventory(null, size, Txt.parse(title));
        ChestGui chestGui = ChestGui.getCreative(inventory);

        chestGui.setAutoclosing(false);
        chestGui.setAutoremoving(true);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);

        MConf cnf = MConf.get();

        IntStream.range(0, chestGui.getInventory().getSize()).forEach(i -> chestGui.getInventory().setItem(i, new ItemBuilder(cnf.blackMarketShopBorderMaterial)
                .amount(1)
                .name(cnf.blackMarketShopBorderName)
                .lore(cnf.blackMarketShopBorderLore)
                .addGlow(cnf.blackMarketShopBorderGlow)
        ));

        return chestGui;
    }

}
