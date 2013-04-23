package net.gamerservices.rpchat;

import java.util.Date;
import java.util.HashSet;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Message implements CommandExecutor {

	rpchat parent;
	String type;
	public Message(String type, rpchat rpchat) {
		// TODO Auto-generated constructor stub
		this.parent = rpchat;
		this.type = type;
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
			String[] arg3) 
	{
		if (arg3.length < 1)
		{
			// no arguments
			return false;
		}
		
		
		
		if (!(arg0 instanceof Player))
		{
			return false;
		}
		
		Player player = (Player)arg0;
		
		PlayerCache pc = this.parent.getPlayerCacheByName(player.getName());
		
		Date now = new Date();
		Long timenow = now.getTime();
		
		if (!pc.lastmessage.equals("") && (Long.parseLong(pc.lastmessage) + 750) > timenow )
		{
			player.sendMessage("You are sending messages too fast and have been choked");
			System.out.println("[RPChat] WARNING " + player.getName() + " was sending messages too fast and has been choked");
			System.out.println("[RPChat] Details " + player.getName() + "lastmessage"+pc.lastmessage + " timenow:" +timenow);
			pc.spamcount++;
			this.parent.checkSpamCount(player);
			return true;
		} else {
			pc.lastmessage = Long.toString(timenow);
			pc.spamcount = 0;
		}
		
		HashSet<Player> recipients = new HashSet<Player>();
		for (World w : player.getServer().getWorlds())
		{
			for (Player p : w.getPlayers())
			{
				recipients.add(p);
			}
		}
		String message = arrayToString(arg3," ");
		// TODO Auto-generated method stub
		if (this.type.equals(""))
		{
			this.parent.DoCachedLocalMessage(player,pc.decoration, message,recipients);
			this.parent.parseQuestText(player.getName(),message);
		}

		if (this.type.equals("local"))
		{
			
			this.parent.DoCachedLocalMessage(player,pc.decoration, message,recipients);
			this.parent.parseQuestText(player.getName(),message);
		}

		if (this.type.equals("nation"))
		{
			if (this.parent.realisticchat == true)
			{
				player.sendMessage("This chat channel is unavailable while the server setting realisticchat is enabled");
				player.sendMessage("You can set your default channel with /setfocus");
			} else {			

			this.parent.DoCachedNationMessage(player,pc.decoration, message,recipients);
			}

		}


		if (this.type.equals("town"))
		{
			if (this.parent.realisticchat == true)
			{
				player.sendMessage("This chat channel is unavailable while the server setting realisticchat is enabled");
				player.sendMessage("You can set your default channel with /setfocus");
			} else {			

			this.parent.DoCachedTownMessage(player,pc.decoration, message, recipients);
			}
		}

		if (this.type.equals("alliance"))
		{
			if (this.parent.realisticchat == true)
			{
				player.sendMessage("This chat channel is unavailable while the server setting realisticchat is enabled");
				player.sendMessage("You can set your default channel with /setfocus");
			} else {			

				this.parent.DoCachedAllianceMessage(player,pc.decoration, message, recipients);
			}
		}

		if (this.type.equals("race"))
		{
			if (this.parent.realisticchat == true)
			{
				player.sendMessage("This chat channel is unavailable while the server setting realisticchat is enabled");
				player.sendMessage("You can set your default channel with /setfocus");
			} else {			

			this.parent.DoCachedRaceMessage(player,pc.decoration, message, recipients);
			}
		}
		return true;
	}


}
