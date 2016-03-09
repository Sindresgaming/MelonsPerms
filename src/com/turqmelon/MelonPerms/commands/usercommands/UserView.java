package com.turqmelon.MelonPerms.commands.usercommands;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 * <p>
 * Written by Devon "Turqmelon" - http://turqmelon.com
 * <p>
 * This code is licensed under Creative Commons Attributation-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * <p>
 * You are free to:
 * SHARE - copy and redistribute the material in any medium or format
 * ADAPT - remix, transform, and build upon the material
 * <p>
 * The licensor cannot revoke these freedoms as long as you follow the license terms.
 * <p>
 * Under the following terms:
 * ATTRIBUTION - You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
 * <p>
 * NONCOMMERCIAL - You may not use the material for commercial purposes.
 * <p>
 * SHAREALIKE - If you remix, transform, or build upon the material, you must distribute your contributions under the same license as the original.
 * <p>
 * Full License: http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 ******************************************************************************/

/*******************************************************************************
 * Written by Devon "Turqmelon" - http://turqmelon.com
 *
 * This code is licensed under Creative Commons Attributation-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 *
 * You are free to:
 * SHARE - copy and redistribute the material in any medium or format
 * ADAPT - remix, transform, and build upon the material
 *
 * The licensor cannot revoke these freedoms as long as you follow the license terms.
 *
 * Under the following terms:
 * ATTRIBUTION - You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
 *
 * NONCOMMERCIAL - You may not use the material for commercial purposes.
 *
 * SHAREALIKE - If you remix, transform, or build upon the material, you must distribute your contributions under the same license as the original.
 *
 * Full License: http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 ******************************************************************************/

import com.turqmelon.MelonPerms.commands.MasterCommand;
import com.turqmelon.MelonPerms.commands.SubCommand;
import com.turqmelon.MelonPerms.exceptions.InsufficientArgumentException;
import com.turqmelon.MelonPerms.exceptions.UserNotFoundException;
import com.turqmelon.MelonPerms.groups.Group;
import com.turqmelon.MelonPerms.users.User;
import com.turqmelon.MelonPerms.users.UserManager;
import com.turqmelon.MelonPerms.util.Privilege;
import org.bukkit.command.CommandSender;

import java.util.List;

public class UserView extends SubCommand {

    public UserView(MasterCommand command) {
        super("view", command, "Shows user information", "<User>", "perms.user.view");
    }

    // Outputs information about a user
    @Override
    protected void execute(CommandSender sender, List<String> args) throws InsufficientArgumentException, UserNotFoundException {

        if (args.size() == 1) {

            User user = UserManager.getUser(args.get(0));

            // Ensures the user exists
            if (user != null) {

                sender.sendMessage("§e§l[MP] §e§n" + user.getName() + "§e information:");
                sender.sendMessage("§e§l[MP] §eUUID: " + user.getUuid().toString());
                sender.sendMessage("§e§l[MP] §eSuper User: " + user.isSuperUser());
                sender.sendMessage("§e§l[MP] §eMember of " + user.getGroups().size() + " group(s):");
                for (Group group : user.getGroups()) {
                    sender.sendMessage("§b§l-> §b" + group.getName() + (group.isConditional() ? " §6§o(Conditional)" : "") + (group.isDefault() ? " §7§o(Default)" : ""));
                }
                sender.sendMessage("§e§l[MP] §e" + user.getPrivileges().size() + " user-specific privilege(s):");
                for (Privilege privilege : user.getPrivileges()) {
                    sender.sendMessage("§b§l-> §b" + privilege.getNode() + (privilege.isNegated() ? " §c§o(Negated)" : "") + (privilege.isConditional() ? " §6§o(Conditional)" : ""));
                }
                sender.sendMessage("§e§l[MP] §eMeta:");
                sender.sendMessage("§b§l-> §bPrefix: " + (user.getMetaPrefix() != null ? user.getMetaPrefix() : "§c§oUndefined"));
                sender.sendMessage("§b§l-> §bSuffix: " + (user.getMetaSuffix() != null ? user.getMetaSuffix() : "§c§oUndefined"));


            } else {
                throw new UserNotFoundException(args.get(0));
            }

        } else {
            throw new InsufficientArgumentException(1);
        }

    }
}
