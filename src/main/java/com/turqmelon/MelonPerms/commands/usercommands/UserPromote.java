package com.turqmelon.MelonPerms.commands.usercommands;

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
import com.turqmelon.MelonPerms.users.User;
import com.turqmelon.MelonPerms.users.UserManager;
import com.turqmelon.MelonPerms.util.Track;
import org.bukkit.command.CommandSender;

import java.util.List;

public class UserPromote extends SubCommand {

    public UserPromote(MasterCommand command) {
        super("promote", command, "Promote user in current track", "<User> [Track]", "perms.track.promote");
    }

    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException, InsufficientArgumentTypeException, TrackNotFoundException, TrackGroupNotDefinedException, TrackEndReachedException, TrackNoGroupsDefinedException {

        if (args.size() >= 1) {

            User user = UserManager.getUser(args.get(0));

            // If no track is supplied, use the default track
            // Otherwise, match a track to the name provided
            Track track = null;
            if (args.size() == 1) {
                for (Track t : GroupManager.getTracks()) {
                    if (t.isDefaultTrack()) {
                        track = t;
                        break;
                    }
                }
            } else if (args.size() == 2) {
                track = GroupManager.getTrack(args.get(1));
            }

            // Ensures the user exists
            if (user != null) {

                // Ensures the track exists
                if (track != null) {

                    // Loops through the player's current groups, looking for the one in the specified track
                    // This is the group the player will be moved from
                    Group toChange = null;
                    for (Group group : user.getGroups()) {
                        if (group.isDefault()) continue;
                        if (track.getGroups().contains(group)) {
                            if (toChange != null) {
                                // The user belong to multiple groups in the same track, so demotion cannot happen automatically.
                                sender.sendMessage("§c§l[MP] §c§n" + user.getName() + "§c belongs to multiple groups in §n" + track.getName() + "§c.");
                                return;
                            }
                            toChange = group;
                        }
                    }

                    // A group to change has been found
                    if (toChange != null) {

                        // Get the group after this one in the track and remove the current group
                        Group next = track.getNext(toChange);
                        user.getGroups().remove(toChange);

                        // If the one after it isn't default, add them to it
                        if (!next.isDefault()) {
                            user.getGroups().add(next);
                        }

                        sender.sendMessage("§a§l[MP] §a§n" + user.getName() + "§a was promoted from §n" + toChange.getName() + "§a to §n" + next.getName() + "§a.");

                        // Saves the user
                        final User finalUser = user;
                        MelonPerms.doAsync(() -> MelonPerms.getDataStore().saveUser(finalUser));

                    } else {
                        sender.sendMessage("§c§l[MP] §c§n" + user.getName() + "§c doesn't belong to any groups in §n" + track.getName() + "§c.");
                    }

                } else {
                    if (args.size() == 2) {
                        throw new TrackNotFoundException(args.get(1));
                    } else {
                        sender.sendMessage("§c§l[MP]§c You have no default track, please specify one.");
                    }
                }

            } else {
                throw new UserNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(1);
        }

    }
}
