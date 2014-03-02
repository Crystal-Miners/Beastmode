package Main;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
	
	static FileConfiguration cfg;
	public Config(){
		load();
		saveConf();
	}
	
	public static void load() { 
		cfg = Main.plugin.getConfig();
		}
		
	
	
 public static void saveConf() {
		Main.plugin.saveConfig();
		 }
 
 
 public static Location getRespawnBlock(String Team){
	 load();
	 String c = "DragonMode.RespawnBlock.";
	 try{
	 World w = Bukkit.getWorld(cfg.getString(c + Team + ".World"));
	 double x = cfg.getDouble(c + Team + ".X");
	 double y = cfg.getDouble(c + Team + ".Y");
	 double z = cfg.getDouble(c + Team + ".Z");
	 Location loc = new Location(w, x, y, z);
	 return loc;
	 }catch (Exception e){
		 
	 }
	 return null;
 }
 
 public static void setRespawnBlock(String Team, Location loc){
	 load();
	 String c = "DragonMode.RespawnBlock.";
	 cfg.set(c + Team + ".World", loc.getWorld().getName());
	 cfg.set(c + Team + ".X", loc.getBlockX());
	 cfg.set(c + Team + ".Y", loc.getBlockY());
	 cfg.set(c + Team + ".Z", loc.getBlockZ());
	 saveConf();
 }
		  
		  
		  
}
