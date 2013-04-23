package net.gamerservices.rpchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RandomCmd implements CommandExecutor {

	rpchat plugin;
	public RandomCmd(rpchat rpchat) {
		// TODO Auto-generated constructor stub
		this.plugin = rpchat;
		
		
		
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		// TODO Auto-generated method stub
		if (!(arg0 instanceof Player))
		{
			return false;
		}
		Player player = (Player)arg0;
		if (arg3.length < 1)
		{
			this.plugin.sendRand(player,100);
			return true;
		}
		
		
		if (this.plugin.isInteger(arg3[0]))
		{
			int sides = Integer.parseInt(arg3[0]);
			this.plugin.sendRand(player,sides);
			return true;
		}
		return false;
	}

}
