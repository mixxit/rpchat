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
     
     if (player.isOp() || this.plugin.hasPerm(player,"rpchat.admin"))
	 {
     
	     Player actualplayer = (Player)sender;
	 
	     PlayerCache sPlayer = this.plugin.getPlayerCacheByName(player.getName());
	     if (args.length == 0) {
	       player.sendMessage(ChatColor.LIGHT_PURPLE + "Your current race is: " + sPlayer.race);
	       player.sendMessage(ChatColor.LIGHT_PURPLE + "To set a new race use the: '/race Racename' command");
	       player.sendMessage(ChatColor.LIGHT_PURPLE + "For a list of races use: '/race list' command");
	       return true;
	     }
	     
	     boolean opchange = false;
	     
	     if (args.length == 2)
	     {
	    	 
	    	 if (actualplayer.isOp() || this.plugin.hasPerm(actualplayer, "rpchat.admin"))
	    	 {
		    	 if (this.plugin.getServer().getPlayerExact(args[1]) == null)
		 		 {
		 			player.sendMessage("Cannot set title, that minecraft account ("+args[1]+") is not online");
		 			return false;
		
		 		 } else {
		 			 player = this.plugin.getServer().getPlayerExact(args[1]);
		 		 }
		    	 
		    	 sPlayer = this.plugin.getPlayerCacheByName(player.getName());
		         opchange = true;
	    	 }
	     }
	 
	     int matchcount = 0;
	     String targetrace = args[0].toLowerCase();
		// System.out.println("Target race for client ("+player.getName()+"): " + targetrace);
	     String[] races = { "human", "fairy","highelf", "woodelf", "halfelf", "darkelf", "vampire", "barbarian", "orc", "ogre", "troll", "halfdragon", "gnome", "goblin", "hobbit", "highhuman", "undead", "dwarf", "ratman", "lizardman", "elemental", "kobold", "angel", "fallenangel", "clockwork" };
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
		       player.sendMessage(ChatColor.RED + "Human, Fairy, HighElf, WoodElf, HalfElf, DarkElf, Vampire, Barbarian, Orc, Ogre, Troll, HalfDragon, Gnome, Goblin, Hobbit, HighHuman, Undead, Dwarf, Ratman, Lizardman, Elemental, Kobold, Angel, FallenAngel, Clockwork");
		       return false;
	       } else {
	    	   actualplayer.sendMessage(ChatColor.RED + "That is not a valid race");
		       String racelist = "";
		       for (String r : races)
		       {
		         racelist = racelist + r + ",";
		       }
		       actualplayer.sendMessage(ChatColor.RED + "Human, Fairy, HighElf, WoodElf, HalfElf, DarkElf, Vampire, Barbarian, Orc, Ogre, Troll, HalfDragon, Gnome, Goblin, Hobbit, HighHuman, Undead, Dwarf, Ratman, Lizardman, Elemental, Kobold, Angel, FallenAngel, Clockwork");
		       return false;
	       }
	     }
	     
	     long lastpoch = System.currentTimeMillis()/1000;
	 
	     if (sPlayer.optedin.equals("done"))
	     {
	    	 lastpoch =  lastpoch - 86401;
	       if (opchange)
	       {
	    	    actualplayer.sendMessage("Resetting players race...");
	       }
	     } else {
	    	 if (!sPlayer.optedin.equals(""))
	         {
	    	 lastpoch = Long.parseLong(sPlayer.optedin);
	         } else {
	        	 lastpoch = System.currentTimeMillis()/1000 - 86401;
	         }
	     }
	 
	     long epoch = System.currentTimeMillis()/1000;
		 if ((epoch > (lastpoch + 86400)) || opchange)
		 {
			 String language = plugin.getLanguageName(args[0].toString().toLowerCase());
				// we should save language flags here
			 player.sendMessage("Your language was reset to " + language + " and your language skills wiped for your new character");
		     sPlayer.race = args[0].toString().toLowerCase();
		     String temprace =sPlayer.race;
		     sPlayer.language = language;
		     sPlayer.languageflags = "0,0,0,0,0,0,0,0,0,0,0,0";
		     String alliance = this.plugin.getRaceAlliance(args[0].toString().toLowerCase());
		 	 sPlayer.alliance = alliance;
		     sPlayer.optedin = Long.toString(epoch);
		     sPlayer.election = 1;
		     sPlayer.vote = "";
	
		     this.plugin.clearVotes(player);
		     
		     player.sendMessage("Your race is now: " + args[0]);
		     //this.plugin.PlayerGotoCapital(player);
		     player.sendMessage("Your alliance is now: " + alliance);
		     sPlayer.decoration = this.plugin.getDecoration(sPlayer);
		     this.plugin.sendRaceMessage("* " + args[0].toString().toLowerCase(),player.getName() + " has joined your race");
		     actualplayer.sendMessage("Players race is now: " + args[0]);
		     actualplayer.sendMessage("Players alliance is now: " + alliance);
		     
		     this.plugin.setLanguageSkill(player.getName(), this.plugin.getLanguageName(temprace), 100);
		 
		     player.sendMessage("Perhaps now would be a good time to find a race skin on the skindex or planetminecraft for " + args[0]);
		 } else {
			 player.sendMessage("You must wait 24 hours before resetting your race again");
		 }
	     
	   	} else {
			player.sendMessage("This is an admin only command");
			return true;
		}
	 	return true;
   }
 }
