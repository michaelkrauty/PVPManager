import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created on 7/20/2014.
 *
 * @author michaelkrauty
 */
public class DataFile {

	private final Main main;

	File dataFile;
	YamlConfiguration data = new YamlConfiguration();

	public DataFile(Main instance) {
		main = instance;
		dataFile = new File(main.getDataFolder() + "/data.yml");
		checkFile();
		reload();
	}

	private void checkFile() {
		if (!dataFile.exists()) {
			try {
				dataFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void save() {
		try {
			data.save(dataFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reload() {
		try {
			data.load(dataFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<UUID> getPlayers() {
		ArrayList<UUID> plist = new ArrayList<UUID>();
		if (data.getList("players") != null) {
			for (String str : (List<String>) data.getList("players")) {
				plist.add(UUID.fromString(str));
			}
		}
		return plist;
	}

	public void setPlayers(ArrayList<UUID> plist) {
		ArrayList<String> strs = new ArrayList<String>();
		for (UUID uuid : plist) {
			strs.add(uuid.toString());
		}
		data.set("players", strs);
		save();
	}
}
