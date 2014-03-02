/*    */ package com.gekgek.ghost;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.block.BlockPhysicsEvent;
/*    */ 
/*    */ public class BlockListener
/*    */   implements Listener
/*    */ {
/*    */   private Location loc;
/*    */ 
/*    */   @EventHandler
/*    */   public void onBlockPhysics(BlockPhysicsEvent event)
/*    */   {
/* 20 */     for (Map.Entry entry : PlayerListener.getPlayerMap().entrySet())
/* 21 */       if ((((Player)entry.getKey()).getWorld().equals(event.getBlock().getWorld())) && 
/* 22 */         (Main.pointDistance((this.loc = ((Player)entry.getKey()).getLocation()).getX(), 0.0D, this.loc.getZ(), event.getBlock().getX(), 0.0D, event.getBlock().getZ()).doubleValue() < 11.0D))
/* 23 */         event.setCancelled(true);
/*    */   }
/*    */ }

/* Location:           /Users/Peace1333/Desktop/PvPPlus Server/Ghost.jar
 * Qualified Name:     com.gekgek.ghost.BlockListener
 * JD-Core Version:    0.6.2
 */