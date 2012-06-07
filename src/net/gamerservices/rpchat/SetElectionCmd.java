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
		// TODO Auto-generated method stub
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player)sender;
		
		if (args.length == 0 || args.length == 1) {
			return false;
		}

		if (!this.parent.isInteger(args[1]))
		{
			return false;
		}
		
		int electionstatus = Integer.parseInt(args[1]);
		
		if (electionstatus > 2 || electionstatus < 0)
		{
			player.sendMessage("Election status must be greater than -1 and less than 3");
			return true;
		}
		
		if (player.isOp())
		{
			String name = args[0];

			sqlPlayer sPlayerme = (sqlPlayer)this.parent.getDatabase().find(sqlPlayer.class).where().ieq("name", name).findUnique();
			if (sPlayerme == null) {
				player.sendMessage("That player does not exist");
				return true;
			} else {
				
				sPlayerme.setElection(electionstatus);
				this.parent.getDatabase().save(sPlayerme);
				player.sendMessage("Player: " + name + " set to election status: " + electionstatus);
				return true;
			}
		} else {
			player.sendMessage("This is an op only command");
			return true;
		}
	}
	
}
