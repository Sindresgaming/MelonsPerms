package com.turqmelon.MelonPerms.users;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/


import com.turqmelon.MelonPerms.MelonPerms;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserManager {

    // A list of all currently online users
    private static List<User> users = new ArrayList<>();

    public static User getUser(Player player) {
        return getUser(player.getName());
    }

    // Matches an online user to UUID
    // If none is found, searches DataStore
    public static User getUser(UUID uuid) {
        for (User user : getUsers()) {
            if (user.getUuid().equals(uuid)) {
                return user;
            }
        }
        return MelonPerms.getDataStore().loadUser(uuid);
    }

    // Matches an online user by name
    // If none is found, searches DataStore
    public static User getUser(String name) {
        for (User user : getUsers()) {
            if (user.getName() != null && user.getName().equalsIgnoreCase(name)) {
                return user;
            }
        }

        return MelonPerms.getDataStore().loadUser(name);
    }

    public static List<User> getUsers() {
        return users;
    }
}
