package me.michaelkrauty.PVPManager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created on 7/20/2014.
 *
 * @author michaelkrauty
 */
public class PVPToggleCommand implements CommandExecutor {

	private static Main main;

	public PVPToggleCommand(Main main) {
		this.main = main;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = (Player) sender;
		if (main.protect.contains(player.getUniqueId())) {
			main.protect.remove(player.getUniqueId());
			player.sendMessage(ChatColor.GRAY + "PVP Enabled!");
			return true;
		}
		sender.sendMessage(ChatColor.GRAY + "You can't disable PVP after you have enabled it!");
		return true;
	}
}
