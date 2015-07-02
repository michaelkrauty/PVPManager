package me.michaelkrauty.PVPManager;

import org.bukkit.entity.Player;

import java.io.File;

/**
 * Created on 7/1/2015.
 *
 * @author michaelkrauty
 */
public class User {

    public final Player player;
    public final YamlFile yaml;

    public boolean enablePVPConfirmation = false;
    public int tickLoopId;

    public boolean firstJoin = false;

    public int combatTicks = 0;

    public User(final Main main, final Player player) {
        this.player = player;
        File file = new File(main.userFolder, player.getUniqueId().toString() + ".yml");
        if (!file.exists())
            firstJoin = true;
        this.yaml = new YamlFile(main, file);
        yaml.data.set("name", player.getName());
        if (!yaml.data.isSet("pvp"))
            yaml.data.set("pvp", true);
        if (!yaml.data.isSet("pve"))
            yaml.data.set("pve", true);
        if (!yaml.data.isSet("invincible"))
            yaml.data.set("invincible", false);
        if (getProtectionTicks() > 0)
            setPVP(false);
        tickLoopId = main.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
            public void run() {
                if (getProtectionTicks() > 0) {
                    setProtectionTicks(getProtectionTicks() - 1);
                    if (getProtectionTicks() == 0) {
                        yaml.data.set("protectionticks", null);
                        setPVP(true);
                        player.sendMessage(Util.parseFormat(main.locale.data.getString("MESSAGE_NOW_VULNERABLE")));
                    }
                }
                if (combatTicks > 0) {
                    combatTicks--;
                    if (combatTicks == 0) {
                        player.sendMessage(Util.parseFormat(main.locale.data.getString("MESSAGE_NO_LONGER_IN_COMBAT")));
                    }
                }
            }
        }, 1, 1);
    }

    public boolean deadLogin() {
        return yaml.data.isSet("deadlogin") && yaml.data.getBoolean("deadlogin");
    }

    public boolean isProtectedFromPVP() {
        return !yaml.data.getBoolean("pvp");
    }

    public boolean isProtectedFromPVE() {
        return !yaml.data.getBoolean("pve");
    }

    public boolean isInvincible() {
        return yaml.data.getBoolean("invincible");
    }

    public void setPVP(boolean pvp) {
        yaml.data.set("pvp", pvp);
    }

    public void setPVE(boolean pve) {
        yaml.data.set("pve", pve);
    }

    public void setInvincible(boolean invincible) {
        yaml.data.set("invincible", invincible);
    }

    public void togglePVP() {
        setPVP(isProtectedFromPVP());
    }

    public void togglePVE() {
        setPVE(isProtectedFromPVE());
    }

    public void toggleInvincibility() {
        setInvincible(!isInvincible());
    }

    public void setProtectionTicks(int ticks) {
        yaml.data.set("protectionticks", ticks);
    }

    public int getProtectionTicks() {
        return yaml.data.getInt("protectionticks");
    }
}
