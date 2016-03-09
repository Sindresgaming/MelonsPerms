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

public class UserSet extends SubCommand {

    public UserSet(MasterCommand command) {
        super("set", command, "Set user permission", "<User> <Permission> [Negated]", "perms.user.set");
    }

    // Defines a privilege for a user
    // User defined privileges override group-defined ones
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException, InsufficientArgumentTypeException {

        if (args.size() >= 2) {

            User user = UserManager.getUser(args.get(0));
            String permission = args.get(1).toLowerCase();
            boolean negated = false;

            if (args.size() == 3) {
                String b = args.get(2);
                if (b.equalsIgnoreCase("true") || b.equalsIgnoreCase("false")) {
                    negated = b.equalsIgnoreCase("true");
                } else {
                    throw new InsufficientArgumentTypeException("BOOLEAN", b, "true/false");
                }
            }

            // Ensures the user exists
            if (user != null) {

                // Ensures the permission is not already defined
                Privilege privilege = user.getPrivilege(permission);
                if (privilege != null) {
                    sender.sendMessage("§c§l[MP] §c§n" + permission + "§c is already set for §n" + user.getName() + "§c.");
                    return;
                }

                // Defines the permission for the user
                privilege = new Privilege(permission, negated);
                user.getPrivileges().add(privilege);

                sender.sendMessage("§a§l[MP] §aSet §n" + permission + "§a for §n" + user.getName() + "§a to §n" + (!privilege.isNegated()) + "§a.");

                // Saves the user
                final User finalUser = user;
                MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveUser(finalUser));

            } else {
                throw new UserNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
