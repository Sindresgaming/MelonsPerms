package com.turqmelon.MelonPerms.data;

import com.turqmelon.MelonPerms.MelonPerms;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * @author Jackson
 * @version 1.0
 */
public class SQLiteDataStore extends SQLDataStore {

    private File file;

    public SQLiteDataStore(File file) {
        super("sqlite");
        this.file = file;
    }

    @Override
    protected Connection openConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception ignored) { // getConnection will provide the error we actually want.
        }
        return DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
    }

    @Override
    protected void setupTables() throws SQLException {
        try (PreparedStatement stmt = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `users` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT , `uuid` VARCHAR(255) NOT NULL , `name` VARCHAR(255) NOT NULL , `data` TEXT NOT NULL);")) {
            stmt.execute();
        }

        try (PreparedStatement stmt = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `tracks` ( `id` INTEGER PRIMARY KEY , `name` VARCHAR(255) NOT NULL , `data` TEXT NOT NULL);")) {
            stmt.execute();
        }

        try (PreparedStatement stmt = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `groups` ( `id` INTEGER PRIMARY KEY , `name` VARCHAR(255) NOT NULL , `data` TEXT NOT NULL);")) {
            stmt.execute();
        }
    }

    @Override
    public void initialize() {
        super.initialize();

        int mins = MelonPerms.getInstance().getConfig().getInt("storage.sqlite.sync-minutes", 0);
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
}
