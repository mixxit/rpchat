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
		
		PlayerCache sPlayerme = this.parent.getPlayerCacheByName(player.getName());
		if (sPlayerme == null) {
			player.sendMessage("You cannot set your default channel while your account is being updated");
			return true;
		} else {
			
			if (args.length == 0) {
				player.sendMessage("Default channel is currently:" + sPlayerme.chatfocus);
				return false;
			} else {
				if (this.parent.realisticchat == true)
				{
					if (args[0].equals("local"))
					{
						sPlayerme.chatfocus = args[0];
						player.sendMessage("Default channel set to:" + args[0]);
						return true;
					} else {
						player.sendMessage("That is not a valid channel");
						player.sendMessage("Valid Channel names: local");
						return true;
					}
				} else {
					if (args[0].equals("local") || args[0].equals("race") || args[0].equals("alliance")|| args[0].equals("town")|| args[0].equals("nation"))
					{
						sPlayerme.chatfocus= args[0];
						player.sendMessage("Default channel set to:" + args[0]);
						return true;
					} else {
						player.sendMessage("That is not a valid channel");
						player.sendMessage("Valid Channel names: local,race,alliance");
						return true;
					}
				}
				
					
			}
		}
	}

}
