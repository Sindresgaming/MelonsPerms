package com.turqmelon.MelonPerms.commands.trackcommands;

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
import com.turqmelon.MelonPerms.exceptions.TrackNotFoundException;
import com.turqmelon.MelonPerms.groups.Group;
import com.turqmelon.MelonPerms.groups.GroupManager;
import com.turqmelon.MelonPerms.util.Track;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TrackRemoveGroup extends SubCommand {

    public TrackRemoveGroup(MasterCommand command) {
        super("removegroup", command, "Remove group from track", "<Track> <Group>", "perms.track.groups.remove");
    }

    // Removes a group from a track
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, TrackNotFoundException, InsufficientArgumentTypeException, GroupNotFoundException {

        if (args.size() == 2) {

            Track track = GroupManager.getTrack(args.get(0));
            Group group = GroupManager.getGroup(args.get(1));

            // Ensures the track and group exist
            if (track != null) {

                if (group != null) {

                    // Removes the group from the track and updates the datastore
                    if (track.getGroups().contains(group)) {
                        track.getGroups().remove(group);
                        sender.sendMessage("§a§l[MP] §aRemoved §n" + group.getName() + "§a from §n" + track.getName() + "§a!");

                        final Track finalTrack = track;
                        MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveTrack(finalTrack));

                    } else {
                        sender.sendMessage("§c§l[MP] §c§n" + group.getName() + "§c is not part of §n" + track.getName() + "§c.");
                    }

                } else {
                    throw new GroupNotFoundException(args.get(1));
                }

            } else {
                throw new TrackNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
