package net.gamerservices.rpchat;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import java.io.PrintStream;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RaceMessage
  implements CommandExecutor
{
  private Towny towny;
  private rpchat parent;

  public RaceMessage(rpchat rpchat)
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
         sendRaceChat(player, message);
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

  public void sendRaceChat(Player player, String message)
  {
     if (this.parent.isMuted(player))
    {
       return;
    }

     String race = "";
     race = this.parent.getPlayerRace(player);
    System.out.println("[RPChat-RC] " + player.getName() + "("+race+"):" + message);

    try
    {
       Resident res = this.towny.getTownyUniverse().getResident(player.getName());
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

       for (World w : player.getServer().getWorlds())
      {
         for (Player p : w.getPlayers())
        {
           if (p.equals(player))
          {
             p.sendMessage("[" + this.parent.getColouredName(player) + "][" + this.parent.getRaceNameShorthand(race) + "] " + ChatColor.WHITE + this.parent.getPlayerDisplayName(player) + " " + this.parent.getPlayerLastName(player)+ " " +this.parent.getPlayerTitle(player) +ChatColor.GOLD + ": " + message);
          }
          else
          {
             String targetrace = this.parent.getPlayerRace(p);

             if (!targetrace.equals(race))
              continue;
             p.sendMessage("[" + this.parent.getColouredName(player) + "][" + this.parent.getRaceNameShorthand(race) + "] " + ChatColor.WHITE + this.parent.getPlayerDisplayName(player) + " " + this.parent.getPlayerLastName(player)+" " +this.parent.getPlayerTitle(player) + ChatColor.GOLD + ": " + message);

             count++;
          }

        }

      }

       if (count < 1)
      {
         player.sendMessage(ChatColor.GRAY + "* You speak but nobody hears you (There is no-one from your race online.)");
      }
    }
    catch (NotRegisteredException localNotRegisteredException)
    {
    }
  }
}

