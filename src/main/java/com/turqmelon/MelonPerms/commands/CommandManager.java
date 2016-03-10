package com.turqmelon.MelonPerms.commands;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/


import com.turqmelon.MelonPerms.MelonPerms;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager implements CommandExecutor {

    // A list of all master commands
    private static List<MasterCommand> masterCommandList = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("§a§l[MelonPermissions]§a version " + MelonPerms.getInstance().getDescription().getVersion());
            for (MasterCommand c : getMasterCommandList()) {
                sender.sendMessage("§b§l-> §b/perms " + c.getName());
            }
        } else {

            // Match the first argument to a MasterCommand
            String cmd = args[0];
            MasterCommand c = null;
            for (MasterCommand mc : getMasterCommandList()) {
                if (mc.getName().equalsIgnoreCase(cmd)) {
                    c = mc;
                    break;
                }
            }

            // Check if command exists
            if (c != null) {

                List<SubCommand> cmds = c.getCommands();

                // If nothing else is specified, output the list of subcommands for the master command
                if (args.length == 1) {
                    if (cmds.size() > 0) {
                        sender.sendMessage("§a§l[MP]§a " + c.getName().toUpperCase() + " Commands");
                        for (SubCommand sc : cmds) {
                            sender.sendMessage("§b§l-> §b/perms " + c.getName() + " " + sc.getName() + " " + (sc.getUsage() != null ? sc.getUsage() : ""));
                        }
                    } else {
                        sender.sendMessage("§c§l[MP]§c No register commands for provided scope.");
                    }
                } else {

                    // Match the second argument to a subcommand
                    SubCommand sc = null;
                    for (SubCommand ssc : cmds) {
                        if (ssc.getName().equalsIgnoreCase(args[1])) {
                            sc = ssc;
                            break;
                        }
                    }

                    // Check if the command exists
                    if (sc != null) {

                        // Pass on any remaining arguments and execute the subcommand
                        List<String> a = new ArrayList<>();
                        a.addAll(Arrays.asList(args).subList(2, args.length));

                        sc.run(sender, a);

                    } else {
                        sender.sendMessage("§c§l[MP] §cUnrecognized command. Try \"/perms " + c.getName() + "\"?");
                    }

                }


            } else {
                sender.sendMessage("§c§l[MP]§c Unrecognized scope. Try \"/perms\"?");
            }

        }


        return true;
    }

    public static List<MasterCommand> getMasterCommandList() {
        return masterCommandList;
    }
}
