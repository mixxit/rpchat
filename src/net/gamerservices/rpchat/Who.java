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
					try
					{
						
						PlayerCache sPlayer = this.parent.getPlayerCacheByName(p.getName());
						String alliance = sPlayer.alliance;
						
						if (this.parent.isPlayerSentry(p))
						{
							continue;
						}
						
						if (this.parent.isPlayerCitizen(p))
						{
							continue;
						}
						
						if (alliance.equals("Unknown"))
						{
							continue;
						}
						sender.sendMessage(alliance + " - " + sPlayer.display + " " + sPlayer.lastname+ "["+p.getName()+"] "+ sPlayer.race + "[C:"+this.parent.getLevelFromExp(sPlayer.combatexperience)+" R:"+this.parent.getLevelFromExp(sPlayer.rangedexperience)+" S:"+this.parent.getLevelFromExp(sPlayer.scholarlyexperience)+" N:"+this.parent.getLevelFromExp(sPlayer.naturalexperience)+"]");
					} catch (Exception e)
					{
						//sender.sendMessage(p.getDisplayName() + " - " + p.getName());
					}
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
					try
					{
						PlayerCache sPlayer = this.parent.getPlayerCacheByName(p.getName());
						String alliance = sPlayer.alliance;
						sender.sendMessage(alliance + " - " + sPlayer.display + " " + sPlayer.lastname+ "["+p.getName()+"] "+ sPlayer.race + "[C:"+this.parent.getLevelFromExp(sPlayer.combatexperience)+" R:"+this.parent.getLevelFromExp(sPlayer.rangedexperience)+" S:"+this.parent.getLevelFromExp(sPlayer.scholarlyexperience)+" N:"+this.parent.getLevelFromExp(sPlayer.naturalexperience)+"]");
					} catch (Exception e)
					{
						//sender.sendMessage(p.getDisplayName() + " - " + p.getName());
					}
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
					
					if (this.parent.isPlayerSentry(p))
					{
						continue;
					}
					
					if (this.parent.isPlayerCitizen(p))
					{
						continue;
					}

					try 
					{
						PlayerCache sPlayer = this.parent.getPlayerCacheByName(p.getName());
						String alliance = sPlayer.alliance;
						if (alliance.equals("Unknown"))
						{
							continue;
						}

						sender.sendMessage(alliance + " - " + sPlayer.display + " " + sPlayer.lastname+ "["+p.getName()+"] "+ sPlayer.race + "[C:"+this.parent.getLevelFromExp(sPlayer.combatexperience)+" R:"+this.parent.getLevelFromExp(sPlayer.rangedexperience)+" S:"+this.parent.getLevelFromExp(sPlayer.scholarlyexperience)+" N:"+this.parent.getLevelFromExp(sPlayer.naturalexperience)+"]");
					} catch (Exception e)
					{
						//sender.sendMessage(p.getDisplayName() + " - " + p.getName());

					}
				}
			}
			return true;
		}


		if (args.length == 1) {

			String querytype = "player";
			if (args[0].equals("neutral"))
			{
				querytype = "alliance";
			}
			if (args[0].equals("all"))
			{
				querytype = "all";
			}

			if (args[0].equals("light"))
			{
				querytype = "alliance";
			}
			if (args[0].equals("dark"))
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
						try
						{
							PlayerCache sPlayer = this.parent.getPlayerCacheByName(p.getName());
							String alliance = sPlayer.alliance;
							if (alliance.equals(args[0]))
							{
								sender.sendMessage(alliance + " - " + sPlayer.display + " " + sPlayer.lastname+ "["+p.getName()+"] "+ sPlayer.race + "[C:"+this.parent.getLevelFromExp(sPlayer.combatexperience)+" R:"+this.parent.getLevelFromExp(sPlayer.rangedexperience)+" S:"+this.parent.getLevelFromExp(sPlayer.scholarlyexperience)+" N:"+this.parent.getLevelFromExp(sPlayer.naturalexperience)+"]");
							}
						} catch (Exception e)
						{
							//sender.sendMessage(p.getDisplayName() + " - " + p.getName());
						}
					} else {
						
						if (this.parent.isPlayerSentry(p))
						{
							continue;
						}
						if (this.parent.isPlayerCitizen(p))
						{
							continue;
						}
						try
						{
							PlayerCache sPlayer = this.parent.getPlayerCacheByName(p.getName());
							String alliance = sPlayer.alliance;
							if (alliance.equals("Unknown"))
							{
								continue;
							}
							if (alliance.equals(args[0]))
							{
								sender.sendMessage(alliance + " - " + sPlayer.display + " " + sPlayer.lastname+ "["+p.getName()+"] "+ sPlayer.race + "[C:"+this.parent.getLevelFromExp(sPlayer.combatexperience)+" R:"+this.parent.getLevelFromExp(sPlayer.rangedexperience)+" S:"+this.parent.getLevelFromExp(sPlayer.scholarlyexperience)+" N:"+this.parent.getLevelFromExp(sPlayer.naturalexperience)+"]");
							}
						} catch (Exception e)
						{
							//sender.sendMessage(p.getDisplayName() + " - " + p.getName());
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
						if (this.parent.isPlayerSentry(p))
						{
							continue;
						}
						if (this.parent.isPlayerCitizen(p))
						{
							continue;
						}
						try
						{
							PlayerCache sPlayer = this.parent.getPlayerCacheByName(p.getName());
							String alliance = sPlayer.alliance;
							if (alliance.equals("Unknown"))
							{
								continue;
							}
							sender.sendMessage(alliance + " - " + sPlayer.display + " " + sPlayer.lastname+ "["+p.getName()+"] "+ sPlayer.race + "[C:"+this.parent.getLevelFromExp(sPlayer.combatexperience)+" R:"+this.parent.getLevelFromExp(sPlayer.rangedexperience)+" S:"+this.parent.getLevelFromExp(sPlayer.scholarlyexperience)+" N:"+this.parent.getLevelFromExp(sPlayer.naturalexperience)+"]");
						} catch (Exception e)
						{
							//sender.sendMessage(p.getDisplayName() + " - " + p.getName());
						}
					}

					return true;
				} else {
					player.sendMessage("Who Results (Player):");
					player.sendMessage("-------------------------");
					for (Player p : player.getWorld().getPlayers())
					{
						if (p.equals(player))
						{
							try
							{
								PlayerCache sPlayer = this.parent.getPlayerCacheByName(p.getName());
								String alliance = sPlayer.alliance;
								if (p.getName().matches(args[0]) || p.getName().matches(this.parent.capitalise(args[0])) || p.getDisplayName().equals(args[0]) || p.getDisplayName().matches(this.parent.capitalise(args[0])))
								{
									sender.sendMessage(alliance + " - " + sPlayer.display + " " + sPlayer.lastname+ "["+p.getName()+"] "+ sPlayer.race + "[C:"+this.parent.getLevelFromExp(sPlayer.combatexperience)+" R:"+this.parent.getLevelFromExp(sPlayer.rangedexperience)+" S:"+this.parent.getLevelFromExp(sPlayer.scholarlyexperience)+" N:"+this.parent.getLevelFromExp(sPlayer.naturalexperience)+"]");
								}
							} catch (Exception e)
							{
								//sender.sendMessage(p.getDisplayName() + " - " + p.getName());
							}
						} else {
							if (this.parent.isPlayerSentry(p))
							{
								continue;
							}
							if (this.parent.isPlayerCitizen(p))
							{
								continue;
							}
							try
							{
								PlayerCache sPlayer = this.parent.getPlayerCacheByName(p.getName());
								String alliance = sPlayer.alliance;
								if (alliance.equals("Unknown"))
								{
									continue;
								}
								if (p.getName().matches(args[0]) || p.getName().matches(this.parent.capitalise(args[0])) || p.getDisplayName().matches(args[0]) || p.getDisplayName().matches(this.parent.capitalise(args[0])))
								{
									sender.sendMessage(alliance + " - " + sPlayer.display + " " + sPlayer.lastname+ "["+p.getName()+"] "+ sPlayer.race + "[C:"+this.parent.getLevelFromExp(sPlayer.combatexperience)+" R:"+this.parent.getLevelFromExp(sPlayer.rangedexperience)+" S:"+this.parent.getLevelFromExp(sPlayer.scholarlyexperience)+" N:"+this.parent.getLevelFromExp(sPlayer.naturalexperience)+"]");
								}
							} catch (Exception e)
							{
								//sender.sendMessage(p.getDisplayName() + " - " + p.getName());
							}
						}
					}

				}
			}


		}


		return true;
	}
}