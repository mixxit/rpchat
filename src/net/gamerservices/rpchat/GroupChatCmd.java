package net.gamerservices.rpchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupChatCmd implements CommandExecutor {

	rpchat parent;
	public GroupChatCmd(rpchat rpchat) {
		
		this.parent = rpchat;
	}
	public static String arrayToString(String[] a, String separator) {
	     String result = "";
	     if (a.length > 0) {
	       result = a[0];
	       for (int i = 1; i < a.length; i++) {
	         result = result + separator + a[i];
	       }
	     }
	     return result;
	   }
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] args) {
		// TODO Auto-generated method stub
		if (!(arg0 instanceof Player)) {
			return false;
		}
		Player player = (Player)arg0;
		PlayerCache pc = this.parent.getPlayerCacheByName(player.getName());
		if (args.length == 0) {
			player.sendMessage("You did not supply a message to send");
			return true;
		}
		
		String message = arrayToString(args," ");
		this.parent.sendMessageToGroupFromPlayer(player,ChatColor.AQUA + message + ChatColor.RESET);
		
		return true;
	}

}
