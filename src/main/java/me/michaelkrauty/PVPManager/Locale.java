package me.michaelkrauty.PVPManager;

import java.io.File;

/**
 * Created on 7/2/2015.
 *
 * @author michaelkrauty
 */
public class Locale extends YamlFile {

    public Locale(Main main) {
        super(main, new File(main.getDataFolder(), "locale.yml"), true);
    }
}
