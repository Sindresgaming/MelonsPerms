package com.turqmelon.MelonPerms.commands;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/


import com.turqmelon.MelonPerms.MelonPerms;
import com.turqmelon.MelonPerms.exceptions.*;
import com.turqmelon.MelonPerms.users.User;
import com.turqmelon.MelonPerms.users.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.logging.Level;

public abstract class SubCommand {

    // The command name
    private String name;

    // The parent command
    private MasterCommand masterCommand;

    // A description of the command
    private String description;

    // The recommended usage
    private String usage;

    // The permission required to execute the command, if null, anybody can use it.
    // If a user is a super user, them having this permissions is irrelevant
    private String permission;

    public SubCommand(String name, MasterCommand masterCommand, String description, String usage, String permission) {
        this.name = name;
        this.masterCommand = masterCommand;
        this.description = description;
        this.usage = usage;
        this.permission = permission;
        MelonPerms.getInstance().getLogger().log(Level.INFO, "Registered " + masterCommand.getName() + " command: " + getName());
    }

    public void run(CommandSender sender, List<String> args) {

        // All commands are async'd as to not interrupt the main thread
        MelonPerms.doAsync(() -> {

            // If the sender is a player, check their permissions
            if ((sender instanceof Player) && getPermission() != null) {
                Player player = (Player) sender;
                User user = UserManager.getUser(player);
                if (!player.hasPermission(getPermission()) && (user == null || !user.isSuperUser())) {
                    sender.sendMessage("§c§l[MP] §cYou don't have permission for that.");
                    return;
                }
            }

            // Handle ALL the exceptions
            try {
                execute(sender, args);

                // After running a command, rebuilds online user permissions. This ensures that bukkit is up to date with any changes that happened.
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            User user = UserManager.getUser(player);
                            if (user != null && user.getAttachment() != null) {
                                user.refreshPermissions(player.getWorld());
                            }
                        }
                    }
                }.runTask(MelonPerms.getInstance());

            } catch (InsufficientArgumentException e) {
                sender.sendMessage("§c§l[MP] §cInsufficient arguments. (Supplied " + args.size() + "/" + e.getRequired() + ")");
                sender.sendMessage("§c§l[MP] §cProper Usage: /perms " + getMasterCommand().getName() + " " + getName() + " " + (getUsage() != null ? getUsage() : ""));
            } catch (UserNotFoundException e) {
                sender.sendMessage("§c§l[MP] §cSupplied user doesn't exist: \"" + e.getUser() + "\"");
            } catch (InsufficientArgumentTypeException e) {
                sender.sendMessage("§c§l[MP] §cInvalid argument(s). " + e.getArgument() + " expected: " + e.getExpected() + "; Received: " + e.getReceived());
            } catch (GroupNotFoundException e) {
                sender.sendMessage("§c§l[MP] §cSupplied group doesn't exist: \"" + e.getGroup() + "\"");
            } catch (TrackNotFoundException e) {
                sender.sendMessage("§c§l[MP] §cSupplied track doesn't exist: \"" + e.getTrack() + "\"");
            } catch (TrackGroupNotDefinedException e) {
                sender.sendMessage("§c§l[MP] §c§n" + e.getGroup().getName() + "§c is not part of the §n" + e.getTrack().getName() + "§c track.");
            } catch (TrackEndReachedException e) {
                sender.sendMessage("§c§l[MP] §cThat's the top of the §n" + e.getTrack().getName() + "§c track.");
            } catch (TrackStartReachedException e) {
                sender.sendMessage("§c§l[MP] §cThat's the bottom of the §n" + e.getTrack().getName() + "§c track.");
            } catch (TrackNoGroupsDefinedException e) {
                sender.sendMessage("§c§l[MP] §c" + e.getTrack().getName() + "§c has no groups defined.");
            }
        });


    }

    protected abstract void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException, InsufficientArgumentTypeException, GroupNotFoundException, TrackNotFoundException, TrackGroupNotDefinedException, TrackEndReachedException, TrackStartReachedException, TrackNoGroupsDefinedException;

    public String getName() {
        return name;
    }

    public MasterCommand getMasterCommand() {
        return masterCommand;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public String getPermission() {
        return permission;
    }
}
