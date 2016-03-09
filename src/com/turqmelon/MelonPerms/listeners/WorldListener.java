package com.turqmelon.MelonPerms.listeners;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/

import com.turqmelon.MelonPerms.users.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import static com.turqmelon.MelonPerms.users.UserManager.getUser;
import static org.bukkit.event.EventPriority.MONITOR;

public class WorldListener implements Listener {

    // Rebuilds user permissions when a user swaps worlds
    @EventHandler(priority = MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        User user = getUser(player);
        if (user != null) {
            user.refreshPermissions(player.getWorld());
        }
    }

}
