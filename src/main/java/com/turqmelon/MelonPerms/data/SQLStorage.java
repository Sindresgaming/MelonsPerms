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
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

// Allows saving permissions and users to a MySQL database
public class SQLStorage extends DataStore {

    private Connection connection;

    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
    private String tablePrefix;

    public SQLStorage(String name, String host, int port, String database, String username, String password, String tablePrefix) {
        super(name);
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.tablePrefix = tablePrefix;
    }

    @Override
    public void initialize() {

        MelonPerms.getInstance().getLogger().log(Level.INFO, "Establishing database connection...");
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase(), getUsername(), getPassword());

            MelonPerms.getInstance().getLogger().log(Level.INFO, "Connection successful! Checking tables...");

            PreparedStatement stmt = getConnection().prepareStatement("" +
                    "CREATE TABLE IF NOT EXISTS `" + getTablePrefix() + "users` ( `id` BIGINT NOT NULL AUTO_INCREMENT , `uuid` VARCHAR(255) NOT NULL , `name` VARCHAR(255) NOT NULL , `data` TEXT NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;");

            stmt.execute();
            stmt.close();

            stmt = getConnection().prepareStatement("" +
                    "CREATE TABLE IF NOT EXISTS `" + getTablePrefix() + "tracks` ( `id` BIGINT NOT NULL AUTO_INCREMENT , `name` VARCHAR(255) NOT NULL , `data` TEXT NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
            stmt.execute();
            stmt.close();

            stmt = getConnection().prepareStatement("" +
                    "CREATE TABLE IF NOT EXISTS `" + getTablePrefix() + "groups` ( `id` BIGINT NOT NULL AUTO_INCREMENT , `name` VARCHAR(255) NOT NULL , `data` TEXT NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
            stmt.execute();
            stmt.close();

            MelonPerms.getInstance().getLogger().log(Level.INFO, "SQL startup complete.");


        } catch (SQLException e) {
            MelonPerms.getInstance().getLogger().log(Level.SEVERE, "Failed to open SQL connection: " + e.getMessage());
        }

        int mins = MelonPerms.getInstance().getConfig().getInt("storage.sql.sync-minutes");
        if (mins > 0) {
            long secs = mins * 60;
            secs = secs * 20;
            new BukkitRunnable() {
                @Override
                public void run() {
                    MelonPerms.getInstance().getLogger().log(Level.INFO, "Resyncing data...");
                    loadGroups();
                    loadTracks();
                }
            }.runTaskTimerAsynchronously(MelonPerms.getInstance(), secs, secs);
        }

    }

    @Override
    public void close() {
        try {
            if (!getConnection().isClosed()) {
                getConnection().close();
            }
        } catch (SQLException e) {
            MelonPerms.getInstance().getLogger().log(Level.SEVERE, "Failed to properly close SQL connection: " + e.getMessage());
        }
    }

    @Override
    public User loadUser(UUID uuid) {

        try {
            PreparedStatement stmt = getConnection().prepareStatement("" +
                    "SELECT * FROM " + getTablePrefix() + "users WHERE uuid = ? LIMIT 1;");
            stmt.setString(1, uuid.toString());
            ResultSet set = stmt.executeQuery();
            return getSQLUser(set);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User loadUser(String name) {

        try {
            PreparedStatement stmt = getConnection().prepareStatement("" +
                    "SELECT * FROM " + getTablePrefix() + "users WHERE name = ? LIMIT 1;");
            stmt.setString(1, name);
            ResultSet set = stmt.executeQuery();
            return getSQLUser(set);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private User getSQLUser(ResultSet set) {
        try {
            if (set.next()) {

                String name = set.getString("name");
                UUID uuid = UUID.fromString(set.getString("uuid"));
                String json = set.getString("data");

                JSONParser parser = new JSONParser();

                Object obj = parser.parse(json);
                JSONObject data = (JSONObject) obj;

                String prefix = (String) data.get("prefix");
                String suffix = (String) data.get("suffix");
                boolean superUser = (boolean) data.get("super");

                JSONArray privileges = (JSONArray) data.get("privileges");
                JSONArray groups = (JSONArray) data.get("groups");

                User user = new User(uuid, name);
                user.setMetaPrefix(prefix);
                user.setMetaSuffix(suffix);
                user.setSuperUser(superUser);

                for (Object priv : privileges) {
                    user.getPrivileges().add(new Privilege(((String) priv).split(":")));
                }

                for (Object gr : groups) {
                    Group group = GroupManager.getGroup((String) gr);
                    if (group != null) {
                        user.getGroups().add(group);
                    }
                }

                return user;

            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveUser(User user) {

        JSONObject obj = new JSONObject();
        obj.put("prefix", user.getMetaPrefix());
        obj.put("suffix", user.getMetaSuffix());
        obj.put("super", user.isSuperUser());

        JSONArray privileges = user.getPrivileges().stream().map(Privilege::toString).collect(Collectors.toCollection(JSONArray::new));

        obj.put("privileges", privileges);

        JSONArray groups = new JSONArray();
        for (Group group : user.getGroups()) {
            groups.add(group.getName());
        }

        obj.put("groups", groups);

        String json = obj.toJSONString();

        try {
            PreparedStatement stmt = getConnection().prepareStatement(
                    "SELECT * FROM " + getTablePrefix() + "users WHERE uuid = ? LIMIT 1;");
            stmt.setString(1, user.getUuid().toString());
            ResultSet set = stmt.executeQuery();
            if (set.next()) {
                stmt.close();
                stmt = getConnection().prepareStatement("UPDATE " + getTablePrefix() + "users " +
                        "SET name = ?, data = ? WHERE uuid = ?;");
                stmt.setString(1, user.getName());
                stmt.setString(2, json);
                stmt.setString(3, user.getUuid().toString());
                stmt.execute();
                stmt.close();
            } else {
                stmt.close();
                stmt = getConnection().prepareStatement("INSERT INTO " + getTablePrefix() + "users " +
                        "(uuid, name, data) VALUES (?, ?, ?);");
                stmt.setString(1, user.getUuid().toString());
                stmt.setString(2, user.getName());
                stmt.setString(3, json);
                stmt.execute();
                stmt.close();
            }
            set.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void loadGroups() {

        try {
            List<String> downloadedNames = new ArrayList<>();
            PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM " + getTablePrefix() + "groups;");
            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                String name = set.getString("name");
                downloadedNames.add(name);
                if (GroupManager.getGroup(name) == null) {
                    GroupManager.getGroups().add(new Group(name, 0));
                }
            }
            set.close();
            stmt.close();
            if (downloadedNames.size() > 0) {
                for (int i = 0; i < GroupManager.getGroups().size(); i++) {
                    Group group = GroupManager.getGroups().get(i);
                    if (!downloadedNames.contains(group.getName())) {
                        group.delete();
                        GroupManager.getGroups().remove(group); // Group was deleted
                    }
                }
            }
            for (Group group : GroupManager.getGroups()) {
                stmt = getConnection().prepareStatement("SELECT * FROM " + getTablePrefix() + "groups WHERE name = ? LIMIT 1;");
                stmt.setString(1, group.getName());
                set = stmt.executeQuery();
                if (set.next()) {

                    String json = set.getString("data");
                    JSONParser parser = new JSONParser();

                    Object obj = parser.parse(json);
                    JSONObject data = (JSONObject) obj;

                    group.setPriority((int) data.get("priority"));

                    group.getPrivileges().clear();

                    for (Object p : (JSONArray) data.get("privileges")) {
                        group.getPrivileges().add(new Privilege(((String) p).split(":")));
                    }

                    group.getInheritance().clear();

                    for (Object i : (JSONArray) data.get("inherit")) {
                        Group ih = GroupManager.getGroup((String) i);
                        if (ih != null) {
                            group.getInheritance().add(ih);
                        }
                    }

                    group.getServers().clear();
                    group.getWorlds().clear();

                    for (Object w : (JSONArray) data.get("worlds")) {
                        World world = Bukkit.getWorld((String) w);
                        if (world != null) {
                            group.getWorlds().add(world);
                        }
                    }

                    for (Object s : (JSONArray) data.get("servers")) {
                        MelonServer server = new MelonServer(UUID.fromString((String) s));
                        group.getServers().add(server);
                    }

                    group.setMetaPrefix((String) data.get("prefix"));
                    group.setMetaSuffix((String) data.get("suffix"));

                }
                set.close();
                stmt.close();
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void saveGroup(Group group) {

        JSONObject obj = new JSONObject();
        obj.put("priority", group.getPriority());

        JSONArray privileges = group.getPrivileges().stream().map(Privilege::toString).collect(Collectors.toCollection(JSONArray::new));
        JSONArray inherit = group.getInheritance().stream().map(Group::getName).collect(Collectors.toCollection(JSONArray::new));
        JSONArray worlds = group.getWorlds().stream().map(World::getName).collect(Collectors.toCollection(JSONArray::new));
        JSONArray servers = group.getServers().stream().map(server -> server.getUuid().toString()).collect(Collectors.toCollection(JSONArray::new));

        obj.put("privileges", privileges);
        obj.put("inherit", inherit);
        obj.put("worlds", worlds);
        obj.put("servers", servers);

        obj.put("prefix", group.getMetaPrefix());
        obj.put("suffix", group.getMetaSuffix());

        String json = obj.toJSONString();

        try {
            PreparedStatement stmt = getConnection().prepareStatement("" +
                    "SELECT * FROM " + getTablePrefix() + "groups WHERE name = ? LIMIT 1;");
            stmt.setString(1, group.getName());
            ResultSet set = stmt.executeQuery();
            if (set.next()) {
                stmt.close();
                stmt = getConnection().prepareStatement("UPDATE " + getTablePrefix() + "groups SET data = ? WHERE name = ?;");
                stmt.setString(1, json);
                stmt.setString(2, group.getName());
                stmt.execute();
                stmt.close();
            } else {
                stmt.close();
                stmt = getConnection().prepareStatement("INSERT INTO " + getTablePrefix() + "groups (name, data) VALUES (?, ?);");
                stmt.setString(1, group.getName());
                stmt.setString(2, json);
                stmt.execute();
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void loadTracks() {

        GroupManager.getTracks().clear();

        try {
            PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM " + getTablePrefix() + "tracks;");
            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                String name = set.getString("name");
                String json = set.getString("data");

                JSONParser parser = new JSONParser();

                Object obj = parser.parse(json);
                JSONObject data = (JSONObject) obj;

                boolean isdefault = (boolean) data.get("isdefault");

                JSONArray groups = (JSONArray) data.get("groups");

                Track track = new Track(name);
                track.setDefaultTrack(isdefault);

                for (Object g : groups) {
                    Group group = GroupManager.getGroup((String) g);
                    if (group != null) {
                        track.getGroups().add(group);
                    }
                }

                GroupManager.getTracks().add(track);

            }
            set.close();
            stmt.close();
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void saveTrack(Track track) {

        JSONObject obj = new JSONObject();
        obj.put("isdefault", track.isDefaultTrack());

        JSONArray groups = new JSONArray();

        for (Group group : track.getGroups()) {
            groups.add(group.getName());
        }

        obj.put("groups", groups);

        String json = obj.toJSONString();

        try {
            PreparedStatement stmt = getConnection().prepareStatement(
                    "SELECT * FROM " + getTablePrefix() + "tracks WHERE name = ? LIMIT 1;"
            );
            stmt.setString(1, track.getName());
            ResultSet set = stmt.executeQuery();
            if (set.next()) {
                stmt.close();
                stmt = getConnection().prepareStatement("" +
                        "UPDATE " + getTablePrefix() + "tracks SET data = ? WHERE name = ?;");
                stmt.setString(1, json);
                stmt.setString(2, track.getName());
                stmt.execute();
                stmt.close();
            } else {
                stmt.close();
                stmt = getConnection().prepareStatement("" +
                        "INSERT INTO " + getTablePrefix() + "tracks (name, data) VALUES (?, ?);");
                stmt.setString(1, track.getName());
                stmt.setString(2, json);
                stmt.execute();
                stmt.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteGroup(Group group) {
        try {
            PreparedStatement stmt = getConnection().prepareStatement(
                    "DELETE FROM " + getTablePrefix() + "groups WHERE name = ?;"
            );
            stmt.setString(1, group.getName());
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTrack(Track track) {
        try {
            PreparedStatement stmt = getConnection().prepareStatement(
                    "DELETE FROM " + getTablePrefix() + "tracks WHERE name = ?;"
            );
            stmt.setString(1, track.getName());
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public Connection getConnection() {
        return connection;
    }
}
