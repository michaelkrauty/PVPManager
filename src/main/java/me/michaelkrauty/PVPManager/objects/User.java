package me.michaelkrauty.PVPManager.objects;

import com.google.gson.Gson;
import me.michaelkrauty.PVPManager.Main;
import org.bukkit.entity.Player;

/**
 * Created on 7/20/2014.
 *
 * @author michaelkrauty
 */
public class User {

	private static Main main;
	private static Player player;
	private boolean pvp;

	public User(Main main, Player player) {
		this.main = main;
		main.users.put(player, this);
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPVPEnabled(boolean pvp) {
		this.pvp = pvp;
	}

	public boolean pvpEnabled() {
		return pvp;
	}

	public void save() {
		Gson gson = new Gson();
		System.out.println(gson.toJson(this));
	}

	public void load() {

	}
}
