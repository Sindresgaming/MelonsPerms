package com.turqmelon.MelonPerms.data;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/

import com.turqmelon.MelonPerms.MelonPerms;
import com.turqmelon.MelonPerms.groups.Group;
import com.turqmelon.MelonPerms.groups.GroupManager;
import com.turqmelon.MelonPerms.users.User;
import com.turqmelon.MelonPerms.util.MelonServer;
import com.turqmelon.MelonPerms.util.Privilege;
import com.turqmelon.MelonPerms.util.Track;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

// Allows saving permissions and users to a yml file in the plugin's directory
public final class YamlStorage extends DataStore {

    private YamlConfiguration config;
    private File file;

    public YamlStorage(File file) {
        super("yaml");
        this.file = file;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public File getFile() {
        return file;
    }

    // Creates and loads necessary storage files
    @Override
    public void initialize() {
        this.config = new YamlConfiguration();
        if (!getFile().exists()) {
            try {
                if (getFile().createNewFile()) {
                    MelonPerms.getInstance().getLogger().log(Level.INFO, "Created storage file: " + getFile().getName());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            getConfig().load(getFile());
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // Writes changes to disk
    public void save() {
        try {
            getConfig().save(getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        save();
    }

    // Loads user data from configuration
    private User loadUserData(UUID uuid) {
        try {
            String path = "users." + uuid.toString();

            String name = getConfig().getString(path + ".name");

            if (name == null)
                return null;

            List<Privilege> privileges = getConfig().getStringList(path + ".privileges").stream().map(p -> new Privilege(p.split(":"))).collect(Collectors.toList());
            List<Group> groups = new ArrayList<>();
            for (String g : getConfig().getStringList(path + ".groups")) {
                Group group = GroupManager.getGroup(g);
                if (group != null && !group.isDefault()) {
                    groups.add(group);
                }
            }

            User user = new User(uuid, name);
            user.getGroups().addAll(groups);
            user.getPrivileges().addAll(privileges);
            user.setSuperUser(getConfig().getBoolean(path + ".super"));
            user.setMetaPrefix(getConfig().getString(path + ".meta.prefix"));
            user.setMetaSuffix(getConfig().getString(path + ".meta.suffix"));

            return user;
        } catch (Exception ex) {
            return null;
        }
    }

    // Passes UUID to private method
    @Override
    public User loadUser(UUID uuid) {
        return loadUserData(uuid);
    }

    // SLOW - Go by UUID if possible, resolves username to UUID, if no match, returns null
    @Override
    public User loadUser(String name) {

        UUID uuid = null;
        ConfigurationSection section = getConfig().getConfigurationSection("users");
        if (section != null) {
            Set<String> uuids = section.getKeys(false);
            if (uuids != null) {
                for (String id : uuids) {
                    String path = "users." + id;
                    String n = getConfig().getString(path + ".name");
                    if (n.equalsIgnoreCase(name)) {
                        uuid = UUID.fromString(id);
                        break;
                    }
                }
            }

        }

        return uuid != null ? loadUserData(uuid) : null;
    }

    // Writes user data to file
    @Override
    public void saveUser(User user) {

        String path = "users." + user.getUuid().toString();

        getConfig().set(path + ".name", user.getName());
        getConfig().set(path + ".uuid", user.getUuid().toString());
        getConfig().set(path + ".super", user.isSuperUser());
        List<String> privs = user.getPrivileges().stream().map(Privilege::toString).collect(Collectors.toList());
        getConfig().set(path + ".privileges", privs);
        List<String> groups = user.getGroups().stream().map(Group::getName).collect(Collectors.toList());
        getConfig().set(path + ".groups", groups);
        getConfig().set(path + ".meta.prefix", user.getMetaPrefix());
        getConfig().set(path + ".meta.suffix", user.getMetaSuffix());


        save();
    }

    @Override
    public void loadGroups() {


        ConfigurationSection section = getConfig().getConfigurationSection("groups");
        if (section != null) {
            Set<String> names = section.getKeys(false);
            if (names != null) {

                for (String name : names) {
                    GroupManager.getGroups().add(new Group(name, 0));
                }

                for (String name : names) {
                    Group group = GroupManager.getGroup(name);
                    if (group == null) continue;
                    String path = "groups." + group.getName();
                    group.setPriority(getConfig().getInt(path + ".priority"));
                    group.setMetaPrefix(getConfig().getString(path + ".meta.prefix"));
                    group.setMetaSuffix(getConfig().getString(path + ".meta.suffix"));
                    List<Privilege> privileges = getConfig().getStringList(path + ".privileges").stream().map(priv -> new Privilege(priv.split(":"))).collect(Collectors.toList());
                    group.getPrivileges().addAll(privileges);
                    for (String inherit : getConfig().getStringList(path + ".inherit")) {
                        Group i = GroupManager.getGroup(inherit);
                        if (i != null) {
                            group.getInheritance().add(i);
                        }
                    }
                    for (String worldName : getConfig().getStringList(path + ".worlds")) {
                        World world = Bukkit.getWorld(worldName);
                        if (world != null) {
                            group.getWorlds().add(world);
                        }
                    }
                    for (String serverUUID : getConfig().getStringList(path + ".servers")) {
                        MelonServer server = new MelonServer(UUID.fromString(serverUUID));
                        group.getServers().add(server);
                    }
                }

            }
        }


    }

    @Override
    public void loadTracks() {

        ConfigurationSection section = getConfig().getConfigurationSection("tracks");
        if (section != null) {
            Set<String> names = section.getKeys(false);
            if (names != null) {
                for (String name : names) {
                    Track track = new Track(name);
                    String path = "tracks." + track.getName();
                    for (String groupName : getConfig().getStringList(path + ".groups")) {
                        Group group = GroupManager.getGroup(groupName);
                        if (group != null) {
                            track.getGroups().add(group);
                        }
                    }
                    track.setDefaultTrack(getConfig().getBoolean(path + ".isdefault"));
                    GroupManager.getTracks().add(track);
                }
            }
        }

    }

    @Override
    public void saveGroup(Group group) {

        String path = "groups." + group.getName();
        getConfig().set(path + ".priority", group.getPriority());
        List<String> privs = group.getPrivileges().stream().map(Privilege::toString).collect(Collectors.toList());
        getConfig().set(path + ".privileges", privs);
        List<String> inherit = group.getInheritance().stream().map(Group::getName).collect(Collectors.toList());
        getConfig().set(path + ".inherit", inherit);
        List<String> worlds = group.getWorlds().stream().map(World::getName).collect(Collectors.toList());
        getConfig().set(path + ".worlds", worlds);
        List<String> servers = group.getServers().stream().map(server -> server.getUuid().toString()).collect(Collectors.toList());
        getConfig().set(path + ".servers", servers);
        getConfig().set(path + ".meta.prefix", group.getMetaPrefix());
        getConfig().set(path + ".meta.suffix", group.getMetaSuffix());

        save();

    }

    @Override
    public void saveTrack(Track track) {

        String path = "tracks." + track.getName();
        List<String> groups = track.getGroups().stream().map(Group::getName).collect(Collectors.toList());
        getConfig().set(path + ".groups", groups);
        getConfig().set(path + ".isdefault", track.isDefaultTrack());

        save();

    }

    @Override
    public void deleteGroup(Group group) {
        String path = "groups." + group.getName();
        getConfig().set(path, null);
        save();
    }

    @Override
    public void deleteTrack(Track track) {
        String path = "tracks." + track.getName();
        getConfig().set(path, null);
        save();
    }
}
