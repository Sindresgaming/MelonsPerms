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
import org.bukkit.command.CommandSender;

import java.util.List;

public class UserUnset extends SubCommand {

    public UserUnset(MasterCommand command) {
        super("unset", command, "Unset user permission", "<User> <Permission>", "perms.user.unset");
    }

    // Removes a defined permission for a user
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException, InsufficientArgumentTypeException {

        if (args.size() == 2) {

            User user = UserManager.getUser(args.get(0));
            String permission = args.get(1).toLowerCase();

            // Ensures the user exists
            if (user != null) {

                // Ensures the permission is defined
                Privilege privilege = user.getPrivilege(permission);
                if (privilege != null) {

                    // Removes the definition
                    user.getPrivileges().remove(privilege);
                    sender.sendMessage("§a§l[MP] §aUnset §n" + permission + "§a for §n" + user.getName() + "§a.");

                    // Saves the user
                    final User finalUser = user;
                    MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveUser(finalUser));

                } else {
                    sender.sendMessage("§c§l[MP] §c§n" + permission + "§c is not currently defined for §n" + user.getName() + "§c.");
                }
            } else {
                throw new UserNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
