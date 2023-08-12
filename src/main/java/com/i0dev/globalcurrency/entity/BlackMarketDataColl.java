package com.i0dev.globalcurrency.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class BlackMarketDataColl extends Coll<BlackMarketData> {
    private static BlackMarketDataColl i = new BlackMarketDataColl();

    public static BlackMarketDataColl get() {
        return BlackMarketDataColl.i;
    }

    @Override
    public void onTick() {
        super.onTick();
    }

    @Override
    public void setActive(final boolean active) {
        super.setActive(active);
        if (!active) return;
        BlackMarketData.i = this.get(MassiveCore.INSTANCE, true);
    }
}
