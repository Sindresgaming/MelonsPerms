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

public class GroupDelete extends SubCommand {

    public GroupDelete(MasterCommand command) {
        super("delete", command, "Removes a group", "<Name>", "perms.group.delete");
    }

    // Deletes an existing permission group
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, GroupNotFoundException {

        if (args.size() == 1) {

            // Ensures the group exists
            Group group = GroupManager.getGroup(args.get(0));
            if (group != null) {

                // Don't allow removing the default group
                if (group.isDefault()) {
                    sender.sendMessage("§c§l[MP] §cDefault group can't be deleted.");
                    return;
                }

                group.delete();

                // Removes the group form the GroupManager
                GroupManager.getGroups().remove(group);

                sender.sendMessage("§a§l[MP] §aDeleted the §n" + group.getName() + "§a group.");

                // Removes the group from the DataStore
                MelonPerms.getDataStore().deleteGroup(group);

            } else {
                throw new GroupNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(1);
        }

    }
}
