package com.turqmelon.MelonPerms.commands.groupcommands;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/


import com.turqmelon.MelonPerms.MelonPerms;
import com.turqmelon.MelonPerms.commands.MasterCommand;
import com.turqmelon.MelonPerms.commands.SubCommand;
import com.turqmelon.MelonPerms.exceptions.GroupNotFoundException;
import com.turqmelon.MelonPerms.exceptions.InsufficientArgumentException;
import com.turqmelon.MelonPerms.exceptions.InsufficientArgumentTypeException;
import com.turqmelon.MelonPerms.exceptions.UserNotFoundException;
import com.turqmelon.MelonPerms.groups.Group;
import com.turqmelon.MelonPerms.groups.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class GroupWorld extends SubCommand {

    public GroupWorld(MasterCommand command) {
        super("world", command, "Sets group access per-world", "<Group> <World>", "perms.group.world");
    }

    // Sets world restrictions for groups
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException, InsufficientArgumentTypeException, GroupNotFoundException {

        if (args.size() == 2) {

            Group group = GroupManager.getGroup(args.get(0));
            World world = Bukkit.getWorld(args.get(1));

            // Ensures the group exists
            if (group != null) {

                // Ensures the world is valid
                if (world != null) {

                    if (group.getWorlds().contains(world)) {
                        group.getWorlds().remove(world);
                    } else {
                        group.getWorlds().add(world);
                    }

                    // Informs the user about the new world preferences
                    if (group.getWorlds().size() > 0) {
                        List<String> worlds = group.getWorlds().stream().map(World::getName).collect(Collectors.toList());
                        sender.sendMessage("§a§l[MP] §a§n" + group.getName() + "§a now available in world(s): " + worlds.toString().replace("[", "").replace("]", ""));
                    } else {
                        sender.sendMessage("§a§l[MP] §a§n" + group.getName() + " §anow available in all worlds.");
                    }

                    // Saves group to datastore
                    final Group finalGroup = group;
                    MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveGroup(finalGroup));

                } else {
                    List<String> worlds = Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
                    throw new InsufficientArgumentTypeException("WORLD", args.get(1), worlds.toString().replace("[", "").replace("]", ""));
                }
            } else {
                throw new GroupNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
