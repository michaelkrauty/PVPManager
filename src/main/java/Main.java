import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created on 7/20/2014.
 *
 * @author michaelkrauty
 */
public class Main extends JavaPlugin {

	public static ArrayList<UUID> protect;

	public DataFile dataFile;

	public void onEnable() {
		getServer().getPluginManager().registerEvents(new Listener(this), this);
		getServer().getPluginCommand("pvp").setExecutor(new PVPToggleCommand(this));
		if (!getDataFolder().exists())
			getDataFolder().mkdir();
		dataFile = new DataFile(this);
		protect = dataFile.getPlayers();
	}

	public void onDisable() {
		dataFile.setPlayers(protect);
	}
}
