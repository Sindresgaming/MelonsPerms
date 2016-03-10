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

import java.util.List;

public class UserPrefix extends SubCommand {

    public UserPrefix(MasterCommand command) {
        super("prefix", command, "Changes user prefix", "<Group> <Prefix|Remove>", "perms.user.prefix");
    }

    // Sets a user's prefix to be used by other plugins (like chat plugins). User prefixes will override group prefixes.
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException {

        if (args.size() >= 2) {

            User user = UserManager.getUser(args.get(0));
            // Ensure the user exists
            if (user != null) {

                if (!args.get(1).equalsIgnoreCase("remove")) {
                    // Builds the prefix
                    String prefix = "";
                    for (int i = 1; i < args.size(); i++) {
                        prefix = prefix + args.get(i) + " ";
                    }
                    user.setMetaPrefix(prefix);
                    sender.sendMessage("§a§l[MP] §aPrefix for §n" + user.getName() + "§a set: §f" + user.getMetaPrefix());
                } else {
                    user.setMetaPrefix(null);
                    sender.sendMessage("§a§l[MP] §aPrefix for §n" + user.getName() + "§a removed.");
                }

                // Saves the suer
                MelonPerms.getDataStore().saveUser(user);

            } else {
                throw new UserNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
