package me.michaelkrauty.PVPManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created on 7/1/2015.
 *
 * @author michaelkrauty
 */
public class PlayerListener implements Listener {

    public final Main main;

    public PlayerListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        // Load user when player joins the server
        main.userManager.loadUser(event.getPlayer());

        // Protect the player if they are a newbie
        if (main.userManager.getUser(event.getPlayer()).firstJoin) {
            main.userManager.getUser(event.getPlayer()).setProtectionTicks(main.config.data.getInt("newbie_protection") * 20);
            main.userManager.getUser(event.getPlayer()).firstJoin = false;
        }

        // If the player is represented by a pig, despawn it
        for (PlayerPig p : main.pigs)
            if (p.playerUUID.toString().equals(event.getPlayer().getUniqueId().toString()))
                p.survived();

        // Kill the player if they combat logged & their pig was killed
        User user = main.userManager.getUser(event.getPlayer());
        if (user.deadLogin()) {
            ItemStack[] newInventory = new ItemStack[user.player.getInventory().getSize()];
            for (int i = 0; i < newInventory.length; i++) {
                newInventory[i] = new ItemStack(Material.AIR);
            }
            ItemStack[] newArmor = new ItemStack[user.player.getInventory().getArmorContents().length];
            for (int i = 0; i < newArmor.length; i++) {
                newArmor[i] = new ItemStack(Material.AIR);
            }
            user.player.getInventory().setContents(newInventory);
            user.player.getInventory().setArmorContents(newArmor);
            user.player.setHealth(0);
            user.yaml.data.set("deadlogin", null);
            Bukkit.broadcastMessage(Util.formatMessage(main.locale.data.getString("BROADCAST_DEAD_LOGIN"), user.player.getName()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        User user = main.userManager.getUser(player);
        // Check if player is combat logging
        if (user.combatTicks > 0) {
            // COMBAT LOG
            main.pigs.add(new PlayerPig(main, player));
        }

        // Unload user when player leaves the server
        main.userManager.unloadUser(event.getPlayer());
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        // Ensure the damaged entity is a player
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }

        Player damaged = (Player) event.getEntity();
        User damagedUser = main.userManager.getUser(damaged);

        Player attacker = null;
        User attackerUser = null;
        if (event.getDamager() instanceof Player) {
            attacker = (Player) event.getDamager();
            attackerUser = main.userManager.getUser(attacker);
        }

        if (event.getDamager() instanceof Player) {
            if (damagedUser.isProtectedFromPVP() || damagedUser.isInvincible()) {
                event.setCancelled(true);
                event.getDamager().sendMessage(Util.formatMessage(main.locale.data.getString("ON_ATTACK_TARGET_IS_PROTECTED"), damaged.getName()));
            } else if (attacker != null) {
                if (damagedUser.isInvincible()) {
                    attacker.sendMessage(Util.formatMessage(main.locale.data.getString("ON_ATTACK_TARGET_IS_INVINCIBLE"), damaged.getName()));
                    event.setCancelled(true);
                }
                if (attackerUser.isProtectedFromPVP() || attackerUser.isInvincible()) {
                    event.getDamager().sendMessage(Util.parseFormat(main.locale.data.getString("ON_ATTACK_ATTACKER_PVP_DISABLED")));
                    event.setCancelled(true);
                }
            }
        } else {
            if (damagedUser.isProtectedFromPVE() || damagedUser.isInvincible())
                event.setCancelled(true);
        }


        if (!event.isCancelled()) {
            if (attacker != null) {
                if (attackerUser.combatTicks == 0)
                    attacker.sendMessage(Util.parseFormat(main.locale.data.getString("ON_ATTACK_NOW_IN_COMBAT_ATTACKER")));
                if (damagedUser.combatTicks == 0)
                    damaged.sendMessage(Util.parseFormat(main.locale.data.getString("ON_ATTACK_NOW_IN_COMBAT_TARGET")));
                attackerUser.combatTicks = 200;
                damagedUser.combatTicks = 200;
            }
        }

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Variables
        Player player = event.getEntity();
        User user = main.userManager.getUser(player);

        if (user.deadLogin()) {
            user.yaml.data.set("deadlogin", null);
            return;
        }

        // Determine whether the player should keep their inventory
        if (player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent dmgEvent = (EntityDamageByEntityEvent) player.getLastDamageCause();
            if (dmgEvent.getDamager() instanceof Player) {
                event.setKeepInventory(main.config.keepInventoryOnPlayerKill);
            } else {
                event.setKeepInventory(main.config.keepInventoryOnNonPlayerKill);
            }
        } else {
            event.setKeepInventory(main.config.keepInventoryOnNonPlayerKill);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.PIG) {
            Pig pig = (Pig) event.getEntity();
            for (PlayerPig pPig : main.pigs)
                if (pPig.pig == pig) {
                    pPig.wasKilled();
                }
        }
    }
}
