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

public class GroupSuffix extends SubCommand {

    public GroupSuffix(MasterCommand command) {
        super("suffix", command, "Changes group suffix", "<Group> <Suffix|Remove>", "perms.group.suffix");
    }

    // Sets a group's meta suffix to be used by other plugins (like chat plugins)
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, GroupNotFoundException {

        if (args.size() >= 2) {

            Group group = GroupManager.getGroup(args.get(0));
            if (group != null) {

                if (!args.get(1).equalsIgnoreCase("remove")) {
                    // Builds the suffix
                    String suffix = "";
                    for (int i = 1; i < args.size(); i++) {
                        suffix = suffix + " " + args.get(i);
                    }
                    suffix = suffix.substring(1);
                    group.setMetaSuffix(suffix);
                    sender.sendMessage("§a§l[MP] §aSuffix for §n" + group.getName() + "§a set: §f" + group.getMetaSuffix());
                } else {
                    group.setMetaSuffix(null);
                    sender.sendMessage("§a§l[MP] §aSuffix for §n" + group.getName() + "§a removed.");
                }

                // Saves the group to data store
                MelonPerms.getDataStore().saveGroup(group);

            } else {
                throw new GroupNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
