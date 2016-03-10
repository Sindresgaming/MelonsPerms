package com.turqmelon.MelonPerms.commands.usercommands;

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
import com.turqmelon.MelonPerms.users.User;
import com.turqmelon.MelonPerms.users.UserManager;
import org.bukkit.command.CommandSender;

import java.util.List;

public class UserRemoveGroup extends SubCommand {

    public UserRemoveGroup(MasterCommand command) {
        super("removegroup", command, "Remove user from group", "<User> <Group>", "perms.user.groups.remove");
    }

    // Removes a user from a permission group
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException, InsufficientArgumentTypeException, GroupNotFoundException {

        if (args.size() == 2) {

            User user = UserManager.getUser(args.get(0));
            Group group = GroupManager.getGroup(args.get(1));

            // Ensures the user exists
            if (user != null) {

                // Ensures the group exists
                if (group != null) {

                    // Ensures the user is in the group
                    if (user.getGroups().contains(group)) {

                        // Prevents removing the default group
                        if (group.isDefault()) {
                            sender.sendMessage("§c§l[MP] §cYou can't remove players from the default group.");
                            return;
                        }

                        user.getGroups().remove(group);
                        sender.sendMessage("§a§l[MP] §aRemoved §n" + user.getName() + "§a from §n" + group.getName() + "§a!");

                        // Saves the user
                        final User finalUser = user;
                        MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveUser(finalUser));

                    } else {
                        sender.sendMessage("§c§l[MP] §c§n" + user.getName() + "§c isn't part of §n" + group.getName() + "§c.");
                    }

                } else {
                    throw new GroupNotFoundException(args.get(1));
                }

            } else {
                throw new UserNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
