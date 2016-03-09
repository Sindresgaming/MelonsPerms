package com.turqmelon.MelonPerms.commands.trackcommands;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/

import com.turqmelon.MelonPerms.commands.MasterCommand;
import com.turqmelon.MelonPerms.commands.SubCommand;
import com.turqmelon.MelonPerms.groups.GroupManager;
import com.turqmelon.MelonPerms.util.Track;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TrackList extends SubCommand {

    public TrackList(MasterCommand command) {
        super("list", command, "Shows track list", "", "perms.track.list");
    }

    // Outputs a list of tracks
    @Override
    protected void execute(CommandSender sender, List<String> args) {

        sender.sendMessage("§e§l[MP] §eThere are " + GroupManager.getTracks().size() + " track(s):");
        List<Track> tracks = GroupManager.getTracks();
        for (Track track : tracks) {
            sender.sendMessage("§b§l-> §b" + track.getName() + " " + (track.isDefaultTrack() ? "§7§o(Default)" : ""));
        }

    }
}
