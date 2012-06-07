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
		
	
        	
		
		if (this.parent.getPlayerAlliance(player).equals(this.parent.getPlayerAlliance(targetplayer)))
		{
			if (!this.parent.hasFlag(targetplayer))
			{
				player.sendMessage("You have granted a flag to " + targetplayer.getName());
				this.parent.grantFlag(targetplayer);		
				return true;
			} else {
				player.sendMessage("The player has been denied the flag");
				this.parent.ungrantFlag(targetplayer);	
				return true;
			}
			
		} else {
			player.sendMessage("You cannot grant flags to players of a different alliance");
			return true;
		}
	}

}
