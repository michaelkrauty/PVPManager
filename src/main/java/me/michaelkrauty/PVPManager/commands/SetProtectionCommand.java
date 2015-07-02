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
public class SetProtectionCommand implements CommandExecutor {

    public final Main main;

    public SetProtectionCommand(Main main) {
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
            player.sendMessage(cmd.getUsage());
            return true;
        }

        Player target;
        User targetUser;
        int seconds;
        if (args.length == 1) {
            target = player;
            targetUser = user;
            seconds = Integer.valueOf(args[0]);
        } else if (args.length == 2) {
            target = Bukkit.getPlayer(args[0]);
            targetUser = main.userManager.getUser(target);
            seconds = Integer.valueOf(args[1]);
        } else {
            player.sendMessage(cmd.getUsage());
            return true;
        }

        if (seconds > 0)
            targetUser.setPVP(false);
        targetUser.setProtectionTicks(seconds * 20);
        player.sendMessage(Util.formatMessage(main.locale.data.getString("SET_PROTECTION"), target.getName(), seconds));

        return true;
    }
}
