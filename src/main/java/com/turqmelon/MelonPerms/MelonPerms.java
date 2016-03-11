package com.turqmelon.MelonPerms;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/

import com.turqmelon.MelonPerms.commands.CommandManager;
import com.turqmelon.MelonPerms.commands.MasterCommand;
import com.turqmelon.MelonPerms.commands.groupcommands.*;
import com.turqmelon.MelonPerms.commands.trackcommands.*;
import com.turqmelon.MelonPerms.commands.usercommands.*;
import com.turqmelon.MelonPerms.data.DataStore;
import com.turqmelon.MelonPerms.data.MySQLStorage;
import com.turqmelon.MelonPerms.data.SQLiteDataStore;
import com.turqmelon.MelonPerms.data.YamlStorage;
import com.turqmelon.MelonPerms.groups.GroupManager;
import com.turqmelon.MelonPerms.listeners.JoinListener;
import com.turqmelon.MelonPerms.listeners.WorldListener;
import com.turqmelon.MelonPerms.users.User;
import com.turqmelon.MelonPerms.users.UserManager;
import com.turqmelon.MelonPerms.util.MelonServer;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;

public class MelonPerms extends JavaPlugin {

    // The instance of the plugin
    private static MelonPerms instance;

    // This server
    private static MelonServer localServer;

    // The selected data store
    private static DataStore dataStore = null;

    @Override
    public void onDisable() {

        // If there's a datastore, close and run any necessary completion tasks
        if (getDataStore() != null) {
            getLogger().log(Level.INFO, "Closing data store...");
            getDataStore().close();
        }
    }

    @Override
    public void onEnable() {
        instance = this;

        // Save the default config if it doesn't exist
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            saveDefaultConfig();
        }

        // Register available DataStore methods
        DataStore.getMethods().add(new YamlStorage(new File(getDataFolder(), getConfig().getString("storage.yaml.file"))));
        DataStore.getMethods().add(new MySQLStorage(
                getConfig().getString("storage.mysql.host"),
                getConfig().getInt("storage.mysql.port"),
                getConfig().getString("storage.mysql.database"),
                getConfig().getString("storage.mysql.username"),
                getConfig().getString("storage.mysql.password"),
                getConfig().getString("storage.mysql.table-prefix")
        ));
        DataStore.getMethods().add(new SQLiteDataStore(new File(getDataFolder(), getConfig().getString("storage.sqlite.file"))));

        // Generate a new server ID, or load the existing one.
        // Allows each server to be uniquely identified.
        String serverID = getConfig().getString("server-uuid");
        if (serverID == null || serverID.equalsIgnoreCase("undefined")) {
            serverID = UUID.randomUUID().toString();
            getConfig().set("server-uuid", serverID);
            saveConfig();
            getLogger().log(Level.INFO, "Hello new server! Unique indentifier generated: " + serverID);
        } else {
            getLogger().log(Level.INFO, "Unique indentifier: " + serverID);
        }

        // Initialises the local server for multi-server support.
        localServer = new MelonServer(UUID.fromString(serverID));

        // Selects a store method, default is YAML
        String storageMethod = getConfig().getString("storage.mode");

        if (storageMethod != null) {
            dataStore = DataStore.getMethod(storageMethod);
        }

        if (dataStore == null) {
            getLogger().log(Level.SEVERE, "No valid storage method provided.");
            getLogger().log(Level.SEVERE, "Check your configuration, then try again.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            // Initializes the data store (open DB connections, create files, etc.)
            getLogger().log(Level.INFO, "Iniitalizing data store \"" + getDataStore().getName() + "\"...");
            getDataStore().initialize();

            // Loads all groups and tracks
            getLogger().log(Level.INFO, "Loading groups...");
            getDataStore().loadGroups();
            getLogger().log(Level.INFO, "Loaded " + GroupManager.getGroups().size() + " group(s)!");

            getLogger().log(Level.INFO, "Loading tracks...");
            getDataStore().loadTracks();
            getLogger().log(Level.INFO, "Loaded " + GroupManager.getTracks().size() + " track(s)!");
        } catch (Throwable e) {
            getLogger().log(Level.SEVERE, "Cannot load initial data from datastore.");
            getLogger().log(Level.SEVERE, "Check your configuration, then try again.");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Registers plugin events
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);

        // Registers plugin commands
        MasterCommand userCommand = new MasterCommand("user");
        MasterCommand groupCommand = new MasterCommand("group");
        MasterCommand trackCommand = new MasterCommand("track");

        userCommand.getCommands().add(new UserAddGroup(userCommand));
        userCommand.getCommands().add(new UserBukkitCheck(userCommand));
        userCommand.getCommands().add(new UserPromote(userCommand));
        userCommand.getCommands().add(new UserDemote(userCommand));
        userCommand.getCommands().add(new UserPrefix(userCommand));
        userCommand.getCommands().add(new UserRemoveGroup(userCommand));
        userCommand.getCommands().add(new UserServer(userCommand));
        userCommand.getCommands().add(new UserSet(userCommand));
        userCommand.getCommands().add(new UserSetGroup(userCommand));
        userCommand.getCommands().add(new UserSuffix(userCommand));
        userCommand.getCommands().add(new UserSuper(userCommand));
        userCommand.getCommands().add(new UserUnset(userCommand));
        userCommand.getCommands().add(new UserView(userCommand));
        userCommand.getCommands().add(new UserWorld(userCommand));

        groupCommand.getCommands().add(new GroupCreate(groupCommand));
        groupCommand.getCommands().add(new GroupDelete(groupCommand));
        groupCommand.getCommands().add(new GroupInherit(groupCommand));
        groupCommand.getCommands().add(new GroupList(groupCommand));
        groupCommand.getCommands().add(new GroupPrefix(groupCommand));
        groupCommand.getCommands().add(new GroupPriority(groupCommand));
        groupCommand.getCommands().add(new GroupServer(groupCommand));
        groupCommand.getCommands().add(new GroupSet(groupCommand));
        groupCommand.getCommands().add(new GroupSuffix(groupCommand));
        groupCommand.getCommands().add(new GroupUnset(groupCommand));
        groupCommand.getCommands().add(new GroupView(groupCommand));
        groupCommand.getCommands().add(new GroupWorld(groupCommand));

        trackCommand.getCommands().add(new TrackAddGroup(trackCommand));
        trackCommand.getCommands().add(new TrackCreate(trackCommand));
        trackCommand.getCommands().add(new TrackDefault(trackCommand));
        trackCommand.getCommands().add(new TrackDelete(trackCommand));
        trackCommand.getCommands().add(new TrackList(trackCommand));
        trackCommand.getCommands().add(new TrackMoveGroup(trackCommand));
        trackCommand.getCommands().add(new TrackRemoveGroup(trackCommand));
        trackCommand.getCommands().add(new TrackView(trackCommand));

        CommandManager.getMasterCommandList().add(userCommand);
        CommandManager.getMasterCommandList().add(groupCommand);
        CommandManager.getMasterCommandList().add(trackCommand);

        // Registers main plugin command
        PluginCommand cmd = getServer().getPluginCommand("melonperms");
        cmd.setExecutor(new CommandManager());
        cmd.setAliases(Arrays.asList("perms", "mp", "permissions", "p", "perm"));

        getLogger().log(Level.INFO, "Startup complete!");

        // Runs through currently online players and recalculates their permissions (in the event of a reload)
        doAsync(() -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                JoinListener.userLoginCheck(player.getName(), player.getUniqueId());
                User user = UserManager.getUser(player);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (user != null) {
                            user.setAttachment(player.addAttachment(getInstance()));
                            user.refreshPermissions(player.getWorld());
                        } else {
                            player.kickPlayer("§c§l[MP] §cNo user data. Contact an admin.");
                        }
                    }
                }.runTask(getInstance());
            }
        });

    }

    public static void doAsync(Runnable runnable) {
        getInstance().getServer().getScheduler().runTaskAsynchronously(getInstance(), runnable);
    }

    public static DataStore getDataStore() {
        return dataStore;
    }

    public static MelonServer getLocalServer() {
        return localServer;
    }

    public static MelonPerms getInstance() {
        return instance;
    }
}
