package net.gamerservices.rpchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FindName implements CommandExecutor {

	rpchat parent;
	public FindName(rpchat rpchat) {
		
		parent = rpchat;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		
		CommandSender sender = arg0;
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player)sender;
		
		
		if (arg3.length == 0) {
			return false;
			
		} else {
			PlayerCache sPlayer = this.parent.getPlayerCacheByNameAsync(arg3[0]);
			if (sPlayer == null) {
				player.sendMessage("That name cannot be found");
				return true;
			} else {
				player.sendMessage(sPlayer.display + "'s minecraft account name is " + sPlayer.name);
				return true;
			}
			
			
			
		}
	}

}
