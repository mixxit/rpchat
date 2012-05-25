 package net.gamerservices.rpchat;
 
 import com.palmergames.bukkit.towny.Towny;
 import java.io.PrintStream;
 import org.bukkit.ChatColor;
 import org.bukkit.Location;
 import org.bukkit.Server;
 import org.bukkit.World;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandExecutor;
 import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
 
 public class EmoteMessage
   implements CommandExecutor
 {
   private rpchat parent;
   private Towny towny;
 
   public EmoteMessage(rpchat rpchat)
   {
     this.parent = rpchat;
     this.towny = this.parent.getTowny();
   }
 
   public static String arrayToString(String[] a, String separator) {
     String result = "";
     if (a.length > 0) {
       result = a[0];
       for (int i = 1; i < a.length; i++) {
         result = result + separator + a[i];
       }
     }
     return result;
   }
 
   public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3)
   {
     String message = arrayToString(arg3, " ");
     try
     {
       Player player = this.parent.getServer().getPlayer(arg0.getName());
 
       if (message.compareTo("") == 0)
       {
         return false;
       }
 
       sendLocal(player, message);
       return true;
     }
     catch (Exception e)
     {
       System.out.println("[RPChatError]: " + e.getMessage());
     }
 
     return false;
   }
 
   public void sendLocal(Player player, String message)
   {
     if (this.parent.isMuted(player))
     {
       return;
     }

     String race = "";
     race = this.parent.getPlayerRace(player);
     String alliance = this.parent.getPlayerAlliance(player);
     System.out.println("[RPChat-EMOTE] " + player.getName() + "("+race+"):" + message);
     String tag = "";
     try
     {
       tag = this.parent.getGroups(player);
     }
     catch (Exception e)
     {
       tag = "Refugee";
     }
 
     int count = 0;
 
     for (Player p : player.getWorld().getPlayers())
     {
       if (p.equals(player))
       {
         p.sendMessage(ChatColor.WHITE + "*" + this.parent.getPlayerDisplayName(player) + " " + this.parent.getPlayerLastName(player)+ " " + this.parent.getPlayerTitle(player) +ChatColor.WHITE + " " + message + "*");
       } else {
    	   
    	   if (!this.parent.isIgnored(player,p))
    	   {
	         double x1 = p.getLocation().getX();
	         double y1 = p.getLocation().getY();
	         double z1 = p.getLocation().getZ();
	 
	         double x2 = player.getLocation().getX();
	         double y2 = player.getLocation().getY();
	         double z2 = player.getLocation().getZ();
	 
	         int xdist = (int)(x1 - x2);
	         int ydist = (int)(y1 - y2);
	         int zdist = (int)(z1 - z2);
	 
	         if ((xdist < -100) || (xdist > 100) || (ydist < -100) || (ydist > 100) || (zdist < -100) || (zdist > 100))
	         {
	           continue;
	         }
	 
	         p.sendMessage(ChatColor.WHITE + "*" + this.parent.getPlayerDisplayName(player) + " " + this.parent.getPlayerLastName(player)+ " " + this.parent.getPlayerTitle(player) +ChatColor.WHITE + " " + message + "*");
	         count++;
    	   }
       }
 
     }
 
     if (count < 1)
     {
    	 player.sendMessage(ChatColor.GRAY + "* You emote but nobody witnesses it");
     }
   }
 }

