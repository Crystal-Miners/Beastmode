/*    */ package SpectatorMode.api;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.event.HandlerList;
/*    */ 
/*    */ public class SpectateScrollEvent extends Event
/*    */ {
/* 11 */   private static final HandlerList handlers = new HandlerList();
/*    */   private Player scroller;
/*    */   private ArrayList<Player> scrollList;
/*    */   private ScrollDirection direction;
/*    */ 
/*    */   public SpectateScrollEvent(Player scroller, ArrayList<Player> scrollList, ScrollDirection direction)
/*    */   {
/* 19 */     this.scroller = scroller;
/* 20 */     this.scrollList = scrollList;
/* 21 */     this.direction = direction;
/*    */   }
/*    */ 
/*    */   public Player getPlayer()
/*    */   {
/* 27 */     return this.scroller;
/*    */   }
/*    */ 
/*    */   public ArrayList<Player> getSpectateList()
/*    */   {
/* 33 */     return this.scrollList;
/*    */   }
/*    */ 
/*    */   public ScrollDirection getDirection()
/*    */   {
/* 39 */     return this.direction;
/*    */   }
/*    */ 
/*    */   public HandlerList getHandlers()
/*    */   {
/* 45 */     return handlers;
/*    */   }
/*    */ 
/*    */   public static HandlerList getHandlerList()
/*    */   {
/* 51 */     return handlers;
/*    */   }
/*    */ }

/* Location:           /Users/Peace1333/Desktop/PvPPlus Server/Spectate.jar
 * Qualified Name:     com.Chipmunk9998.Spectate.api.SpectateScrollEvent
 * JD-Core Version:    0.6.2
 */