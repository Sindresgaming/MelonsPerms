package com.turqmelon.MelonPerms.listeners;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/


import com.turqmelon.MelonPerms.MelonPerms;
import com.turqmelon.MelonPerms.groups.Group;
import com.turqmelon.MelonPerms.groups.GroupManager;
import com.turqmelon.MelonPerms.users.User;
import com.turqmelon.MelonPerms.users.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.logging.Level;

public class JoinListener implements Listener {

    // Fired when a user logs in, or if the plugin is reloaded to online players
    public static void userLoginCheck(String name, UUID uuid) {
        User user = UserManager.getUser(uuid);
        if (user == null) {
            // Initializes a new user and saves them to the datastore
            user = new User(uuid, name);
            MelonPerms.getDataStore().saveUser(user);
            MelonPerms.getInstance().getLogger().log(Level.INFO, "Created New User: " + user.getName() + " (" + user.getUuid() + ")");
        }
        // Updates the user's cached name in case of a name change, then saves the user's new name
        else if (!user.getName().equals(name)) {
            user.setName(name);
            MelonPerms.getDataStore().saveUser(user);
            MelonPerms.getInstance().getLogger().log(Level.INFO, "Name change detected, updated user " + user.getName() + " (" + user.getUuid() + ")");
        }

        // Adds the user to the online users
        UserManager.getUsers().add(user);
    }

    // Establishes permissions without interrupting server thread
    // Ensures player will have user data for login
    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        String name = event.getName();
        UUID uuid = event.getUniqueId();

        userLoginCheck(name, uuid);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        User user = UserManager.getUser(player);
        // If the server was not able to pull data, reject the login
        // The plugin was likely unable to communicate with the datastore
        if (user == null) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§c§l[MP] §cNo user data. Please contact an admin.");
            return;
        }
        // Register a permission attachment with bukkit and refresh the permissions
        user.setAttachment(player.addAttachment(MelonPerms.getInstance()));

        // We don't have a world yet, we'll refresh again when a player has fully logged in
        user.refreshPermissions(null);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = UserManager.getUser(player);
        // Unload user data if they exist
        if (user != null) {
            if (user.getAttachment() != null) {
                player.removeAttachment(user.getAttachment());
                user.setAttachment(null);
            }
            UserManager.getUsers().remove(user);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Group group = GroupManager.getDefaultGroup();
        Player player = event.getPlayer();
        // If there is no default group, notify the OP to create one
        if (group == null) {
            if (player.isOp()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.sendMessage("§c§l[MP] §cYou don't have a default group.");
                        player.sendMessage("§c§l[MP] §cYou can fix this by simply making a group. The default group is automatically the one with the lowest priority.");
                    }
                }.runTaskLater(MelonPerms.getInstance(), 40L);
            }
        }
        // Rebuild user permissions with their current world
        User user = UserManager.getUser(player);
        if (user != null) {
            user.refreshPermissions(player.getWorld());
        }

    }

}
