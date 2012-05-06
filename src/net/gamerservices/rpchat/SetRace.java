 package net.gamerservices.rpchat;
 
 import com.avaje.ebean.EbeanServer;
 import com.avaje.ebean.ExpressionList;
 import com.avaje.ebean.Query;
 import org.bukkit.ChatColor;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandExecutor;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.Player;
 
 public class SetRace
   implements CommandExecutor
 {
   private final rpchat plugin;
 
   public SetRace(rpchat plugin)
   {
     this.plugin = plugin;
   }
 
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
   {
     if (!(sender instanceof Player)) {
       return false;
     }
 
     Player player = (Player)sender;
 
     sqlPlayer sPlayer = (sqlPlayer)this.plugin.getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
     if (sPlayer == null) {
       sPlayer = new sqlPlayer();
       sPlayer.setName(player.getName());
 
       sPlayer.setDisplay(player.getName());
       sPlayer.setRace("human");
       sPlayer.setLanguage("human");
       this.plugin.getDatabase().save(sPlayer);
     }	
 
     if (args.length == 0) {
       player.sendMessage(ChatColor.LIGHT_PURPLE + "Your current race is: " + sPlayer.getRace());
       player.sendMessage(ChatColor.LIGHT_PURPLE + "To set a new race use the: '/race Racename' command");
       player.sendMessage(ChatColor.LIGHT_PURPLE + "For a list of races use: '/race list' command");
       return true;
     }
 
     int matchcount = 0;
     String targetrace = args[0].toLowerCase();
			 System.out.println("Target race for client ("+player.getName()+"): " + targetrace);
     String[] races = { "human", "highelf", "woodelf", "halfelf", "darkelf", "vampire", "barbarian", "orc", "ogre", "troll", "halfdragon", "gnome", "goblin", "hobbit", "highhuman", "undead", "dwarf", "ratman", "lizardman", "elemental", "kobold", "angel", "fallenangel" };
     for (String rs : races)
     {
       if (!rs.equals(targetrace))
         continue;
       matchcount++;
     }
 
     if (matchcount < 1)
     {
       player.sendMessage(ChatColor.RED + "That is not a valid race");
       String racelist = "";
       for (String r : races)
       {
         racelist = racelist + r + ",";
       }
       player.sendMessage(ChatColor.RED + "Human, HighElf, WoodElf, HalfElf, DarkElf, Vampire, Barbarian, Orc, Ogre, Troll, HalfDragon, Gnome, Goblin, Hobbit, HighHuman, Undead, Dwarf, Ratman, Lizardman, Elemental, Kobold, Angel, FallenAngel");
       return false;
     }
 
     if (sPlayer.getFlags().equals("done"))
     {
       player.sendMessage(ChatColor.LIGHT_PURPLE + "Your race has already been set");
       return true;
     }
 
     sPlayer.setRace(args[0].toString().toLowerCase());
     sPlayer.setFlags("done");
     this.plugin.getDatabase().save(sPlayer);
 
     player.sendMessage("Your race is now: " + args[0]);
 
     player.sendMessage("Perhaps now would be a good time to find a race skin on the skindex or planetminecraft for " + args[0]);
     return true;
   }
 }
