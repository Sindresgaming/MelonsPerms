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

public class GroupInherit extends SubCommand {

    public GroupInherit(MasterCommand command) {
        super("inherit", command, "Toggle group inheritance", "<Group> <Group to Inherit>", "perms.group.inherit");
    }

    // Allows a group to inherit permissions of another group
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, GroupNotFoundException {

        if (args.size() == 2) {

            Group group = GroupManager.getGroup(args.get(0));
            Group toInherit = GroupManager.getGroup(args.get(1));

            // Ensures both groups exist

            if (group != null) {

                if (toInherit != null) {

                    // Ensures that the group to inherit isn't itself (infinite loop...)
                    if (toInherit.getName().equals(group.getName())) {
                        sender.sendMessage("§c§l[MP]§c §n" + group.getName() + "§c can't inherit itself.");
                        return;
                    }

                    // Ensures that the group to inherit isn't already inherited by an inheritance of the group to inherit. (Another infinite loop...)
                    for (Group subinheritance : toInherit.getInheritance()) {
                        if (subinheritance.getName().equals(group.getName())) {
                            sender.sendMessage("§c§l[MP]§c §n" + group.getName() + "§c can't inherit from a group that inherits it.");
                            return;
                        }
                    }

                    // Adds or removes the inehrited group
                    if (group.getInheritance().contains(toInherit)) {
                        group.getInheritance().remove(toInherit);
                        sender.sendMessage("§a§l[MP] §a§n" + group.getName() + "§a will no longer inherit permissions from §n" + toInherit.getName() + "§a.");
                    } else {
                        group.getInheritance().add(toInherit);
                        sender.sendMessage("§a§l[MP] §a§n" + group.getName() + "§a will now inherit permissions from §n" + toInherit.getName() + "§a.");
                    }

                    // Saves the group to datastore
                    final Group finalGroup = group;
                    MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveGroup(finalGroup));

                } else {
                    throw new GroupNotFoundException(args.get(1));
                }
            } else {
                throw new GroupNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
