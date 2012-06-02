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
     Player actualplayer = (Player)sender;
 
     sqlPlayer sPlayer = (sqlPlayer)this.plugin.getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
     if (sPlayer == null) {
       sPlayer = new sqlPlayer();
       sPlayer.setName(player.getName());
 
       sPlayer.setDisplay(player.getName());
       sPlayer.setRace("human");
       sPlayer.setLanguage("human");
       sPlayer.setAlliance("combine");
       this.plugin.getDatabase().save(sPlayer);
     }	
 
     if (args.length == 0) {
       player.sendMessage(ChatColor.LIGHT_PURPLE + "Your current race is: " + sPlayer.getRace());
       player.sendMessage(ChatColor.LIGHT_PURPLE + "To set a new race use the: '/race Racename' command");
       player.sendMessage(ChatColor.LIGHT_PURPLE + "For a list of races use: '/race list' command");
       return true;
     }
     
     boolean opchange = false;
     
     if (args.length == 2)
     {
    	 
    	 if (actualplayer.isOp())
    	 {
	    	 if (this.plugin.getServer().getPlayerExact(args[1]) == null)
	 		 {
	 			player.sendMessage("Cannot set title, that minecraft account ("+args[1]+") is not online");
	 			return false;
	
	 		 } else {
	 			 player = this.plugin.getServer().getPlayerExact(args[1]);
	 		 }
	    	 
	    	 sPlayer = (sqlPlayer)this.plugin.getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
	         if (sPlayer == null) {
	           sPlayer = new sqlPlayer();
	           sPlayer.setName(player.getName());
	     
	           sPlayer.setDisplay(player.getName());
	           sPlayer.setRace("human");
	           sPlayer.setLanguage("human");
	           sPlayer.setAlliance("combine");
	           this.plugin.getDatabase().save(sPlayer);
	         }	
	         
	         opchange = true;
    	 }
     }
 
     int matchcount = 0;
     String targetrace = args[0].toLowerCase();
	 System.out.println("Target race for client ("+player.getName()+"): " + targetrace);
     String[] races = { "human", "highelf", "woodelf", "halfelf", "darkelf", "vampire", "barbarian", "orc", "ogre", "troll", "halfdragon", "gnome", "goblin", "hobbit", "highhuman", "undead", "dwarf", "ratman", "lizardman", "elemental", "kobold", "angel", "fallenangel", "clockwork" };
     for (String rs : races)
     {
       if (!rs.equals(targetrace))
         continue;
       matchcount++;
     }
 
     if (matchcount < 1)
     {
       if (!opchange)
       {
    	 
	       player.sendMessage(ChatColor.RED + "That is not a valid race");
	       String racelist = "";
	       for (String r : races)
	       {
	         racelist = racelist + r + ",";
	       }
	       player.sendMessage(ChatColor.RED + "Human, HighElf, WoodElf, HalfElf, DarkElf, Vampire, Barbarian, Orc, Ogre, Troll, HalfDragon, Gnome, Goblin, Hobbit, HighHuman, Undead, Dwarf, Ratman, Lizardman, Elemental, Kobold, Angel, FallenAngel, Clockwork");
	       return false;
       } else {
    	   actualplayer.sendMessage(ChatColor.RED + "That is not a valid race");
	       String racelist = "";
	       for (String r : races)
	       {
	         racelist = racelist + r + ",";
	       }
	       actualplayer.sendMessage(ChatColor.RED + "Human, HighElf, WoodElf, HalfElf, DarkElf, Vampire, Barbarian, Orc, Ogre, Troll, HalfDragon, Gnome, Goblin, Hobbit, HighHuman, Undead, Dwarf, Ratman, Lizardman, Elemental, Kobold, Angel, FallenAngel, Clockwork");
	       return false;
       }
     }
 
     if (sPlayer.getFlags().equals("done"))
     {
       if (opchange)
       {
           actualplayer.sendMessage("Resetting players race...");
       } else {
    	   player.sendMessage(ChatColor.LIGHT_PURPLE + "Your race has already been set");
           return true;
       }
    	 
     }
 
     sPlayer.setRace(args[0].toString().toLowerCase());
     
     String alliance = "unknown";
 	if (args[0].toString().toLowerCase().equals("human")) {
 		alliance = "combine";
	}
 	if (args[0].toString().toLowerCase().equals("highelf")) {
 		alliance = "realm";
 	}
 	if (args[0].toString().toLowerCase().equals("woodelf")) {
 		alliance = "realm";
 	}
 	if (args[0].toString().toLowerCase().equals("halfelf")) {
 		alliance = "realm";
 	}
 	if (args[0].toString().toLowerCase().equals("darkelf")) {
 		alliance = "dominion";
 	}
 	if (args[0].toString().toLowerCase().equals("vampire")) {
 		alliance = "dominion";
 	}
 	if (args[0].toString().toLowerCase().equals("barbarian")) {
 		alliance = "combine";
 	}
 	if (args[0].toString().toLowerCase().equals("orc")) {
 		alliance = "legion";
 	}
 	if (args[0].toString().toLowerCase().equals("ogre")) {
 		alliance = "legion";
 	}
 	if (args[0].toString().toLowerCase().equals("troll")) {
 		alliance = "legion";
 	}
 	if (args[0].toString().toLowerCase().equals("halfdragon")) {
 		alliance = "legacy";
 	}
 	if (args[0].toString().toLowerCase().equals("gnome")) {
 		alliance = "combine";
 	}
 	if (args[0].toString().toLowerCase().equals("goblin")) {
 		alliance = "legion";
 	}
 	if (args[0].toString().toLowerCase().equals("hobbit")) {
 		alliance = "combine";
 	}
 	if (args[0].toString().toLowerCase().equals("highhuman")) {
 		alliance = "combine";
 	}
 	if (args[0].toString().toLowerCase().equals("undead")) {
 		alliance = "dominion";
 	}
 	if (args[0].toString().toLowerCase().equals("dwarf")) {
 		alliance = "combine";
 	}
 	if (args[0].toString().toLowerCase().equals("ratman")) {
 		alliance = "legion";
 	}
 	if (args[0].toString().toLowerCase().equals("lizardman")) {
 		alliance = "legacy";
 	}
 	if (args[0].toString().toLowerCase().equals("elemental")) {
 		alliance = "legion";
 	}
 	if (args[0].toString().toLowerCase().equals("kobold")) {
 		alliance = "legion";
 	}
 	if (args[0].toString().toLowerCase().equals("angel")) {
 		alliance = "foresworn";
 	}
 	if (args[0].toString().toLowerCase().equals("fallenangel")) {
 		alliance = "forsaken";
 	}
 	
 	if (args[0].toString().toLowerCase().equals("clockwork")) {
 		alliance = "collective";
 	}
 	 sPlayer.setAlliance(alliance);
     
     sPlayer.setFlags("done");
     sPlayer.setBitwise(0);
     sPlayer.setElection(0);
     sPlayer.setVote("");
     this.plugin.getDatabase().save(sPlayer);
     this.plugin.clearVotes(player);
     
     player.sendMessage("Your race is now: " + args[0]);
     player.sendMessage("Your alliance is now: " + alliance);
     
     actualplayer.sendMessage("Players race is now: " + args[0]);
     actualplayer.sendMessage("Players alliance is now: " + alliance);
 
     player.sendMessage("Perhaps now would be a good time to find a race skin on the skindex or planetminecraft for " + args[0]);
     return true;
   }
 }
