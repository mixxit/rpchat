package net.gamerservices.rpchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OverLevelCmd implements CommandExecutor {

	rpchat parent;
	public OverLevelCmd(rpchat rpchat) {
		
		this.parent = rpchat;
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] args) {
		// TODO Auto-generated method stub
		if (!(arg0 instanceof Player)) {
			return false;
		}
		Player player = (Player)arg0;
		
		if (player.isOp() || this.parent.hasPerm(player,"rpchat.admin"))
		{
			this.parent.getPlayersOverCap(player);
			return true;
		} else {
			player.sendMessage("This command requires rpchat.admin privileges");
			return true;
		}
	}

}
