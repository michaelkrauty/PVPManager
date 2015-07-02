package me.michaelkrauty.PVPManager.commands;

import me.michaelkrauty.PVPManager.Main;
import me.michaelkrauty.PVPManager.User;
import me.michaelkrauty.PVPManager.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created on 7/2/2015.
 *
 * @author michaelkrauty
 */
public class PVPTimeCommand implements CommandExecutor {

    public final Main main;

    public PVPTimeCommand(Main main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        // Ensure the sender is a player
        if (!(sender instanceof Player))
            return true;

        // Initiate variables
        Player player = (Player) sender;
        User user = main.userManager.getUser(player);

        // Check PVP time for self
        if (user.combatTicks > 0) {
            player.sendMessage(Util.formatMessage(main.locale.data.getString("PVP_TIME"), (Math.round(user.combatTicks / 20) + 1)));
            return true;
        } else {
            player.sendMessage(Util.parseFormat(main.locale.data.getString("PVP_TIME_NOT_IN_COMBAT")));
            return true;
        }
    }
}
