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
						
				sqlPlayer sPlayerme = (sqlPlayer)this.parent.getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
				if (sPlayerme == null) {
					player.sendMessage("You cannot goto the racial capital while your account is being updated");
					return true;
				} else {
					
					sqlRaces sRace = (sqlRaces)this.parent.getDatabase().find(sqlRaces.class).where().ieq("name", sPlayerme.getRace()).findUnique();
					if (sRace == null) {
						sRace = new sqlRaces();
						sRace.setName(sPlayerme.getRace());
						this.parent.getDatabase().save(sRace);
						player.sendMessage("Your racial capital is not set");
						return true;
					} else {
						if (sRace.getCapitalloc().equals(""))
						{
							player.sendMessage("Your racial capital is not set");
							return true;
						} else {
							String capital = sRace.getCapitalloc();
							String[] locArray = capital.split(",");
							World world = this.parent.getServer().getWorld(locArray[0]);
							
							Location loc = new Location(world, Double.parseDouble(locArray[1]), Double.parseDouble(locArray[2]), Double.parseDouble(locArray[3]),Float.parseFloat(locArray[4]),Float.parseFloat(locArray[5]));
							this.parent.teleport(player,loc);
							
						}
					}
					return true;
					
						
					
				}

	}

}
