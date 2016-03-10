package com.turqmelon.MelonPerms.users;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/


import com.turqmelon.MelonPerms.MelonPerms;
import com.turqmelon.MelonPerms.groups.Group;
import com.turqmelon.MelonPerms.groups.GroupManager;
import com.turqmelon.MelonPerms.util.Privilege;
import org.bukkit.World;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class User {

    // A user's unique mojang ID
    private UUID uuid;

    // The last known username of a player
    // If it was changed, it'll be updated next time they login
    private String name;

    // The list of user defined permissions
    private List<Privilege> privileges = new ArrayList<>();

    // The list of groups a user belong to
    private List<Group> groups = new ArrayList<>();

    // Super Users have full access to plugin controls
    // A user can be defined as a super user via console
    private boolean superUser = false;

    // A user's prefix and suffix, to be used by other plugins
    // User-defined prefix/suffixes will override group defined ones.
    private String metaPrefix = null;
    private String metaSuffix = null;

    // Bukkit's permission attachment
    private PermissionAttachment attachment = null;

    public User(UUID uuid) {
        this.uuid = uuid;
        this.name = null;
    }

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    // Returns the user's prefix, respecting group priority
    // Returns an empty string if there is none
    public String getPrefix() {
        if (getMetaPrefix() != null) {
            return getMetaPrefix();
        }
        List<Group> groups = getGroups();
        Collections.sort(groups);
        if (groups.size() > 0) {
            Group group = groups.get(groups.size() - 1);
            if (group.getMetaPrefix() != null) {
                return group.getMetaPrefix();
            }
        }
        return "";
    }

    // Returns the user's suffix, respecting group priority
    // Returns an empty string if there is none
    public String getSuffix() {
        if (getMetaSuffix() != null) {
            return getMetaSuffix();
        }
        List<Group> groups = getGroups();
        Collections.sort(groups);
        if (groups.size() > 0) {
            Group group = groups.get(groups.size() - 1);
            if (group.getMetaSuffix() != null) {
                return group.getMetaSuffix();
            }
        }
        return "";
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

    // Denied all currently defined permissions by the attachment, then recalculates
    public void refreshPermissions(World world) {
        for (String p : getAttachment().getPermissions().keySet()) {
            getAttachment().setPermission(p, false);
        }
        for (Privilege privilege : getAllPrivileges(world)) {
            if (privilege.isSupportingServer(MelonPerms.getLocalServer()) && (world == null || privilege.isSupportingWorld(world))) {
                getAttachment().setPermission(privilege.getNode(), !privilege.isNegated());
            }
        }
    }

    public PermissionAttachment getAttachment() {
        return attachment;
    }

    public void setAttachment(PermissionAttachment attachment) {
        this.attachment = attachment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSuperUser(boolean superUser) {
        this.superUser = superUser;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    // Matches a permission node to a user-defiend privilege
    // Returns null if there isn't one defined in this scope
    public Privilege getPrivilege(String node) {
        for (Privilege privilege : getPrivileges()) {
            if (privilege.getNode().equalsIgnoreCase(node)) {
                return privilege;
            }
        }
        return null;
    }

    // Returns a list of ALL user permissions, including groups and group inherited permissions, respecting priority
    // If a world is supplied, only permissions supporting the world will be returned
    // If a world is null, all permissions will be returned (assuming they support the current server)
    public List<Privilege> getAllPrivileges(World world) {
        List<Privilege> privileges = new ArrayList<>();
        List<Group> groups = getGroups();
        Collections.sort(groups);
        for (Group group : groups) {
            if ((world != null && !group.isSupportingWorld(world)) || !group.isSupportingServer(MelonPerms.getLocalServer()))
                continue;
            ploop:
            for (Privilege privilege : group.getAllPrivileges(world)) {
                for (Privilege p : privileges) {
                    if (p.matches(privilege)) {
                        p.setNegated(privilege.isNegated());
                        continue ploop;
                    }
                }
                privileges.add(privilege.copy());
            }
        }
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

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    // Gets all groups for this user that support the provided world
    public List<Group> getGroups(World world) {
        List<Group> groups = new ArrayList<>();
        groups.addAll(getGroups());

        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            if (GroupManager.getDefaultGroup() != null && GroupManager.getDefaultGroup().getName().equals(group.getName())) {
                continue;
            }
            if (world != null) {
                if (!group.isSupportingWorld(world)) {
                    groups.remove(group);
                    continue;
                }
            }
            if (!group.isSupportingServer(MelonPerms.getLocalServer())) {
                groups.remove(group);
            }
        }

        return groups;
    }

    // Returns a list of user groups, ensuring that the default group is always there.
    public List<Group> getGroups() {
        if (GroupManager.getDefaultGroup() != null && !groups.contains(GroupManager.getDefaultGroup())) {
            groups.add(GroupManager.getDefaultGroup());
        }
        return groups;
    }

    public boolean isSuperUser() {
        return superUser;
    }
}
