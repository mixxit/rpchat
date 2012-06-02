package net.gamerservices.rpchat;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Query;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DoDropship
  implements CommandExecutor
{
  rpchat plugin;

  public DoDropship(rpchat rpchat)
  {
    this.plugin = rpchat;
  }

  public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3)
  {
    if (arg3.length == 0)
    {
      return false;
    }

    String dropshipcode = arg3[0];
    String dropshipid = Long.toString(rpchat.decode(dropshipcode));

    sqlDropships sDropship = (sqlDropships)this.plugin.getDatabase().find(sqlDropships.class).where().ieq("name", dropshipid).findUnique();
    if (sDropship == null) {
      arg0.sendMessage(" * Combat Zone (ID) does not exist");
      return true;
    }
    Player player = (Player)arg0;
    arg0.sendMessage(" * You are teleported by the alliance mages!");
    Location loc = new Location(this.plugin.getServer().getWorld(sDropship.getWorld()), Double.parseDouble(sDropship.getX()), Double.parseDouble(sDropship.getY()), Double.parseDouble(sDropship.getZ()));
    player.teleport(loc);
    return true;
  }
}