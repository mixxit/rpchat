/*    */ package net.gamerservices.rpchat;
/*    */ 
/*    */ import com.avaje.ebean.EbeanServer;
/*    */ import com.avaje.ebean.ExpressionList;
/*    */ import com.avaje.ebean.Query;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class SetRace
/*    */   implements CommandExecutor
/*    */ {
/*    */   private final rpchat plugin;
/*    */ 
/*    */   public SetRace(rpchat plugin)
/*    */   {
/* 19 */     this.plugin = plugin;
/*    */   }
/*    */ 
/*    */   public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
/*    */   {
/* 24 */     if (!(sender instanceof Player)) {
/* 25 */       return false;
/*    */     }
/*    */ 
/* 29 */     Player player = (Player)sender;
/*    */ 
/* 31 */     sqlPlayer sPlayer = (sqlPlayer)this.plugin.getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
/* 32 */     if (sPlayer == null) {
/* 33 */       sPlayer = new sqlPlayer();
/* 34 */       sPlayer.setName(player.getName());
/*    */ 
/* 36 */       sPlayer.setDisplay(player.getName());
/* 37 */       sPlayer.setRace("human");
/* 38 */       sPlayer.setLanguage("human");
/*    */     }
/*    */ 
/* 44 */     if (args.length == 0) {
/* 45 */       player.sendMessage(ChatColor.LIGHT_PURPLE + "Your current race is: " + sPlayer.getRace());
/* 47 */       player.sendMessage(ChatColor.LIGHT_PURPLE + "To set a new race use the: '/race Racename' command");
/* 48 */       player.sendMessage(ChatColor.LIGHT_PURPLE + "For a list of races use: '/race list' command");
/*    */ 
/* 51 */       return true;
/*    */     }
/*    */ 
/* 54 */     int matchcount = 0;
/* 55 */     String targetrace = args[0].toLowerCase();
			 System.out.println("Target race for client ("+player.getName()+"): " + targetrace);
/* 56 */     String[] races = { "human", "highelf", "woodelf", "halfelf", "darkelf", "vampire", "barbarian", "orc", "ogre", "troll", "halfDragon", "gnome", "goblin", "hobbit", "highHuman", "undead", "dwarf", "ratman", "lizardman", "elemental", "kobold", "angel", "fallenangel" };
/* 57 */     for (String rs : races)
/*    */     {
/* 59 */       if (!rs.equals(targetrace))
/*    */         continue;
/* 61 */       matchcount++;
/*    */     }
/*    */ 
/* 64 */     if (matchcount < 1)
/*    */     {
/* 66 */       player.sendMessage(ChatColor.RED + "That is not a valid race");
/* 67 */       String racelist = "";
/* 68 */       for (String r : races)
/*    */       {
/* 70 */         racelist = racelist + r + ",";
/*    */       }
/* 72 */       player.sendMessage(ChatColor.RED + "Human, HighElf, WoodElf, HalfElf, DarkElf, Vampire, Barbarian, Orc, Ogre, Troll, HalfDragon, Gnome, Goblin, Hobbit, HighHuman, Undead, Dwarf, Ratman, Lizardman, Elemental, Koboldm, Angel, FallenAngel");
/* 73 */       return false;
/*    */     }
/*    */ 
/* 76 */     if (sPlayer.getFlags().equals("done"))
/*    */     {
/* 78 */       player.sendMessage(ChatColor.LIGHT_PURPLE + "Your race has already been set");
/* 79 */       return true;
/*    */     }
/*    */ 
/* 82 */     sPlayer.setRace(args[0].toString().toLowerCase());
/* 83 */     sPlayer.setFlags("done");
/* 84 */     this.plugin.getDatabase().save(sPlayer);
/*    */ 
/* 87 */     player.sendMessage("Your race is now: " + args[0]);
/*    */ 
/* 89 */     player.sendMessage("Perhaps now would be a good time to find a race skin on the skindex or planetminecraft for " + args[0]);
/* 90 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\end\Desktop\rpchatlite.jar
 * Qualified Name:     net.gamerservices.rpchat.SetRace
 * JD-Core Version:    0.6.0
 */