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

public class UserSuffix extends SubCommand {

    public UserSuffix(MasterCommand command) {
        super("suffix", command, "Changes user suffix", "<Group> <Suffix|Remove>", "perms.user.suffix");
    }

    // Sets the user suffix to be used by other plugins (Such as chat plugins). User suffixes override group suffixes.
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException {

        if (args.size() >= 2) {

            User user = UserManager.getUser(args.get(0));
            if (user != null) {

                if (!args.get(1).equalsIgnoreCase("remove")) {
                    String prefix = "";
                    for (int i = 1; i < args.size(); i++) {
                        prefix = prefix + " " + args.get(i);
                    }
                    prefix = prefix.substring(1);
                    user.setMetaSuffix(prefix);
                    sender.sendMessage("§a§l[MP] §aSuffix for §n" + user.getName() + "§a set: §f" + user.getMetaSuffix());
                } else {
                    user.setMetaSuffix(null);
                    sender.sendMessage("§a§l[MP] §aSuffix for §n" + user.getName() + "§a removed.");
                }

                MelonPerms.getDataStore().saveUser(user);

            } else {
                throw new UserNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
