package com.turqmelon.MelonPerms.commands.usercommands;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/


import com.turqmelon.MelonPerms.MelonPerms;
import com.turqmelon.MelonPerms.commands.MasterCommand;
import com.turqmelon.MelonPerms.commands.SubCommand;
import com.turqmelon.MelonPerms.exceptions.InsufficientArgumentException;
import com.turqmelon.MelonPerms.exceptions.UserNotFoundException;
import com.turqmelon.MelonPerms.users.User;
import com.turqmelon.MelonPerms.users.UserManager;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.List;

public class UserSuper extends SubCommand {

    public UserSuper(MasterCommand command) {
        super("super", command, "Toggles super user", "<User>", "perms.user.super");
    }

    // Make a user a super user
    // Super users can control all aspects of MelonPerms, regardless of OP or permission status
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException {

        if (args.size() == 1) {

            User user = UserManager.getUser(args.get(0));

            // Ensures the user exists
            if (user != null) {

                // Ensures that console is the one sending this command
                if ((sender instanceof ConsoleCommandSender)) {

                    // Toggles super user status
                    user.setSuperUser(!user.isSuperUser());
                    sender.sendMessage("§a§l[MP] §aSet super user status of §n" + user.getName() + "§a to §n" + user.isSuperUser() + "§a.");

                    // Saves the user
                    final User finalUser = user;
                    MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveUser(finalUser));

                } else {
                    sender.sendMessage("§c§l[MP] §cSuper user modifications may only be done as console.");
                }

            } else {
                throw new UserNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(1);
        }

    }
}
