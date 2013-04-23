package net.gamerservices.rpchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetInfectionCmd implements CommandExecutor {

	rpchat plugin;
	public GetInfectionCmd(rpchat rpchat) {
		// TODO Auto-generated constructor stub
		this.plugin = rpchat;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		// TODO Auto-generated method stub
		if (arg0 instanceof Player)
		{
			Player player = (Player)arg0;
			player.sendMessage(ChatColor.YELLOW + "* There are currently " + this.plugin.getInfectedCount() + " plague carriers");
			
			return true;
		}
		return false;
	}

}
