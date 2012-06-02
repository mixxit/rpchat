package net.gamerservices.rpchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MeditateCmd implements CommandExecutor {

	rpchat parent;
	public MeditateCmd(rpchat rpchat) {
		// TODO Auto-generated constructor stub
		this.parent = rpchat;
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		// TODO Auto-generated method stub
		if (!(arg0 instanceof Player)) {
			return false;
		}
		Player player = (Player)arg0;
		
		if (this.parent.isPlayerAbleToMeditate(player))
		{
			int power = this.parent.getPlayerPower(player);
			if (power < 100)
			{
				if (this.parent.isPlayerAbleToFullMeditate(player))
				{
					this.parent.setPlayerPowerMeditate(player,100);
					
					this.parent.setLastMeditateAsNow(player);
					return true;
				}
				
				if (this.parent.isPlayerAbleTo3qMeditate(player))
				{
					this.parent.setPlayerPowerMeditate(player,75);
					
					this.parent.setLastMeditateAsNow(player);
					return true;
				}
				
				if (this.parent.isPlayerAbleTo2qMeditate(player))
				{
					this.parent.setPlayerPowerMeditate(player,50);
					
					this.parent.setLastMeditateAsNow(player);
					return true;
				}
				
				int newpower = power + 25;
				
				// don't want to go over the limit
				if (newpower > 100)
				{
					newpower = 100;
				}
				
				
				this.parent.setPlayerPowerMeditate(player,newpower);
				
				this.parent.setLastMeditateAsNow(player);
				return true;
				
			} else {
				player.sendMessage("Meditating has no effect on your already full power (100/100)");
				return true;
			}
		} else {
			int timeleft = 30 - this.parent.getPlayerTimeSinceLastMeditate(player);
			player.sendMessage("You must wait " + timeleft + " seconds before meditating again ("+this.parent.getPlayerPower(player)+"/100)");
			return true;
		}
	}

}
