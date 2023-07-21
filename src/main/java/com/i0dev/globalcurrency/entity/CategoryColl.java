package com.i0dev.globalcurrency.entity;

import com.massivecraft.massivecore.store.Coll;

public class CategoryColl extends Coll<Category> {

    private static final CategoryColl i = new CategoryColl();

    public static CategoryColl get() {
        return i;
    }

    @Override
    public void onTick() {
        super.onTick();
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
    }
}