package net.gamerservices.rpchat;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LocalMessage implements CommandExecutor {

	private rpchat parent;
	
	public LocalMessage(rpchat rpchat) {
		// TODO Auto-generated constructor stub
		this.parent = rpchat;
	}

	public static String arrayToString(String[] a, String separator) {
	    String result = "";
	    if (a.length > 0) {
	        result = a[0];    // start with the first element
	        for (int i=1; i<a.length; i++) {
	            result = result + separator + a[i];
	        }
	    }
	    return result;
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		// TODO Auto-generated method stub
		String message = arrayToString(arg3," ");
		// find the player
		try 
		{
			Player player = parent.getServer().getPlayer(arg0.getName());
			// find players around player
			
			for (Player p : player.getWorld().getPlayers())
			{
				
				// first we need to check if this player is in the redstone world, if so they receive the message by deafult
				if (p.getWorld().getName().compareTo("Redstone") > 0)
				{
					// ARE in Redstone world - do none distance based checking
					p.sendMessage(player.getName() + " yells '" + message + "'");
					
				} else {
					// NOT in Redstone world - do distance based checking
				
					// this player is in the players world, are they in range?
					double x1 = p.getLocation().getX();
	                double y1 = p.getLocation().getY();
	                double z1 = p.getLocation().getZ();
	
	                double x2 = player.getLocation().getX();
	                double y2 = player.getLocation().getY();
	                double z2 = player.getLocation().getZ();
					
					int xdist = (int) (x1 - x2);
	                int ydist = (int) (y1 - y2);
	                int zdist = (int) (z1 - z2);
	                
	                if ((xdist < -300 || xdist > 300) || (ydist < -300 || ydist > 300) || (zdist < -300 || zdist > 300)) {
	                    // out of range to do this
	                	
	                } else {
	                	
	                	p.sendMessage(player.getName() + " says '" + message + "'");
	                
	                }
				}
			}
			return true;
		} 
		catch (Exception e)
		{
			// could not find player
			 
		}
				
		return false;
	}

}
