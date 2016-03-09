package com.turqmelon.MelonPerms.commands.groupcommands;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/


import com.turqmelon.MelonPerms.commands.MasterCommand;
import com.turqmelon.MelonPerms.commands.SubCommand;
import com.turqmelon.MelonPerms.exceptions.GroupNotFoundException;
import com.turqmelon.MelonPerms.exceptions.InsufficientArgumentException;
import com.turqmelon.MelonPerms.groups.Group;
import com.turqmelon.MelonPerms.groups.GroupManager;
import com.turqmelon.MelonPerms.util.Privilege;
import com.turqmelon.MelonPerms.util.Track;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupView extends SubCommand {

    public GroupView(MasterCommand command) {
        super("view", command, "Shows group information", "<User>", "perms.group.view");
    }

    // Outposts information about a group
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, GroupNotFoundException {

        if (args.size() == 1) {

            Group group = GroupManager.getGroup(args.get(0));

            // Ensures the group exists
            if (group != null) {

                sender.sendMessage("§e§l[MP] §e§n" + group.getName() + " information:");
                sender.sendMessage("§e§l[MP] §ePriority: " + group.getPriority() + (group.isDefault() ? " §7§o(Default)" : ""));
                sender.sendMessage("§e§l[MP] §eInheriting privileges from " + group.getInheritance().size() + " group(s):");
                for (Group inherited : group.getInheritance()) {
                    sender.sendMessage("§b§l-> §b" + inherited.getName() + (inherited.isConditional() ? " §6§o(Conditional)" : "") + (inherited.isDefault() ? " §7§o(Default)" : ""));
                }
                sender.sendMessage("§e§l[MP] §e" + group.getPrivileges().size() + " defined privilege(s):");
                for (Privilege privilege : group.getPrivileges()) {
                    sender.sendMessage("§b§l-> §b" + privilege.getNode() + (privilege.isNegated() ? " §c§o(Negated)" : "") + (privilege.isConditional() ? " §6§o(Conditional)" : ""));
                }
                if (group.getInheritance().size() > 0) {
                    Map<Privilege, Group> inherited = group.getInheritedPrivileges(null);
                    sender.sendMessage("§e§l[MP] §eGroup inherits " + inherited.size() + " privilege(s):");
                    ploop:
                    for (Privilege privilege : inherited.keySet()) {
                        Group g = inherited.get(privilege);
                        for (Privilege p : group.getPrivileges()) {
                            if (p.matches(privilege)) {
                                continue ploop;
                            }
                        }
                        sender.sendMessage("§b§l-> §b" + privilege.getNode() + "§o from " + g.getName() + (privilege.isNegated() ? " §c§o(Negated)" : "") + (privilege.isConditional() ? " §6§o(Conditional)" : ""));
                    }
                }
                if (group.getWorlds().size() > 0) {
                    List<String> worlds = group.getWorlds().stream().map(World::getName).collect(Collectors.toList());
                    sender.sendMessage("§e§l[MP] §eConditional to " + worlds.size() + " world(s): " + worlds.toString().replace("[", "").replace("]", ""));
                } else {
                    sender.sendMessage("§e§l[MP] §eGroup is not world specific.");
                }
                if (group.getServers().size() > 0) {
                    sender.sendMessage("§e§l[MP] §eConditional to " + group.getServers().size() + " server(s).");
                } else {
                    sender.sendMessage("§e§l[MP] §eGroup is not server specific.");
                }

                sender.sendMessage("§e§l[MP] §eMeta:");
                sender.sendMessage("§b§l-> §bPrefix: " + (group.getMetaPrefix() != null ? group.getMetaPrefix() : "§c§oUndefined"));
                sender.sendMessage("§b§l-> §bSuffix: " + (group.getMetaSuffix() != null ? group.getMetaSuffix() : "§c§oUndefined"));

                sender.sendMessage("§e§l[MP] §eMember of " + group.getTracks().size() + " track(s):");
                for (Track track : group.getTracks()) {
                    sender.sendMessage("§b§l-> §b" + track.getName());
                }

            } else {
                throw new GroupNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(1);
        }

    }
}
