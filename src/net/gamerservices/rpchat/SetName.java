package net.gamerservices.rpchat;

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
		
		CommandSender sender = arg0;
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player)sender;
		
		sqlPlayer sPlayer = (sqlPlayer)this.parent.getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
		if (sPlayer == null) {
			sPlayer = new sqlPlayer();
			sPlayer.setName(player.getName());
			
			sPlayer.setDisplay(player.getName());
			sPlayer.setRace("human");
			sPlayer.setLanguage("human");
			sPlayer.setAlliance("combine");
			this.parent.getDatabase().save(sPlayer);
		}
		if (arg3.length == 0) {
			player.sendMessage(ChatColor.LIGHT_PURPLE + "Your current name is: " + sPlayer.getDisplay());
			player.sendMessage(ChatColor.LIGHT_PURPLE + "To set a new name use the: '/name Name' command");
			return true;
			
		}
		
		if (arg3[0].matches("^[a-zA-Z]+$"))
		{
			sqlPlayer sPlayer2 = (sqlPlayer)this.parent.getDatabase().find(sqlPlayer.class).where().ieq("display", arg3[0]).findUnique();
			if (sPlayer2 == null)
			{
				sPlayer.setDisplay(arg3[0].toString().toLowerCase());
			    player.setDisplayName(arg3[0]);
				this.parent.getDatabase().save(sPlayer);
				player.sendMessage("Your name is now: " + arg3[0]);
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
