package me.michaelkrauty.PVPManager;

import me.michaelkrauty.PVPManager.objects.User;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.File;

/**
 * Created on 7/20/2014.
 *
 * @author michaelkrauty
 */
public class Listener implements org.bukkit.event.Listener {

	private static Main main;

	public Listener(Main main) {
		this.main = main;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if (!event.isCancelled()) {
			Player target;
			Player damager;
			if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
				damager = (Player) event.getDamager();
				target = (Player) event.getEntity();
			} else return;
			User targetUser = main.users.get(target);
			User damagerUser = main.users.get(damager);

			if (!targetUser.pvpEnabled()) {
				event.setCancelled(true);
				damager.sendMessage(ChatColor.GRAY + target.getName() + " has PVP disabled!");
				target.sendMessage(ChatColor.GRAY + damager.getName() + " tried to hit you, but you have PVP disabled. Enable it by using " + ChatColor.GREEN + "/pvp" + ChatColor.GRAY + ".");
				return;
			}
			if (!damagerUser.pvpEnabled()) {
				event.setCancelled(true);
				damager.sendMessage(ChatColor.GRAY + "You have PVP disabled! Enable it by using " + ChatColor.GREEN + "/pvp" + ChatColor.GRAY + ".");
				target.sendMessage(ChatColor.GRAY + damager.getName() + " tried to hit you, but they have PVP disabled.");
				return;
			}
			boolean targetCombat = targetUser.inCombat();
			boolean damagerCombat = damagerUser.inCombat();
			targetUser.pvpEvent();
			damagerUser.pvpEvent();
			if (!targetCombat) {
				target.sendMessage(ChatColor.LIGHT_PURPLE + "You are in combat for the next " + targetUser.getCombatTime() / 20 + " seconds. DO NOT LOG OUT.");
			}
			if (!damagerCombat) {
				damager.sendMessage(ChatColor.LIGHT_PURPLE + "You are in combat for the next " + damagerUser.getCombatTime() / 20 + " seconds. DO NOT LOG OUT.");
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		User user = new User(main, event.getPlayer());
		if (!user.pvpEnabled())
			event.getPlayer().sendMessage(ChatColor.GRAY + "You have PVP disabled! Enable it by using " + ChatColor.GREEN + "/pvp" + ChatColor.GRAY + ".");
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		User user = main.users.get(player);
		if (user.inCombat()) {
			Pig pig = (Pig) player.getWorld().spawnEntity(player.getLocation(), EntityType.PIG);
			pig.setCustomName("COMBAT LOGGER: " + player.getName());
			pig.setCustomNameVisible(true);
			pig.setRemoveWhenFarAway(false);
			pig.setMetadata("inplaceofplayer", new FixedMetadataValue(main, player.getUniqueId().toString()));
			main.uuidPigHashMap.put(player.getUniqueId(), pig);
			main.combatLoggers.put(pig, player.getInventory());
			main.combatLoggersArmor.put(pig, player.getInventory().getArmorContents());
			main.getServer().broadcastMessage(ChatColor.RED + "COMBAT LOGGER: " + player.getName());
			final Pig finalPig = pig;
			final Player finalPlayer = player;
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				public void run() {
					if (finalPig != null) {
						Pig pig = main.uuidPigHashMap.get(finalPlayer.getUniqueId());
						main.combatLoggers.remove(pig);
						main.combatLoggersArmor.remove(pig);
						pig.remove();
					}
				}
			}, 200);
		}
		user.save();
		main.users.remove(player);
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity().getType() == EntityType.PIG) {
			Pig pig = (Pig) event.getEntity();
			if (main.combatLoggers.get(pig) != null) {
				World world = event.getEntity().getWorld();
				Location loc = event.getEntity().getLocation();
				for (ItemStack i : main.combatLoggers.get(pig).getContents()) {
					if (i != null)
						world.dropItem(loc, i);
				}
				if (main.combatLoggersArmor.get(pig) != null) {
					for (ItemStack i : main.combatLoggersArmor.get(pig)) {
						if (i != null && i.getType() != Material.AIR)
							world.dropItem(loc, i);
					}
				}
				try {
					File userFile = new File(main.getDataFolder() + "/userdata/" + pig.getMetadata("inplaceofplayer").get(0).asString() + ".yml");
					YamlConfiguration yaml = new YamlConfiguration();
					yaml.load(userFile);
					yaml.set("deadlogin", true);
					yaml.save(userFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {

	}

	@EventHandler
	public void onCommandprocess(PlayerCommandPreprocessEvent event) {
		if (event.getPlayer() != null) {
			if (main.users.get(event.getPlayer()).inCombat()) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "You can't use commands while in combat!");
			}
		}
	}
}
