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

public class TrackAddGroup extends SubCommand {

    public TrackAddGroup(MasterCommand command) {
        super("addgroup", command, "Add group to track", "<Track> <Group>", "perms.track.groups.add");
    }

    // Adds a group to a track
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, TrackNotFoundException, InsufficientArgumentTypeException, GroupNotFoundException {

        if (args.size() == 2) {

            Track track = GroupManager.getTrack(args.get(0));
            Group group = GroupManager.getGroup(args.get(1));

            // Ensures the track and group exists
            if (track != null) {

                if (group != null) {

                    // Prevents adding the default group
                    if (group.isDefault()) {
                        sender.sendMessage("§c§l[MP]§c The default group can't be added to a track, since users never leave it.");
                        return;
                    }

                    // Updates the track
                    if (!track.getGroups().contains(group)) {
                        track.getGroups().add(group);
                        sender.sendMessage("§a§l[MP] §aAdded §n" + group.getName() + "§a to §n" + track.getName() + "§a!");
                    } else {
                        sender.sendMessage("§c§l[MP] §c§n" + group.getName() + "§c is already part of §n" + track.getName() + "§c.");
                    }

                    // Saves the track to datastore
                    final Track finalTrack = track;
                    MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveTrack(finalTrack));

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
