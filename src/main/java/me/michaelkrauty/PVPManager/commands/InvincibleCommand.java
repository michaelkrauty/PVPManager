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
public class InvincibleCommand implements CommandExecutor {

    public final Main main;

    public InvincibleCommand(Main main) {
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
            user.toggleInvincibility();
            sendResultMessage(user, user);
            return true;
        }

        String sub = args[0];
        String[] tmp = new String[args.length - 1];
        for (int i = 1; i < args.length; i++)
            tmp[i - 1] = args[i];
        args = tmp;

        Player target;
        User targetUser;
        if (args.length == 1) {
            target = Bukkit.getPlayer(args[0]);
            targetUser = main.userManager.getUser(target);
        } else {
            target = player;
            targetUser = user;
        }

        if (sub.equalsIgnoreCase("enable")) {
            targetUser.setInvincible(true);
        } else if (sub.equalsIgnoreCase("disable")) {
            targetUser.setInvincible(false);
        } else if (sub.equalsIgnoreCase("toggle")) {
            targetUser.toggleInvincibility();
        }
        sendResultMessage(user, targetUser);

        return true;
    }

    private void sendResultMessage(User user, User target) {
        if (target.isInvincible())
            user.player.sendMessage(Util.formatMessage(main.locale.data.getString("INVINCIBILITY_ENABLED"), target.player.getName()));
        else
            user.player.sendMessage(Util.formatMessage(main.locale.data.getString("INVINCIBILITY_DISABLED"), target.player.getName()));
    }
}
