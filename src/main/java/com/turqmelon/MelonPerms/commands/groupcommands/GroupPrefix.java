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
import com.turqmelon.MelonPerms.groups.Group;
import com.turqmelon.MelonPerms.groups.GroupManager;
import org.bukkit.command.CommandSender;

import java.util.List;

public class GroupPrefix extends SubCommand {

    public GroupPrefix(MasterCommand command) {
        super("prefix", command, "Changes group prefix", "<Group> <Prefix|Remove>", "perms.group.prefix");
    }

    // Sets a group's meta prefix to be used by other plugins (like chat plugins)
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, GroupNotFoundException {

        if (args.size() >= 2) {

            Group group = GroupManager.getGroup(args.get(0));
            if (group != null) {

                if (!args.get(1).equalsIgnoreCase("remove")) {

                    // Builds the prefix and adds a space at the end, because chats that don't have spacing between names and prefixes make me sad
                    String prefix = "";
                    for (int i = 1; i < args.size(); i++) {
                        prefix = prefix + args.get(i) + " ";
                    }
                    group.setMetaPrefix(prefix);
                    sender.sendMessage("§a§l[MP] §aPrefix for §n" + group.getName() + "§a set: §f" + group.getMetaPrefix());
                } else {
                    group.setMetaPrefix(null);
                    sender.sendMessage("§a§l[MP] §aPrefix for §n" + group.getName() + "§a removed.");
                }

                // Saves the group to datastore
                MelonPerms.getDataStore().saveGroup(group);

            } else {
                throw new GroupNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
