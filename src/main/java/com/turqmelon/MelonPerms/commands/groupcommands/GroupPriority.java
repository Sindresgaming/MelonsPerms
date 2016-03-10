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

public class GroupPriority extends SubCommand {

    public GroupPriority(MasterCommand command) {
        super("priority", command, "Adjusts group priority", "<Group> <Priority>", "perms.group.priority");
    }

    // Adjusts the group priority
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, GroupNotFoundException {

        if (args.size() == 2) {

            Group group = GroupManager.getGroup(args.get(0));
            if (group != null) {

                try {

                    int priority = Integer.parseInt(args.get(1));
                    group.setPriority(priority);

                    sender.sendMessage("§a§l[MP] §aAdjusted priority for §n" + group.getName() + "§a to " + priority + ".");

                    // Informs the player if the change has changed their default group.
                    boolean lowest = true;
                    for (Group g : GroupManager.getGroups()) {
                        if (g.getName().equalsIgnoreCase(group.getName())) continue;
                        if (g.getPriority() < group.getPriority()) {
                            lowest = false;
                            break;
                        }
                    }

                    if (lowest) {
                        sender.sendMessage("§a§l[MP] §a§n" + group.getName() + "§a is your default group.");
                    }

                    // Saves group to datastore
                    final Group finalGroup = group;
                    MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveGroup(finalGroup));

                } catch (NumberFormatException ex) {
                    sender.sendMessage("§c§l[MP] §cPriority must be numeric.");
                }

            } else {
                throw new GroupNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
