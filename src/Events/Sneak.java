package Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import Main.Main;

import com.gekgek.ghost.PlayerListener;

public class Sneak implements Listener{
	
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e){
		Player p = e.getPlayer();
			 if (!Main.getAPI().isSpectating(p))
		        {
		          return;
		        }
			p.sendMessage(Main.Prefix + "Du spectatest §c" + Main.getAPI().getTarget(p) + " §anun nicht mehr");
			PlayerListener.toggleGhostMode(p);
			Main.getAPI().stopSpectating(p, true);
	}
}
