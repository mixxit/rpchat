package net.gamerservices.rpchat;
 
import com.palmergames.bukkit.towny.Towny;
import java.io.PrintStream;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
 
 public class GlobalMessage
   implements CommandExecutor
 {
   private Towny towny;
   private rpchat parent;
 
   public GlobalMessage(rpchat rpchat)
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
       if (message.compareTo("") > 0)
       {
         sendGlobal(player, message);
         return true;
       }
       return false;
     }
     catch (Exception e)
     {
       System.out.println("[RPChat Error]: " + e.getMessage());
     }
 
     return false;
   }
 
   public void sendGlobal(Player player, String message)
   {
			  
     if (this.parent.isMuted(player))
     {
       return;
     }

     String race = "";
     race = this.parent.getPlayerRace(player);
     String alliance = this.parent.getPlayerAlliance(player);
     
				System.out.println("[RPChat-G] " + player.getName() + "("+race+"):" + message);
     String tag = "";
     try
     {
       tag = this.parent.getGroups(player);
     }
     catch (Exception e)
     {
       System.out.println(e.getMessage());
       tag = "Refugee";
     }
 
     int count = 0;
 
     for (World w : player.getServer().getWorlds())
     {
       for (Player p : w.getPlayers())
       {
         if (p.equals(player))
         {
           p.sendMessage("[" + this.parent.getColouredName(player) + "][" + this.parent.getAllianceNameShorthand(alliance) + "] " + ChatColor.WHITE + this.parent.getPlayerDisplayName(player) + " " + this.parent.getPlayerLastName(player)+ ChatColor.WHITE + " " +this.parent.getPlayerTitle(player) + ": " + message);
         } else {
        	 if (!this.parent.isIgnored(player,p))
           p.sendMessage("[" + this.parent.getColouredName(player) + "][" + this.parent.getAllianceNameShorthand(alliance) + "] " + ChatColor.WHITE + this.parent.getPlayerDisplayName(player) + " " + this.parent.getPlayerLastName(player)+ ChatColor.WHITE + " " +this.parent.getPlayerTitle(player) +": " + message);
 
           count++;
         }
       }
 
     }
 
     if (count < 1)
     {
       player.sendMessage(ChatColor.GRAY + "* You speak but nobody is online.");
     }
   }
 }

