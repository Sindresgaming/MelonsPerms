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

public class UserServer extends SubCommand {

    public UserServer(MasterCommand command) {
        super("server", command, "Sets permission access per-server", "<User> <Permission>", "perms.user.server");
    }

    // Edits the scope of a user-defined privilege to just specific servers
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

                    // Adjusts the allowed servers
                    if (privilege.getServers().contains(MelonPerms.getLocalServer())) {
                        privilege.getServers().remove(MelonPerms.getLocalServer());
                    } else {
                        privilege.getServers().add(MelonPerms.getLocalServer());
                    }

                    // Informs the user of the new preferences
                    if (privilege.getServers().size() > 0) {
                        sender.sendMessage("§a§l[MP] §a§n" + permission + "§a is now only granted on §n" + privilege.getServers().size() + "§a server(s).");
                    } else {
                        sender.sendMessage("§a§l[MP] §a§n" + permission + "§a is now granted on all servers.");
                    }

                    // Saves the user
                    final User finalUser = user;
                    MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveUser(finalUser));

                } else {
                    sender.sendMessage("§c§l[MP] §cUndefined permission. Set using \"/perms user set\", first.");
                }
            } else {
                throw new UserNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
