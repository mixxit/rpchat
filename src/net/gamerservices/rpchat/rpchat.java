/*     */ package net.gamerservices.rpchat;
/*     */ 
/*     */ import com.avaje.ebean.EbeanServer;
/*     */ import com.avaje.ebean.ExpressionList;
/*     */ import com.avaje.ebean.Query;
/*     */ import com.earth2me.essentials.Essentials;
/*     */ import com.earth2me.essentials.User;
/*     */ import com.palmergames.bukkit.towny.Towny;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.persistence.PersistenceException;
/*     */ import net.milkbowl.vault.permission.Permission;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.command.PluginCommand;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Event.Priority;
/*     */ import org.bukkit.event.Event.Type;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.PluginDescriptionFile;
/*     */ import org.bukkit.plugin.PluginManager;
/*     */ import org.bukkit.plugin.RegisteredServiceProvider;
/*     */ import org.bukkit.plugin.ServicesManager;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.bukkit.scheduler.BukkitScheduler;
/*     */ 
/*     */ public class rpchat extends JavaPlugin
/*     */ {
/*     */   private PluginManager pm;
/*  54 */   private Towny towny = null;
/*     */   private Listener rpchatPlayerListener;
/*     */   private Listener rpchatEntityListener;
/*     */   public static Permission permission;
/*  58 */   private Essentials essentials = null;
/*  59 */   public boolean isEssentials = false;
/*     */   static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
/*     */ 
/*     */   static
/*     */   {
/*  57 */     permission = null;
/*     */   }
/*     */ 
/*     */   public boolean isMuted(Player player)
/*     */   {
/*  64 */     if (this.isEssentials)
/*     */     {
/*  66 */       return this.essentials.getUser(player).isMuted();
/*     */     }
/*     */ 
/*  69 */     return false;
/*     */   }
/*     */ 
/*     */   public void onDisable()
/*     */   {
/*  76 */     PluginDescriptionFile desc = getDescription();
/*  77 */     System.out.println(desc.getFullName() + " has been disabled");
/*     */   }
/*     */ 
/*     */   private Boolean setupPermissions()
/*     */   {
/*  82 */     RegisteredServiceProvider permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
/*  83 */     if (permissionProvider != null) {
/*  84 */       permission = (Permission)permissionProvider.getProvider();
/*     */     }
/*  86 */     if (permission != null) return Boolean.valueOf(true); return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   public boolean hasPerm(Player player, String string)
/*     */   {
/*  92 */     return permission.has(player, string);
/*     */   }
/*     */ 
/*     */   public String getGroups(Player player)
/*     */   {
/* 102 */     if (player.getName().toLowerCase().equals("Tuppinator".toLowerCase()))
/*     */     {
/* 104 */       return ChatColor.DARK_RED + "General" + ChatColor.WHITE;
/*     */     }
/*     */ 
/* 107 */     if (player.getName().toLowerCase().equals("Nas_The_Creator".toLowerCase()))
/*     */     {
/* 109 */       return ChatColor.RED + "Colonel" + ChatColor.WHITE;
/*     */     }
/*     */ 
/* 112 */     if (player.getName().toLowerCase().equals("Richkilla".toLowerCase()))
/*     */     {
/* 114 */       return ChatColor.RED + "Colonel" + ChatColor.WHITE;
/*     */     }
/*     */ 
/* 117 */     if (player.getName().toLowerCase().equals("Siliputi".toLowerCase()))
/*     */     {
/* 119 */       return ChatColor.RED + "Colonel" + ChatColor.WHITE;
/*     */     }
/*     */ 
/* 123 */     if (player.getName().toLowerCase().equals("mixxit".toLowerCase()))
/*     */     {
/* 125 */       if (player.isOp())
/*     */       {
/* 127 */         return ChatColor.LIGHT_PURPLE + "SuperDev" + ChatColor.WHITE;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 132 */     if (hasPerm(player, "permissions.tag.emperor"))
/*     */     {
/* 134 */       return ChatColor.DARK_PURPLE + "Emperor" + ChatColor.WHITE;
/*     */     }
/*     */ 
/* 137 */     if (hasPerm(player, "permissions.tag.dev"))
/*     */     {
/* 139 */       return ChatColor.LIGHT_PURPLE + "Dev" + ChatColor.WHITE;
/*     */     }
/*     */ 
/* 142 */     if (hasPerm(player, "permissions.tag.general"))
/*     */     {
/* 144 */       return ChatColor.DARK_RED + "General" + ChatColor.WHITE;
/*     */     }
/*     */ 
/* 147 */     if (hasPerm(player, "permissions.tag.colonel"))
/*     */     {
/* 149 */       return ChatColor.RED + "Colonel" + ChatColor.WHITE;
/*     */     }
/*     */ 
/* 152 */     if (hasPerm(player, "permissions.tag.captain"))
/*     */     {
/* 154 */       return ChatColor.DARK_GREEN + "Captain" + ChatColor.WHITE;
/*     */     }
/*     */ 
/* 157 */     if (hasPerm(player, "permissions.tag.sergeant"))
/*     */     {
/* 159 */       return ChatColor.GREEN + "Sergeant" + ChatColor.WHITE;
/*     */     }
/* 161 */     if (hasPerm(player, "permissions.tag.goliath"))
/*     */     {
/* 163 */       return ChatColor.BLACK + "Goliath" + ChatColor.WHITE;
/*     */     }
/*     */ 
/* 166 */     if (hasPerm(player, "permissions.tag.immortal"))
/*     */     {
/* 168 */       return ChatColor.GOLD + "Immortal" + ChatColor.WHITE;
/*     */     }
/*     */ 
/* 171 */     if (hasPerm(player, "permissions.tag.elite"))
/*     */     {
/* 173 */       return ChatColor.DARK_BLUE + "Elite" + ChatColor.WHITE;
/*     */     }
/*     */ 
/* 176 */     if (hasPerm(player, "permissions.tag.prestige"))
/*     */     {
/* 178 */       return ChatColor.BLUE + "Prestige" + ChatColor.WHITE;
/*     */     }
/*     */ 
/* 181 */     if (hasPerm(player, "permissions.tag.premium"))
/*     */     {
/* 183 */       return ChatColor.AQUA + "Premium" + ChatColor.WHITE;
/*     */     }
/*     */ 
/* 186 */     if (hasPerm(player, "permissions.tag.juggernaut"))
/*     */     {
/* 188 */       return ChatColor.BLUE + "Juggernaut" + ChatColor.WHITE;
/*     */     }
/*     */ 
/* 191 */     if (hasPerm(player, "permissions.tag.lord"))
/*     */     {
/* 193 */       return ChatColor.DARK_GRAY + "Lord" + ChatColor.WHITE;
/*     */     }
/*     */ 
/* 196 */     if (hasPerm(player, "permissions.tag.citizen"))
/*     */     {
/* 198 */       return ChatColor.GRAY + "Citizen" + ChatColor.WHITE;
/*     */     }
/*     */ 
/* 201 */     if (hasPerm(player, "permissions.tag.refugee"))
/*     */     {
/* 203 */       return ChatColor.WHITE + "Refugee" + ChatColor.WHITE;
/*     */     }
/*     */ 
/* 206 */     return "Refugee";
/*     */   }
/*     */ 
/*     */   public String formatString(String string) {
/* 210 */     String s = string;
/* 211 */     for (ChatColor color : ChatColor.values()) {
/* 212 */       s = s.replaceAll("(&([a-f0-9]))", "§$2");
/*     */     }
/* 214 */     return s;
/*     */   }
/*     */ 
/*     */   public void onEnable()
/*     */   {
/* 219 */     PluginDescriptionFile desc = getDescription();
/* 220 */     System.out.println(desc.getFullName() + " has been enabled");
/*     */ 
/* 223 */     this.pm = getServer().getPluginManager();
/*     */ 
/* 227 */     checkPlugins();
/* 228 */     setupPermissions();
/* 229 */     setupDatabase();
/*     */ 
/* 235 */     if ((this.towny == null) || (getServer().getScheduler().scheduleSyncDelayedTask(this, new onLoadedTask(this), 1L) == -1))
/*     */     {
/* 240 */       System.out.println("SEVERE - Could not schedule onLoadedTask.");
/* 241 */       this.pm.disablePlugin(this);
/*     */     }
/*     */ 
/* 245 */     getCommand("local").setExecutor(new LocalMessage(this));
/* 246 */     getCommand("ooc").setExecutor(new OOCMessage(this));
/* 247 */     getCommand("global").setExecutor(new GlobalMessage(this));
/* 248 */     getCommand("townchat").setExecutor(new TownMessage(this));
/* 249 */     getCommand("racechat").setExecutor(new RaceMessage(this));
/* 250 */     getCommand("race").setExecutor(new SetRace(this));
/* 251 */     getCommand("metatron").setExecutor(new MetatronMessage(this));
/* 252 */     getCommand("dropship").setExecutor(new Dropship(this));
/* 253 */     getCommand("sector").setExecutor(new Sector(this));
/* 254 */     registerEvents();
/*     */   }
/*     */ 
/*     */   public void registerEvents()
/*     */   {
/* 259 */     this.rpchatPlayerListener = new RPchatPlayerListener(this);
/* 260 */     this.pm.registerEvent(Event.Type.PLAYER_CHAT, this.rpchatPlayerListener, Event.Priority.Monitor, this);
/* 261 */     this.pm.registerEvent(Event.Type.PLAYER_JOIN, this.rpchatPlayerListener, Event.Priority.Highest, this);
/* 262 */     this.rpchatEntityListener = new RPchatEntityListener(this);
/* 263 */     this.pm.registerEvent(Event.Type.ENTITY_DAMAGE, this.rpchatEntityListener, Event.Priority.Normal, this);
/*     */   }
/*     */ 
/*     */   public void sendMessageToAll(String message)
/*     */   {
/* 268 */     for (World w : getServer().getWorlds())
/*     */     {
/* 270 */       for (Player p : w.getPlayers())
/*     */       {
/* 272 */         p.sendMessage(message);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String capitalizeFirstLetters(String s)
/*     */   {
/* 280 */     for (int i = 0; i < s.length(); i++)
/*     */     {
/* 282 */       if (i == 0)
/*     */       {
/* 284 */         s = String.format("%s%s", new Object[] { 
/* 285 */           Character.valueOf(Character.toUpperCase(s.charAt(0))), 
/* 286 */           s.substring(1) });
/*     */       }
/*     */ 
/* 292 */       if ((Character.isLetterOrDigit(s.charAt(i))) || 
/* 293 */         (i + 1 >= s.length())) continue;
/* 294 */       s = String.format("%s%s%s", new Object[] { 
/* 295 */         s.subSequence(0, i + 1), 
/* 296 */         Character.valueOf(Character.toUpperCase(s.charAt(i + 1))), 
/* 297 */         s.substring(i + 2) });
/*     */     }
/*     */ 
/* 303 */     return s;
/*     */   }
/*     */ 
/*     */   protected Towny getTowny()
/*     */   {
/* 308 */     return this.towny;
/*     */   }
/*     */ 
/*     */   boolean onlyletters(String string) {
/* 312 */     return string.matches("^[a-zA-Z]+$");
/*     */   }
/*     */ 
/*     */   private void checkPlugins() {
/* 316 */     Plugin testessentials = getServer().getPluginManager().getPlugin("Essentials");
/* 317 */     if (testessentials == null)
/*     */     {
/* 319 */       this.isEssentials = false;
/*     */     } else {
/* 321 */       this.isEssentials = true;
/* 322 */       this.essentials = ((Essentials)testessentials);
/*     */     }
/*     */ 
/* 326 */     Plugin test = this.pm.getPlugin("Towny");
/* 327 */     if ((test != null) && ((test instanceof Towny)))
/* 328 */       this.towny = ((Towny)test);
/*     */   }
/*     */ 
/*     */   public String capitalise(String string)
/*     */   {
/* 334 */     if (string == null)
/* 335 */       throw new NullPointerException("string");
/* 336 */     if (string.equals(""))
/* 337 */       throw new NullPointerException("string");
/* 338 */     return Character.toUpperCase(string.charAt(0)) + string.substring(1);
/*     */   }
/*     */ 
/*     */   private void setupDatabase()
/*     */   {
/*     */     try
/*     */     {
/* 345 */       getDatabase().find(sqlPlayer.class).findRowCount();
/* 346 */       getDatabase().find(sqlSector.class).findRowCount();
/* 347 */       getDatabase().find(sqlDropships.class).findRowCount();
/* 348 */       getDatabase().find(sqlRaces.class).findRowCount();
/*     */     }
/*     */     catch (PersistenceException ex) {
/* 351 */       System.out.println("Installing database for " + getDescription().getName() + " due to first time usage");
/* 352 */       removeDDL();
/* 353 */       installDDL();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getPlayerRace(Player player)
/*     */   {
/*     */     try
/*     */     {
/* 363 */       sqlPlayer sPlayer = (sqlPlayer)getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
/* 364 */       if (sPlayer == null) {
/* 365 */         return "Unknown";
/*     */       }
/* 367 */       return sPlayer.getRace();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 371 */       System.out.println("[rpchat] Exception: " + e.getMessage());
/* 372 */     }return "Unknown";
/*     */   }
/*     */ 
/*     */   public List<Class<?>> getDatabaseClasses()
/*     */   {
/* 379 */     List list = new ArrayList();
/* 380 */     list.add(sqlPlayer.class);
/* 381 */     list.add(sqlSector.class);
/* 382 */     list.add(sqlDropships.class);
/* 383 */     list.add(sqlRaces.class);
/* 384 */     return list;
/*     */   }
/*     */ 
/*     */   public void addSectorKill(String sector, String attackerrace)
/*     */   {
/*     */     try
/*     */     {
/* 391 */       sqlSector sSector = (sqlSector)getDatabase().find(sqlSector.class).where().ieq("name", sector).findUnique();
/* 392 */       if (sSector == null) {
/* 393 */         sSector = new sqlSector();
/* 394 */         sSector.setName(sector);
/*     */       }
/*     */ 
/* 397 */       if (attackerrace.equals("human"))
/* 398 */         sSector.setHuman(sSector.getHuman() + 1);
/* 399 */       if (attackerrace.equals("chelok"))
/* 400 */         sSector.setChelok(sSector.getChelok() + 1);
/* 401 */       if (attackerrace.equals("vishim"))
/* 402 */         sSector.setVishim(sSector.getVishim() + 1);
/* 403 */       if (attackerrace.equals("chaotic"))
/* 404 */         sSector.setChaotic(sSector.getChaotic() + 1);
/* 405 */       if (attackerrace.equals("terrix"))
/* 406 */         sSector.setTerrix(sSector.getTerrix() + 1);
/* 407 */       if (attackerrace.equals("cybran"))
/* 408 */         sSector.setCybran(sSector.getCybran() + 1);
/* 409 */       if (attackerrace.equals("gray"))
/* 410 */         sSector.setGray(sSector.getGray() + 1);
/* 411 */       if (attackerrace.equals("sylik"))
/* 412 */         sSector.setSylik(sSector.getSylik() + 1);
/* 413 */       if (attackerrace.equals("mysmaal"))
/* 414 */         sSector.setMysmaal(sSector.getMysmaal() + 1);
/* 415 */       if (attackerrace.equals("triume"))
/* 416 */         sSector.setTriume(sSector.getTriume() + 1);
/* 417 */       if (attackerrace.equals("draconic")) {
/* 418 */         sSector.setDraconic(sSector.getDraconic() + 1);
/*     */       }
/*     */ 
/* 421 */       getDatabase().save(sSector);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 425 */       System.out.println("[RPchatError] " + e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getSectorDominator(String sector)
/*     */   {
/* 432 */     String dominator = "none";
/*     */     try
/*     */     {
/* 436 */       sqlSector sSector = (sqlSector)getDatabase().find(sqlSector.class).where().ieq("name", sector).findUnique();
/* 437 */       if (sSector == null) {
/* 438 */         sSector = new sqlSector();
/* 439 */         sSector.setName(sector);
/*     */ 
/* 441 */         getDatabase().save(sSector);
/*     */       }
/*     */ 
/* 446 */       Map map = new HashMap();
/* 447 */       map.put("human", Integer.valueOf(sSector.getHuman()));
/* 448 */       map.put("vishim", Integer.valueOf(sSector.getVishim()));
/* 449 */       map.put("chaotic", Integer.valueOf(sSector.getChaotic()));
/* 450 */       map.put("terrix", Integer.valueOf(sSector.getTerrix()));
/* 451 */       map.put("cybran", Integer.valueOf(sSector.getCybran()));
/* 452 */       map.put("gray", Integer.valueOf(sSector.getGray()));
/* 453 */       map.put("sylik", Integer.valueOf(sSector.getSylik()));
/* 454 */       map.put("mysmaal", Integer.valueOf(sSector.getMysmaal()));
/* 455 */       map.put("triume", Integer.valueOf(sSector.getTriume()));
/* 456 */       map.put("draconic", Integer.valueOf(sSector.getDraconic()));
/*     */ 
/* 459 */       Set s = map.entrySet();
/*     */ 
/* 462 */       Iterator it = s.iterator();
/*     */ 
/* 464 */       String highestrace = "none";
/* 465 */       Integer highestvalue = Integer.valueOf(0);
/*     */ 
/* 467 */       while (it.hasNext())
/*     */       {
/* 471 */         Map.Entry m = (Map.Entry)it.next();
/*     */ 
/* 474 */         String key = (String)m.getKey();
/*     */ 
/* 477 */         Integer value = (Integer)m.getValue();
/* 478 */         if (value.intValue() <= highestvalue.intValue())
/*     */           continue;
/* 480 */         highestrace = key;
/* 481 */         highestvalue = value;
/*     */       }
/*     */ 
/* 485 */       dominator = highestrace;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 490 */       System.out.println("[RPchatError] " + e.getMessage());
/* 491 */       dominator = "none";
/*     */     }
/*     */ 
/* 494 */     return dominator;
/*     */   }
/*     */ 
/*     */   public void sendMessageToRace(String race, String message)
/*     */   {
/* 499 */     for (World w : getServer().getWorlds())
/*     */     {
/* 501 */       for (Player p : w.getPlayers())
/*     */       {
/* 503 */         if (!getPlayerRace(p).equals(race))
/*     */           continue;
/* 505 */         p.sendMessage(message);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String addDropship(String world, double x, double y, double z)
/*     */   {
/*     */     try
/*     */     {
/* 518 */       sqlDropships sDropship = new sqlDropships();
/* 519 */       String x1 = Double.toString(x);
/* 520 */       String y1 = Double.toString(y);
/* 521 */       String z1 = Double.toString(z);
/* 522 */       Date now = new Date();
/* 523 */       Long time26now = Long.valueOf(now.getTime());
/* 524 */       String timenow = Long.toString(now.getTime());
/*     */ 
/* 526 */       sDropship.setName(timenow);
/* 527 */       sDropship.setWorld(world);
/*     */ 
/* 529 */       sDropship.setX(x1);
/* 530 */       sDropship.setY(y1);
/* 531 */       sDropship.setZ(z1);
/*     */ 
/* 533 */       getDatabase().save(sDropship);
/*     */ 
/* 537 */       return encode(time26now.longValue());
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 542 */       System.out.println("[rpchat] Exception: " + e.getMessage());
/*     */     }
/*     */ 
/* 546 */     return null;
/*     */   }
/*     */ 
/*     */   public static String encode(long number) {
/* 550 */     StringBuilder b = new StringBuilder();
/* 551 */     assert (number > 0L);
/*     */     do {
/* 553 */       long div = number / 26L;
/* 554 */       int mod = (int)(number % 26L);
/* 555 */       b.append("abcdefghijklmnopqrstuvwxyz".charAt(mod));
/* 556 */       number = div;
/* 557 */     }while (number != 0L);
/* 558 */     return b.toString();
/*     */   }
/*     */   static int error(String s) {
/* 561 */     throw new RuntimeException(s);
/*     */   }
/*     */   public static long decode(String string) {
/* 564 */     int l = string.length();
/* 565 */     long answer = 0L;
/* 566 */     long mul = 1L;
/* 567 */     for (int i = 0; i < l; i++) {
/* 568 */       int val = string.charAt(i);
/* 569 */       int num = (val >= 65) && (val <= 90) ? val - 65 : (val >= 97) && (val <= 122) ? val - 97 : error("bad format");
/* 570 */       answer += num * mul; mul *= 26L;
/*     */     }
/* 572 */     return answer;
/*     */   }
/*     */ 
/*     */   public void sendMessageToRaceExcept(Player victim, String victimrace, String string)
/*     */   {
/* 579 */     for (World w : getServer().getWorlds())
/*     */     {
/* 581 */       for (Player p : w.getPlayers())
/*     */       {
/* 583 */         if (p.equals(victim))
/*     */           continue;
/* 585 */         if (!getPlayerRace(p).equals(victimrace))
/*     */           continue;
/* 587 */         p.sendMessage(string);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void sendMessageToAllExcept(Player victim, String string)
/*     */   {
/* 599 */     for (World w : getServer().getWorlds())
/*     */     {
/* 601 */       for (Player p : w.getPlayers())
/*     */       {
/* 603 */         if (p.equals(victim))
/*     */           continue;
/* 605 */         p.sendMessage(string);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getSectorName(Location location)
/*     */   {
/* 614 */     String World = location.getWorld().getName();
/* 615 */     String ChunkX = Integer.toString(location.getChunk().getX());
/* 616 */     String ChunkZ = Integer.toString(location.getChunk().getZ());
/* 617 */     return World + ":" + ChunkX + "," + ChunkZ;
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\end\Desktop\rpchatlite.jar
 * Qualified Name:     net.gamerservices.rpchat.rpchat
 * JD-Core Version:    0.6.0
 */