package me.michaelkrauty.PVPManager;

import me.michaelkrauty.PVPManager.objects.User;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created on 7/20/2014.
 *
 * @author michaelkrauty
 */
public class Main extends JavaPlugin {

	public static ArrayList<UUID> protect;

	public static HashMap<Player, User> users;

	public void onEnable() {
		getServer().getPluginManager().registerEvents(new Listener(this), this);
		getServer().getPluginCommand("pvp").setExecutor(new PVPToggleCommand(this));
		if (!getDataFolder().exists())
			getDataFolder().mkdir();
		for (Player player : getServer().getOnlinePlayers()) {
			users.put(player, new User(this, player));
		}
	}
}
