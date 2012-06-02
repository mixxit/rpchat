package net.gamerservices.rpchat;

import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Sector
  implements CommandExecutor
{
  rpchat plugin;

  public Sector(rpchat rpchat)
  {
    this.plugin = rpchat;
  }

  public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3)
  {
    Player player = (Player)arg0;

    String sectorname = this.plugin.getSectorName(player.getLocation());

    player.sendMessage(ChatColor.GREEN + "Territory breakdown for: " + sectorname);
    player.sendMessage(ChatColor.GREEN + "Dominating race at this location: " + this.plugin.getSectorDominator(this.plugin.getSectorName(player.getLocation())));

    
    sqlSector sSector = (sqlSector)this.plugin.getDatabase().find(sqlSector.class).where().ieq("name", sectorname).findUnique();
    
    String storedresource = "0";
    if (!sSector.getFlags().equals("") && sSector != null)
    {
    	int count = 0;
    	Date now = new Date();
        String timenow = Long.toString(now.getTime());

        Date then = new Date(Long.parseLong(sSector.getFlags()));
        
        // elapsed miliseconds
        Long elapsed = now.getTime() - then.getTime();
        // elapsed time in hours times by 100
        Long resourcegained = (long)((elapsed / 60000) * 1);
        count = this.plugin.safeLongToInt(resourcegained);
        storedresource = Integer.toString(count);
    }
    player.sendMessage(ChatColor.GREEN + "Stored resource value at this location: $" + storedresource );
    

    return true;
  }
}