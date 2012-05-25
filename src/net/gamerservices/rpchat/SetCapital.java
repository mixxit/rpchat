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
			player.sendMessage("You cannot set the capital while your account is being updated");
			return true;
		} else {
			
			if (sPlayerme.getElection() == 2)
			{
				
				sqlAlliances sAlliance = (sqlAlliances)this.parent.getDatabase().find(sqlAlliances.class).where().ieq("name", sPlayerme.getAlliance()).findUnique();
				if (sAlliance == null) {
					sAlliance = new sqlAlliances();
					sAlliance.setName(sPlayerme.getAlliance());
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
					
					sAlliance.setCapitalloc(location);
					this.parent.getDatabase().save(sAlliance);
					player.sendMessage("You have set the capital!");
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
					
					
					sAlliance.setCapitalloc(location);
					this.parent.getDatabase().save(sAlliance);
				}
				return true;
			} else {
				player.sendMessage("You are not the King of this alliance");
				return true;
			}
		}
		
	}

}
