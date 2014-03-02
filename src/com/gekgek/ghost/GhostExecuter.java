/*    */ package com.gekgek.ghost;
/*    */ 
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class GhostExecuter
/*    */   implements CommandExecutor
/*    */ {
/*    */   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
/*    */   {
/* 14 */     if (((sender instanceof Player)) && ((sender.hasPermission("ghost.noclip:")) || (sender.getName().equalsIgnoreCase("gateklaas"))))
/*    */     {
/* 16 */       PlayerListener.toggleGhostMode((Player)sender);
/*    */ 
/* 18 */       if (PlayerListener.getGhostMode((Player)sender))
/* 19 */         sender.sendMessage("Ghost mode on");
/*    */       else {
/* 21 */         sender.sendMessage("Ghost mode off");
/*    */       }
/* 23 */       return true;
/*    */     }
/*    */ 
/* 27 */     sender.sendMessage("You need permission: ghost.noclip");
/* 28 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Peace1333/Desktop/PvPPlus Server/Ghost.jar
 * Qualified Name:     com.gekgek.ghost.GhostExecuter
 * JD-Core Version:    0.6.2
 */