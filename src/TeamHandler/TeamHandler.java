package TeamHandler;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class TeamHandler {
	
	ArrayList<Player> blau = new ArrayList<Player>();
	ArrayList<Player> rot = new ArrayList<Player>();
	ArrayList<Player> grün = new ArrayList<Player>();
	ArrayList<Player> gelb = new ArrayList<Player>();

	
	public String addPlayer(Player p ){
		String Team = null;
		if(blau.size()!= 4){
			blau.add(p);
			Team = "Blau";
		}
		if(rot.size()!= 4){
			rot.add(p);
			Team = "Rot";
		}
		if(grün.size()!= 4){
			grün.add(p);
			Team = "Grün";
		}
		if(gelb.size()!= 4){
			gelb.add(p);
			Team = "Gelb";
		}
		return Team;
	}
	
	public void removePlayer(Player p ){
		if(blau.contains(p)){
			blau.remove(p);
		}
		if(rot.contains(p)){
			rot.remove(p);
		}
		if(grün.contains(p)){
			grün.remove(p);
		}
		if(gelb.contains(p)){
			gelb.remove(p);
		}
	}

}
