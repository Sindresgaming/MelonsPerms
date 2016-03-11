package com.turqmelon.MelonPerms.data;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/


import com.turqmelon.MelonPerms.MelonPerms;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

// Allows saving permissions and users to a MySQL database
public final class MySQLStorage extends SQLDataStore {

    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
    private String tablePrefix;

    public MySQLStorage(String host, int port, String database, String username, String password, String tablePrefix) {
        super("mysql");
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.tablePrefix = tablePrefix;
    }

    @Override
    protected Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase(), getUsername(), getPassword());
    }

    @Override
    protected void setupTables() throws SQLException {
        try (PreparedStatement stmt = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `" + getTablePrefix() + "users` ( `id` BIGINT NOT NULL AUTO_INCREMENT , `uuid` VARCHAR(255) NOT NULL , `name` VARCHAR(255) NOT NULL , `data` TEXT NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;")) {
            stmt.execute();
        }

        try (PreparedStatement stmt = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `" + getTablePrefix() + "tracks` ( `id` BIGINT NOT NULL AUTO_INCREMENT , `name` VARCHAR(255) NOT NULL , `data` TEXT NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;")) {
            stmt.execute();
        }

        try (PreparedStatement stmt = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `" + getTablePrefix() + "groups` ( `id` BIGINT NOT NULL AUTO_INCREMENT , `name` VARCHAR(255) NOT NULL , `data` TEXT NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;")) {
            stmt.execute();
        }
    }

    @Override
    public void initialize() {
        super.initialize();

        int mins = MelonPerms.getInstance().getConfig().getInt("storage.mysql.sync-minutes", 0);
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
}
