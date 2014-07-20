package me.michaelkrauty.PVPManager;

import me.michaelkrauty.PVPManager.objects.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		Player target;
		Player damager;
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			damager = (Player) event.getDamager();
			target = (Player) event.getEntity();
		} else return;

		if (!main.users.get(target).pvpEnabled()) {
			event.setCancelled(true);
			damager.sendMessage(ChatColor.GRAY + target.getName() + " has PVP disabled!");
			target.sendMessage(ChatColor.GRAY + damager.getName() + " tried to hit you, but you have PVP disabled. Enable it by using " + ChatColor.GREEN + "/pvp" + ChatColor.GRAY + ".");
		}
		if (!main.users.get(damager).pvpEnabled()) {
			event.setCancelled(true);
			damager.sendMessage(ChatColor.GRAY + "You have PVP disabled! Enable it by using " + ChatColor.GREEN + "/pvp" + ChatColor.GRAY + ".");
			target.sendMessage(ChatColor.GRAY + damager.getName() + " tried to hit you, but they have PVP disabled.");
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		User user = new User(main, event.getPlayer());
		if (!event.getPlayer().hasPlayedBefore())
			user.setPVPEnabled(false);
		if (!user.pvpEnabled())
			event.getPlayer().sendMessage(ChatColor.GRAY + "You have PVP disabled! Enable it by using " + ChatColor.GREEN + "/pvp" + ChatColor.GRAY + ".");
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		main.users.remove(event.getPlayer());
	}
}
