package net.gamerservices.rpchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetElectionCmd implements CommandExecutor {

	rpchat parent;
	public SetElectionCmd(rpchat rpchat) {
		
		this.parent = rpchat;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] args) {
		
		if (!(sender instanceof Player)) {
			return false;
		}

		Player player = (Player)sender;

		if (player.isOp() || this.parent.hasPerm(player,"rpchat.admin"))
		{

		if (args.length == 0) {
			return false;			
		}

		if (args.length == 1) {
			return false;			
		}

		if (this.parent.getServer().getPlayerExact(args[0]) == null)
		{
			player.sendMessage("Cannot set title, that minecraft account is not online");
			return true;

		} else {
			this.parent.setPlayerElection(player, this.parent.getServer().getPlayerExact(args[0]),args[1]);
			return true;
		}
		
		} else {
			player.sendMessage("This is an admin only command");
			return true;
		}
	}

}
