package com.turqmelon.MelonPerms.commands.usercommands;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/


import com.turqmelon.MelonPerms.MelonPerms;
import com.turqmelon.MelonPerms.commands.MasterCommand;
import com.turqmelon.MelonPerms.commands.SubCommand;
import com.turqmelon.MelonPerms.exceptions.InsufficientArgumentException;
import com.turqmelon.MelonPerms.exceptions.InsufficientArgumentTypeException;
import com.turqmelon.MelonPerms.exceptions.UserNotFoundException;
import com.turqmelon.MelonPerms.users.User;
import com.turqmelon.MelonPerms.users.UserManager;
import com.turqmelon.MelonPerms.util.Privilege;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class UserWorld extends SubCommand {

    public UserWorld(MasterCommand command) {
        super("world", command, "Sets permission access per-world", "<User> <Permission> <World>", "perms.user.world");
    }

    // Restricts certain permissions to some worlds
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException, InsufficientArgumentTypeException {

        if (args.size() == 3) {

            User user = UserManager.getUser(args.get(0));
            String permission = args.get(1).toLowerCase();
            World world = Bukkit.getWorld(args.get(2));

            // Ensures the user exists
            if (user != null) {

                // Ensures the world exists
                if (world != null) {
                    Privilege privilege = user.getPrivilege(permission);

                    // Ensures the permission is defined
                    if (privilege != null) {

                        // Toggles the restricted state of the world
                        if (privilege.getWorlds().contains(world)) {
                            privilege.getWorlds().remove(world);
                        } else {
                            privilege.getWorlds().add(world);
                        }

                        // Informs the user of their new world preferences
                        if (privilege.getWorlds().size() > 0) {
                            List<String> worlds = privilege.getWorlds().stream().map(World::getName).collect(Collectors.toList());
                            sender.sendMessage("§a§l[MP] §a§n" + permission + "§a granted for §n" + user.getName() + "§a in world(s): " + worlds.toString().replace("[", "").replace("]", ""));
                        } else {
                            sender.sendMessage("§a§l[MP] §a§n" + permission + "§a granted for §n" + user.getName() + "§a in all worlds.");
                        }

                        // Saves the user
                        final User finalUser = user;
                        MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveUser(finalUser));

                    } else {
                        sender.sendMessage("§c§l[MP] §cUndefined permission. Set using \"/perms user set\", first.");
                    }
                } else {
                    List<String> worlds = Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
                    throw new InsufficientArgumentTypeException("WORLD", args.get(2), worlds.toString().replace("[", "").replace("]", ""));
                }
            } else {
                throw new UserNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(3);
        }

    }
}
