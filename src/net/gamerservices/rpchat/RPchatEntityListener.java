package net.gamerservices.rpchat;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
 
public class RPchatEntityListener implements Listener
{
	private rpchat plugin;
	
	public RPchatEntityListener(rpchat rpchat)
	{
		this.plugin = rpchat;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event)
	{
		
		if ((event.getEntity() instanceof Monster))
		{
			if(event.getEntity().getKiller() != null)
			{
				Player attacker = event.getEntity().getKiller();
				Monster victim = (Monster)event.getEntity();
				plugin.giveExperience(attacker,40);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		if(!event.isCancelled()){
			if ((event.getEntity() instanceof Monster))
			{
				if (event.getEntityType() == EntityType.BLAZE && event.getEntity().getWorld().getName().equals("world"))
				{
					System.out.println("Replacing blaze with enderdragon in world 'world'");
					event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), EntityType.ENDER_DRAGON);
					event.getEntity().remove();
	
				}
			}
		}
	}
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event)
	{

		if(!event.isCancelled()){
			if (!(event instanceof EntityDamageByEntityEvent))
			{
				return;
			}
		
			
			EntityDamageByEntityEvent damagecause = (EntityDamageByEntityEvent)event;
			if (!(damagecause.getDamager() instanceof Player))
			{
				return;
			}
	
			Player attacker = (Player)damagecause.getDamager();
			
			if (!(event.getEntity() instanceof Player))
			{
				
				return;
			}
		
			Player victim = (Player)event.getEntity();
		
			String victimrace = this.plugin.getPlayerRace(victim);
			String attackerrace = this.plugin.getPlayerRace(attacker);
		
			if (victimrace.equals(attackerrace))
			{
				attacker.sendMessage("* You are attacking a player of the same race!");
				if (victim.getHealth() > damagecause.getDamage())
				{
					return;
				}
		 
				return;
			}
		
			if (victim.getHealth() > damagecause.getDamage())
			{
				return;
			}
		}
	}
}
