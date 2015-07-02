package me.michaelkrauty.PVPManager.commands;

import me.michaelkrauty.PVPManager.Main;
import me.michaelkrauty.PVPManager.User;
import me.michaelkrauty.PVPManager.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created on 7/2/2015.
 *
 * @author michaelkrauty
 */
public class CancelProtectionCommand implements CommandExecutor {

    public final Main main;

    public CancelProtectionCommand(Main main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        // Ensure the sender is a player
        if (!(sender instanceof Player))
            return true;

        // Initiate variables
        Player player = (Player) sender;
        User user = main.userManager.getUser(player);


        // Toggle PVP for self
        if (args.length == 0) {
            if (user.getProtectionTicks() > 0) {
                if (!user.enablePVPConfirmation) {
                    user.enablePVPConfirmation = true;
                    player.sendMessage(Util.parseFormat(main.locale.data.getString("CANCEL_PROTECTION_CONFIRM")));
                    return true;
                } else {
                    user.setProtectionTicks(0);
                    player.sendMessage(Util.parseFormat(main.locale.data.getString("CANCEL_PROTECTION_NOW_VULNERABLE")));
                    return true;
                }
            } else {
                player.sendMessage(Util.parseFormat(main.locale.data.getString("CANCEL_PROTECTION_NOT_PROTECTED")));
                return true;
            }
        } else {
            if (!player.hasPermission("pvpmanager.cancelprotection.others")) {
                player.sendMessage(Util.parseFormat(main.locale.data.getString("NO_PERMISSION")));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (!(target instanceof Player)) {
                player.sendMessage(Util.formatMessage(main.locale.data.getString("PLAYER_NOT_FOUND"), target.getName()));
                return true;
            }
            User targetUser = main.userManager.getUser(target);
            targetUser.setProtectionTicks(0);
            player.sendMessage(Util.formatMessage(main.locale.data.getString("CANCEL_PROTECTION_NOW_VULNERABLE_OTHER_PLAYER"), target.getName()));
            return true;
        }
    }
}
