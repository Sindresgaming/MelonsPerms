package com.turqmelon.MelonPerms.util;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/


import java.util.UUID;

// A unique server that runs MelonPerms.
// UUIDs are generated on first startup for future support for redis
public class MelonServer {

    private UUID uuid;
    private long lastHeartbeat = 0;

    public MelonServer(UUID uuid) {
        this.uuid = uuid;
    }

    public void setLastHeartbeat(long lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getLastHeartbeat() {
        return lastHeartbeat;
    }
}
