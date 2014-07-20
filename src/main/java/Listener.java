import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

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

		if (main.protect.contains(target.getUniqueId())) {
			event.setCancelled(true);
			damager.sendMessage(ChatColor.GRAY + target.getName() + " has PVP disabled!");
			target.sendMessage(ChatColor.GRAY + damager.getName() + " tried to hit you, but you have PVP disabled. Enable it by using " + ChatColor.GREEN + "/pvp" + ChatColor.GRAY + ".");
		}
		if (main.protect.contains(damager.getUniqueId())) {
			event.setCancelled(true);
			damager.sendMessage(ChatColor.GRAY + "You have PVP disabled! Enable it by using " + ChatColor.GREEN + "/pvp" + ChatColor.GRAY + ".");
			target.sendMessage(ChatColor.GRAY + damager.getName() + " tried to hit you, but they have PVP disabled.");
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (!event.getPlayer().hasPlayedBefore())
			main.protect.add(event.getPlayer().getUniqueId());
		if (main.protect.contains(event.getPlayer().getUniqueId()))
			event.getPlayer().sendMessage(ChatColor.GRAY + "You have PVP disabled! Enable it by using " + ChatColor.GREEN + "/pvp" + ChatColor.GRAY + ".");
	}
}
