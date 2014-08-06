package me.michaelkrauty.PVPManager;

import me.michaelkrauty.CarbonCore.CarbonCore;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created on 7/20/2014.
 *
 * @author michaelkrauty
 */
public class Main extends JavaPlugin {

	public static Main main;
	public static CarbonCore core;

	public void onEnable() {
		main = this;
		core = (CarbonCore) getServer().getPluginManager().getPlugin("CarbonCore");
		getServer().getPluginManager().registerEvents(new Listener(core), this);
		getServer().getPluginCommand("pvp").setExecutor(new Command(core));
	}
}
