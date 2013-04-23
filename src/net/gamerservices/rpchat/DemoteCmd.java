package net.gamerservices.rpchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DemoteCmd implements CommandExecutor {

	rpchat parent;
	public DemoteCmd(rpchat rpchat) {
		
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
			if (args.length == 0) {
				player.sendMessage("Example: /demote onlineplayername");
				return true;
			}
			
			String targetplayername = args[0];
			Player targetplayer = this.parent.getServer().getPlayer(targetplayername);
			if (targetplayer != null)
			{
				this.parent.demotePlayer(targetplayer);
				player.sendMessage("Player demoted");
				return true;
			} else {
				player.sendMessage("That player does not exist or is offline");
				return true;
			}
		} else {
			player.sendMessage("This command requires rpchat.admin privileges");
			return true;
		}
	}

}
