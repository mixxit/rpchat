package net.gamerservices.rpchat;

import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DoPlantFlag implements CommandExecutor {

	rpchat parent;
	public DoPlantFlag(rpchat rpchat) {
		
		this.parent = rpchat;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandstring,
			String[] args) {
		
		
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player)sender;
				
		sqlPlayer sPlayerme = (sqlPlayer)this.parent.getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
		if (sPlayerme == null) {
			player.sendMessage("You cannot plant a flag while your account is being updated");
			return true;
		} else {
			
			if (sPlayerme.getFlagpole() == 1 || this.parent.isKing(player))
			{
				Location location = player.getLocation();
			    String sector = location.getWorld().getName() + ":" + location.getChunk().getX() + "," + location.getChunk().getZ();
			    
			    if (this.parent.isAdjacentToAllianceSector(sector,sPlayerme.getAlliance()) || (this.parent.getAllianceSectorCount(sPlayerme.getAlliance()) == 0))
			    {
				    String dominance = this.parent.getSectorDominator(sector);
				    String victimrace = dominance;
				    String tidyvictimrace = victimrace;
				    String tidyattackerrace = sPlayerme.getAlliance();
				    
				    player.sendMessage(ChatColor.YELLOW + "You notice a gold vein which is ripe harvesting by your alliance! You take your share of gold and plant the flag!");
					
				    this.parent.setSectorDominator(sector,sPlayerme.getAlliance());
				    if (dominance.equals("none"))
				    {
				    	// spammy
				    	//this.parent.sendMessageToAll(ChatColor.YELLOW + "Unchartered Territory has been captured by " + sPlayerme.getName() + " of The " + this.parent.capitalise(tidyattackerrace) + " Empire in sector " + sector + "!");
					    ItemStack stack = new ItemStack(Material.GOLD_INGOT, 1);
						player.getWorld().dropItemNaturally(player.getLocation(),stack);

				    	return true;
				    } else {
				      if (tidyattackerrace.equals(victimrace)) {
				    	  // already claimed it
				    	  player.sendMessage(ChatColor.YELLOW + "You have refreshed the flag");
				        return true;
				      }
				      this.parent.sendMessageToAlliance(victimrace, ChatColor.RED + sPlayerme.getName() + " of The " + this.parent.capitalise(tidyattackerrace) + " Empire has initiated an invasion of one of your sectors!!! Sector: (" + sector + ")");
	
				      String dropshipid = this.parent.addDropship(player.getLocation().getWorld().getName(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
	
				      if (!dropshipid.equals(null))
				      {
				        this.parent.sendMessageToAlliance(victimrace, ChatColor.GOLD + "To be teleported by the alliance mages use '/teleport " + dropshipid + "'");
				      }
					    ItemStack stack = new ItemStack(Material.GOLD_INGOT, 1);
						player.getWorld().dropItemNaturally(player.getLocation(),stack);

				      return true;
				    }
			    } else {
			    	player.sendMessage("You can only place the flag in territory adjacent to territory you own");
			    	return true;
			    }
				
			} else {
				player.sendMessage(ChatColor.YELLOW + "You are not carrying your alliances flag! Ask an alliance King for permission in /ac");
				return true;
			}
			
			
		}
	}

}
