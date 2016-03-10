package com.turqmelon.MelonPerms.commands.groupcommands;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/


import com.turqmelon.MelonPerms.commands.MasterCommand;
import com.turqmelon.MelonPerms.commands.SubCommand;
import com.turqmelon.MelonPerms.groups.Group;
import com.turqmelon.MelonPerms.groups.GroupManager;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupList extends SubCommand {

    public GroupList(MasterCommand command) {
        super("list", command, "Shows group list", "", "perms.group.list");
    }

    // Outposts a list of all groups, ordered by priority ascending
    @Override
    protected void execute(CommandSender sender, List<String> args) {

        sender.sendMessage("§e§l[MP] §eThere are " + GroupManager.getGroups().size() + " group(s):");
        List<Group> groups = new ArrayList<>();
        groups.addAll(GroupManager.getGroups());
        Collections.sort(groups);
        for (Group group : groups) {
            sender.sendMessage("§b§l-> §b" + group.getName() + ", Priority: " + group.getPriority());
        }

    }
}
