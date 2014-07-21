package me.michaelkrauty.PVPManager.objects;

import me.michaelkrauty.PVPManager.Main;
import org.bukkit.configuration.file.YamlConfiguration;
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
		if (deadLogin) {
			player.getInventory().setArmorContents(null);
			player.getInventory().setContents(new ItemStack[] {null});
			player.setHealth(0);
		}
		main.getServer().getScheduler().scheduleAsyncRepeatingTask(main, new Runnable() {
			public void run() {
				if (combatTime > 0)
					combatTime = combatTime - 1;
			}
		}, 1, 1);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPVP(boolean pvp) {
		this.pvp = pvp;
	}

	public void setCombat(boolean combat) {
		this.combat = combat;
	}

	public void setCombatTime(long combatTime) {
		this.combatTime = combatTime;
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

	public void wasHit() {
		combat = true;
		combatTime = 200;
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
