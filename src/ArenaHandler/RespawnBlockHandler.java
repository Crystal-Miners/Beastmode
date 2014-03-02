package ArenaHandler;

import java.util.ArrayList;

import org.bukkit.Location;

public class RespawnBlockHandler {
	
	private ArrayList<Location> broken = new ArrayList<Location>();
	private ArrayList<String> Team = new ArrayList<String>();

	
	public boolean isBroken(Location loc){
		if(broken.contains(loc)){
			return true;
		}
		return false;
	}
	
	public boolean isTeamBroken(String Blue){
		if(Team.contains(Blue)){
			return true;
		}
		return false;
	}
	
	public void addBroken(Location loc, String bb){
		if(broken.contains(loc)){
			return;
		}
		broken.add(loc);
		Team.add(bb);
	}
	
	public void removeBroken(Location loc){
		if(!(broken.contains(loc))){
			return;
		}
		broken.remove(loc);
		Team.remove(Main.Main.rb.get(loc));
	}
}
