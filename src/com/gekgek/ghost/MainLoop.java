/*    */ package com.gekgek.ghost;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Calendar;
/*    */ 
/*    */ public class MainLoop
/*    */   implements Runnable
/*    */ {
/*    */ 
/* 11 */   private boolean running = true;
/* 12 */   private static ArrayList<UpdatableObject> objectList = new ArrayList<UpdatableObject>();
/*    */ 
/*    */   public void run()
/*    */   {
/* 20 */     while (this.running)
/*    */     {
/* 22 */       long beginTime = Calendar.getInstance().getTimeInMillis();
/*    */ 
/* 24 */       for (UpdatableObject object : objectList)
/* 25 */         object.update();
/*    */       long totalTime;
/* 27 */       if ((totalTime = Calendar.getInstance().getTimeInMillis() - beginTime) < 100L)
/*    */       {
/*    */         try
/*    */         {
/* 31 */           Thread.sleep(100L - totalTime);
/*    */         }
/*    */         catch (InterruptedException e)
/*    */         {
/* 35 */           e.printStackTrace();
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void addUpdatableObject(UpdatableObject object) {
/* 42 */     objectList.add(object);
/*    */   }
/*    */ 
/*    */   public void close()
/*    */   {
/* 47 */     this.running = false;
/* 48 */     objectList.clear();
/*    */   }
/*    */ 
/*    */   public static abstract interface UpdatableObject
/*    */   {
/*    */     public abstract void update();
/*    */   }
/*    */ }

/* Location:           /Users/Peace1333/Desktop/PvPPlus Server/Ghost.jar
 * Qualified Name:     com.gekgek.ghost.MainLoop
 * JD-Core Version:    0.6.2
 */