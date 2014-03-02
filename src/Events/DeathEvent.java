package Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import ArenaHandler.RespawnBlockHandler;
import Main.Main;

import com.gekgek.ghost.PlayerListener;

public class DeathEvent implements Listener{

	RespawnBlockHandler h;
	
	public DeathEvent(RespawnBlockHandler rbh){
		h = rbh;
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		Player p = e.getEntity();
		if(h.isTeamBroken(Main.playerTeam.get(p))){
			PlayerListener.toggleGhostMode(p);
			p.setAllowFlight(true);
			p.setFlying(true);
		}
	}

}
