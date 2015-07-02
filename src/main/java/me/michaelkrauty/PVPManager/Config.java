package me.michaelkrauty.PVPManager;

import java.io.File;

/**
 * Created on 7/1/2015.
 *
 * @author michaelkrauty
 */
public class Config extends YamlFile {

    public boolean keepInventoryOnPlayerKill;
    public boolean keepInventoryOnNonPlayerKill;

    public Config(Main main) {
        super(main, new File(main.getDataFolder(), "config.yml"));
        keepInventoryOnPlayerKill = data.getBoolean("keep_inventory_player");
        keepInventoryOnNonPlayerKill = data.getBoolean("keep_inventory_non_player");
    }
}
