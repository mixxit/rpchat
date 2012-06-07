package net.gamerservices.rpchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetStatsCmd implements CommandExecutor {

	rpchat parent;
	public GetStatsCmd(rpchat rpchat) {
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
		
		player.sendMessage("Stats:");
		player.sendMessage("Level: " + this.parent.getPlayerLevel(player) + " ["+this.parent.getPlayerExperience(player)+"/"+this.parent.getExpFromLevel(this.parent.getPlayerLevel(player)+1)+"]");
		player.sendMessage("Combat Level: " + this.parent.getPlayerCombatLevel(player) + " ["+this.parent.getPlayerCombatExp(player)+"/"+this.parent.getExpFromLevel(this.parent.getPlayerCombatLevel(player)+1)+"]");
		player.sendMessage("Ranged Level: " + this.parent.getPlayerRangedLevel(player) + " ["+this.parent.getPlayerRangedExp(player)+"/"+this.parent.getExpFromLevel(this.parent.getPlayerRangedLevel(player)+1)+"]");
		player.sendMessage("Scholarly Magic Level: " + this.parent.getScholarlyMagicLevel(player) + " ["+this.parent.getPlayerScholarlyExp(player)+"/"+this.parent.getExpFromLevel(this.parent.getScholarlyMagicLevel(player)+1)+"]");
		player.sendMessage("Natural/Divine Magic Level: " + this.parent.getNaturalMagicLevel(player) + " ["+this.parent.getPlayerNaturalExp(player)+"/"+this.parent.getExpFromLevel(this.parent.getNaturalMagicLevel(player)+1)+"]");
		player.sendMessage("Power: " + this.parent.getPlayerPower(player));
		return true;
	}

}
