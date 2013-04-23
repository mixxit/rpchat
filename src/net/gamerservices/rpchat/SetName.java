package net.gamerservices.rpchat;

import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetName implements CommandExecutor {

	rpchat parent;
	public SetName(rpchat rpchat) {
		
		this.parent = rpchat;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		
		Date now = new Date();
        String timenow = Long.toString(now.getTime());
		
		CommandSender sender = arg0;
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player)sender;
		
		PlayerCache sPlayer = this.parent.getPlayerCacheByName(player.getName());
		
		if (arg3.length == 0) {
			player.sendMessage(ChatColor.LIGHT_PURPLE + "Your current name is: " + sPlayer.display);
			player.sendMessage(ChatColor.LIGHT_PURPLE + "To set a new name use the: '/name Name' command");
			return true;
			
		}
		
		if (arg3[0].matches("^[a-zA-Z]+$"))
		{
			sqlPlayer sPlayer2 = (sqlPlayer)this.parent.getDatabase().find(sqlPlayer.class).where().ieq("display", arg3[0]).findUnique();
			if (sPlayer2 == null)
			{
				sPlayer.display = arg3[0].toString().toLowerCase();
			    player.setDisplayName(arg3[0]);
			    //sPlayer.setBirthstamp(timenow);
				player.sendMessage("Your name is now: " + arg3[0]);
				sPlayer.decoration = this.parent.getDecoration(sPlayer);
				this.parent.commitPlayerCache(player.getName());
				//player.sendMessage("Happy birthday!");
				return true;
			} else {
				player.sendMessage("You cannot use that name as it is already in use");
				return false;
			}
		} else {

			player.sendMessage("Your name can only contain letters");
			return false;
		}
		
		
		
	}

}
