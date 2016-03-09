package com.turqmelon.MelonPerms.commands.groupcommands;

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
import org.bukkit.command.CommandSender;

import java.util.List;

public class GroupServer extends SubCommand {

    public GroupServer(MasterCommand command) {
        super("server", command, "Sets group access per-server", "Group>", "perms.group.server");
    }

    // Allows restricting groups to specific servers
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException, InsufficientArgumentTypeException, GroupNotFoundException {

        if (args.size() == 1) {

            Group group = GroupManager.getGroup(args.get(0));

            // Ensures the group exists
            if (group != null) {

                // Because servers are UUID based, it's not friendly to expect users to type them out
                // When this command is run, it updates the setting for the server the command is run on

                // If this server is in the groups list, toggle it
                if (group.getServers().contains(MelonPerms.getLocalServer())) {
                    group.getServers().remove(MelonPerms.getLocalServer());
                } else {
                    group.getServers().add(MelonPerms.getLocalServer());
                }

                // Inform the user of the new preferences for their group
                if (group.getServers().size() > 0) {
                    sender.sendMessage("§a§l[MP] §a§n" + group.getName() + "§a is now only available on §n" + group.getServers().size() + "§a server(s).");
                } else {
                    sender.sendMessage("§a§l[MP] §a§n" + group.getName() + "§a is now available on all servers.");
                }

                // Saves group to datastore
                final Group finalGroup = group;
                MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveGroup(finalGroup));

            } else {
                throw new GroupNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(1);
        }
    }

}
