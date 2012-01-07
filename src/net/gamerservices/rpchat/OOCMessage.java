package net.gamerservices.rpchat;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.object.Resident;

public class OOCMessage implements CommandExecutor {

	private rpchat parent;
	private Towny towny;
	
	public OOCMessage(rpchat rpchat) {
		// TODO Auto-generated constructor stub
		this.parent = rpchat;
		this.towny = this.parent.getTowny();
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
			
			// now that we have the player lets grab the players towny info
			
			Resident res = towny.getTownyUniverse().getResident(player.getName());

			// find players around player
			int count = 0;
			
			for (Player p : player.getWorld().getPlayers())
			{
				if (p.equals(player))
				{
					// talking to self
				} else {
					// not talking to self
					// ARE in the same world and not self - no distance based checking required
						p.sendMessage(res.getTown().getName() + "|" + res.getTitle() + " [OOC] " + ChatColor.LIGHT_PURPLE + player.getName() + " says out of character '" + message + "'");
						count++;
				}
			}
			
			if (count < 1)
			{
				player.sendMessage("You speak but nobody hears you (There is no-one in this world, try another world!)");
			}
			
			return true;
		} 
		catch (Exception e)
		{
			// could not find player
			System.out.println("Error: " + e.getMessage());
		}
				
		return false;
	}

}
