 package net.gamerservices.rpchat;
 
 import com.avaje.ebean.EbeanServer;
 import com.avaje.ebean.ExpressionList;
 import com.avaje.ebean.Query;
 import org.bukkit.ChatColor;
 import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
 import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

 
 public class RPchatPlayerListener implements Listener
 {
   private rpchat plugin;
 
		   @EventHandler(priority = EventPriority.HIGHEST)
   public void onPlayerJoin(PlayerJoinEvent event)
   {
     sqlPlayer sPlayer = (sqlPlayer)this.plugin.getDatabase().find(sqlPlayer.class).where().ieq("name", event.getPlayer().getName()).findUnique();
     if (sPlayer == null) {
       sPlayer = new sqlPlayer();
       sPlayer.setName(event.getPlayer().getName());
 
       sPlayer.setDisplay(event.getPlayer().getName());
       sPlayer.setRace("human");
       sPlayer.setLanguage("human");
       sPlayer.setAlliance("combine");
 
       this.plugin.getDatabase().save(sPlayer);
     }
 
     if (!sPlayer.getFlags().equals("done"))
     {
       event.getPlayer().sendMessage(ChatColor.RED + "*********************************************************");
       event.getPlayer().sendMessage(ChatColor.RED + "You have been given the opportunity to reset your /race");
       event.getPlayer().sendMessage(ChatColor.RED + "NOTE: You will lose all votes and king status if you do this");
       event.getPlayer().sendMessage(ChatColor.RED + "*********************************************************");
     }

			 event.getPlayer().setDisplayName(sPlayer.getDisplay());
   }
 
   public RPchatPlayerListener(rpchat rpchat)
   {
     this.plugin = rpchat;
   }
   
   @EventHandler(priority = EventPriority.MONITOR)
   public void onPlayerChat(PlayerChatEvent event)
   {
	   if(!event.isCancelled()){
		   DoMessageToChannel(event.getPlayer(), event.getMessage());
		   event.setCancelled(true);
	   }
   }
 
   private void DoMessageToChannel(Player player, String message) {
	
	
	   sqlPlayer sPlayer = (sqlPlayer)this.plugin.getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
	   if (sPlayer == null) {
		   
		   DoLocalMessage(player, message);
	   } else {
		   if (sPlayer.getChatfocus().equals(""))
		   {
			   
			   DoLocalMessage(player, message);
			   
		   }
		   
		   if (sPlayer.getChatfocus().equals("local"))
		   {
			   
			   DoLocalMessage(player, message);
			   
		   }
		   
		   if (sPlayer.getChatfocus().equals("global"))
		   {
			   
			   DoGlobalMessage(player, message);
			   
		   }
		   
		   if (sPlayer.getChatfocus().equals("town"))
		   {
			   
			   DoTownMessage(player, message);
			   
		   }
		   
		   if (sPlayer.getChatfocus().equals("alliance"))
		   {
			   
			   DoAllianceMessage(player, message);
			   
		   }
		   
		   if (sPlayer.getChatfocus().equals("ooc"))
		   {
			   
			   DoOOCMessage(player, message);
			   
		   }
		   
		   if (sPlayer.getChatfocus().equals("race"))
		   {
			   
			   DoRaceMessage(player, message);
			   
		   }
		   
	   }
	   
	   

}

private void DoTownMessage(Player playername, String message) {
	
	TownMessage tm = new TownMessage(this.plugin);
	tm.sendTownChat(playername, message);
}

private void DoAllianceMessage(Player playername, String message) {
	
	AllianceMessage tm = new AllianceMessage(this.plugin);
	tm.sendAllianceChat(playername, message);
}

private void DoRaceMessage(Player playername, String message) {
	
	RaceMessage rm = new RaceMessage(this.plugin);
	rm.sendRaceChat(playername, message);
}

public void DoGlobalMessage(Player playername, String message)
   {
		GlobalMessage gm = new GlobalMessage(this.plugin);
		gm.sendGlobal(playername, message);
   }
 
   public void DoOOCMessage(Player playername, String message)
   {
     OOCMessage ooc = new OOCMessage(this.plugin);
     ooc.sendOOC(playername, message);
   }
 
   public void DoLocalMessage(Player playername, String message)
   {
     LocalMessage lm = new LocalMessage(this.plugin);
     lm.sendLocal(playername, message);
   }
 }

