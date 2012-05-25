package net.gamerservices.rpchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetDefaultChannel implements CommandExecutor {

	rpchat parent;
	public SetDefaultChannel(rpchat rpchat) {
		
		this.parent = rpchat;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandname,
			String[] args) {
		
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player)sender;
		
		sqlPlayer sPlayerme = (sqlPlayer)this.parent.getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
		if (sPlayerme == null) {
			player.sendMessage("You cannot set your default channel while your account is being updated");
			return true;
		} else {
			
			if (args.length == 0) {
				player.sendMessage("Default channel is currently:" + sPlayerme.getChatfocus());
				return false;
			} else {
				if (args[0].equals("local") || args[0].equals("global") || args[0].equals("town") || args[0].equals("ooc") || args[0].equals("race") || args[0].equals("alliance"))
				{
					sPlayerme.setChatfocus(args[0]);
					this.parent.getDatabase().save(sPlayerme);
					player.sendMessage("Default channel set to:" + args[0]);
					return true;
				} else {
					return false;
				}
					
			}
		}
	}

}
