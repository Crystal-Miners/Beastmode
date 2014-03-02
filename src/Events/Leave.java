package Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import TeamHandler.TeamHandler;

public class Leave implements Listener{
	TeamHandler tm = null;
	
	public Leave(TeamHandler tm){
		this.tm = tm;
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		Player p = e.getPlayer();
		if(Main.Main.players.contains(p)){
			Main.Main.players.remove(p);
			Main.Main.playerTeam.remove(p);
			tm.removePlayer(p);
			for(Player broad : Main.Main.players){
				broad.sendMessage(Main.Main.Prefix + "§c" + p + " §ahat das Spiel verlassen");
			}
		}
	}
}
