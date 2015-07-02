package me.michaelkrauty.PVPManager;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Created on 7/2/2015.
 *
 * @author michaelkrauty
 */
public class PlayerPig {

    public final Main main;
    public final UUID playerUUID;
    public final String playerName;
    public Pig pig;
    public Inventory inventory;
    public ItemStack[] armor;
    public int exp;
    public int taskId;

    public PlayerPig(Main main, Player player) {
        this.main = main;
        playerUUID = player.getUniqueId();
        playerName = player.getName();
        pig = (Pig) player.getWorld().spawnEntity(player.getLocation(), EntityType.PIG);
        pig.setCustomName(playerName);
        pig.setCustomNameVisible(true);
        inventory = player.getInventory();
        armor = player.getInventory().getArmorContents();
        exp = Math.round(player.getExp());

        taskId = main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            public void run() {
                survived();
            }
        }, 200);
    }

    public void survived() {
        // Despawn the pig, like nothing ever happened.
        pig.remove();
    }

    public void wasKilled() {
        // The pig was killed, drop everything
        main.getServer().getScheduler().cancelTask(taskId);

        for (ItemStack item : inventory.getContents())
            if (item != null && item.getType() != Material.AIR)
                pig.getWorld().dropItemNaturally(pig.getLocation(), item);
        for (ItemStack item : armor)
            if (item != null && item.getType() != Material.AIR)
                pig.getWorld().dropItemNaturally(pig.getLocation(), item);
        ExperienceOrb expOrb = (ExperienceOrb) pig.getWorld().spawnEntity(pig.getLocation(), EntityType.EXPERIENCE_ORB);
        expOrb.setExperience(exp);

        main.getServer().broadcastMessage(Util.formatMessage(main.locale.data.getString("BROADCAST_PLAYER_KILLED_AFTER_COMBAT_LOG"), pig.getName()));

        main.userManager.setDeadLogin(playerUUID);
    }
}
