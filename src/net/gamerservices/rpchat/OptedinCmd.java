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

public class OptedinCmd implements CommandExecutor {

	rpchat parent;
	public OptedinCmd(rpchat rpchat) {
		
		this.parent = rpchat;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandstring,
			String[] args) {
		
		
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player)sender;
		sqlPlayer splayer = this.parent.getPlayerObject(player);
		
		if (splayer != null)
		{
			if (splayer.getOptedin().equals("true"))
			{
				player.sendMessage("You have now been toggled off for receiving global chat");
				splayer.setOptedin("false");
				this.parent.getDatabase().save(splayer);
				return true;
			} else {
				// toggle it on
				player.sendMessage("You have now been toggled in to receive global chat");
				splayer.setOptedin("true");
				this.parent.getDatabase().save(splayer);
				return true;
			}
		} else {
			player.sendMessage("You cannot toggle global chat while your account is being updated");
			return true;
		}
	}

}
