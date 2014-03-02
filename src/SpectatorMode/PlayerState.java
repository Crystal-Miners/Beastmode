/*    */ package SpectatorMode;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;

/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.GameMode;
/*    */ import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.potion.PotionEffect;
/*    */ 
/*    */ public class PlayerState
/*    */ {
/*    */   public Player player;
/*    */   public ItemStack[] inventory;
/*    */   public ItemStack[] armor;
/*    */   public int hunger;
/*    */   public double health;
/*    */   public int level;
/*    */   public float exp;
/*    */   public int slot;
/*    */   public boolean allowFlight;
/*    */   public boolean isFlying;
/*    */   public GameMode mode;
/*    */   public Location location;
/*    */   public Collection<PotionEffect> potions;
/* 30 */   public ArrayList<Player> vanishedFrom = new ArrayList<Player>();
/*    */ 
/*    */   public PlayerState(CraftPlayer p)
/*    */   {
/* 34 */     this.player = p;
/* 35 */     this.inventory = p.getInventory().getContents();
/* 36 */     this.armor = p.getInventory().getArmorContents();
/* 37 */     this.hunger = p.getFoodLevel();
/* 38 */     this.health = p.getHealth();
/* 39 */     this.level = p.getLevel();
/* 40 */     this.exp = p.getExp();
/* 41 */     this.slot = p.getInventory().getHeldItemSlot();
/* 42 */     this.allowFlight = p.getAllowFlight();
/* 43 */     this.isFlying = p.isFlying();
/* 44 */     this.mode = p.getGameMode();
/* 45 */     this.location = p.getLocation();
/*    */ 
/* 47 */     this.potions = p.getActivePotionEffects();
/*    */ 
/* 49 */     for (Player players : Bukkit.getServer().getOnlinePlayers())
/*    */     {
/* 51 */       if (players != p)
/*    */       {
/* 53 */         if (!players.canSee(p))
/*    */         {
/* 55 */           this.vanishedFrom.add(players);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/Peace1333/Desktop/PvPPlus Server/Spectate.jar
 * Qualified Name:     com.Chipmunk9998.Spectate.PlayerState
 * JD-Core Version:    0.6.2
 */