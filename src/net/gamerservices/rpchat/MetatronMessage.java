package net.gamerservices.rpchat;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MetatronMessage
  implements CommandExecutor
{
  rpchat plugin;

  public MetatronMessage(rpchat rpchat)
  {
     this.plugin = rpchat;
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
     if (arg0.isOp())
    {
       String message = arrayToString(arg3, " ");
       for (World w : arg0.getServer().getWorlds())
      {
         for (Player p : w.getPlayers())
        {
           p.sendMessage(ChatColor.RED + "BEHOLD! The Metatron has arrived with a message from " + arg0.getName());
           p.sendMessage("§K" + message);
           p.sendMessage(ChatColor.RED + "With the holy message delivered the Metatron returns on high");
        }
      }
    }
    else {
       arg0.sendMessage("You may not summon the metatron");
    }

     return true;
  }
}

