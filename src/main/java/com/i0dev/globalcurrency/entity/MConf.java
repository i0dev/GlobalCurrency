package com.i0dev.globalcurrency.entity;

import com.i0dev.globalcurrency.entity.object.*;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

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

    @Override
    public MConf load(MConf that) {
        super.load(that);
        return this;
    }

}
