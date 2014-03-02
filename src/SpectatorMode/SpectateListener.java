/*     */ package SpectatorMode;
/*     */ 
/*     */ import java.util.ArrayList;

/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.entity.Monster;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.block.BlockBreakEvent;
/*     */ import org.bukkit.event.block.BlockPlaceEvent;
/*     */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*     */ import org.bukkit.event.entity.EntityDamageEvent;
/*     */ import org.bukkit.event.entity.EntityRegainHealthEvent;
/*     */ import org.bukkit.event.entity.EntityTargetEvent;
/*     */ import org.bukkit.event.entity.FoodLevelChangeEvent;
/*     */ import org.bukkit.event.entity.PlayerDeathEvent;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryCloseEvent;
/*     */ import org.bukkit.event.inventory.InventoryOpenEvent;
/*     */ import org.bukkit.event.player.PlayerCommandPreprocessEvent;
/*     */ import org.bukkit.event.player.PlayerDropItemEvent;
/*     */ import org.bukkit.event.player.PlayerGameModeChangeEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEntityEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.player.PlayerJoinEvent;
/*     */ import org.bukkit.event.player.PlayerPickupItemEvent;
/*     */ import org.bukkit.event.player.PlayerQuitEvent;

import Main.Main;
import SpectatorMode.api.ScrollDirection;
import SpectatorMode.api.SpectateMode;
import SpectatorMode.api.SpectateScrollEvent;
/*     */ 
/*     */ public class SpectateListener
/*     */   implements Listener
/*     */ {
/*     */   Main plugin;
/*     */ 
/*     */   public SpectateListener(Main main)
/*     */   {
/*  43 */     this.plugin = main;
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onPlayerJoin(PlayerJoinEvent event)
/*     */   {
/*  50 */     for (Player p : Main.getAPI().getSpectatingPlayers())
/*     */     {
/*  52 */       event.getPlayer().hidePlayer(p);
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onPlayerQuit(PlayerQuitEvent event)
/*     */   {
/*  61 */     if (Main.getAPI().isSpectating(event.getPlayer()))
/*     */     {
/*  63 */       Main.getAPI().stopSpectating(event.getPlayer(), true);
/*  64 */       return;
/*     */     }
/*  66 */     if (Main.getAPI().isBeingSpectated(event.getPlayer()))
/*     */     {
/*  68 */       for (Player p : Main.getAPI().getSpectators(event.getPlayer()))
/*     */       {
/*  70 */         if ((Main.getAPI().getSpectateMode(p) == SpectateMode.SCROLL) || (Main.getAPI().isScanning(p)))
/*     */         {
/*  72 */           SpectateScrollEvent scrollEvent = new SpectateScrollEvent(p, Main.getAPI().getSpectateablePlayers(), ScrollDirection.RIGHT);
/*  73 */           Bukkit.getServer().getPluginManager().callEvent(scrollEvent);
/*     */ 
/*  75 */           ArrayList<Player> playerList = scrollEvent.getSpectateList();
/*     */ 
/*  77 */           playerList.remove(p);
/*  78 */           playerList.remove(event.getPlayer());
/*     */ 
/*  80 */           p.sendMessage(ChatColor.GRAY + "The person you were previously spectating has disconnected.");
/*     */ 
/*  82 */           if (!Main.getAPI().scrollRight(p, playerList))
/*     */           {
/*  84 */             Main.getAPI().stopSpectating(p, true);
/*  85 */             p.sendMessage(ChatColor.GRAY + "You were forced to stop spectating because there is nobody left to spectate.");
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*  91 */           Main.getAPI().stopSpectating(p, true);
/*  92 */           p.sendMessage(ChatColor.GRAY + "You were forced to stop spectating because the person you were spectating disconnected.");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*  98 */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onPlayerDeath(PlayerDeathEvent event)
/*     */   {
/* 107 */     if (Main.getAPI().isBeingSpectated(event.getEntity()))
/*     */     {
/* 109 */       for (Player p : Main.getAPI().getSpectators(event.getEntity()))
/*     */       {
/* 111 */         if ((Main.getAPI().getSpectateMode(p) == SpectateMode.SCROLL) || (Main.getAPI().isScanning(p)))
/*     */         {
/* 113 */           SpectateScrollEvent scrollEvent = new SpectateScrollEvent(p, Main.getAPI().getSpectateablePlayers(), ScrollDirection.RIGHT);
/* 114 */           Bukkit.getServer().getPluginManager().callEvent(scrollEvent);
/*     */ 
/* 116 */           ArrayList<Player> playerList = scrollEvent.getSpectateList();
/*     */ 
/* 118 */           playerList.remove(p);
/* 119 */           playerList.remove(event.getEntity());
/*     */ 
/* 121 */           p.sendMessage(ChatColor.GRAY + "The person you were previously spectating has died.");
/*     */ 
/* 123 */           if (!Main.getAPI().scrollRight(p, playerList))
/*     */           {
/* 125 */             Main.getAPI().stopSpectating(p, true);
/* 126 */             p.sendMessage(ChatColor.GRAY + "You were forced to stop spectating because there is nobody left to spectate.");
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 132 */           Main.getAPI().stopSpectating(p, true);
/* 133 */           p.sendMessage(ChatColor.GRAY + "You were forced to stop spectating because the person you were spectating died.");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 139 */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onPlayerDamage(EntityDamageEvent event)
/*     */   {
/* 148 */     if ((event instanceof EntityDamageByEntityEvent))
/*     */     {
/* 150 */       EntityDamageByEntityEvent event1 = (EntityDamageByEntityEvent)event;
/*     */ 
/* 152 */       if ((event1.getDamager() instanceof Player))
/*     */       {
/* 154 */         if (Main.getAPI().isSpectating((Player)event1.getDamager()))
/*     */         {
/* 156 */           event.setCancelled(true);
/* 157 */           return;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 165 */     if (!(event.getEntity() instanceof Player))
/*     */     {
/* 167 */       return;
/*     */     }
/*     */ 
/* 171 */     Player p = (Player)event.getEntity();
/*     */ 
/* 173 */     if (Main.getAPI().isSpectating(p))
/*     */     {
/* 175 */       event.setCancelled(true);
/* 176 */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onPlayerInteract(PlayerInteractEvent event)
/*     */   {
/* 185 */     if (Main.getAPI().isSpectating(event.getPlayer()))
/*     */     {
/* 187 */       if (Main.getAPI().isReadyForNextScroll(event.getPlayer()))
/*     */       {
/* 189 */         if (Main.getAPI().getSpectateMode(event.getPlayer()) == SpectateMode.SCROLL)
/*     */         {
/* 191 */           if ((event.getAction() == Action.LEFT_CLICK_AIR) || (event.getAction() == Action.LEFT_CLICK_BLOCK))
/*     */           {
/* 193 */             if (Bukkit.getServer().getOnlinePlayers().length > 2)
/*     */             {
/* 195 */               Main.getAPI().scrollLeft(event.getPlayer(), Main.getAPI().getSpectateablePlayers());
/* 196 */               Main.getAPI().disableScroll(event.getPlayer(), 5L);
/*     */             }
/*     */ 
/*     */           }
/* 200 */           else if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK))
/*     */           {
/* 202 */             if (Bukkit.getServer().getOnlinePlayers().length > 2)
/*     */             {
/* 204 */               Main.getAPI().scrollRight(event.getPlayer(), Main.getAPI().getSpectateablePlayers());
/* 205 */               Main.getAPI().disableScroll(event.getPlayer(), 5L);
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 215 */       event.setCancelled(true);
/* 216 */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
/*     */   {
/* 225 */     if (Main.getAPI().isSpectating(event.getPlayer()))
/*     */     {
/* 227 */       if (Main.getAPI().isReadyForNextScroll(event.getPlayer()))
/*     */       {
/* 229 */         if (Main.getAPI().getSpectateMode(event.getPlayer()) == SpectateMode.SCROLL)
/*     */         {
/* 231 */           if (Bukkit.getServer().getOnlinePlayers().length > 2)
/*     */           {
/* 233 */             Main.getAPI().scrollRight(event.getPlayer(), Main.getAPI().getSpectateablePlayers());
/* 234 */             Main.getAPI().disableScroll(event.getPlayer(), 5L);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 242 */       event.setCancelled(true);
/* 243 */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onFoodLevelChange(FoodLevelChangeEvent event)
/*     */   {
/* 252 */     if ((event.getEntity() instanceof Player))
/*     */     {
/* 254 */       Player player = (Player)event.getEntity();
/*     */ 
/* 256 */       if (!event.isCancelled())
/*     */       {
/* 258 */         if (Main.getAPI().isBeingSpectated(player))
/*     */         {
/* 260 */           for (Player p : Main.getAPI().getSpectators(player))
/*     */           {
/* 262 */             p.setFoodLevel(event.getFoodLevel());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onPlayerGameModeChange(PlayerGameModeChangeEvent event)
/*     */   {
/* 277 */     if (!event.isCancelled())
/*     */     {
/* 279 */       if (Main.getAPI().isBeingSpectated(event.getPlayer()))
/*     */       {
/* 281 */         for (Player p : Main.getAPI().getSpectators(event.getPlayer()))
/*     */         {
/* 283 */           p.setGameMode(event.getNewGameMode());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onInventoryOpen(InventoryOpenEvent event)
/*     */   {
/* 296 */     if (!(event.getPlayer() instanceof Player))
/*     */     {
/* 298 */       return;
/*     */     }
/*     */ 
/* 302 */     Player p = (Player)event.getPlayer();
/*     */ 
/* 304 */     if (Main.getAPI().isBeingSpectated(p))
/*     */     {
/* 306 */       for (Player spectators : Main.getAPI().getSpectators(p))
/*     */       {
/* 308 */         spectators.openInventory(event.getInventory());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onInventoryClose(InventoryCloseEvent event)
/*     */   {
/* 319 */     if (!(event.getPlayer() instanceof Player))
/*     */     {
/* 321 */       return;
/*     */     }
/*     */ 
/* 325 */     Player p = (Player)event.getPlayer();
/*     */ 
/* 327 */     if (Main.getAPI().isBeingSpectated(p))
/*     */     {
/* 329 */       for (Player spectators : Main.getAPI().getSpectators(p))
/*     */       {
/* 331 */         spectators.closeInventory();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onInventoryClick(InventoryClickEvent event)
/*     */   {
/* 342 */     if (!(event.getWhoClicked() instanceof Player))
/*     */     {
/* 344 */       return;
/*     */     }
/*     */ 
/* 348 */     Player p = (Player)event.getWhoClicked();
/*     */ 
/* 350 */     if (Main.getAPI().isSpectating(p))
/*     */     {
/* 352 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onPlayerDropItem(PlayerDropItemEvent event)
/*     */   {
/* 361 */     if (Main.getAPI().isSpectating(event.getPlayer()))
/*     */     {
/* 363 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onPlayerPickupItem(PlayerPickupItemEvent event)
/*     */   {
/* 372 */     if (Main.getAPI().isSpectating(event.getPlayer()))
/*     */     {
/* 374 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onBlockBreak(BlockBreakEvent event)
/*     */   {
/* 383 */     if (Main.getAPI().isSpectating(event.getPlayer()))
/*     */     {
/* 385 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onBlockPlace(BlockPlaceEvent event)
/*     */   {
/* 394 */     if (Main.getAPI().isSpectating(event.getPlayer()))
/*     */     {
/* 396 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onPlayerRegen(EntityRegainHealthEvent event)
/*     */   {
/* 405 */     if ((event.getEntity() instanceof Player))
/*     */     {
/* 407 */       Player p = (Player)event.getEntity();
/*     */ 
/* 409 */       if (Main.getAPI().isSpectating(p))
/*     */       {
/* 411 */         event.setCancelled(true);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onMobTarget(EntityTargetEvent event)
/*     */   {
/* 422 */     if ((event.getEntity() instanceof Monster))
/*     */     {
/* 424 */       if ((event.getTarget() instanceof Player))
/*     */       {
/* 426 */         if (Main.getAPI().isSpectating((Player)event.getTarget()))
/*     */         {
/* 428 */           event.setCancelled(true);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   @EventHandler
/*     */   public void onCommandPreprocess(PlayerCommandPreprocessEvent event)
/*     */   {
/* 441 */     if (Main.getAPI().isSpectating(event.getPlayer()))
/*     */     {
/* 443 */       if (this.plugin.disable_commands)
/*     */       {
/* 445 */         if ((!event.getMessage().startsWith("/spectate")) && (!event.getMessage().startsWith("/spec")))
/*     */         {
/* 447 */           event.setCancelled(true);
/* 448 */           event.getPlayer().sendMessage(ChatColor.RED + "You can not execute this command while spectating.");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Peace1333/Desktop/PvPPlus Server/Spectate.jar
 * Qualified Name:     com.Chipmunk9998.Spectate.SpectateListener
 * JD-Core Version:    0.6.2
 */