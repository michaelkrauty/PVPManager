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
 * Created on 7/1/2015.
 *
 * @author michaelkrauty
 */
public class ProtectionCommand implements CommandExecutor {

    public final Main main;

    public ProtectionCommand(Main main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        // Ensure the sender is a player
        if (!(sender instanceof Player))
            return true;

        // Initiate variables
        Player player = (Player) sender;
        User user = main.userManager.getUser(player);

        if (args.length == 0) {
            sendResultMessage(user, user);
            return true;
        }

        Player target;
        User targetUser;
        if (args.length == 1) {
            target = Bukkit.getPlayer(args[0]);
            targetUser = main.userManager.getUser(target);
        } else {
            target = player;
            targetUser = user;
        }
        sendResultMessage(user, targetUser);

        return true;
    }

    private void sendResultMessage(User user, User target) {
        int seconds = Math.round(target.getProtectionTicks() / 20);
        if (user == target)
            if (seconds == 0)
                user.player.sendMessage(Util.parseFormat(main.locale.data.getString("PROTECTION_NOT_PROTECTED")));
            else
                user.player.sendMessage(Util.formatMessage(main.locale.data.getString("PROTECTION_PROTECTED"), seconds));
        else if (seconds == 0)
            user.player.sendMessage(Util.formatMessage(main.locale.data.getString("PROTECTION_NOT_PROTECTED_OTHER_PLAYER"), target.player.getName()));
        else
            user.player.sendMessage(Util.formatMessage(main.locale.data.getString("PROTECTION_PROTECTED_OTHER_PLAYER"), target.player.getName()));
    }
}
