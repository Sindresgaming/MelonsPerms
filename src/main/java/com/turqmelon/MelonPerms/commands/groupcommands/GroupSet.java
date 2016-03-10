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

public class GroupSet extends SubCommand {

    public GroupSet(MasterCommand command) {
        super("set", command, "Set group permission", "<Group> <Permission> [Negated]", "perms.group.set");
    }

    // Defines a permission for a group
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, GroupNotFoundException, InsufficientArgumentTypeException {

        if (args.size() >= 2) {

            Group group = GroupManager.getGroup(args.get(0));
            String permission = args.get(1).toLowerCase();
            boolean negated = false;

            if (args.size() == 3) {
                String b = args.get(2);
                if (b.equalsIgnoreCase("true") || b.equalsIgnoreCase("false")) {
                    negated = b.equalsIgnoreCase("true");
                } else {
                    throw new InsufficientArgumentTypeException("BOOLEAN", b, "true/false");
                }
            }

            // Ensures the group exists
            if (group != null) {

                // Ensures the permission isn't already defined for this scope
                Privilege privilege = group.getPrivilege(permission);
                if (privilege != null) {
                    sender.sendMessage("§c§l[MP] §c§n" + permission + "§c is already set for §n" + group.getName() + "§c.");
                    return;
                }

                // Creates a new privilege
                privilege = new Privilege(permission, negated);
                group.getPrivileges().add(privilege);

                sender.sendMessage("§a§l[MP] §aSet §n" + permission + "§a for §n" + group.getName() + "§a to §n" + (!privilege.isNegated()) + "§a.");

                // Saves the group
                final Group finalGroup = group;
                MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveGroup(finalGroup));

            } else {
                throw new GroupNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
