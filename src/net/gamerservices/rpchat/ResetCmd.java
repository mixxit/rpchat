package net.gamerservices.rpchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetCmd implements CommandExecutor {

	rpchat parent;
	public ResetCmd(rpchat rpchat) {
		
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
		
		if (args.length == 0) {
			player.sendMessage("Resets specialisation level by 1");
			player.sendMessage("Example: /reset scholarly");
			player.sendMessage("Valid options are: combat,ranged,scholarly,natural");
			return true;
		}
		
		if (args[0].equals("combat"))
		{
			int level = this.parent.getPlayerCombatLevel(player);
			if (level > 1)
			{
				sqlPlayer sPlayer = this.parent.getPlayerObject(player);
				sPlayer.setCombatexperience(this.parent.getExpFromLevel(level-1));
				this.parent.getDatabase().save(sPlayer);
				player.sendMessage("Your level was reset to: " + this.parent.getLevelFromExp(sPlayer.getCombatexperience()));
				return true;


			} else {
				player.sendMessage("You are already at the lowest level");
				return true;

			}
		}
		
		
		if (args[0].equals("ranged"))
		{
			int level = this.parent.getPlayerRangedLevel(player);
			if (level > 1)
			{
				sqlPlayer sPlayer = this.parent.getPlayerObject(player);
				sPlayer.setRangedexperience(this.parent.getExpFromLevel(level-1));
				this.parent.getDatabase().save(sPlayer);
				player.sendMessage("Your level was reset to: " + this.parent.getLevelFromExp(sPlayer.getRangedexperience()));
				return true;


			} else {
				player.sendMessage("You are already at the lowest level");
				return true;

			}
		}
		
		if (args[0].equals("scholarly"))
		{
			int level = this.parent.getScholarlyMagicLevel(player);
			if (level > 1)
			{
				sqlPlayer sPlayer = this.parent.getPlayerObject(player);
				sPlayer.setScholarlyexperience(this.parent.getExpFromLevel(level-1));
				this.parent.getDatabase().save(sPlayer);
				player.sendMessage("Your level was reset to: " + this.parent.getLevelFromExp(sPlayer.getScholarlyexperience()));
				return true;


			} else {
				player.sendMessage("You are already at the lowest level");
				return true;

			}
		}
		
		if (args[0].equals("natural"))
		{
			int level = this.parent.getNaturalMagicLevel(player);
			if (level > 1)
			{
				sqlPlayer sPlayer = this.parent.getPlayerObject(player);
				sPlayer.setNaturalexperience(this.parent.getExpFromLevel(level-1));
				this.parent.getDatabase().save(sPlayer);
				player.sendMessage("Your level was reset to: " + this.parent.getLevelFromExp(sPlayer.getNaturalexperience()));
				return true;

				
			} else {
				player.sendMessage("You are already at the lowest level");
				return true;

			}
		}
		return false;
	}

}
