package com.i0dev.plugin.globalcurrency.object;

import java.util.UUID;

public class QueuedMessage {
    UUID uuid;
    long time;
    long amountSpent;

    public long getAmountSpent() {
        return amountSpent;
    }

    public long getTime() {
        return time;
    }

    public UUID getUuid() {
        return uuid;
    }


    public QueuedMessage(UUID uuid, long time, long amountSpent) {
        this.uuid = uuid;
        this.time = time;
        this.amountSpent = amountSpent;
    }

    public void setAmountSpent(long amountSpent) {
        this.amountSpent = amountSpent;
    }
}