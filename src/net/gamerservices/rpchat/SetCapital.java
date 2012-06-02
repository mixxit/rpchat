package net.gamerservices.rpchat;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCapital implements CommandExecutor {

	rpchat parent;
	public SetCapital(rpchat rpchat) {
		
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
			player.sendMessage("You cannot set the racial capital while your account is being updated");
			return true;
		} else {
			
			if (sPlayerme.getElection() == 2)
			{
				
				sqlRaces sRace = (sqlRaces)this.parent.getDatabase().find(sqlRaces.class).where().ieq("name", sPlayerme.getRace()).findUnique();
				if (sRace == null) {
					sRace = new sqlRaces();
					sRace.setName(sPlayerme.getRace());
					String world = player.getWorld().getName();
					Double x = player.getLocation().getX();
					Double y = player.getLocation().getY();
					Double z = player.getLocation().getZ();
					
					Float fx = Float.parseFloat(x.toString());
					Float fy = Float.parseFloat(y.toString());;
					Float fz = Float.parseFloat(z.toString());;
					
					Float fpitch = player.getLocation().getPitch();
					Float fyaw = player.getLocation().getYaw();
					String location = world+","+fx+","+fy+","+fz+","+fyaw+","+fpitch;
					
					sRace.setCapitalloc(location);
					this.parent.getDatabase().save(sRace);
					player.sendMessage("You have set the racial capital!");
				} else {
					String world = player.getWorld().getName();
					Double x = player.getLocation().getX();
					Double y = player.getLocation().getY();
					Double z = player.getLocation().getZ();
					
					Float fx = Float.parseFloat(x.toString());
					Float fy = Float.parseFloat(y.toString());;
					Float fz = Float.parseFloat(z.toString());;
					Float fpitch = player.getLocation().getPitch();
					Float fyaw = player.getLocation().getYaw();
					
					String location = world+","+fx+","+fy+","+fz+","+fyaw+","+fpitch;
					
					
					sRace.setCapitalloc(location);
					this.parent.getDatabase().save(sRace);
				}
				return true;
			} else {
				player.sendMessage("You are not the King of this race");
				return true;
			}
		}
		
	}

}
