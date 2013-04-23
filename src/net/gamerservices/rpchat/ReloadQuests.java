package net.gamerservices.rpchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadQuests implements CommandExecutor {

	rpchat parent;
	public ReloadQuests(rpchat rpchat) {
		// TODO Auto-generated constructor stub
		parent = rpchat;
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		// TODO Auto-generated method stub
	
		if (!(arg0 instanceof Player)) {
			this.parent.loadQuests();
			arg0.sendMessage("Quests Reloaded");
			return true;
		}
		Player player = (Player)arg0;
		
		if (player.isOp() || this.parent.hasPerm(player, "rpitems.admin"))
		{
			this.parent.loadQuests();
			player.sendMessage("Quests Reloaded");
			return true;
		}
		
		return false;
		
	}

}
