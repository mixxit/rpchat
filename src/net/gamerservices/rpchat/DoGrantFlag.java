package net.gamerservices.rpchat;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DoGrantFlag implements CommandExecutor {

	rpchat parent;
	public DoGrantFlag(rpchat rpchat) {
		
		this.parent = rpchat;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandstring,
			String[] args) {
		
		
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player)sender;
		
		if (args.length == 0) {
			return false;			
		}
		
		if (!this.parent.isKing(player))
		{
			player.sendMessage("You cannot grant flags to players if you are not the alliance king");
			return true;
		}
		
		if (this.parent.getServer().getPlayerExact(args[0]) == null)
		{
			player.sendMessage("You cannot grant flags to offline players");
			return true;
		}
		
		
		
		
		Player targetplayer = this.parent.getServer().getPlayerExact(args[0]);
		
		double x1 = targetplayer.getLocation().getX();
        double y1 = targetplayer.getLocation().getY();
        double z1 = targetplayer.getLocation().getZ();

        double x2 = player.getLocation().getX();
        double y2 = player.getLocation().getY();
        double z2 = player.getLocation().getZ();

        int xdist = (int)(x1 - x2);
        int ydist = (int)(y1 - y2);
        int zdist = (int)(z1 - z2);

        if ((xdist < -100) || (xdist > 100) || (ydist < -100) || (ydist > 100) || (zdist < -100) || (zdist > 100))
        {
          player.sendMessage("You are too far away to give the flag to " + targetplayer.getName());
          return true;
        }
		
		
		if (this.parent.getPlayerAlliance(player).equals(this.parent.getPlayerAlliance(targetplayer)))
		{
			if (!this.parent.hasFlag(targetplayer))
			{
				player.sendMessage("You have granted a flag to " + targetplayer.getName());
				this.parent.grantFlag(targetplayer);		
				return true;
			} else {
				player.sendMessage("That player already has a flag");
				return true;
			}
			
		} else {
			player.sendMessage("You cannot grant flags to players of a different alliance");
			return true;
		}
	}

}
