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

public class TrackDefault extends SubCommand {

    public TrackDefault(MasterCommand command) {
        super("default", command, "Sets default track", "<Track>", "perms.track.default");
    }

    // Changes the default track
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, TrackNotFoundException {

        if (args.size() == 1) {

            Track track = GroupManager.getTrack(args.get(0));

            // Ensures the track exists
            if (track != null) {

                // Removes the current default track
                GroupManager.getTracks().stream().filter(Track::isDefaultTrack).forEach(t -> {
                    t.setDefaultTrack(false);
                    MelonPerms.getDataStore().saveTrack(t);
                });

                // Sets the new default track and saves to datastore
                track.setDefaultTrack(true);
                MelonPerms.getDataStore().saveTrack(track);
                sender.sendMessage("§a§l[MP] §a§n" + track.getName() + "§a is now your default track.");

            } else {
                throw new TrackNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(1);
        }

    }
}
