package com.turqmelon.MelonPerms.commands.usercommands;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/

import com.turqmelon.MelonPerms.commands.MasterCommand;
import com.turqmelon.MelonPerms.commands.SubCommand;
import com.turqmelon.MelonPerms.exceptions.InsufficientArgumentException;
import com.turqmelon.MelonPerms.exceptions.UserNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class UserBukkitCheck extends SubCommand {

    public UserBukkitCheck(MasterCommand command) {
        super("bukkitcheck", command, "Check if a user has a permission (according to bukkit)", "<Player> <Permission>", "perms.user.bukkitcheck");
    }

    // Performs a bukkit hasPermission() check, for permission testing
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException {

        if (args.size() == 2) {

            Player player = Bukkit.getPlayer(args.get(0));
            if (player != null && player.isOnline()) {

                sender.sendMessage("§a§l[MP] §aTest Result: §n" + player.hasPermission(args.get(1)));

            } else {
                sender.sendMessage("§c§l[MP] §cAn online bukkit player must be provided.");
            }
        } else {
            throw new InsufficientArgumentException(2);
        }

    }
}
