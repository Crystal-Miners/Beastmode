/*     */ package com.gekgek.ghost;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockState;
/*     */ import org.bukkit.block.Chest;
/*     */ import org.bukkit.block.Sign;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.player.PlayerMoveEvent;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.InventoryHolder;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class PlayerListener
/*     */   implements Listener
/*     */ {
/*  24 */   private static Map<Player, ArrayList<BlockState>> playerMap = new HashMap<Player, ArrayList<BlockState>>();
/*  25 */   private static Map<InventoryHolder, ItemStack[]> cargoMap = new HashMap<InventoryHolder, ItemStack[]>();
/*     */ 
/*     */   @EventHandler
/*     */   public void onPlayerMoveListener(PlayerMoveEvent event)
/*     */   {
/*  30 */     Player player = event.getPlayer();
/*  31 */     World world = player.getWorld();
/*     */ 
/*  33 */     if (playerMap.containsKey(player))
/*  34 */       if ((player.hasPermission("ghost.noclip:")) || (player.getName().equalsIgnoreCase("gateklaas")))
/*     */       {
/*  36 */         Location location = player.getLocation();
/*  37 */         ArrayList<BlockState> blockStateList = (ArrayList<BlockState>)playerMap.get(player);
/*     */ 
/*  39 */         player.setFlying(true);
/*     */ 
/*  42 */         for (int xx = -2; xx <= 2; xx++) {
/*  43 */           for (int yy = -2; yy <= 2; yy++) {
/*  44 */             for (int zz = -2; zz <= 2; zz++)
/*  45 */               if (Main.pointDistance(0.0D, 0.0D, 0.0D, xx, yy, zz).doubleValue() <= 2.5D)
/*  46 */                 blockSave(blockStateList, world, location.getBlockX() + xx, location.getBlockY() + yy + 1, location.getBlockZ() + zz);
/*     */           }
/*     */         }
/*  49 */         for (int i = 0; i < blockStateList.size(); i++)
/*     */         {
/*  51 */           BlockState blockState = (BlockState)blockStateList.get(i);
/*  52 */           int x = blockState.getX();
/*  53 */           int y = blockState.getY();
/*  54 */           int z = blockState.getZ();
/*     */ 
/*  56 */           if ((Main.pointDistance(location.getBlockX(), 0.0D, location.getBlockZ(), x, 0.0D, z).doubleValue() > 8.0D) || (
/*  57 */             (Main.pointDistance(location.getBlockX(), location.getBlockY(), location.getBlockZ(), x, y, z).doubleValue() > 4.0D) && (
/*  58 */             (MaterialIsSolid(blockState.getType())) || (!world.getBlockAt(x, y - 1, z).isEmpty()))))
/*     */           {
/*  60 */             blockLoad(blockState);
/*  61 */             blockStateList.remove(i);
/*     */           }
/*     */           else
/*     */           {
/*  65 */             for (Player otherPlayer : world.getPlayers())
/*  66 */               if ((otherPlayer != player) && (otherPlayer != null))
/*  67 */                 otherPlayer.sendBlockChange(blockState.getLocation(), blockState.getTypeId(), blockState.getRawData());
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/*  72 */         toggleGhostMode(player);
/*     */       }
/*     */   }
/*     */ 
/*     */   private static void blockSave(ArrayList<BlockState> blockStateList, World world, int x, int y, int z) {
/*  77 */     Block block = world.getBlockAt(x, y, z);
/*     */ 
/*  79 */     if (!block.isEmpty())
/*     */     {
/*  81 */       for (int i = 0; i < blockStateList.size(); i++) {
/*  82 */         if (block.getLocation() == ((BlockState)blockStateList.get(i)).getLocation())
/*     */         {
/*  84 */           blockStateList.remove(i);
/*  85 */           break;
/*     */         }
/*     */       }
/*  88 */       if (block.getType() == Material.PISTON_EXTENSION)
/*     */       {
/*  90 */         switch (block.getData() << 8 >> 8)
/*     */         {
/*     */         case 0:
/*  93 */           blockSave(blockStateList, world, x, y + 1, z);
/*  94 */           break;
/*     */         case 1:
/*  96 */           blockSave(blockStateList, world, x, y - 1, z);
/*  97 */           break;
/*     */         case 2:
/*  99 */           blockSave(blockStateList, world, x, y, z + 1);
/* 100 */           break;
/*     */         case 3:
/* 102 */           blockSave(blockStateList, world, x, y, z - 1);
/* 103 */           break;
/*     */         case 4:
/* 105 */           blockSave(blockStateList, world, x + 1, y, z);
/* 106 */           break;
/*     */         case 5:
/* 108 */           blockSave(blockStateList, world, x - 1, y, z);
/* 109 */           break;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 115 */       BlockState state = block.getState();
/* 116 */       blockStateList.add(state);
/*     */ 
/* 118 */       if ((state instanceof Chest))
/*     */       {
/* 120 */         Chest chest = (Chest)state;
/* 121 */         cargoMap.put(chest, chest.getBlockInventory().getContents());
/* 122 */         chest.getBlockInventory().clear();
/*     */       }
/* 125 */       else if ((state instanceof InventoryHolder))
/*     */       {
/* 127 */         InventoryHolder inv = (InventoryHolder)state;
/* 128 */         cargoMap.put(inv, inv.getInventory().getContents());
/* 129 */         inv.getInventory().clear();
/*     */       }
/*     */ 
/* 132 */       block.setType(Material.AIR);
/*     */ 
/* 134 */       if ((!MaterialIsSolid((block = world.getBlockAt(x, y + 1, z)).getType())) && (!block.isEmpty()))
/* 135 */         blockSave(blockStateList, world, x, y + 1, z);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void blockLoad(BlockState blockState)
/*     */   {
/* 141 */     blockState.update(true);
/* 142 */     BlockState state = blockState.getWorld().getBlockAt(blockState.getX(), blockState.getY(), blockState.getZ()).getState();
/*     */ 
/* 144 */     if ((state instanceof Sign))
/*     */     {
/* 146 */       for (int j = 0; j < 4; j++) {
/* 147 */         ((Sign)state).setLine(j, ((Sign)blockState).getLine(j));
/*     */       }
/* 149 */       state.update(true);
/*     */     }
/* 152 */     else if ((state instanceof Chest)) {
/* 153 */       ((Chest)state).getBlockInventory().setContents((ItemStack[])cargoMap.remove(state));
/*     */     }
/* 155 */     else if (cargoMap.containsKey(state)) {
/* 156 */       ((InventoryHolder)state).getInventory().setContents((ItemStack[])cargoMap.remove(state));
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void toggleGhostMode(Player player) {
/* 161 */     if (playerMap.containsKey(player))
/*     */     {
/* 163 */       ArrayList<BlockState> blockStateList = (ArrayList<BlockState>)playerMap.get(player);
/*     */ 
/* 165 */       for (BlockState blockState : blockStateList) {
/* 166 */         blockLoad(blockState);
/*     */       }
/* 168 */       blockStateList.clear();
/* 169 */       playerMap.remove(player);
/*     */     }
/*     */     else
/*     */     {
/* 173 */       playerMap.put(player, new ArrayList<BlockState>());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean getGhostMode(Player player) {
/* 178 */     return playerMap.containsKey(player);
/*     */   }
/*     */ 
/*     */   public static Map<Player, ArrayList<BlockState>> getPlayerMap() {
/* 182 */     return playerMap;
/*     */   }
/*     */ 
/*     */   public static void close()
/*     */   {
/* 187 */     for (Map.Entry entry : playerMap.entrySet())
/*     */     {
/* 189 */       ArrayList<BlockState> blockStateList = (ArrayList<BlockState>)entry.getValue();
/*     */ 
/* 191 */       for (BlockState blockState : blockStateList) {
/* 192 */         blockLoad(blockState);
/*     */       }
/* 194 */       blockStateList.clear();
/*     */     }
/*     */ 
/* 197 */     playerMap.clear();
/*     */   }
/*     */ 
/*     */   public static boolean MaterialIsSolid(Material m)
/*     */   {
/* 283 */     return (m.equals(Material.STONE)) || 
/* 203 */       (m.equals(Material.GRASS)) || 
/* 204 */       (m.equals(Material.DIRT)) || 
/* 205 */       (m.equals(Material.COBBLESTONE)) || 
/* 206 */       (m.equals(Material.WOOD)) || 
/* 207 */       (m.equals(Material.BEDROCK)) || 
/* 208 */       (m.equals(Material.GOLD_ORE)) || 
/* 209 */       (m.equals(Material.IRON_ORE)) || 
/* 210 */       (m.equals(Material.COAL_ORE)) || 
/* 211 */       (m.equals(Material.WOOD)) || 
/* 212 */       (m.equals(Material.LEAVES)) || 
/* 213 */       (m.equals(Material.LOG)) || 
/* 214 */       (m.equals(Material.SPONGE)) || 
/* 215 */       (m.equals(Material.GLASS)) || 
/* 216 */       (m.equals(Material.LAPIS_ORE)) || 
/* 217 */       (m.equals(Material.LAPIS_BLOCK)) || 
/* 218 */       (m.equals(Material.DISPENSER)) || 
/* 219 */       (m.equals(Material.SANDSTONE)) || 
/* 220 */       (m.equals(Material.NOTE_BLOCK)) || 
/* 221 */       (m.equals(Material.PISTON_STICKY_BASE)) || 
/* 222 */       (m.equals(Material.PISTON_BASE)) || 
/* 223 */       (m.equals(Material.PISTON_EXTENSION)) || 
/* 224 */       (m.equals(Material.WOOL)) || 
/* 225 */       (m.equals(Material.GOLD_BLOCK)) || 
/* 226 */       (m.equals(Material.IRON_BLOCK)) || 
/* 227 */       (m.equals(Material.DOUBLE_STEP)) || 
/* 228 */       (m.equals(Material.STEP)) || 
/* 229 */       (m.equals(Material.BRICK)) || 
/* 230 */       (m.equals(Material.TNT)) || 
/* 231 */       (m.equals(Material.BOOKSHELF)) || 
/* 232 */       (m.equals(Material.MOSSY_COBBLESTONE)) || 
/* 233 */       (m.equals(Material.OBSIDIAN)) || 
/* 234 */       (m.equals(Material.MOB_SPAWNER)) || 
/* 235 */       (m.equals(Material.WOOD_STAIRS)) || 
/* 236 */       (m.equals(Material.CHEST)) || 
/* 237 */       (m.equals(Material.DIAMOND_ORE)) || 
/* 238 */       (m.equals(Material.DIAMOND_BLOCK)) || 
/* 239 */       (m.equals(Material.WORKBENCH)) || 
/* 240 */       (m.equals(Material.SOIL)) || 
/* 241 */       (m.equals(Material.FURNACE)) || 
/* 242 */       (m.equals(Material.BURNING_FURNACE)) || 
/* 243 */       (m.equals(Material.COBBLESTONE_STAIRS)) || 
/* 244 */       (m.equals(Material.REDSTONE_ORE)) || 
/* 245 */       (m.equals(Material.GLOWING_REDSTONE_ORE)) || 
/* 246 */       (m.equals(Material.ICE)) || 
/* 247 */       (m.equals(Material.SNOW_BLOCK)) || 
/* 248 */       (m.equals(Material.CLAY)) || 
/* 249 */       (m.equals(Material.JUKEBOX)) || 
/* 250 */       (m.equals(Material.FENCE)) || 
/* 251 */       (m.equals(Material.PUMPKIN)) || 
/* 252 */       (m.equals(Material.NETHERRACK)) || 
/* 253 */       (m.equals(Material.SOUL_SAND)) || 
/* 254 */       (m.equals(Material.GLOWSTONE)) || 
/* 255 */       (m.equals(Material.JACK_O_LANTERN)) || 
/* 256 */       (m.equals(Material.CAKE_BLOCK)) || 
/* 257 */       (m.equals(Material.LOCKED_CHEST)) || 
/* 258 */       (m.equals(Material.MONSTER_EGG)) || 
/* 259 */       (m.equals(Material.SMOOTH_BRICK)) || 
/* 260 */       (m.equals(Material.MELON_BLOCK)) || 
/* 261 */       (m.equals(Material.FENCE_GATE)) || 
/* 262 */       (m.equals(Material.BRICK_STAIRS)) || 
/* 263 */       (m.equals(Material.SMOOTH_STAIRS)) || 
/* 264 */       (m.equals(Material.MYCEL)) || 
/* 265 */       (m.equals(Material.NETHER_BRICK)) || 
/* 266 */       (m.equals(Material.NETHER_FENCE)) || 
/* 267 */       (m.equals(Material.NETHER_BRICK_STAIRS)) || 
/* 268 */       (m.equals(Material.ENCHANTMENT_TABLE)) || 
/* 269 */       (m.equals(Material.CAULDRON)) || 
/* 270 */       (m.equals(Material.ENDER_PORTAL_FRAME)) || 
/* 271 */       (m.equals(Material.ENDER_STONE)) || 
/* 272 */       (m.equals(Material.REDSTONE_LAMP_OFF)) || 
/* 273 */       (m.equals(Material.REDSTONE_LAMP_ON)) || 
/* 274 */       (m.equals(Material.WOOD_DOUBLE_STEP)) || 
/* 275 */       (m.equals(Material.WOOD_STEP)) || 
/* 276 */       (m.equals(Material.SANDSTONE_STAIRS)) || 
/* 277 */       (m.equals(Material.EMERALD_ORE)) || 
/* 278 */       (m.equals(Material.ENDER_CHEST)) || 
/* 279 */       (m.equals(Material.EMERALD_BLOCK)) || 
/* 280 */       (m.equals(Material.SPRUCE_WOOD_STAIRS)) || 
/* 281 */       (m.equals(Material.BIRCH_WOOD_STAIRS)) || 
/* 282 */       (m.equals(Material.JUNGLE_WOOD_STAIRS)) || 
/* 283 */       (m.equals(Material.BOOK_AND_QUILL));
/*     */   }
/*     */ }

/* Location:           /Users/Peace1333/Desktop/PvPPlus Server/Ghost.jar
 * Qualified Name:     com.gekgek.ghost.PlayerListener
 * JD-Core Version:    0.6.2
 */