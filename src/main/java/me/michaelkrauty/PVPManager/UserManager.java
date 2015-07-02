package me.michaelkrauty.PVPManager;

import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created on 7/1/2015.
 *
 * @author michaelkrauty
 */
public class UserManager {

    public final Main main;
    public ArrayList<User> users = new ArrayList<User>();

    public UserManager(Main main) {
        this.main = main;
    }

    public void loadUser(Player player) {
        if (!users.contains(player)) {
            users.add(new User(main, player));
            main.getLogger().info("Loaded user: " + player.getName());
        }
    }

    public void unloadUser(Player player) {
        User user = getUser(player);
        if (user != null) {
            if (user.tickLoopId != 0)
                main.getServer().getScheduler().cancelTask(user.tickLoopId);
            user.yaml.saveAsync();
            users.remove(user);
            main.getLogger().info("Unloaded user: " + player.getName());
        }
    }

    public void loadAllUsers() {
        for (Player player : main.getServer().getOnlinePlayers())
            loadUser(player);
    }

    public void unloadAllUsers() {
        for (User user : users) {
            user.yaml.save();
            users.remove(user);
            main.getLogger().info("Unloaded user: " + user.player.getName());
        }
    }

    public User getUser(Player player) {
        for (User user : users)
            if (user.player == player)
                return user;
        return null;
    }

    public void setDeadLogin(UUID uuid) {
        YamlFile playerFile = new YamlFile(main, new File(main.userFolder, uuid.toString() + ".yml"));
        playerFile.data.set("deadlogin", true);
        playerFile.saveAsync();
    }
}
