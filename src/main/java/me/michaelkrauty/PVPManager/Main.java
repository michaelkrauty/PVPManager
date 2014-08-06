package me.michaelkrauty.PVPManager;

import me.michaelkrauty.PVPManager.objects.User;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created on 7/20/2014.
 *
 * @author michaelkrauty
 */
public class Main extends JavaPlugin {

	public static Main main;

	public static HashMap<Player, User> users = new HashMap<Player, User>();

	public static HashMap<UUID, Zombie> uuidZombieHashMap = new HashMap<UUID, Zombie>();
	public static HashMap<Zombie, Inventory> combatLoggerZombies = new HashMap<Zombie, Inventory>();
	public static HashMap<Zombie, ItemStack[]> combatLoggerZombiesArmor = new HashMap<Zombie, ItemStack[]>();

	public void onEnable() {
		main = this;
		getServer().getPluginManager().registerEvents(new Listener(this), this);
		getServer().getPluginCommand("pvp").setExecutor(new Command(this));
		if (!getDataFolder().exists()) getDataFolder().mkdir();
		File userdata = new File(getDataFolder(), "userdata");
		if (!userdata.exists()) userdata.mkdir();
		for (Player player : getServer().getOnlinePlayers()) {
			users.put(player, new User(this, player));
		}
	}

	public void onDisable() {
		for (Player player : getServer().getOnlinePlayers()) {
			users.get(player).save();
			users.remove(player);
		}
	}
}
