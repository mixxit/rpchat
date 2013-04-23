package net.gamerservices.rpchat;

import net.citizensnpcs.api.CitizensAPI;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
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
 
public class RPchatEntityListener implements Listener
{
	private rpchat plugin;
	
	public RPchatEntityListener(rpchat rpchat)
	{
		this.plugin = rpchat;
	}
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		if(!event.isCancelled()){
			if ((event.getEntity() instanceof Monster))
			{
				if (event.getEntityType() == EntityType.BLAZE && event.getEntity().getWorld().getName().equals("world"))
				{
					//System.out.println("Replacing blaze with enderdragon in world 'world'");
					event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), EntityType.SLIME);
					
					event.getEntity().remove();
	
				}
				if (event.getEntityType() == EntityType.ENDER_DRAGON)
				{
					//System.out.println("Replacing blaze with enderdragon in world 'world'");
					event.getEntity().getWorld().spawnCreature(event.getEntity().getLocation(), EntityType.SLIME);
					
					event.getEntity().remove();
	
				}
			}
		}
	}
	
	
	
	@EventHandler(priority = EventPriority.MONITOR) // citizens is highest due to worldguard being high
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
				// attack is from a mob
				if ((event.getEntity() instanceof Player))
				{
					// mob is attacking player
					Player victim = (Player)event.getEntity();
					
					// get the hp bonus of the player
					int hpbonus = this.plugin.getPlayerHPBonus(victim);
					// is the hp bonus and damage more than nothing?

					if (hpbonus > 0 && event.getDamage() > 0)
					{
						// player has some hp bonus 											
						
						//we should absorb the damage 
						int currentdamage = event.getDamage();
						
						int remainingdamage = currentdamage - hpbonus;
						if (remainingdamage > 0)
						{
							// removed all hpbonus
							
							victim.sendMessage("Your shielding protected you from " + hpbonus + " points of damage");
							
							// remove hpbonus completely
							victim.sendMessage("Your shielding can no longer withstand any damage!");
							hpbonus = 0;
							this.plugin.setPlayerHPBonus(victim, hpbonus);

							// remaining damage left to damage natural health
							event.setDamage(remainingdamage);
							
						} else {
							// removed some of hp bonus							
							victim.sendMessage("Your shielding protected you from " + currentdamage + " points of damage");
							
							// remove current damage from hpbonus
							hpbonus = hpbonus - currentdamage;
							this.plugin.setPlayerHPBonus(victim, hpbonus);
							// damage was avoided thanks to shielding
							event.setDamage(0);
							// can cancel event since it does not apply
							event.setCancelled(true);
							
						}
					}
					
				
				}
				
				return;
			}
	
			Player attacker = (Player)damagecause.getDamager();
			
			if (!(event.getEntity() instanceof Player))
			{
				return;
			}
			
			
			if (attacker.isFlying())
			{
				event.setCancelled(true);
				attacker.sendMessage(ChatColor.GRAY+"You cannot attack while flying");
				return;
			}
			
			// attack is from a player
			Player victim = (Player)event.getEntity();
			
			String victimrace = this.plugin.getPlayerRace(victim);
			String attackerrace = this.plugin.getPlayerRace(attacker);
			
			if(attacker.getItemInHand().hasItemMeta())
			{
				if (attacker.getItemInHand().getType().equals(Material.DIAMOND_SWORD))
				{
					if (attacker.getItemInHand().getItemMeta().getLore().get(0).contains("Encrusted Sword"))
					{
						String parts[] = attacker.getItemInHand().getItemMeta().getLore().get(0).split(" ");
						String type = parts[0];
						Metal metal = this.plugin.getMetal(type);
						if (metal != null)
						{
							event.setDamage(event.getDamage()+metal.value);
						}
					}
				}
			}
			
			// hp shielding only applies when an npc is attacking the player
			if (attacker.hasMetadata("NPC"))
			{
				
				int hpbonus = this.plugin.getPlayerHPBonus(victim);
				// is the hp bonus and damage more than nothing?
			
			
				if (hpbonus > 0 && event.getDamage() > 0)
				{
					// player has some hp bonus 											
					
					//we should absorb the damage 
					int currentdamage = event.getDamage();
					
					int remainingdamage = currentdamage - hpbonus;
					if (remainingdamage > 0)
					{
						// removed all hpbonus
						
						victim.sendMessage("Your shielding protected you from " + hpbonus + " points of damage");
						
						// remove hpbonus completely
						victim.sendMessage("Your shielding can no longer withstand any damage!");
						hpbonus = 0;
						this.plugin.setPlayerHPBonus(victim, hpbonus);
	
						// remaining damage left to damage natural health
						event.setDamage(remainingdamage);
						
					} else {
						// removed some of hp bonus							
						victim.sendMessage("Your shielding protected you from " + currentdamage + " points of damage");
						
						// remove current damage from hpbonus
						hpbonus = hpbonus - currentdamage;
						this.plugin.setPlayerHPBonus(victim, hpbonus);
						// damage was avoided thanks to shielding
						event.setDamage(0);
						// can cancel event since it does not apply
						event.setCancelled(true);
						
					}
				}	
			}
		
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
