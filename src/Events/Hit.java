package Events;

import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import Main.Main;

import com.gekgek.ghost.PlayerListener;

public class Hit implements Listener {
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e ){
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
			Player p = (Player) e.getEntity();
			Player d = (Player) e.getDamager();
			if(PlayerListener.getGhostMode(d)){
				PlayerListener.toggleGhostMode(p);
				Main.getAPI().startSpectating(d, (CraftPlayer) p, true);
				e.setCancelled(true);
			}
		}
	}

}
