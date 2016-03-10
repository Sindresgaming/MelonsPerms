package com.turqmelon.MelonPerms.commands.trackcommands;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/

import com.turqmelon.MelonPerms.MelonPerms;
import com.turqmelon.MelonPerms.commands.MasterCommand;
import com.turqmelon.MelonPerms.commands.SubCommand;
import com.turqmelon.MelonPerms.exceptions.InsufficientArgumentException;
import com.turqmelon.MelonPerms.exceptions.TrackNotFoundException;
import com.turqmelon.MelonPerms.groups.GroupManager;
import com.turqmelon.MelonPerms.util.Track;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TrackDelete extends SubCommand {

    public TrackDelete(MasterCommand command) {
        super("delete", command, "Removes a track", "<Name>", "perms.track.delete");
    }

    // Deletes a track
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, TrackNotFoundException {

        if (args.size() == 1) {

            Track track = GroupManager.getTrack(args.get(0));
            // Ensures the track exists
            if (track != null) {

                // Unregisters the track
                GroupManager.getTracks().remove(track);

                sender.sendMessage("§a§l[MP] §aDeleted the §n" + track.getName() + "§a track.");

                // If it was the default track, and others exist, change the default track
                if (GroupManager.getTracks().size() > 0 && track.isDefaultTrack()) {
                    Track t = GroupManager.getTracks().get(0);
                    t.setDefaultTrack(true);
                    MelonPerms.getDataStore().saveTrack(t);
                }

                // Delete the track from the datastore
                MelonPerms.getDataStore().deleteTrack(track);

            } else {
                throw new TrackNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(1);
        }

    }
}
