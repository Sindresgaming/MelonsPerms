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

public class UserSetGroup extends SubCommand {

    public UserSetGroup(MasterCommand command) {
        super("setgroup", command, "Set user to group", "<User> <Group>", "perms.user.groups.set");
    }

    // Sets a users group
    // Setting a group removes all other memberships (except the default group)
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException, InsufficientArgumentTypeException, GroupNotFoundException {

        if (args.size() == 2) {

            User user = UserManager.getUser(args.get(0));
            Group group = GroupManager.getGroup(args.get(1));
            // Ensures the user exists
            if (user != null) {

                // Ensures the group exists
                if (group != null) {

                    // Informs the user that users are ALWAYS in the default group
                    if (group.isDefault()) {
                        sender.sendMessage("§c§l[MP] §cUsers are always in the default group.");
                        return;
                    }

                    // Clears the user's groups
                    user.getGroups().clear();

                    // Adds the speciifc group
                    user.getGroups().add(group);
                    sender.sendMessage("§a§l[MP] §aSet §n" + user.getName() + "§a to §n" + group.getName() + "§a.");

                    // Saves the user
                    final User finalUser = user;
                    MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveUser(finalUser));

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
