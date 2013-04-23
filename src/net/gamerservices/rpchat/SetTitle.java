package net.gamerservices.rpchat;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTitle implements CommandExecutor {
	rpchat parent;
	public SetTitle(rpchat rpchat) {
		
		this.parent = rpchat;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] args) {
		
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player)sender;
		if (args.length == 0) {
			return false;			
		}

		if (args.length == 1) {
			return false;			
		}

		String title = "";
		int count = 0;
		for (String item : args)
		{

			if (count != 0)
			{
				if (count == 1)
				{
					title = item;
				} else {
					title = title + " " + item;					
				}
			}
			count++;
		}


		if (this.parent.getServer().getPlayerExact(args[0]) == null)
		{
			player.sendMessage("Cannot set title, that minecraft account is not online");

		} else {


			this.parent.setPlayerTitle(player, this.parent.getServer().getPlayerExact(args[0]), title);
			
		}
		return true;
	}

}
