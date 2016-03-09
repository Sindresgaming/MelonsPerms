package com.turqmelon.MelonPerms.commands.trackcommands;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/

import com.turqmelon.MelonPerms.MelonPerms;
import com.turqmelon.MelonPerms.commands.MasterCommand;
import com.turqmelon.MelonPerms.commands.SubCommand;
import com.turqmelon.MelonPerms.exceptions.InsufficientArgumentException;
import com.turqmelon.MelonPerms.groups.GroupManager;
import com.turqmelon.MelonPerms.util.Track;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TrackCreate extends SubCommand {

    public TrackCreate(MasterCommand command) {
        super("create", command, "Creates a new track", "<Name>", "perms.track.create");
    }

    // Creates a new track, to make promoting users simple
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException {

        if (args.size() == 1) {

            Track track = GroupManager.getTrack(args.get(0));

            // Ensures the track name is unique
            if (track == null) {

                track = new Track(args.get(0).replace("_", " "));

                // Adds track to group manager
                GroupManager.getTracks().add(track);

                sender.sendMessage("§a§l[MP] §aCreated the §n" + track.getName() + "§a track.");

                // If this is the first track, set it as the default
                if (GroupManager.getTracks().size() == 1) {
                    track.setDefaultTrack(true);
                }

                // Saves new track to datastore
                final Track finalTrack = track;
                MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveTrack(finalTrack));

            } else {
                sender.sendMessage("§c§l[MP] §cTrack already exists: \"" + args.get(0) + "\"");
            }

        } else {
            throw new InsufficientArgumentException(1);
        }

    }
}
