package com.turqmelon.MelonPerms.commands.groupcommands;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/


import com.turqmelon.MelonPerms.MelonPerms;
import com.turqmelon.MelonPerms.commands.MasterCommand;
import com.turqmelon.MelonPerms.commands.SubCommand;
import com.turqmelon.MelonPerms.exceptions.InsufficientArgumentException;
import com.turqmelon.MelonPerms.groups.Group;
import com.turqmelon.MelonPerms.groups.GroupManager;
import org.bukkit.command.CommandSender;

import java.util.List;

public class GroupCreate extends SubCommand {

    public GroupCreate(MasterCommand command) {
        super("create", command, "Creates a new group", "<Name>", "perms.group.create");
    }

    // Creates a new permission group
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException {

        if (args.size() == 1) {

            // Checks to make sure the group doesn't already exist
            Group group = GroupManager.getGroup(args.get(0));
            if (group == null) {

                // Sets the new groups default priority as one higher than any existing groups
                int priority = 0;
                for (Group g : GroupManager.getGroups()) {
                    if (priority <= g.getPriority()) {
                        priority = g.getPriority() + 1;
                    }
                }

                // Instantiates the new group and adds it to the GroupManager.
                group = new Group(args.get(0).replace("_", " "), priority);
                GroupManager.getGroups().add(group);

                sender.sendMessage("§a§l[MP] §aCreated the §n" + group.getName() + "§a group.");
                if (GroupManager.getDefaultGroup() != null && GroupManager.getDefaultGroup().getName().equals(group.getName())) {
                    sender.sendMessage("§a§l[MP] §a§n" + group.getName() + "§a is your new default group.");
                }

                // Saves the new group
                final Group finalGroup = group;
                MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveGroup(finalGroup));

            } else {
                sender.sendMessage("§c§l[MP] §cGroup already exists: \"" + args.get(0) + "\"");
            }

        } else {
            throw new InsufficientArgumentException(1);
        }

    }
}
