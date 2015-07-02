package me.michaelkrauty.PVPManager;

import me.michaelkrauty.PVPManager.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

/**
 * Created on 7/1/2015.
 *
 * @author michaelkrauty
 */
public class Main extends JavaPlugin {

    public File userFolder;
    public UserManager userManager;
    public Config config;
    public Locale locale;

    public ArrayList<PlayerPig> pigs = new ArrayList<PlayerPig>();

    public void onEnable() {

        // Check data folder
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        // Setup config
        if (!new File(getDataFolder(), "config.yml").exists())
            saveDefaultConfig();
        config = new Config(this);
        locale = new Locale(this);

        // Check users folder
        userFolder = new File(getDataFolder(), "users");
        if (!userFolder.exists())
            userFolder.mkdir();

        // Register the player listener
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        // Register commands
        getServer().getPluginCommand("pvp").setExecutor(new PVPCommand(this));
        getServer().getPluginCommand("pve").setExecutor(new PVECommand(this));
        getServer().getPluginCommand("invincible").setExecutor(new InvincibleCommand(this));
        getServer().getPluginCommand("protection").setExecutor(new ProtectionCommand(this));
        getServer().getPluginCommand("cancelprotection").setExecutor(new CancelProtectionCommand(this));
        getServer().getPluginCommand("pvptime").setExecutor(new PVPTimeCommand(this));
        getServer().getPluginCommand("setprotection").setExecutor(new SetProtectionCommand(this));

        // Setup user manager
        userManager = new UserManager(this);
        userManager.loadAllUsers();
    }

    public void onDisable() {
        userManager.unloadAllUsers();
    }
}
