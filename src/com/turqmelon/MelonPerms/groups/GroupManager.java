package com.turqmelon.MelonPerms.groups;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/


import com.turqmelon.MelonPerms.util.Track;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class GroupManager {

    // A list of all groups and tracks
    private static List<Group> groups = new ArrayList<>();
    private static List<Track> tracks = new ArrayList<>();

    // Returns the default group, defined by the group with the lowest priority
    public static Group getDefaultGroup() {
        Group group = null;
        for (Group g : getGroups()) {
            if (group == null || group.getPriority() > g.getPriority()) {
                group = g;
            }
        }
        return group;
    }

    // If world is nu
    public static List<Group> getGroups(World world) {
        List<Group> groups = new ArrayList<>();
        for (Group group : getGroups()) {
            if (group.isSupportingWorld(world)) {
                groups.add(group);
            }
        }
        return groups;
    }

    // Returns a track that matches the provided name
    public static Track getTrack(String name) {
        name = name.replace("_", " ");
        for (Track track : getTracks()) {
            if (track.getName().equalsIgnoreCase(name)) {
                return track;
            }
        }
        return null;
    }

    // Returns a group that matches the provided name
    public static Group getGroup(String name) {
        name = name.replace("_", " ");
        for (Group group : getGroups()) {
            if (group.getName().equalsIgnoreCase(name)) {
                return group;
            }
        }
        return null;
    }


    public static List<Track> getTracks() {
        return tracks;
    }

    public static List<Group> getGroups() {
        return groups;
    }
}
