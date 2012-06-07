package net.gamerservices.rpchat;

import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AllianceCmd implements CommandExecutor {

	rpchat parent;
	public AllianceCmd(rpchat rpchat) {
		
		this.parent = rpchat;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandstring,
			String[] args) {
		
		
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player)sender;
		
		if (args.length == 0) {
			// go to alliance assembly
			sqlPlayer sPlayerme = (sqlPlayer)this.parent.getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
			if (sPlayerme == null) {
				player.sendMessage("You cannot goto the capital while your account is being updated");
				return true;
			} else {
				
				sqlAlliances sAlliance = (sqlAlliances)this.parent.getDatabase().find(sqlAlliances.class).where().ieq("name", sPlayerme.getAlliance()).findUnique();
				if (sAlliance == null) {
					sAlliance = new sqlAlliances();
					sAlliance.setName(sPlayerme.getAlliance());
					this.parent.getDatabase().save(sAlliance);
					player.sendMessage("Your capital is not set");
					return true;
				} else {
					if (sAlliance.getCapitalloc().equals(""))
					{
						player.sendMessage("Your capital is not set");
						return true;
					} else {
						String capital = sAlliance.getCapitalloc();
						String[] locArray = capital.split(",");
						World world = this.parent.getServer().getWorld(locArray[0]);
						
						Location loc = new Location(world, Double.parseDouble(locArray[1]), Double.parseDouble(locArray[2]), Double.parseDouble(locArray[3]),Float.parseFloat(locArray[4]),Float.parseFloat(locArray[5]));
						this.parent.teleport(player,loc);
						
					}
				}
				return true;
			}
			
		}
		
		if (!this.parent.isKing(player))
		{
			player.sendMessage(ChatColor.YELLOW + "Only King's can access the Alliance sub commands");
			return true;
		}
		
		if (args[0].toString().toLowerCase().equals("withdraw"))
		{
			String alliance = this.parent.getPlayerAlliance(player);
			int alliancevalue = this.parent.getAllianceResourcesValue(alliance);
			if (alliancevalue > 0)
			{
				int stacks = alliancevalue / 64;
				if (stacks > 0)
				{
					int itemsleft = this.parent.getAndClearAllianceResources(alliance);
					for (int i = 0; i < stacks; i = i + 1)
					{
						int removecount = 0;
						if (itemsleft >= 64)
						{
							removecount = 64;
						} else {
							removecount = itemsleft;
						}
						
						ItemStack stack = new ItemStack(Material.GOLD_INGOT, removecount);
						player.getWorld().dropItemNaturally(player.getLocation(),stack);
						player.sendMessage(ChatColor.YELLOW + "You were delivered " + removecount + " gold ingots from the alliance vault");
						
						itemsleft = itemsleft - removecount;
					}
					
					return true;
					
				} else {
					// remove less than one stack 
					int itemsleft = this.parent.getAndClearAllianceResources(alliance);
					ItemStack stack = new ItemStack(Material.GOLD_INGOT, itemsleft);
					player.getWorld().dropItemNaturally(player.getLocation(),stack);
					player.sendMessage(ChatColor.YELLOW + "You were delivered " + itemsleft + " gold ingots from the alliance vault");
					return true;
				}
			} else {
				player.sendMessage(ChatColor.YELLOW + "There is no gold in the vault to remove");
				return true;
			}
			
		}
		
		if (args[0].equals("set"))
		{
			sqlPlayer sPlayerme = (sqlPlayer)this.parent.getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
			if (sPlayerme == null) {
				player.sendMessage(ChatColor.YELLOW + "You cannot set the alliance assembly while your account is being updated");
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
						player.sendMessage(ChatColor.YELLOW + "You have set the alliance assembly!");
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
						
						player.sendMessage(ChatColor.YELLOW + "You have set the alliance assembly!");
						sAlliance.setCapitalloc(location);
						this.parent.getDatabase().save(sAlliance);
					}
					return true;
				} else {
					player.sendMessage(ChatColor.YELLOW + "You are not a member of the council of this alliance");
					return true;
				}
			}
		}
		
		player.sendMessage("Valid commands are: /alliance | /alliance withdraw | /alliance set");
		return false;
	}

}
