package com.turqmelon.MelonPerms.groups;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/


import com.turqmelon.MelonPerms.MelonPerms;
import com.turqmelon.MelonPerms.users.User;
import com.turqmelon.MelonPerms.users.UserManager;
import com.turqmelon.MelonPerms.util.MelonServer;
import com.turqmelon.MelonPerms.util.Privilege;
import com.turqmelon.MelonPerms.util.Track;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class Group implements Comparable {

    // The unique name of the group
    // Used for reference purposes and data purposes
    private String name;

    // The priority of the group
    // The lowest priority group is the "default" group
    // Higher priority groups will override the permission decisions of lower priority ones
    private int priority;

    // A list of defined permissions for this group
    private List<Privilege> privileges = new ArrayList<>();

    // A list of groups to inherit permissions from
    private List<Group> inheritance = new ArrayList<>();

    // A list of worlds where this group is valid.
    // If this list is empty, there are no world restrictions
    private List<World> worlds = new ArrayList<>();

    // A list of servers where this group is valid.
    // If this list is empty, there are no server restrictions
    private List<MelonServer> servers = new ArrayList<>();

    // The prefix and suffix to be used by other plugins
    private String metaPrefix = null;
    private String metaSuffix = null;

    public Group(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public boolean isDefault() {
        return GroupManager.getDefaultGroup() != null && GroupManager.getDefaultGroup().getName().equals(getName());
    }

    // Allows easily ordering groups by priority
    @Override
    public int compareTo(Object o) {

        if (o != null && (o instanceof Group)) {
            Group group = (Group) o;
            if (group.getPriority() > getPriority()) {
                return -1;
            } else if (group.getPriority() < getPriority()) {
                return 1;
            }
        }

        return 0;
    }

    // Safely deletes a group
    public void delete() {
        // Edits the cache to remove this group as an inheritance for any that have it
        GroupManager.getGroups().stream().filter(g -> g.getInheritance().contains(this)).forEach(g -> {
            g.getInheritance().remove(this);
            MelonPerms.getDataStore().saveGroup(g);
        });

        // Removes the group from any specific tracks
        GroupManager.getTracks().stream().filter(track -> track.getGroups().contains(this)).forEach(track -> {
            track.getGroups().remove(this);
            MelonPerms.getDataStore().saveTrack(track);
        });

        // Removes the group from any online users that have it.
        // Offline users will be updated next time they connect
        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = UserManager.getUser(player);
            if (user.getGroups().contains(this)) {
                user.getGroups().remove(this);
                MelonPerms.getDataStore().saveUser(user);
            }
        }
    }

    // Returns all privileges for a group, including privileges of groups it inherits
    // If a world is provided, it will only return privileges supporting that world
    // If a world is null, then all privileges will be returned (assuming they support the server)
    public List<Privilege> getAllPrivileges(World world) {
        List<Privilege> privileges = new ArrayList<>();
        privileges.addAll(getInheritedPrivileges(world).keySet());
        ploop:
        for (Privilege privilege : getPrivileges()) {
            if ((world != null && !privilege.isSupportingWorld(world)) || !privilege.isSupportingServer(MelonPerms.getLocalServer()))
                continue;
            for (Privilege p : privileges) {
                if (p.matches(privilege)) {
                    p.setNegated(privilege.isNegated());
                    continue ploop;
                }
            }
            privileges.add(privilege.copy());
        }
        return privileges;
    }

    // Returns a map of permissions for inherited groups, and the group the permission is from
    // If a world is provided, only permissions that support that world will be returned
    // If a world is null, then all permissions will be returned (assuming they support the server)
    public Map<Privilege, Group> getInheritedPrivileges(World world) {
        Map<Privilege, Group> privileges = new HashMap<>();

        List<Group> groups = new ArrayList<>();
        processInheritanceTree(world, groups, this);

        Collections.sort(groups);

        for (Group group : groups) {
            if ((world != null && !group.isSupportingWorld(world)) || !group.isSupportingServer(MelonPerms.getLocalServer()))
                continue;
            for (Privilege privilege : group.getPrivileges()) {
                for (Privilege p : privileges.keySet()) {
                    if (p.matches(privilege)) {
                        privilege.setNegated(p.isNegated());
                    }
                }
                privileges.put(privilege.copy(), group);
            }
        }

        return privileges;
    }

    // Gets a list of every single group to take inheritance from
    // If a world is supplied, only groups which support the world will be considered
    // If a world is null, all groups will be returned (assuming they support the server)
    private void processInheritanceTree(World world, List<Group> groups, Group group) {
        for (Group i : group.getInheritance()) {
            if ((world != null && !i.isSupportingWorld(world)) || !i.isSupportingServer(MelonPerms.getLocalServer()))
                continue;
            groups.add(i);
            if (i.getInheritance().size() > 0) {
                processInheritanceTree(world, groups, i);
            }
        }
    }

    // Returns a list of tracks that this group belongs to
    public List<Track> getTracks() {
        List<Track> tracks = new ArrayList<>();

        for (Track track : GroupManager.getTracks()) {
            if (track.getGroups().contains(this)) {
                tracks.add(track);
            }
        }

        return tracks;
    }

    public String getMetaPrefix() {
        return metaPrefix;
    }

    public void setMetaPrefix(String metaPrefix) {
        this.metaPrefix = metaPrefix;
    }

    public String getMetaSuffix() {
        return metaSuffix;
    }

    public void setMetaSuffix(String metaSuffix) {
        this.metaSuffix = metaSuffix;
    }

    // Returns true if there are world or server restrictions
    public boolean isConditional() {
        return getWorlds().size() > 0 || getServers().size() > 0;
    }

    // Returns true if the server list is empty, or contains the provided server
    public boolean isSupportingServer(MelonServer server) {
        if (getServers().size() == 0) return true;
        for (MelonServer s : getServers()) {
            if (server.getUuid().equals(s.getUuid())) {
                return true;
            }
        }
        return false;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    // Returns true if the world list is empty, or contains the provided world
    public boolean isSupportingWorld(World world) {
        if (getWorlds().size() == 0) return true;
        for (World w : getWorlds()) {
            if (world.getName().equalsIgnoreCase(w.getName())) {
                return true;
            }
        }
        return false;
    }

    // Returns the PRIVILEGE if a permission node is defined
    // Returns null if it isn't
    public Privilege getPrivilege(String node) {
        for (Privilege privilege : getPrivileges()) {
            if (privilege.getNode().equalsIgnoreCase(node)) {
                return privilege;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public List<Group> getInheritance() {
        return inheritance;
    }

    public List<World> getWorlds() {
        return worlds;
    }

    public List<MelonServer> getServers() {
        return servers;
    }
}
