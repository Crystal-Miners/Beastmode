package Events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import ArenaHandler.RespawnBlockHandler;

public class BlockBreak implements Listener{

	RespawnBlockHandler h;
	
	public BlockBreak(RespawnBlockHandler rbh){
		h = rbh;
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		Block b = e.getBlock();
		Player p = e.getPlayer();
		if(Main.Main.rb.containsKey(b.getLocation())){
			String Team = Main.Main.rb.get(b.getLocation());
			if(Main.Main.playerTeam.containsKey(p)){
				if(Main.Main.playerTeam.get(p) == Team){
					e.setCancelled(true);
					return;
				}else{
					if(h.isBroken(b.getLocation()) == false){
						for(Player broad : Main.Main.players){
							broad.sendMessage(Main.Main.Prefix + "Respawnblock von §c" + Team + " §aabgebaut");
						}
						h.addBroken(b.getLocation(), Team);
					}
				}
					
				}
		}
	}
}
