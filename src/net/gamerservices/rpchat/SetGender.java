package net.gamerservices.rpchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetGender implements CommandExecutor {
	rpchat parent;
	public SetGender(rpchat rpchat) {
		
		this.parent = rpchat;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		
		CommandSender sender = arg0;
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player)sender;
		
		PlayerCache sPlayer = this.parent.getPlayerCacheByName(player.getName());
		
		if (arg3.length == 0) {
			player.sendMessage(ChatColor.LIGHT_PURPLE + "Your current gender is: " + this.parent.getPlayerGender(player));
			player.sendMessage(ChatColor.LIGHT_PURPLE + "To set a new gender use the: '/gender gender' command");
			return true;
			
		}
		
		if (arg3[0].matches("male") || arg3[0].matches("female"))
		{
			sPlayer.gender = arg3[0].toString().toLowerCase();
			player.sendMessage("Your gender is now: " + arg3[0]);
			sPlayer.decoration = this.parent.getDecoration(sPlayer);
			
			return true;
		} else {

			player.sendMessage("Your gender can only be male or female (lowercase)");
			return false;
		}
		
		
		
	}

}
