/*     */ package SpectatorMode.api;

/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;

/*     */ import net.minecraft.server.v1_7_R1.EntityPlayer;

/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.potion.PotionEffect;
/*     */ import org.bukkit.util.Vector;

import Main.Main;
import SpectatorMode.PlayerState;
/*     */ 
/*     */ public class SpectateManager
/*     */ {
/*     */   private Main plugin;
/*  23 */   private int spectateTask = -1;
/*     */ 
/*  25 */   private ArrayList<Player> isSpectating = new ArrayList<Player>();
/*  26 */   private ArrayList<Player> isBeingSpectated = new ArrayList<Player>();
/*  27 */   private HashMap<Player, ArrayList<Player>> spectators = new HashMap<Player, ArrayList<Player>>();
/*  28 */   private HashMap<Player, Player> target = new HashMap<Player, Player>();
/*     */ 
/*  30 */   private ArrayList<String> isClick = new ArrayList<String>();
/*     */ 
/*  32 */   private HashMap<String, SpectateMode> playerMode = new HashMap<String, SpectateMode>();
/*  33 */   private HashMap<String, SpectateAngle> playerAngle = new HashMap<String, SpectateAngle>();
/*     */ 
/*  35 */   private ArrayList<String> isScanning = new ArrayList<String>();
/*  36 */   private HashMap<String, Integer> scanTask = new HashMap<String, Integer>();
/*     */ 
/*  38 */   private HashMap<Player, PlayerState> states = new HashMap<Player, PlayerState>();
/*  39 */   private HashMap<Player, PlayerState> multiInvStates = new HashMap<Player, PlayerState>();
/*     */ 
/*     */   public SpectateManager(Main main)
/*     */   {
/*  43 */     this.plugin = main;
/*     */   }
/*     */ 
/*     */   private void updateSpectators()
/*     */   {
/*  49 */     this.spectateTask = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/*  53 */         for (Player pll : SpectateManager.this.plugin.getServer().getOnlinePlayers())
/*     */         {
	CraftPlayer p = (CraftPlayer) pll;
/*  55 */           if (SpectateManager.this.isSpectating(p))
/*     */           {
/*  57 */             if (SpectateManager.this.plugin.multiverseInvEnabled())
/*     */             {
/*  59 */               if (!p.getWorld().getName().equals(SpectateManager.this.getTarget(p).getWorld().getName()))
/*     */               {
/*  61 */                 p.sendMessage(ChatColor.GRAY + "You were forced to stop spectating because the person you were spectating switched worlds.");
/*  62 */                 SpectateManager.this.stopSpectating(p, true);
/*  63 */                 continue;
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/*  69 */             if (SpectateManager.this.getSpectateAngle(p) == SpectateAngle.FIRST_PERSON)
/*     */             {
/*  71 */               if ((SpectateManager.this.roundTwoDecimals(p.getLocation().getX()) != SpectateManager.this.roundTwoDecimals(SpectateManager.this.getTarget(p).getLocation().getX())) || (SpectateManager.this.roundTwoDecimals(p.getLocation().getY()) != SpectateManager.this.roundTwoDecimals(SpectateManager.this.getTarget(p).getLocation().getY())) || (SpectateManager.this.roundTwoDecimals(p.getLocation().getZ()) != SpectateManager.this.roundTwoDecimals(SpectateManager.this.getTarget(p).getLocation().getZ())) || (SpectateManager.this.roundTwoDecimals(p.getLocation().getYaw()) != SpectateManager.this.roundTwoDecimals(SpectateManager.this.getTarget(p).getLocation().getYaw())) || (SpectateManager.this.roundTwoDecimals(p.getLocation().getPitch()) != SpectateManager.this.roundTwoDecimals(SpectateManager.this.getTarget(p).getLocation().getPitch())))
/*     */               {
/*  73 */                 p.teleport(SpectateManager.this.getTarget(p));
/*     */               }
/*     */ 
/*     */             }
/*  79 */             else if (SpectateManager.this.getSpectateAngle(p) != SpectateAngle.FREEROAM)
/*     */             {
/*  81 */               p.teleport(SpectateManager.this.getSpectateLocation(p));
/*     */             }
/*     */ 
/*  87 */             p.getInventory().setContents(SpectateManager.this.getTarget(p).getInventory().getContents());
/*  88 */             p.getInventory().setArmorContents(SpectateManager.this.getTarget(p).getInventory().getArmorContents());
/*     */ 
/*  90 */             if (SpectateManager.this.getTarget(p).getHealth() == 0.0D)
/*     */             {
/*  92 */               p.setHealth(1.0D);
/*     */             }
/*  96 */             else if (SpectateManager.this.getTarget(p).getHealth() < p.getHealth())
/*     */             {
/*  98 */               double difference = p.getHealth() - SpectateManager.this.getTarget(p).getHealth();
/*  99 */               p.damage(difference);
/*     */             }
/* 101 */             else if (SpectateManager.this.getTarget(p).getHealth() > p.getHealth())
/*     */             {
/* 103 */               p.setHealth(SpectateManager.this.getTarget(p).getHealth());
/*     */             }
/*     */ 
/* 109 */             p.setLevel(SpectateManager.this.getTarget(p).getLevel());
/* 110 */             p.setExp(SpectateManager.this.getTarget(p).getExp());
/*     */ 
/* 112 */             for (PotionEffect e : p.getActivePotionEffects())
/*     */             {
/* 114 */               boolean foundPotion = false;
/*     */ 
/* 116 */               for (PotionEffect e1 : SpectateManager.this.getTarget(p).getActivePotionEffects())
/*     */               {
/* 118 */                 if (e1.getType() == e.getType())
/*     */                 {
/* 120 */                   foundPotion = true;
/* 121 */                   break;
/*     */                 }
/*     */ 
/*     */               }
/*     */ 
/* 127 */               if (!foundPotion)
/*     */               {
/* 129 */                 p.removePotionEffect(e.getType());
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/* 135 */             for (PotionEffect e : SpectateManager.this.getTarget(p).getActivePotionEffects())
/*     */             {
/* 137 */               p.addPotionEffect(e);
/*     */             }
/*     */ 
/* 141 */             p.getInventory().setHeldItemSlot(SpectateManager.this.getTarget(p).getInventory().getHeldItemSlot());
/*     */ 
/* 143 */             if (SpectateManager.this.getTarget(p).isFlying())
/*     */             {
/* 145 */               if (!p.isFlying())
/*     */               {
/* 147 */                 p.setFlying(true);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     , 0L, 1L);
/*     */   }
/*     */ 
/*     */   public void startSpectateTask()
/*     */   {
/* 165 */     if (this.spectateTask == -1)
/*     */     {
/* 167 */       updateSpectators();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void stopSpectateTask()
/*     */   {
/* 175 */     if (this.spectateTask != -1)
/*     */     {
/* 177 */       this.plugin.getServer().getScheduler().cancelTask(this.spectateTask);
/* 178 */       this.spectateTask = -1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startSpectating(Player p, CraftPlayer target, boolean saveState)
/*     */   {
/* 186 */     if (!isSpectating(p))
/*     */     {
/* 188 */       if (saveState)
/*     */       {
/* 190 */         savePlayerState(p);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 196 */     boolean saveMultiInvState = false;
/*     */ 
/* 198 */     if (this.plugin.multiverseInvEnabled())
/*     */     {
/* 200 */       if (!p.getWorld().getName().equals(target.getWorld().getName()))
/*     */       {
/* 202 */         saveMultiInvState = true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 208 */     for (Player player1 : this.plugin.getServer().getOnlinePlayers())
/*     */     {
/* 210 */       player1.hidePlayer(p);
/*     */     }
/*     */ 
/* 214 */     if (saveMultiInvState)
/*     */     {
/* 216 */       p.teleport(target.getWorld().getSpawnLocation());
/* 217 */       this.multiInvStates.put(p, new PlayerState((CraftPlayer) p));
/*     */     }
/*     */ 
/* 221 */     String playerListName = p.getPlayerListName();
/*     */ 
/* 223 */     if (getSpectateAngle(p) == SpectateAngle.FIRST_PERSON)
/*     */     {
/* 225 */       p.hidePlayer(target);
/*     */     }
/*     */     else
/*     */     {
/* 229 */       p.showPlayer(target);
/*     */     }
/*     */ 
/* 233 */     p.setPlayerListName(playerListName);
/*     */ 
/* 235 */     p.setHealth(target.getHealth());
/*     */ 
/* 237 */     p.teleport(target);
/*     */ 
/* 239 */     if (isSpectating(p))
/*     */     {
/* 241 */       setBeingSpectated(getTarget(p), false);
/* 242 */       p.showPlayer(getTarget(p));
/* 243 */       removeSpectator(getTarget(p), p);
/*     */     }
/*     */ 
/* 247 */     for (PotionEffect e : p.getActivePotionEffects())
/*     */     {
/* 249 */       p.removePotionEffect(e.getType());
/*     */     }
/*     */ 
/* 253 */     setTarget(p, target);
/* 254 */     addSpectator(target, p);
/*     */ 
/* 256 */     p.setGameMode(target.getGameMode());
/* 257 */     p.setFoodLevel(target.getFoodLevel());
/*     */ 
/* 259 */     setExperienceCooldown(p, 2147483647);
/* 260 */     p.setAllowFlight(true);
/*     */ 
/* 262 */     setSpectating(p, true);
/* 263 */     setBeingSpectated(target, true);
/*     */ 
/* 265 */     p.sendMessage(ChatColor.GRAY + "You are now spectating " + target.getName() + ".");
/*     */   }
/*     */ 
/*     */   public void stopSpectating(Player p, boolean loadState)
/*     */   {
/* 271 */     setSpectating(p, false);
/* 272 */     setBeingSpectated(getTarget(p), false);
/*     */ 
/* 274 */     removeSpectator(getTarget(p), p);
/*     */ 
/* 276 */     if (isScanning(p))
/*     */     {
/* 278 */       stopScanning(p);
/*     */     }
/*     */ 
/* 282 */     for (PotionEffect e : p.getActivePotionEffects())
/*     */     {
/* 284 */       p.removePotionEffect(e.getType());
/*     */     }
/*     */ 
/* 288 */     if (loadState)
/*     */     {
/* 290 */       loadPlayerState(p);
/*     */     }
/*     */ 
/* 294 */     setExperienceCooldown(p, 0);
/*     */ 
/* 296 */     p.showPlayer(getTarget(p));
/*     */   }
/*     */ 
/*     */   public boolean scrollRight(Player p, ArrayList<Player> playerList)
/*     */   {
/* 302 */     SpectateScrollEvent event = new SpectateScrollEvent(p, playerList, ScrollDirection.RIGHT);
/* 303 */     this.plugin.getServer().getPluginManager().callEvent(event);
/*     */ 
/* 305 */     playerList = new ArrayList<Player>(event.getSpectateList());
/*     */ 
/* 307 */     playerList.remove(p);
/*     */ 
/* 309 */     if (playerList.size() == 0)
/*     */     {
/* 311 */       return false;
/*     */     }
/*     */ 
/* 315 */     if (this.plugin.multiverseInvEnabled())
/*     */     {
/* 317 */       if (isScanning(p))
/*     */       {
/* 319 */         for (Player players : event.getSpectateList())
/*     */         {
/* 321 */           if (!players.getWorld().getName().equals(p.getWorld().getName()))
/*     */           {
/* 323 */             playerList.remove(players);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 333 */     int scrollToIndex = 1;
/*     */ 
/* 335 */     if (getScrollNumber(p, playerList) == playerList.size())
/*     */     {
/* 337 */       scrollToIndex = 1;
/*     */     }
/*     */     else
/*     */     {
/* 341 */       scrollToIndex = getScrollNumber(p, playerList) + 1;
/*     */     }
/*     */ 
/* 345 */     startSpectating(p, (CraftPlayer)playerList.get(scrollToIndex - 1), false);
/*     */ 
/* 347 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean scrollLeft(Player p, ArrayList<Player> playerList)
/*     */   {
/* 353 */     SpectateScrollEvent event = new SpectateScrollEvent(p, playerList, ScrollDirection.LEFT);
/* 354 */     this.plugin.getServer().getPluginManager().callEvent(event);
/*     */ 
/* 356 */     playerList = new ArrayList<Player>(event.getSpectateList());
/*     */ 
/* 358 */     playerList.remove(p);
/*     */ 
/* 360 */     if (playerList.size() == 0)
/*     */     {
/* 362 */       return false;
/*     */     }
/*     */ 
/* 366 */     if (this.plugin.multiverseInvEnabled())
/*     */     {
/* 368 */       if (isScanning(p))
/*     */       {
/* 370 */         for (Player players : event.getSpectateList())
/*     */         {
/* 372 */           if (!players.getWorld().getName().equals(p.getWorld().getName()))
/*     */           {
/* 374 */             playerList.remove(players);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 384 */     int scrollToIndex = 1;
/*     */ 
/* 386 */     if (getScrollNumber(p, playerList) == 1)
/*     */     {
/* 388 */       scrollToIndex = playerList.size();
/*     */     }
/*     */     else
/*     */     {
/* 392 */       scrollToIndex = getScrollNumber(p, playerList) - 1;
/*     */     }
/*     */ 
/* 396 */     startSpectating(p, (CraftPlayer)playerList.get(scrollToIndex - 1), false);
/*     */ 
/* 398 */     return true;
/*     */   }
/*     */ 
/*     */   public int getScrollNumber(Player p, ArrayList<Player> playerList)
/*     */   {
/* 404 */     if (!isSpectating(p))
/*     */     {
/* 406 */       return 1;
/*     */     }
/*     */ 
/* 410 */     if (!playerList.contains(getTarget(p)))
/*     */     {
/* 412 */       return 1;
/*     */     }
/*     */ 
/* 416 */     playerList.remove(p);
/*     */ 
/* 418 */     return playerList.indexOf(getTarget(p)) + 1;
/*     */   }
/*     */ 
/*     */   public void setSpectateMode(Player p, SpectateMode newMode)
/*     */   {
/* 424 */     if (newMode == SpectateMode.DEFAULT)
/*     */     {
/* 426 */       this.playerMode.remove(p.getName());
/*     */     }
/*     */     else
/*     */     {
/* 430 */       this.playerMode.put(p.getName(), newMode);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SpectateMode getSpectateMode(Player p)
/*     */   {
/* 438 */     if (this.playerMode.get(p.getName()) == null)
/*     */     {
/* 440 */       return SpectateMode.DEFAULT;
/*     */     }
/*     */ 
/* 444 */     return (SpectateMode)this.playerMode.get(p.getName());
/*     */   }
/*     */ 
/*     */   public void setSpectateAngle(Player p, SpectateAngle newAngle)
/*     */   {
/* 450 */     if (isSpectating(p))
/*     */     {
/* 452 */       if (newAngle == SpectateAngle.FIRST_PERSON)
/*     */       {
/* 454 */         p.hidePlayer(getTarget(p));
/*     */       }
/*     */       else
/*     */       {
/* 458 */         p.showPlayer(getTarget(p));
/*     */       }
/*     */ 
/* 462 */       if (newAngle == SpectateAngle.FREEROAM)
/*     */       {
/* 464 */         p.teleport(getTarget(p));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 470 */     if (newAngle == SpectateAngle.FIRST_PERSON)
/*     */     {
/* 472 */       this.playerAngle.remove(p.getName());
/*     */     }
/*     */     else
/*     */     {
/* 476 */       this.playerAngle.put(p.getName(), newAngle);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SpectateAngle getSpectateAngle(Player p)
/*     */   {
/* 484 */     if (this.playerAngle.get(p.getName()) == null)
/*     */     {
/* 486 */       return SpectateAngle.FIRST_PERSON;
/*     */     }
/*     */ 
/* 490 */     return (SpectateAngle)this.playerAngle.get(p.getName());
/*     */   }
/*     */ 
/*     */   public void startScanning(final Player p, int interval)
/*     */   {
/* 496 */     this.isScanning.add(p.getName());
/*     */ 
/* 498 */     this.scanTask.put(p.getName(), Integer.valueOf(this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 502 */         SpectateManager.this.scrollRight(p, SpectateManager.this.getSpectateablePlayers());
/*     */       }
/*     */     }
/*     */     , 0L, 20 * interval)));
/*     */   }
/*     */ 
/*     */   public void stopScanning(Player p)
/*     */   {
/* 512 */     this.plugin.getServer().getScheduler().cancelTask(((Integer)this.scanTask.get(p.getName())).intValue());
/* 513 */     this.isScanning.remove(p.getName());
/*     */   }
/*     */ 
/*     */   public boolean isScanning(Player p)
/*     */   {
/* 519 */     if (this.isScanning.contains(p.getName()))
/*     */     {
/* 521 */       return true;
/*     */     }
/*     */ 
/* 525 */     return false;
/*     */   }
/*     */ 
/*     */   public ArrayList<Player> getSpectateablePlayers()
/*     */   {
/* 531 */     ArrayList<Player> spectateablePlayers = new ArrayList<Player>();
/*     */ 
/* 533 */     for (Player onlinePlayers : this.plugin.getServer().getOnlinePlayers())
/*     */     {
/* 535 */       if (!onlinePlayers.isDead())
/*     */       {
/* 541 */         if (!this.isSpectating.contains(onlinePlayers.getName()))
/*     */         {
/* 547 */           if ((!this.plugin.cantspectate_permission_enabled) || 
/* 549 */             (!onlinePlayers.hasPermission("spectate.cantspectate")))
/*     */           {
/* 557 */             spectateablePlayers.add(onlinePlayers);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 561 */     return spectateablePlayers;
/*     */   }
/*     */ 
/*     */   private void setTarget(Player p, Player ptarget)
/*     */   {
/* 567 */     this.target.put(p, ptarget);
/*     */   }
/*     */ 
/*     */   public CraftPlayer getTarget(Player p)
/*     */   {
/* 573 */     return (CraftPlayer)this.target.get(p);
/*     */   }
/*     */ 
/*     */   public boolean isSpectating(Player p)
/*     */   {
/* 579 */     return this.isSpectating.contains(p);
/*     */   }
/*     */ 
/*     */   public boolean isBeingSpectated(Player p)
/*     */   {
/* 585 */     return this.isBeingSpectated.contains(p);
/*     */   }
/*     */ 
/*     */   private void setBeingSpectated(Player p, boolean beingSpectated)
/*     */   {
/* 591 */     if (beingSpectated)
/*     */     {
/* 593 */       if (this.isBeingSpectated.contains(p))
/*     */       {
/* 595 */         return;
/*     */       }
/*     */ 
/* 599 */       this.isBeingSpectated.add(p);
/*     */     }
/*     */     else
/*     */     {
/* 603 */       this.isBeingSpectated.remove(p);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addSpectator(Player p, Player spectator)
/*     */   {
/* 611 */     if (this.spectators.get(p) == null)
/*     */     {
/* 613 */       ArrayList<Player> newSpectators = new ArrayList<Player>();
/*     */ 
/* 615 */       newSpectators.add(spectator);
/*     */ 
/* 617 */       this.spectators.put(p, newSpectators);
/*     */     }
/*     */     else
/*     */     {
/* 621 */       ((ArrayList<Player>)this.spectators.get(p)).add(spectator);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void removeSpectator(Player p, Player spectator)
/*     */   {
/* 629 */     if (this.spectators.get(p) == null)
/*     */     {
/* 631 */       return;
/*     */     }
/*     */ 
/* 635 */     if (((ArrayList<?>)this.spectators.get(p)).size() == 1)
/*     */     {
/* 637 */       this.spectators.remove(p);
/*     */     }
/*     */     else
/*     */     {
/* 641 */       ((ArrayList<?>)this.spectators.get(p)).remove(spectator);
/*     */     }
/*     */   }
/*     */ 
/*     */   @SuppressWarnings("unchecked")
public ArrayList<Player> getSpectators(Player p)
/*     */   {
/* 651 */     return (ArrayList<Player>) (this.spectators.get(p) == null ? new ArrayList<Object>() : (ArrayList<?>)this.spectators.get(p));
/*     */   }
/*     */ 
/*     */   public ArrayList<Player> getSpectatingPlayers()
/*     */   {
/* 657 */     ArrayList<Player> spectatingPlayers = new ArrayList<Player>();
/*     */ 
/* 659 */     for (Player p : this.plugin.getServer().getOnlinePlayers())
/*     */     {
/* 661 */       if (isSpectating(p))
/*     */       {
/* 663 */         spectatingPlayers.add(p);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 669 */     return spectatingPlayers;
/*     */   }
/*     */ 
/*     */   private void setSpectating(Player p, boolean spectating)
/*     */   {
/* 675 */     if (spectating)
/*     */     {
/* 677 */       if (this.isSpectating.contains(p))
/*     */       {
/* 679 */         return;
/*     */       }
/*     */ 
/* 683 */       this.isSpectating.add(p);
/*     */     }
/*     */     else
/*     */     {
/* 687 */       this.isSpectating.remove(p);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void disableScroll(final Player player, long ticks)
/*     */   {
/* 695 */     if (!this.isClick.contains(player.getName()))
/*     */     {
/* 697 */       this.isClick.add(player.getName());
/*     */ 
/* 699 */       this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 703 */           SpectateManager.this.isClick.remove(player.getName());
/*     */         }
/*     */       }
/*     */       , ticks);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Location getSpectateLocation(Player p)
/*     */   {
/* 715 */     if (getSpectateAngle(p) == SpectateAngle.FIRST_PERSON)
/*     */     {
/* 717 */       return getTarget(p).getLocation();
/*     */     }
/*     */ 
/* 721 */     Location playerLoc = getTarget(p).getLocation();
/*     */ 
/* 723 */     double currentSubtraction = 0.0D;
/* 724 */     Location previousLoc = playerLoc;
/*     */ 
/* 726 */     while (currentSubtraction <= 5.0D)
/*     */     {
/* 728 */       playerLoc = getTarget(p).getLocation();
/*     */ 
/* 730 */       Vector v = getTarget(p).getLocation().getDirection().normalize();
/* 731 */       v.multiply(currentSubtraction);
/*     */ 
/* 733 */       if (getSpectateAngle(p) == SpectateAngle.THIRD_PERSON)
/*     */       {
/* 735 */         playerLoc.subtract(v);
/*     */       }
/* 737 */       else if (getSpectateAngle(p) == SpectateAngle.THIRD_PERSON_FRONT)
/*     */       {
/* 739 */         playerLoc.add(v);
/*     */ 
/* 741 */         if (playerLoc.getYaw() < -180.0F)
/*     */         {
/* 743 */           playerLoc.setYaw(playerLoc.getYaw() + 180.0F);
/*     */         }
/*     */         else
/*     */         {
/* 747 */           playerLoc.setYaw(playerLoc.getYaw() - 180.0F);
/*     */         }
/*     */ 
/* 751 */         playerLoc.setPitch(-playerLoc.getPitch());
/*     */       }
/*     */ 
/* 755 */       Material tempMat = new Location(playerLoc.getWorld(), playerLoc.getX(), playerLoc.getY() + 1.5D, playerLoc.getZ()).getBlock().getType();
/*     */ 
/* 757 */       if ((tempMat != Material.AIR) && (tempMat != Material.WATER) && (tempMat != Material.STATIONARY_WATER))
/*     */       {
/* 759 */         return previousLoc;
/*     */       }
/*     */ 
/* 763 */       previousLoc = playerLoc;
/*     */ 
/* 765 */       currentSubtraction += 0.5D;
/*     */     }
/*     */ 
/* 769 */     return playerLoc;
/*     */   }
/*     */ 
/*     */   public PlayerState getPlayerState(Player p)
/*     */   {
/* 775 */     return (PlayerState)this.states.get(p);
/*     */   }
/*     */ 
/*     */   public void savePlayerState(Player p)
/*     */   {
/* 781 */     PlayerState playerstate = new PlayerState((CraftPlayer) p);
/* 782 */     this.states.put(p, playerstate);
/*     */   }
/*     */ 
/*     */   public void loadPlayerState(Player toPlayer)
/*     */   {
/* 788 */     loadPlayerState(toPlayer, toPlayer);
/*     */   }
/*     */ 
/*     */   public void loadPlayerState(Player fromState, Player toPlayer)
/*     */   {
/* 794 */     if ((this.plugin.multiverseInvEnabled()) && (this.multiInvStates.get(fromState) != null))
/*     */     {
/* 796 */       loadFinalState((PlayerState)this.multiInvStates.get(fromState), toPlayer);
/* 797 */       this.multiInvStates.remove(fromState);
/*     */     }
/*     */ 
/* 801 */     loadFinalState(getPlayerState(fromState), toPlayer);
/* 802 */     this.states.remove(fromState);
/*     */   }
/*     */ 
/*     */   private void loadFinalState(PlayerState state, Player toPlayer)
/*     */   {
/* 808 */     toPlayer.teleport(state.location);
/*     */ 
/* 810 */     toPlayer.getInventory().setContents(state.inventory);
/* 811 */     toPlayer.getInventory().setArmorContents(state.armor);
/* 812 */     toPlayer.setFoodLevel(state.hunger);
/* 813 */     toPlayer.setHealth(state.health);
/* 814 */     toPlayer.setLevel(state.level);
/* 815 */     toPlayer.setExp(state.exp);
/* 816 */     toPlayer.getInventory().setHeldItemSlot(state.slot);
/* 817 */     toPlayer.setAllowFlight(state.allowFlight);
/* 818 */     toPlayer.setFlying(state.isFlying);
/* 819 */     toPlayer.setGameMode(state.mode);
/*     */ 
/* 821 */     for (Player onlinePlayers : this.plugin.getServer().getOnlinePlayers())
/*     */     {
/* 823 */       if (!state.vanishedFrom.contains(onlinePlayers))
/*     */       {
/* 825 */         onlinePlayers.showPlayer(toPlayer);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 831 */     for (PotionEffect e : state.potions)
/*     */     {
/* 833 */       toPlayer.addPotionEffect(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ArrayList<Player> getVanishedFromList(Player p)
/*     */   {
/* 841 */     return getPlayerState(p).vanishedFrom;
/*     */   }
/*     */ 
/*     */   public void setExperienceCooldown(Player p, int cooldown)
/*     */   {
/* 847 */     CraftPlayer craft = (CraftPlayer)p;
/* 848 */     EntityPlayer entity = craft.getHandle();
/* 849 */     entity.bv = cooldown;
/*     */   }
/*     */ 
/*     */   public boolean isReadyForNextScroll(Player p)
/*     */   {
/* 855 */     return !this.isClick.contains(p.getName());
/*     */   }
/*     */ 
/*     */   public double roundTwoDecimals(double d)
/*     */   {
/*     */     try
/*     */     {
/* 863 */       DecimalFormat twoDForm = new DecimalFormat("#.##");
/* 864 */       return Double.valueOf(twoDForm.format(d)).doubleValue();
/*     */     }
/*     */     catch (NumberFormatException e) {
/*     */     }
/* 868 */     return d;
/*     */   }
/*     */ }

/* Location:           /Users/Peace1333/Desktop/PvPPlus Server/Spectate.jar
 * Qualified Name:     com.Chipmunk9998.Spectate.api.SpectateManager
 * JD-Core Version:    0.6.2
 */