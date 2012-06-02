package net.gamerservices.rpchat;

import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Who
  implements CommandExecutor
{
  rpchat parent;

  public Who(rpchat rpchat)
  {
    this.parent = rpchat;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String cmdstr, String[] args)
  {
	  if (!(sender instanceof Player)) {
		  sender.sendMessage("Who Results (All):");
		  sender.sendMessage("-------------------------");
		  for (World w : sender.getServer().getWorlds())
		  {
			  for (Player p : w.getPlayers())
			  {
				  if (this.parent.getPlayerAlliance(p).equals("Unknown"))
				  {
					  continue;
				  }
				  sender.sendMessage(this.parent.getPlayerAlliance(p) + " - " + this.parent.getPlayerDisplayName(p) + " " + this.parent.getPlayerLastName(p)+ " " + this.parent.getPlayerTitle(p) + " " +"["+p.getName()+"] "+ this.parent.getPlayerRace(p));
			  }
		  }
		  return true;
	  }
	  
	  Player player = (Player)sender;
	  if (args.length == 0) {
		  player.sendMessage("Who Results (Local Area):");
		  player.sendMessage("-------------------------");
		  
		  for (Player p : player.getWorld().getPlayers())
		  {
			  if (p.equals(player))
			  {
				  String alliance = this.parent.getPlayerAlliance(p);
				  player.sendMessage(alliance + " - " + this.parent.getPlayerDisplayName(p) + " " + this.parent.getPlayerLastName(p)+ " " +this.parent.getPlayerTitle(p) +" " + "["+p.getName()+"] "+ this.parent.getPlayerRace(p));
			  } else {
				  
					  double x1 = p.getLocation().getX();
					  double y1 = p.getLocation().getY();
					  double z1 = p.getLocation().getZ();
					
					  double x2 = player.getLocation().getX();
					  double y2 = player.getLocation().getY();
					  double z2 = player.getLocation().getZ();
					 
					  int xdist = (int)(x1 - x2);
					  int ydist = (int)(y1 - y2);
					  int zdist = (int)(z1 - z2);
					 
					  if ((xdist < -100) || (xdist > 100) || (ydist < -100) || (ydist > 100) || (zdist < -100) || (zdist > 100))
					  {
						  continue;
					  }
					  String alliance = this.parent.getPlayerAlliance(p);
					  if (alliance.equals("Unknown"))
					  {
						  continue;
					  }
					  
					  player.sendMessage(alliance + " - " + this.parent.getPlayerDisplayName(p) + " " + this.parent.getPlayerLastName(p)+ " " +this.parent.getPlayerTitle(p) +" " + "["+p.getName()+"] "+ this.parent.getPlayerRace(p));
			  }
		  }
		  return true;
	  }
	  
	  
	  if (args.length == 1) {
		  
		  String querytype = "player";
		  if (args[0].equals("combine"))
		  {
			  querytype = "alliance";
		  }
		  if (args[0].equals("all"))
		  {
			  querytype = "all";
		  }
		  
		  if (args[0].equals("realm"))
		  {
			  querytype = "alliance";
		  }
		  if (args[0].equals("dominion"))
		  {
			  querytype = "alliance";
		  }
		  if (args[0].equals("legion"))
		  {
			  querytype = "alliance";
		  }
		  if (args[0].equals("legacy"))
		  {
			  querytype = "alliance";
		  }
		  if (args[0].equals("foresworn"))
		  {
			  querytype = "alliance";
		  }
		  if (args[0].equals("forsaken"))
		  {
			  querytype = "alliance";
		  }
		  if (args[0].equals("collective"))
		  {
			  querytype = "alliance";
		  }
		  
		  
		  if (querytype.equals("alliance"))
		  {
			  player.sendMessage("Who Results (Alliance):");
			  player.sendMessage("-------------------------");
			  for (Player p : player.getWorld().getPlayers())
			  {
				  if (p.equals(player))
				  {
					  
					  String alliance = this.parent.getPlayerAlliance(p);
					  if (alliance.equals(args[0]))
					  {
						  player.sendMessage(alliance + " - " + this.parent.getPlayerDisplayName(p) + " " + this.parent.getPlayerLastName(p)+ " " +this.parent.getPlayerTitle(p) + " " +"["+p.getName()+"] "+ this.parent.getPlayerRace(p));
					  }
				  } else {
						  String alliance = this.parent.getPlayerAlliance(p);
						  if (alliance.equals("Unknown"))
						  {
							  continue;
						  }
						  if (alliance.equals(args[0]))
						  {
							  player.sendMessage(alliance + " - " + this.parent.getPlayerDisplayName(p) + " " + this.parent.getPlayerLastName(p)+ " " +this.parent.getPlayerTitle(p) + " " +"["+p.getName()+"] "+ this.parent.getPlayerRace(p));
						  }
				  }
			  }
			  return true;
			  
		  } else {
			  if (querytype.equals("all"))
			  {
				  player.sendMessage("Who Results (All):");
				  player.sendMessage("-------------------------");
				  for (Player p : player.getWorld().getPlayers())
				  {
					  if (this.parent.getPlayerAlliance(p).equals("Unknown"))
					  {
						  continue;
					  }
					  player.sendMessage(this.parent.getPlayerAlliance(p) + " - " + this.parent.getPlayerDisplayName(p) + " " + this.parent.getPlayerLastName(p)+ " " + this.parent.getPlayerTitle(p) + " " +"["+p.getName()+"] "+ this.parent.getPlayerRace(p));
				  }
			  
			  return true;
			  } else {
				  player.sendMessage("Who Results (Player):");
				  player.sendMessage("-------------------------");
				  for (Player p : player.getWorld().getPlayers())
				  {
					  if (p.equals(player))
					  {
						  
						  String alliance = this.parent.getPlayerAlliance(p);
						  if (p.getName().matches(args[0]) || p.getName().matches(this.parent.capitalise(args[0])) || p.getDisplayName().equals(args[0]) || p.getDisplayName().matches(this.parent.capitalise(args[0])))
						  {
							  player.sendMessage(alliance + " - " + this.parent.getPlayerDisplayName(p) + " " + this.parent.getPlayerLastName(p)+ " " +this.parent.getPlayerTitle(p) +" " + "["+p.getName()+"] " + this.parent.getPlayerRace(p));
						  }
					  } else {
							  String alliance = this.parent.getPlayerAlliance(p);
							  if (alliance.equals("Unknown"))
							  {
								  continue;
							  }
							  if (p.getName().matches(args[0]) || p.getName().matches(this.parent.capitalise(args[0])) || p.getDisplayName().matches(args[0]) || p.getDisplayName().matches(this.parent.capitalise(args[0])))
							  {
								  player.sendMessage(alliance + " - " + this.parent.getPlayerDisplayName(p) + " " + this.parent.getPlayerLastName(p)+ " " + this.parent.getPlayerTitle(p) + " " +"["+p.getName()+"] "+ this.parent.getPlayerRace(p));
							  }
					  }
				  }
				  
			  }
		  }
		  
		  
	  }
	  
	  
	  return true;
  }
}