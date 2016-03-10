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

public class UserAddGroup extends SubCommand {

    public UserAddGroup(MasterCommand command) {
        super("addgroup", command, "Add user to group", "<User> <Group>", "perms.user.groups.add");
    }

    // Adds a user to a permission group
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException, InsufficientArgumentTypeException, GroupNotFoundException {

        if (args.size() == 2) {

            User user = UserManager.getUser(args.get(0));
            Group group = GroupManager.getGroup(args.get(1));

            // Ensures the user exists
            if (user != null) {

                // Ensures the group exists
                if (group != null) {

                    // If its the default group, informs the user that everyone is always in the default group
                    if (group.isDefault()) {
                        sender.sendMessage("§c§l[MP] §cUsers are always in the default group.");
                        return;
                    }

                    // Ensures that the user is not already part of the group
                    if (!user.getGroups().contains(group)) {
                        user.getGroups().add(group);
                        sender.sendMessage("§a§l[MP] §aAdded §n" + user.getName() + "§a to" + (group.isConditional() ? " the conditional group" : " the group") + " §n" + group.getName() + "§a!");

                        // Saves the user
                        final User finalUser = user;
                        MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveUser(finalUser));

                    } else {
                        sender.sendMessage("§c§l[MP] §c§n" + user.getName() + "§c is already part of §n" + group.getName() + "§c.");
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
