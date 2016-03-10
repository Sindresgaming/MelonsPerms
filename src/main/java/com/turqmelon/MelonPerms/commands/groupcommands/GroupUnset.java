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
import com.turqmelon.MelonPerms.groups.Group;
import com.turqmelon.MelonPerms.groups.GroupManager;
import com.turqmelon.MelonPerms.util.Privilege;
import org.bukkit.command.CommandSender;

import java.util.List;

public class GroupUnset extends SubCommand {

    public GroupUnset(MasterCommand command) {
        super("unset", command, "Unset group permission", "<Group> <Permission>", "perms.group.unset");
    }

    // Removes a defined permission from a group
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, GroupNotFoundException, InsufficientArgumentTypeException {

        if (args.size() == 2) {

            Group group = GroupManager.getGroup(args.get(0));
            String permission = args.get(1).toLowerCase();

            // Ensures the group exists
            if (group != null) {

                // Ensures the permission is defined
                Privilege privilege = group.getPrivilege(permission);
                if (privilege != null) {

                    // Removes the definition
                    group.getPrivileges().remove(privilege);
                    sender.sendMessage("§a§l[MP] §aUnset §n" + permission + "§a for §n" + group.getName() + "§a.");

                    // Saves the group to datastore
                    final Group finalGroup = group;
                    MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveGroup(finalGroup));

                } else {
                    sender.sendMessage("§c§l[MP] §c§n" + permission + "§c is not currently defined for §n" + group.getName() + "§c.");
                }
            } else {
                throw new GroupNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
