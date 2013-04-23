package net.gamerservices.rpchat;

import java.util.Date;
import java.util.HashSet;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;


public class RPchatPlayerListener implements Listener
{
	private rpchat plugin;

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		if (!event.isCancelled())
		{
			Date now = new Date();
			Long timenow = now.getTime();
			Player player = event.getPlayer();
			PlayerCache pc = this.plugin.getPlayerCacheByNameAsync(player.getName());
			if (pc != null)
			{
				if (!pc.lastcommand.equals("") && (Long.parseLong(pc.lastcommand) + 750) > timenow )
				{
					pc.spamcount++;
					player.sendMessage("You are sending commands too fast and have been choked");
					System.out.println("[RPChat] WARNING " + player.getName() + " was sending commands too fast and has been choked");
					this.plugin.checkSpamCount(player);
					event.setCancelled(true);
				} else {
					pc.lastcommand = Long.toString(timenow);
					pc.spamcount = 0;
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		if (plugin.isPlayerSentry(event.getPlayer()))
		{
			return;
		}
		PlayerCache sPlayer = this.plugin.getPlayerCacheByName(event.getPlayer().getName());
		if (!sPlayer.group.equals("") && !sPlayer.lastgroupinvite.equals(""))
		{ 
			System.out.println("Player " +event.getPlayer()+" joined with group data, cleared");
			sPlayer.group = "";
			sPlayer.lastgroupinvite = "";
		}
			
		event.getPlayer().setDisplayName(sPlayer.display);
		
		/*
		if (sPlayer.election < 1)
		{
			this.plugin.jailPlayer(event.getPlayer(),"infirmary");
			
		}
		*/
	}
	

	
	@EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
		if (plugin.isPlayerSentry(event.getPlayer()))
		{
			return;
		}
		if (!this.plugin.getPlayerGroupAsync(event.getPlayer()).equals(""))
		{
			System.out.println("Removed " + event.getPlayer() + " from group on quit");
			this.plugin.removePlayerFromGroup(event.getPlayer());
		}
    }
    
    
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event)
	{
		if (plugin.isPlayerSentry(event.getPlayer()))
		{
			return;
		}
		if (!this.plugin.getPlayerGroupAsync(event.getPlayer()).equals(""))
		{
			System.out.println("Removed " + event.getPlayer() + " from group on quit");
			this.plugin.removePlayerFromGroup(event.getPlayer());
		}

	}
	/*
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		if (event.getEntity() instanceof Player)
		{
			if (plugin.isPlayerSentry(event.getEntity()))
			{
						return;
			}
			System.out.println("[RPChat] Saved player data on Death");
		}
	}
	*/
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if (event.isCancelled())
            return;
        if (event.getRightClicked() instanceof Player)
        {
        	Player target = (Player) event.getRightClicked();
        	if(this.plugin.isPlayerNPC(target))
        	{
        		this.plugin.parseNpcInteract(event.getPlayer(),target);
        	}
        }
		
	}
	public RPchatPlayerListener(rpchat rpchat)
	{
		this.plugin = rpchat;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		if(!event.isCancelled()){
			this.plugin.DoAsyncChat(event.getPlayer(), event.getPlayer().getName(),event.getMessage(),event.getRecipients());
			event.setCancelled(true);
		}
	}
	

	
}

