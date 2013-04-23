package net.gamerservices.rpchat;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GotoCapital implements CommandExecutor {

	rpchat parent;
	public GotoCapital(rpchat rpchat) {
		
		this.parent = rpchat;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] args) {
		
		
				if (!(sender instanceof Player)) {
					return false;
				}
				Player player = (Player)sender;
						
				PlayerCache sPlayerme = this.parent.getPlayerCacheByName(player.getName());
				if (sPlayerme == null) {
					player.sendMessage("You cannot goto the racial capital while your account is being updated");
					return true;
				} else {
					//this.parent.PlayerGotoCapital(player);
					return true;
				}

	}

}
