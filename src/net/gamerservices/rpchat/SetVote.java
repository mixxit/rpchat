package net.gamerservices.rpchat;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetVote implements CommandExecutor {

	
	rpchat parent;
	public SetVote(rpchat rpchat) {
		
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
			player.sendMessage("You cannot vote while your account is being updated");
			return true;
		} else {
			if (args.length == 0) {
				player.sendMessage(sPlayerme.getRace() + " Leadership Election");
				player.sendMessage("----------------------");
				
				List<sqlPlayer> players = parent.getDatabase().find(sqlPlayer.class).where().ieq("race", sPlayerme.getRace()).findList();
				for (sqlPlayer p : players){
                    if (p.getElection() == 1)
                    {
                    	player.sendMessage(p.getName() + "("+p.getDisplay()+")");
                    }
                    if (p.getElection() == 2)
                    {
                    	player.sendMessage(p.getName() + "("+p.getDisplay()+") - King");
                    }
                    
				}

				
				player.sendMessage("----------------------");
				player.sendMessage("To see more information on a candidate and their leadership plans visit http://tinyurl.com/fallofanempire");
				player.sendMessage("Your current vote is for: " + sPlayerme.getVote());
				return true;
				
			} else {
				sqlPlayer sPlayer = (sqlPlayer)this.parent.getDatabase().find(sqlPlayer.class).where().ieq("name", args[0]).findUnique();
				if (sPlayer == null) {
					player.sendMessage("That account name cannot be found");
					player.sendMessage("If you used their display name, find their account with /findname Displayname");
					
					return true;
				} else {
					System.out.println("[RPChat] Vote request is checking if Race: "+sPlayerme.getRace() +"("+sPlayerme.getName() +") equals Race: " + sPlayer.getRace() + " ("+sPlayer.getName()+")");
					if (sPlayerme.getRace().equals(sPlayer.getRace()))
					{
						if (sPlayer.getElection() == 1 || sPlayer.getElection() == 2)
						{
							if (sPlayerme.getVote().equals(""))
							{
								sPlayerme.setVote(sPlayer.getName());
								this.parent.getDatabase().save(sPlayerme);
								player.sendMessage("Thank you, your vote has been cast");
								return true;
							} else {
								player.sendMessage("You have already voted");
								return true;
							}
						} else {
							player.sendMessage("That player is not running for election");
							return true;
						}
					} else {
						player.sendMessage("You cannot vote for someone of that race (only your own)");
						return true;
					}
				}
				
				
				
			}
		}
	}

}
