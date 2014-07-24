package me.michaelkrauty.PVPManager.objects;

import me.michaelkrauty.PVPManager.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

/**
 * Created on 7/20/2014.
 *
 * @author michaelkrauty
 */
public class User {

	private static Player player;
	private boolean pvp;
	private boolean combat;
	private boolean deadLogin;
	private long combatTime;
	private Main main;
	private File file;

	public User(Main main, Player player) {
		this.main = main;
		file = new File(main.getDataFolder() + "/userdata/" + player.getUniqueId().toString() + ".yml");
		if (!file.exists()) {
			pvp = false;
			combat = false;
			combatTime = 0;
			save();
		}
		main.users.put(player, this);
		this.player = player;
		load();
		main.getLogger().info("loaded user: " + player.getName());
		if (main.uuidPigHashMap.get(player.getUniqueId()) instanceof Pig) {
			Pig pig = main.uuidPigHashMap.get(player.getUniqueId());
			main.combatLoggers.remove(pig);
			main.combatLoggersArmor.remove(pig);
			pig.remove();
		}
		if (deadLogin) {
			player.getInventory().setArmorContents(null);
			player.getInventory().setContents(new ItemStack[]{null});
			player.setHealth(0);
		}
		final Player playerFinal = player;
		main.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
			public void run() {
				boolean start = combat;
				if (combatTime > 0)
					combatTime = combatTime - 1;
				else
					combat = false;
				if (start != combat)
					playerFinal.sendMessage(ChatColor.LIGHT_PURPLE + "You may now safely log out.");

			}
		}, 1, 1);
	}

	public void setPVP(boolean pvp) {
		this.pvp = pvp;
	}

	public boolean pvpEnabled() {
		return pvp;
	}

	public boolean inCombat() {
		return combat;
	}

	public long getCombatTime() {
		return combatTime;
	}

	public void pvpEvent() {
		combatTime = 200;
		combat = true;
		if (player.isFlying())
			player.setFlying(false);
	}

	public void save() {
		YamlConfiguration yml = new YamlConfiguration();
		yml.set("pvp", pvp);
		try {
			if (!file.exists()) file.createNewFile();
			yml.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void load() {
		YamlConfiguration yml = new YamlConfiguration();
		try {
			yml.load(file);
			pvp = yml.getBoolean("pvp");
			deadLogin = yml.getBoolean("deadlogin");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
