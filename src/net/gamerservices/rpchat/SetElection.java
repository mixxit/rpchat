package net.gamerservices.rpchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetElection implements CommandExecutor {

	rpchat parent;
	public SetElection(rpchat rpchat) {
		
		this.parent = rpchat;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandstring,
			String[] args) {
		
		
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player)sender;
				
		sqlPlayer sPlayerme = (sqlPlayer)this.parent.getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
		if (sPlayerme == null) {
			player.sendMessage("You cannot run for election while your account is being updated");
			return true;
		} else {
			
			if (sPlayerme.getElection() == 0)
			{
				player.sendMessage("You are now running for leadership! Good luck!");
				player.sendMessage("*WARNING* Not posting your template at http://tinyurl.com/fallofanempire will disqualify you on vote count day");
				
				sPlayerme.setElection(1);
				this.parent.getDatabase().save(sPlayerme);
				return true;
			} else {
				player.sendMessage("You have already set yourself to run for election or are the emperor");
				return true;
			}
		}
	}

}
