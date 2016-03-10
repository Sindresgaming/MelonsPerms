package com.turqmelon.MelonPerms.data;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/

import com.turqmelon.MelonPerms.groups.Group;
import com.turqmelon.MelonPerms.users.User;
import com.turqmelon.MelonPerms.util.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// A data store is a method of saving information to file
// Simply create a new one and register it in MelonPerms.java to allow it to be used
public abstract class DataStore {

    private static List<DataStore> methods = new ArrayList<>();

    public static DataStore getMethod(String name) {
        for (DataStore store : getMethods()) {
            if (store.getName().equalsIgnoreCase(name)) {
                return store;
            }
        }
        return null;
    }

    public static List<DataStore> getMethods() {
        return methods;
    }

    private String name;

    public DataStore(String name) {
        this.name = name;
    }

    public abstract void initialize();

    public abstract void close();

    public abstract User loadUser(UUID uuid);

    public abstract User loadUser(String name);

    public abstract void saveUser(User user);

    public abstract void loadGroups();

    public abstract void loadTracks();

    public abstract void saveGroup(Group group);

    public abstract void saveTrack(Track track);

    public abstract void deleteGroup(Group group);

    public abstract void deleteTrack(Track track);

    public String getName() {
        return name;
    }
}
