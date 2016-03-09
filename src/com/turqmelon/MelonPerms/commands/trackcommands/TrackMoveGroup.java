package com.turqmelon.MelonPerms.commands.trackcommands;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/

import com.turqmelon.MelonPerms.MelonPerms;
import com.turqmelon.MelonPerms.commands.MasterCommand;
import com.turqmelon.MelonPerms.commands.SubCommand;
import com.turqmelon.MelonPerms.exceptions.*;
import com.turqmelon.MelonPerms.groups.Group;
import com.turqmelon.MelonPerms.groups.GroupManager;
import com.turqmelon.MelonPerms.util.Track;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TrackMoveGroup extends SubCommand {

    public TrackMoveGroup(MasterCommand command) {
        super("move", command, "Move group in track", "<Track> <Group> <Direction>", "perms.track.groups.move");
    }

    // Moves a group up or down in a track
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, TrackNotFoundException, InsufficientArgumentTypeException, GroupNotFoundException, TrackGroupNotDefinedException, TrackEndReachedException, TrackStartReachedException {

        if (args.size() == 3) {

            Track track = GroupManager.getTrack(args.get(0));
            Group group = GroupManager.getGroup(args.get(1));
            String direction = args.get(2);

            // Ensures the direction is valid
            if (!direction.equalsIgnoreCase("up") && !direction.equalsIgnoreCase("down")) {
                throw new InsufficientArgumentTypeException("DIRECTION", direction, "up/down");
            }

            // Ensures the track and group exist
            if (track != null) {

                if (group != null) {

                    // Get the current position of the group in the track
                    int index = -1;
                    for (int i = 0; i < track.getGroups().size(); i++) {
                        Group g = track.getGroups().get(i);
                        if (g.getName().equals(group.getName())) {
                            index = i;
                            break;
                        }
                    }

                    if (index == -1) {
                        // The group is not in this track, abort!
                        throw new TrackGroupNotDefinedException(track, group);
                    }

                    int previousIndex = index;

                    // If the direction is "up", increase the index
                    if (direction.equalsIgnoreCase("up")) {
                        if (index >= track.getGroups().size()) {
                            // There is nothing above the group, abort!
                            throw new TrackEndReachedException(track);
                        }
                    }
                    // If the direction is "down", decrease the index
                    else if (direction.equalsIgnoreCase("down")) {
                        index--;
                        if (index < 0) {
                            // There is nothing below the group, abort!
                            throw new TrackStartReachedException(track);
                        }
                    }

                    // Remove the group at its previous index, and add it again at the new position
                    track.getGroups().remove(previousIndex);
                    track.getGroups().add(index, group);

                    sender.sendMessage("§a§l[MP] §aMoved §n" + group.getName() + "§a " + direction.toLowerCase() + " in the §n" + track.getName() + "§a track.");

                    // Save track to datastore
                    final Track finalTrack = track;
                    MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveTrack(finalTrack));

                } else {
                    throw new GroupNotFoundException(args.get(1));
                }

            } else {
                throw new TrackNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(3);
        }

    }
}
