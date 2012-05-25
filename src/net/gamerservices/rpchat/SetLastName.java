package net.gamerservices.rpchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLastName implements CommandExecutor {
	rpchat parent;
	public SetLastName(rpchat rpchat) {
		
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
			player.sendMessage(ChatColor.LIGHT_PURPLE + "Your current lastname is: " + sPlayer.getLastname());
			player.sendMessage(ChatColor.LIGHT_PURPLE + "To set a new lastname use the: '/lastname lastame' command");
			return true;
			
		}
		
		if (arg3[0].matches("clear"))
		{
			sPlayer.setLastname("");
			this.parent.getDatabase().save(sPlayer);
			player.sendMessage("Your lastname has been cleared");
			return true;
		}
		
		if (arg3[0].matches("^[a-zA-Z]+$"))
		{
			sPlayer.setLastname(arg3[0].toString().toLowerCase());
			this.parent.getDatabase().save(sPlayer);
			player.sendMessage("Your lastname is now: " + arg3[0]);
			return true;
		} else {

			player.sendMessage("Your lastname can only contain letters");
			return false;
		}
		
		
		
	}

}
