package net.gamerservices.rpchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupCmd implements CommandExecutor {

	rpchat parent;
	public GroupCmd(rpchat rpchat) {
		
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
		PlayerCache pc = this.parent.getPlayerCacheByName(player.getName());
		if (args.length == 0) {
			player.sendMessage("Your group: " + pc.group);
			player.sendMessage("Valid commands are: /group invite <name>, /group list, /group accept, /group decline, /group leave,/g <msg>");
			return true;
		}
		
		if (args[0].equals("invite"))
		{
			if (args.length == 2)
			{
				if (this.parent.getServer().getPlayer(args[1]) != null)
				{
					Player target = this.parent.getServer().getPlayer(args[1]);
					if (!target.equals(player))
					{
						System.out.println(player.getName() + " invited " + args[1] + " to join a group");
						this.parent.invitePlayerToGroup(player,target);					
					} else {
						player.sendMessage("You cannot invite yourself to a group");
					}
				} else {
					player.sendMessage(args[1] + " is not online");
				}
				return true;
			} else {
				player.sendMessage("Incorrect arguments ["+args.length+"] for group invite - see /group");
				return true;
			}
		}
		
		if (args[0].equals("leave"))
		{
			this.parent.removePlayerFromGroup(player);
			return true;
		}
		if (args[0].equals("list"))
		{
			this.parent.sendPlayerGroupList(player);
			return true;
		}
		if (args[0].equals("accept"))
		{
			this.parent.acceptGroup(player);
			return true;
		}
		if (args[0].equals("decline"))
		{
			this.parent.declineGroup(player);
			return true;
		}
		
		return true;
	}

}
