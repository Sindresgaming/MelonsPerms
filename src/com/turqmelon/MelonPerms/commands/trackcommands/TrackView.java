package com.turqmelon.MelonPerms.commands.trackcommands;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/


import com.turqmelon.MelonPerms.commands.MasterCommand;
import com.turqmelon.MelonPerms.commands.SubCommand;
import com.turqmelon.MelonPerms.exceptions.InsufficientArgumentException;
import com.turqmelon.MelonPerms.exceptions.TrackNotFoundException;
import com.turqmelon.MelonPerms.groups.Group;
import com.turqmelon.MelonPerms.groups.GroupManager;
import com.turqmelon.MelonPerms.util.Track;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TrackView extends SubCommand {

    public TrackView(MasterCommand command) {
        super("view", command, "Shows track information", "<Track>", "perms.track.view");
    }

    // Outputs information about a track
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, TrackNotFoundException {

        if (args.size() == 1) {

            Track track = GroupManager.getTrack(args.get(0));

            // Ensures the track exists
            if (track != null) {

                sender.sendMessage("§e§l[MP] §e§n" + track.getName() + " information:");
                sender.sendMessage("§e§l[MP] §eHas " + track.getGroups().size() + " group(s):");
                for (Group group : track.getGroups()) {
                    sender.sendMessage("§b§l-> §b" + group.getName() + (group.isConditional() ? " §6§o(Conditional)" : "") + (group.isDefault() ? " §7§o(Default)" : ""));
                }

            } else {
                throw new TrackNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(1);
        }

    }
}
