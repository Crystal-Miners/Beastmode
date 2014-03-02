package Main;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.gekgek.ghost.BlockListener;
import com.gekgek.ghost.MainLoop;
import com.gekgek.ghost.PlayerListener;

import SpectatorMode.SpectateListener;
import SpectatorMode.api.SpectateManager;
import TeamHandler.TeamHandler;
import ArenaHandler.RespawnBlockHandler;
import Events.BlockBreak;
import Events.DeathEvent;
import Events.Hit;
import Events.Leave;
import Events.Sneak;

public class Main extends JavaPlugin{
	public static String Prefix = "§8[§6DragonMode§8] §a";
	public static HashMap<Location, String> rb = new HashMap<Location, String>();
	public static HashMap<Player, String> playerTeam = new HashMap<Player, String>();
	public static ArrayList<Player> players = new ArrayList<Player>();
	public static Main plugin;
	TeamHandler tm = new TeamHandler();
	MainLoop mainLoop = new MainLoop();
	RespawnBlockHandler h = new RespawnBlockHandler();
	
	public void onEnable(){
		/*  25 */     Manager = new SpectateManager(this);
		/*  81 */     getServer().getPluginManager().registerEvents(new SpectateListener(this), this);
		/*  83 */     getAPI().startSpectateTask();
		/* 11 */     new Thread(this.mainLoop).start();
		/*    */ 
		/* 13 */     getServer().getPluginManager().registerEvents(new BlockListener(), this);
		/* 14 */     getServer().getPluginManager().registerEvents(new PlayerListener(), this);
Bukkit.getPluginManager().registerEvents(new BlockBreak(h), this);
Bukkit.getPluginManager().registerEvents(new DeathEvent(h), this);
Bukkit.getPluginManager().registerEvents(new Hit(), this);
Bukkit.getPluginManager().registerEvents(new Sneak(), this);
Bukkit.getPluginManager().registerEvents(new Leave(tm), this);
plugin = this;
rb.put(Config.getRespawnBlock("Blau") , "Blau");
rb.put(Config.getRespawnBlock("Rot") , "Rot");
rb.put(Config.getRespawnBlock("Grün") , "Grün");
rb.put(Config.getRespawnBlock("Gelb") , "Gelb");
	}
	
	/*    */   public void onDisable()
	/*    */   {
		/*  89 */     for (Player p : getAPI().getSpectatingPlayers())
		/*     */     {
		/*  91 */       getAPI().stopSpectating(p, true);
		/*  92 */       p.sendMessage(ChatColor.GRAY + "You were forced to stop spectating because of a server reload.");
		/*     */     }
		/*     */ 
		/*  96 */     getAPI().stopSpectateTask();
	/* 20 */     this.mainLoop.close();
	/* 21 */     PlayerListener.close();
	/*    */   }

	/*     */   private static SpectateManager Manager;
	/*  20 */   public boolean cantspectate_permission_enabled = false;
	/*  21 */   public boolean disable_commands = false;
	/*     */ 
	/*     */   public static SpectateManager getAPI()
	/*     */   {
	/* 102 */     return Manager;
	/*     */   }
	/*     */ 
	/*     */   public boolean multiverseInvEnabled()
	/*     */   {
	/* 108 */     if ((getServer().getPluginManager().getPlugin("Multiverse-Inventories") != null) && (getServer().getPluginManager().getPlugin("Multiverse-Inventories").isEnabled()))
	/*     */     {
	/* 110 */       return true;
	/*     */     }
	/*     */ 
	/* 114 */     return false;
	/*     */   }
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("dm")) {
			Player p = (Player) sender;
		    if (args.length == 0){
		    	p.sendMessage(Prefix + "Plugin by DragonSephHD(Peace1333)");
		    }

		    if (args.length == 2){
		    	if(args[0].equalsIgnoreCase("setRespawn")){
		    		Config.setRespawnBlock(args[1], p.getTargetBlock(null, 30).getLocation());
		    		rb.put(p.getTargetBlock(null, 30).getLocation(), args[1]);
			    	p.sendMessage(Prefix + "Respawnblock gesetzt");
		    	}
		    }
		    	if(args[0].equalsIgnoreCase("join")){
		    		players.add(p);
		    		String Team = null;
		    		Team = tm.addPlayer(p);
		    		playerTeam.put(p, Team);
			    	p.sendMessage(Prefix + "Du bist DragonMode beigetreten");
			    	p.sendMessage(Prefix + "Team: §c" + Team);
		    	}
		    	if(args[0].equalsIgnoreCase("leave")){
		    		players.remove(p);
		    		playerTeam.remove(p);
			    	p.sendMessage(Prefix + "Du hast DragonMode verlassen");
		    	}
		}
		return true;
	}

	
	
	
	
	
	
	
	FileConfiguration cfg;
	
	
	
	public void load() { 
		cfg = getConfig();
		}
		
	
	
 public void saveConf() {
		saveConfig();
		 }
 
 
 public Location getRespawnBlock(String Team){
	 load();
	 String c = "DragonMode.RespawnBlock.";
	 World w = Bukkit.getWorld(cfg.getString(c + Team + ".World"));
	 double x = cfg.getDouble(c + Team + ".X");
	 double y = cfg.getDouble(c + Team + ".Y");
	 double z = cfg.getDouble(c + Team + ".Z");
	 Location loc = new Location(w, x, y, z);
	 return loc;
 }
 
 public void setRespawnBlock(String Team, Location loc){
	 load();
	 String c = "DragonMode.RespawnBlock.";
	 cfg.set(c + Team + ".World", loc.getWorld().getName());
	 cfg.set(c + Team + ".X", loc.getBlockX());
	 cfg.set(c + Team + ".Y", loc.getBlockY());
	 cfg.set(c + Team + ".Z", loc.getBlockZ());
	 saveConf();
 }
}
