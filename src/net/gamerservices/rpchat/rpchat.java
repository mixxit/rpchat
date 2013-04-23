package net.gamerservices.rpchat;

import com.earth2me.essentials.Essentials;
import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Coord;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.palmergames.bukkit.towny.object.TownyWorld;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import net.aufdemrand.sentry.Sentry;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class rpchat extends JavaPlugin
{
	private PluginManager pm;
	private Towny towny = null;
	private Sentry sentry = null;
	private Listener rpchatPlayerListener;
	private Listener rpchatEntityListener;
	public static Permission permission = null;
	public static Economy econ = null;
	private Essentials essentials = null;
	public boolean isEssentials = false;
	public boolean infectionmode = false;
	static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
	public ConcurrentHashMap<String, PlayerCache> playerdata = new ConcurrentHashMap<String, PlayerCache>();
	public ConcurrentHashMap<String, GroupCache> groupdata = new ConcurrentHashMap<String, GroupCache>();
	public boolean realisticchat = true;
	private static final String LOG_PREFIX = "[Dynmap-RPChat] ";
	private static final String ADMIN_ID = "administrator";
	private List<Quest> quests = new ArrayList<Quest>();
	private List<ColHeader> columnnames = new ArrayList();
	public ConcurrentHashMap<String, String> rewardqueue = new ConcurrentHashMap<String, String>();
	// MAX EXPERIENCE (level 15 atm)
	final int maxxp = 137000;
	final int maxextendedxp = 3469000;
	public List<Metal> metals;
	
	public boolean isInteger( String input )  
	{  
		try  
		{  
			Integer.parseInt( input );  
			return true;  
		}  
		catch( Exception e)  
		{  
			return false;  
		}  
	}  
	
	public void onSentryPlayerHeal(Player from, Player target)
	{
		// pass it back to sentry
		this.sentry.onSentryPlayerHealed(from, target);
		
	}
	
	
	public void loadMetals()
	{
		Metal metal = new Metal();
		metal.name = "Adamantine";
		metal.value = 4;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Amazonium";
		metal.value = 10;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Argonite";
		metal.value = 13;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Banite";
		metal.value = 5;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Austrium";
		metal.value = 7;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Bombastium";
		metal.value = 4;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Bathusium";
		metal.value = 2;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Cobalt";
		metal.value = 7;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Brightsteel";
		metal.value = 3;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Corbonium";
		metal.value = 4;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Cavorite";
		metal.value = 1;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Darksteel";
		metal.value = 5;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Cuendillar";
		metal.value = 2;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Ebony";
		metal.value = 7;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Eitr";
		metal.value = 2;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Fractite";
		metal.value = 13;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Elementium";
		metal.value = 4;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Infinium";
		metal.value = 19;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Gorgonite";
		metal.value = 6;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Kratonite";
		metal.value = 10;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Katagonium";
		metal.value = 3;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Melangium";
		metal.value = 12;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Latinum";
		metal.value = 2;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Morphite";
		metal.value = 17;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Mithril";
		metal.value = 6;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Obdurium";
		metal.value = 15;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Novite";
		metal.value = 4;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Octogen";
		metal.value = 12;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Obsidium";
		metal.value = 3;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Omnesium";
		metal.value = 8;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Omnium";
		metal.value = 10;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Pergium";
		metal.value = 1;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Oxium";
		metal.value = 4;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Parium";
		metal.value = 2;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Phazite";
		metal.value = 4;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Photonium";
		metal.value = 3;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Philotium";
		metal.value = 6;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Polarite";
		metal.value = 4;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Pizzazium";
		metal.value = 4;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Promethium";
		metal.value = 19;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Primium";
		metal.value = 16;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Psitanium";
		metal.value = 17;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Protonite";
		metal.value = 17;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Quantium";
		metal.value = 18;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Pyerite";
		metal.value = 4;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Relux";
		metal.value = 15;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Radanium";
		metal.value = 4;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Rovolon";
		metal.value = 11;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Residuum";
		metal.value = 6;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Ryanium";
		metal.value = 5;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Runite";
		metal.value = 9;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Sheol";
		metal.value = 6;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Saronite";
		metal.value = 14;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Sivanium";
		metal.value = 8;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Sinisite";
		metal.value = 16;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Solonite";
		metal.value = 17;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Solarite";
		metal.value = 19;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Stravidium";
		metal.value = 10;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Soulsteel";
		metal.value = 11;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Synthium";
		metal.value = 11;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Stormphrax";
		metal.value = 14;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Thaesium";
		metal.value = 23;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Tarydium";
		metal.value = 8;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Thyrium";
		metal.value = 1;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Thorium";
		metal.value = 7;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Timonium";
		metal.value = 14;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Tibanna";
		metal.value = 8;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Triidium";
		metal.value = 5;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Titanite";
		metal.value = 17;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Tronium";
		metal.value = 2;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Tritanium";
		metal.value = 10;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Turbidium";
		metal.value = 1;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Tungite";
		metal.value = 8;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Valyrium";
		metal.value = 7;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Uridium";
		metal.value = 6;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Vionesium";
		metal.value = 7;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Veridium";
		metal.value = 8;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Vizorium";
		metal.value = 10;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Viridium";
		metal.value = 9;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Xenium";
		metal.value = 9;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Voltairium";
		metal.value = 3;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Xithricite";
		metal.value = 12;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Xirdalium";
		metal.value = 7;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Zanium";
		metal.value = 3;
		this.metals.add(metal);


		metal = new Metal();
		metal.name = "Yuanon";
		metal.value = 8;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Zoridium";
		metal.value = 8;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Zexonite";
		metal.value = 6;
		this.metals.add(metal);

		metal = new Metal();
		metal.name = "Solinium";
		metal.value = 20;
		this.metals.add(metal);
	}
	
	public void jailPlayer(Player player, String location)
	{
		System.out.println("Jailing " + player.getName() + " at " + location);
		try {
			this.essentials.getJails().sendToJail(this.essentials.getUser(player), location);
			this.essentials.getUser(player).setJailed(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void unjailPlayer(Player player)
	{
		System.out.println("Unjailing " + player.getName());
		try {
			World world = this.getServer().getWorld("world");
			Location loc = new Location(world,164,56,275);
			this.essentials.getUser(player).setJailed(false);
			this.essentials.getUser(player).teleport(loc);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public boolean isPlayerInRegion(Player player, String regionname)
	{
		// get the list of regions that contain the given location
		RegionManager regionManager = getWorldGuard().getRegionManager( player.getWorld());
		ApplicableRegionSet set = regionManager.getApplicableRegions( player.getLocation() );
		LinkedList< String > parentNames = new LinkedList< String >();
		LinkedList< String > regions = new LinkedList< String >();
		for ( ProtectedRegion region : set ) {
			String id = region.getId();
			if (id.equals(regionname))
			{
				return true;
			}
		}
		
		return false;
		
	}
	
	public Location getPlayerHomeLoc(Player player)
	{
		return player.getBedSpawnLocation();
	}

	private WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
	 
	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	        return null; // Maybe you want throw an exception instead
	    }
	 
	    return (WorldGuardPlugin) plugin;
	}
	
	public void loadQuests()
	{
		System.out.println("[RPChat] Loading quests...");
		int loadquests = 0;
		try
		{
			//File file = new File("abilities.tsv");
			
			// Now pulled automatically from the site
			BufferedReader bufRdr;
			String filepath = this.getDataFolder().getAbsoluteFile()+"/quests.tsv";
			FileReader file = new FileReader(filepath);
			
			bufRdr = new BufferedReader (file);
			
			//BufferedReader bufRdr  = new BufferedReader(new FileReader(file));
			String line = null;
			int row = 0;
			int col = 0;
			this.columnnames.clear();
			this.quests.clear();

			while((line = bufRdr.readLine()) != null)
			{
				Quest quest = new Quest();
				String dataArray[] = line.split("\\t");
				for (String columndata : dataArray)
				{
					if (row == 0)
					{
						ColHeader column = new ColHeader();
						column.id = col;
						column.column = columndata;
						columnnames.add(column);
					} else {
						String thiscolheader = getColumnByID(col);

						if (thiscolheader.equals("npcname"))
						{
							quest.npcname = columndata;
						}
						if (thiscolheader.equals("req_word"))
						{
							quest.req_word = columndata;
						}
						if (thiscolheader.equals("req_itemid"))
						{
							quest.req_itemid = columndata;
						}
						if (thiscolheader.equals("req_enchantmentid"))
						{
							quest.req_enchantmentid = columndata;
						}
						if (thiscolheader.equals("req_flag"))
						{
							quest.req_flag = columndata;
						}
						if (thiscolheader.equals("req_flagvalue"))
						{
							quest.req_flagvalue = columndata;
						}
						if (thiscolheader.equals("not_flag"))
						{
							quest.not_flag = columndata;
						}
						if (thiscolheader.equals("response"))
						{
							quest.response = columndata;
						}
						if (thiscolheader.equals("not_flagresponse"))
						{
							quest.not_flagresponse = columndata;
						}
						if (thiscolheader.equals("noreq_flagresponse"))
						{
							quest.noreq_flagresponse = columndata;
						}
						if (thiscolheader.equals("reward"))
						{
							quest.reward = columndata;
						}
						
						if (thiscolheader.equals("reward_flag"))
						{
							quest.reward_flag = columndata;
						}
						
						if (thiscolheader.equals("reward_flagvalue"))
						{
							quest.reward_flagvalue = columndata;
						}
					}
					col++;
				}
				col = 0;
				if (row != 0)
				{
					// Check if it's an npc name so skip it
					if (quest.npcname.equals(""))
					{
						//System.out.println("Invalid Ability Skipped (Missing ID or Name");
					} else {
						//System.out.println("Loaded Quest Entry for: " + quest.npcname + ": " + quest.req_word);
						this.quests.add(quest);
						loadquests++;
					}
				}
				row++;
			}
			bufRdr.close();
		} catch (Exception e)
		{
			System.out.println("RPItems Exception (loadQuests): " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("[RPItems] Loaded " + loadquests + " quest entries");


	}
	
	
	public String getColumnByID(int id)
	{
		for (ColHeader col : this.columnnames)
		{
			if (col.id == id)
			{
				return col.column;
			}
		}
		return null;
	}
	
	private void sendPlayerQuestResponse(Player player, Player sentry,
			String triggerword) 
	{
		for (Quest q : this.quests)
		{
			if (q.npcname.equals(sentry.getName()) && triggerword.contains(q.req_word.toLowerCase()) && !q.req_word.equals(""))
			{
				if (!q.not_flag.equals(""))
				{
					// wait - we have a flag they musnt have lets check it!
					
					if (hasFlag(player, q.not_flag))
					{
						
						// doh he has it - respond
						
						String text = q.npcname + " says " + "'"+ChatColor.YELLOW+q.not_flagresponse+ChatColor.RESET+"'";
						player.sendMessage(text);
						return;
					}
				}
				// is a flag needed
				if (!q.req_flag.equals(""))
				{
					// incase they dont have it ...
					if (!hasFlag(player, q.req_flag))
					{
						// doesnt have the required flag
						if (!q.noreq_flagresponse.equals(""))
						{
							
							String text = q.npcname + " says " + "'"+ChatColor.YELLOW+q.noreq_flagresponse+ChatColor.RESET+"'";
							player.sendMessage(text);
							
						}
						// exit
						return;
					}
				}
				
				String text = q.npcname + " says " + "'"+ChatColor.YELLOW+q.response+ChatColor.RESET+"'";
				player.sendMessage(text);
				
				if (!q.reward.equals(""))
				{
					this.rewardqueue.put(player.getName(), q.reward);
					player.sendMessage(ChatColor.LIGHT_PURPLE + "* An item drops at your feet");
					
				}
				
				if (!q.reward_flag.equals(""))
				{
					this.giveFlag(player,q.reward_flag);
					player.sendMessage("* You have received a character flag!");
				}
			}
		}
		
		// do nothing
		
	}

	private void giveFlag(Player player, String reward_flag) {
		// TODO Auto-generated method stub
		PlayerCache pc = this.getPlayerCacheByNameAsync(player.getName());
		
		if (!hasFlag(player,reward_flag))
		{
			pc.flags = pc.flags + reward_flag + ",";
		}
		
	}
	
	private boolean hasFlag(Player player, String reward_flag)
	{
		PlayerCache pc = this.getPlayerCacheByNameAsync(player.getName());
		
		for (String flag : pc.flags.split(","))
		{
			if (flag.equals(reward_flag))
			{
				return true;
			}
		}
		
		return false;
	}

	public void setCheatBypass(Player player, boolean bool)
	{
		if (bool == true)
		{
			permission.playerAdd(player, "towny.cheat.bypass");
		} else {
			permission.playerRemove(player, "towny.cheat.bypass");
		}
	}

	public int getMaxExperience(Player player)
	{
		if (!hasPerm(player,"rpchat.extended"))
		{
			return this.maxxp;
		} else {
			// has more xp
			return this.maxextendedxp;
		}
	}
	
	public String getPlayerStandingInTownName(Player player)
	{
	 
		try {
			Coord coord = Coord.parseCoord(player);
			TownyWorld world = TownyUniverse.getDataSource().getWorld(player.getWorld().getName());

			TownBlock townblock = world.getTownBlock(coord);
			
			return townblock.getTown().getName();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//System.out.println("isPlayerInTownCalled: " + e.getMessage());
			return "";
		}
	} 
	
	public boolean isPlayerInTownCalled(Player player, String town)
	{
	 
		try {
			Coord coord = Coord.parseCoord(player);
			TownyWorld world = TownyUniverse.getDataSource().getWorld(player.getWorld().getName());

			TownBlock townblock = world.getTownBlock(coord);
			
			if (townblock.getTown().getName().equals(town))
			{
				return true;
			} else {
				return false;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//System.out.println("isPlayerInTownCalled: " + e.getMessage());
			return false;
		}
	} 
	
	public String getClassTitle(PlayerCache sPlayer)
	{
		if (sPlayer == null)
		{
			return "Survivor";
		}
		
		int combatexp = sPlayer.combatexperience;
		int rangedexp = sPlayer.rangedexperience;
		int scholarlyexp = sPlayer.scholarlyexperience;
		int naturalexp = sPlayer.naturalexperience;
		
		if (combatexp == 0  && rangedexp == 0 && scholarlyexp == 0 && naturalexp == 0)		
		{
			return "Survivor";
		}
		
		// Core classes
		
		if (combatexp > 0  && rangedexp == 0 && scholarlyexp == 0 && naturalexp == 0)		
		{
			return "Guardian";
		}
		
		if (combatexp == 0  && rangedexp > 0 && scholarlyexp == 0 && naturalexp == 0)		
		{
			return "Ranger";
		}
		
		if (combatexp == 0  && rangedexp == 0 && scholarlyexp > 0 && naturalexp == 0)		
		{
			return "Sorcerer";
		}
		
		if (combatexp == 0  && rangedexp == 0 && scholarlyexp == 0 && naturalexp > 0)		
		{
			return "Mystic";
		}
		
		// Hybrids
		SkillEntry combat = new SkillEntry("combat",combatexp);
		SkillEntry ranged = new SkillEntry("ranged",rangedexp);
		SkillEntry scholarly = new SkillEntry("scholarly",scholarlyexp);
		SkillEntry natural = new SkillEntry("natural",naturalexp);
		
		ArrayList<SkillEntry> skills = new ArrayList<SkillEntry>();
		skills.add(combat);
		skills.add(ranged);
		skills.add(scholarly);
		skills.add(natural);
		
		Collections.sort(skills,Collections.reverseOrder());
		
		SkillEntry first = new SkillEntry("null",0);
		SkillEntry second = new SkillEntry("null",0);
		
		int count = 1;
		for (SkillEntry se : skills) {
			if (count == 1)
			{
				first = se;
			}
			
			if (count == 2)
			{
				second = se;
			}
			if (count > 2)
			{
				break;
			}
			
			count++;
        }
		
		if (first.name.equals("null") || second.name.equals("null"))
		{
			return "Survivor";
		}
		
		// Paladin
		
		if (first.name.equals("combat") && second.name.equals("natural"))
		{
			return "Paladin";
		}
		
		if (second.name.equals("combat") && first.name.equals("natural"))
		{
			return "Paladin";
		}
		
		// Assassin
		
		if (first.name.equals("combat") && second.name.equals("ranged"))
		{
			return "Assassin";
		}
		
		if (second.name.equals("combat") && first.name.equals("ranged"))
		{
			return "Assassin";
		}
		
		// Battlemage
		
		if (first.name.equals("combat") && second.name.equals("scholarly"))
		{
			return "Battlemage";
		}
		
		if (second.name.equals("combat") && first.name.equals("scholarly"))
		{
			return "Battlemage";
		}
		
		// Shadowcaster
		
		if (first.name.equals("scholarly") && second.name.equals("ranged"))
		{
			return "Shadowcaster";
		}
		
		if (second.name.equals("scholarly") && first.name.equals("ranged"))
		{
			return "Shadowcaster";
		}
		
		//Oracle
				
		if (first.name.equals("scholarly") && second.name.equals("natural"))
		{
			return "Oracle";
		}
		
		if (second.name.equals("scholarly") && first.name.equals("natural"))
		{
			return "Oracle";
		}
		
		//Warden
		
		if (first.name.equals("ranged") && second.name.equals("natural"))
		{
			return "Warden";
		}
		
		if (second.name.equals("ranged") && first.name.equals("natural"))
		{
			return "Warden";
		}
		
		return "Survivor";
	}
	
	public boolean isPlayerInOtherPlayersTown(Player player)
	{
		// lets just check if they have permission to build there it will save alot of code checks
		Block block = player.getLocation().getBlock();
		boolean permission = PlayerCacheUtil.getCachePermission(player, block.getLocation(), block.getTypeId(), block.getData(), TownyPermission.ActionType.DESTROY);
		if (permission == true)
		{
			// has permission, is not in otherplayers town he cant build in so return FALSE
			return false;
		} else {
			// doesnt have permission, must be in otherplayers town he cant build in so return TRUE
			return true;
		}
		
		/*
		try {
			Coord coord = Coord.parseCoord(player);
			TownyWorld world = TownyUniverse.getDataSource().getWorld(player.getWorld().getName());

			TownBlock townblock = world.getTownBlock(coord);
			
			
			List<Resident> residents = this.towny.getTownyUniverse().getActiveResidents();
			for (Resident resident : residents)
			{
				if (resident.getName().equals(player.getName()))
				{
					try
					{
						if (resident.getTown().equals(townblock.getTown()))
						{
							// we are a member of a town and part of this town
							return false;
						} else {
							// we are a member of a town and not in this particular town!
							// are we in an embasy?
							townblock.
							

							return true;
						}
					} catch (Exception e)
					{
						// check for resident not in a town
						if (!(townblock.getTown() == null))
						{
							// we are in a town block and not a member of a town ourselves!
							return true;
						}
						
						// we are not in a town 
						return false;
					}
				}
			}
			
			return false;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("isPlayerInOtherPlayersTown: " + e.getMessage());
			return false;
		}
		*/
	}

	public boolean isPlayerInPVPArea(Player player)
	{
		try {
			Coord coord = Coord.parseCoord(player);
			TownyWorld world = TownyUniverse.getDataSource().getWorld(player.getWorld().getName());

			TownBlock townblock = world.getTownBlock(coord);

			if ((townblock.getTown().isPVP() || world.isForcePVP() || townblock.getPermissions().pvp))
			{
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//System.out.println("isPlayerInPVPArea: " + e.getMessage());
			return true;
		}
	}
	
	public boolean isPlayerCitizen(Player player)
	{
		return isPlayerNPC(player);
	}
	
	public boolean isPlayerNPC(Player player)
	{
		if (player.hasMetadata("NPC"))
		{
			return true;
		} else {
			return false;
		}
	}
	public boolean isPlayerSentry(Player player)
	{
		// there is no sentry system loaded so return false
		if (sentry == null)
		{
			return false;
		}
		
		// its not null, sentry system is loaded lets check if its a sentry
		Entity e = (Entity)player;
		if (sentry.getSentry(e) == null)
		{
			return false;
		} else {
			return true;
		}
	}

	public void setPlayerRace(Player player, String racename)
	{
		String language = getLanguageName(racename);
		PlayerCache sPlayer = this.getPlayerCacheByName(player.getName());
		player.sendMessage("Your language and language skills were reset");
		sPlayer.language = language;
		sPlayer.languageflags = "0,0,0,0,0,0,0,0,0,0,0,0";
		//System.out.println("[RPChat] Changing " + player.getName() + " to a " + racename);
		sPlayer.race = racename;
		//System.out.println("Clearing votes for:" + player.getName());
		this.clearVotes(player);
		sPlayer.vote ="";
		sPlayer.language =language;
		sPlayer.languageflags ="0,0,0,0,0,0,0,0,0,0,0,0";
		sPlayer.alliance =this.getRaceAlliance(racename);
	}
	
	public void clearVotes(Player player) {
		// clear database
		List<sqlPlayer> players = getDatabase().find(sqlPlayer.class).where().ieq("vote", player.getName()).findList();
		for (sqlPlayer p : players){
			p.setVote("");
			PlayerCache pc = this.getPlayerCacheByName(p.getName());
			if (pc != null)
			{
				pc.vote = "";
			}
			this.getDatabase().save(p);
		}
	}

	public static String getScrambled(String race, String s) {
		
		String t = "human,naturestongue,elvish,deathspeak,anciebnthuman,undertongue,gnomish,ancientgobel,lidkish,tiktok";
		
        String[] scram = s.split("");
        List<String> letters = Arrays.asList(scram);
        Collections.shuffle(letters);
        StringBuilder sb = new StringBuilder(s.length());
        for (String c : letters) {
            sb.append(c);
        }
        return sb.toString();
    }
	
	public String getLanguageName(String racename) {
		// TODO Auto-generated method stub
		if (racename.equals("human"))
		{
			return "human";
		}
		if (racename.equals("fairy"))
		{
			return "naturestongue";
		}
		
		if (racename.equals("highelf"))
		{
			return "elvish";
		}
		if (racename.equals("woodelf"))
		{
			return "elvish";
		}
		if (racename.equals("halfelf"))
		{
			return "elvish";
		}
		if (racename.equals("darkelf"))
		{
			return "elvish";
		}
		if (racename.equals("vampire"))
		{
			return "deathspeak";
		}
		
		if (racename.equals("barbarian"))
		{
			return "ancienthuman";
		}
		if (racename.equals("orc"))
		{
			return "undertongue";
		}
		if (racename.equals("ogre"))
		{
			return "undertongue";
		}
		if (racename.equals("troll"))
		{
			return "undertongue";
		}
		if (racename.equals("gnome"))
		{
			return "gnomish";
		}
		if (racename.equals("goblin"))
		{
			return "ancientgobel";
		}
		if (racename.equals("hobbit"))
		{
			return "thiefspeak";
		}
		if (racename.equals("halfdragon"))
		{
			return "ancientdragon";
		}
		if (racename.equals("highhuman"))
		{
			return "human";
		}
		if (racename.equals("undead"))
		{
			return "deathspeak";
		}
		if (racename.equals("dwarf"))
		{
			return "undertongue";
		}
		if (racename.equals("ratman"))
		{
			return "undertongue";
		}
		if (racename.equals("lizardman"))
		{
			return "lidkish";
		}
		if (racename.equals("elemental"))
		{
			return "undertongue";
		}
		if (racename.equals("kobold"))
		{
			return "undertongue";
		}
		if (racename.equals("angel"))
		{
			return "naturestongue";
		}
		if (racename.equals("fallenangel"))
		{
			return "deathspeak";
		}
		if (racename.equals("clockwork"))
		{
			return "tiktok";
		}
		return "human";
	}

	public int getLevelFromExp(int xp)
	{
		if (xp <= 400)
		{
			return 1;
		}
		if (xp <= 900)
		{
			return 2;
		}
		if (xp <= 1400)
		{
			return 3;
		}
		if (xp <= 2100)
		{
			return 4;
		}
		if (xp <= 2800)
		{
			return 5;
		}
		if (xp <= 3600)
		{
			return 6;
		}
		if (xp <= 4500)
		{
			return 7;
		}
		if (xp <= 5400)
		{
			return 8;
		}
		if (xp <= 6500)
		{
			return 9;
		}
		if (xp <= 7600)
		{
			return 10;
		}
		if (xp <= 8700)
		{
			return 11;
		}
		if (xp <= 9800)
		{
			return 12;
		}
		if (xp <= 11000)
		{
			return 13;
		}
		if (xp <= 12300)
		{
			return 14;
		}
		if (xp <= 13600)
		{
			return 15;
		}
		if (xp <= 15000)
		{
			return 16;
		}
		if (xp <= 16400)
		{
			return 17;
		}
		if (xp <= 17800)
		{
			return 18;
		}
		if (xp <= 19300)
		{
			return 19;
		}
		if (xp <= 20800)
		{
			return 20;
		}
		if (xp <= 22400)
		{
			return 21;
		}
		if (xp <= 24000)
		{
			return 22;
		}
		if (xp <= 25500)
		{
			return 23;
		}
		if (xp <= 27200)
		{
			return 24;
		}
		if (xp <= 28900)
		{
			return 25;
		}
		if (xp <= 30500)
		{
			return 26;
		}
		if (xp <= 32200)
		{
			return 27;
		}
		if (xp <= 33900)
		{
			return 28;
		}
		if (xp <= 36300)
		{
			return 29;
		}
		if (xp <= 38800)
		{
			return 30;
		}
		if (xp <= 41600)
		{
			return 31;
		}
		if (xp <= 44600)
		{
			return 32;
		}
		if (xp <= 48000)
		{
			return 33;
		}
		if (xp <= 51400)
		{
			return 34;
		}
		if (xp <= 55000)
		{
			return 35;
		}
		if (xp <= 58700)
		{
			return 36;
		}
		if (xp <= 62400)
		{
			return 37;
		}
		if (xp <= 66200)
		{
			return 38;
		}
		if (xp <= 70200)
		{
			return 39;
		}
		if (xp <= 74300)
		{
			return 40;
		}
		if (xp <= 78500)
		{
			return 41;
		}
		if (xp <= 82800)
		{
			return 42;
		}
		if (xp <= 87100)
		{
			return 43;
		}
		if (xp <= 91600)
		{
			return 44;
		}
		if (xp <= 96300)
		{
			return 45;
		}
		if (xp <= 101000)
		{
			return 46;
		}
		if (xp <= 105800)
		{
			return 47;
		}
		if (xp <= 110700)
		{
			return 48;
		}
		if (xp <= 115700)
		{
			return 49;
		}
		if (xp <= 120900)
		{
			return 50;
		}
		if (xp <= 126100)
		{
			return 51;
		}
		if (xp <= 131500)
		{
			return 52;
		}
		if (xp <= 137000)
		{
			return 53;
		}
		if (xp <= 142500)
		{
			return 54;
		}
		if (xp <= 148200)
		{
			return 55;
		}
		if (xp <= 154000)
		{
			return 56;
		}
		if (xp <= 159900)
		{
			return 57;
		}
		if (xp <= 165800)
		{
			return 58;
		}
		if (xp <= 172000)
		{
			return 59;
		}
		if (xp <= 290000 )
		{
			return 60;
		}
		if (xp <= 317000 )
		{
			return 61;
		}
		if (xp <= 349000 )
		{
			return 62;
		}
		if (xp <= 386000 )
		{
			return 63;
		}
		if (xp <= 428000 )
		{
			return 64;
		}
		if (xp <= 475000 )
		{
			return 65;
		}
		if (xp <= 527000 )
		{
			return 66;
		}
		if (xp <= 585000 )
		{
			return 67;
		}
		if (xp <= 648000 )
		{
			return 68;
		}
		if (xp <= 717000)
		{
			return 69;
		}
		if (xp <= 1523800)
		{
			return 70;
		}
		if (xp <= 1539000)
		{
			return 71;
		}
		if (xp <= 1555700)
		{
			return 72;
		}
		if (xp <= 1571800)
		{
			return 73;
		}
		if (xp <= 1587900)
		{
			return 74;
		}
		if (xp <= 1604200)
		{
			return 75;
		}
		if (xp <= 1620700)
		{
			return 76;
		}
		if (xp <= 1637400)
		{
			return 77;
		}
		if (xp <= 1653900)
		{
			return 78;
		}
		if (xp <= 1670800)
		{
			return 79;
		}
		if (xp <= 1686300)
		{
			return 80;
		}
		if (xp <= 2121500)
		{
			return 81;
		}
		if (xp <= 2669000)
		{
			return 82;
		}
		if (xp <= 3469000)
		{
			return 83;
		}
		
		
		return 1;
	}

	public int getExpFromLevel(int level)
	{
		if (level == 1)
		{
			return 400;
		}
		if (level == 2)
		{
			return 900;
		}
		if (level == 3)
		{
			return 1400;
		}
		if (level == 4)
		{
			return 2100;
		}
		if (level == 5)
		{
			return 2800;
		}
		if (level == 6)
		{
			return 3600;
		}
		if (level == 7)
		{
			return 4500;
		}
		if (level == 8)
		{
			return 5400;
		}
		if (level == 9)
		{
			return 6500;
		}
		if (level == 10)
		{
			return 7600;
		}
		if (level == 11)
		{
			return 8700;
		}
		if (level == 12)
		{
			return 9800;
		}
		if (level == 13)
		{
			return 11000;
		}
		if (level == 14)
		{
			return 12300;
		}
		if (level == 15)
		{
			return 13600;
		}
		if (level == 16)
		{
			return 15000;
		}
		if (level ==17 )
		{
			return 16400;
		}
		if (level ==18 )
		{
			return 17800;
		}
		if (level ==19 )
		{
			return 19300;
		}
		if (level ==20 )
		{
			return 20800;
		}
		if (level ==21 )
		{
			return 22400;
		}
		if (level ==22 )
		{
			return 24000;
		}
		if (level ==23 )
		{
			return 25500;
		}
		if (level ==24 )
		{
			return 27200;
		}
		if (level ==25 )
		{
			return 28900;
		}
		if (level ==26 )
		{
			return 30500;
		}
		if (level ==27 )
		{
			return 32200;
		}
		if (level ==28 )
		{
			return 33900;
		}
		if (level ==29 )
		{
			return 36300;
		}
		if (level ==30 )
		{
			return 38800;
		}
		if (level ==31 )
		{
			return 41600;
		}
		if (level ==32 )
		{
			return 44600;
		}
		if (level ==33 )
		{
			return 48000;
		}
		if (level ==34 )
		{
			return 51400;
		}
		if (level ==35 )
		{
			return 55000;
		}
		if (level ==36 )
		{
			return 58700;
		}
		if (level ==37 )
		{
			return 62400;
		}
		if (level ==38 )
		{
			return 66200;
		}
		if (level ==39 )
		{
			return 70200;
		}
		if (level ==40 )
		{
			return 74300;
		}
		if (level ==41 )
		{
			return 78500;
		}
		if (level ==42 )
		{
			return 82800;
		}
		if (level ==43 )
		{
			return 87100;
		}
		if (level ==44 )
		{
			return 91600;
		}
		if (level ==45 )
		{
			return 96300;
		}
		if (level ==46 )
		{
			return 101000;
		}
		if (level ==47 )
		{
			return 105800;
		}
		if (level ==48 )
		{
			return 110700;
		}
		if (level == 49 )
		{
			return 115700;
		}
		if (level == 50 )
		{
			return 120900;
		}
		if (level == 51 )
		{
			return 126100;
		}
		if (level == 52 )
		{
			return 131500;
		}
		if (level == 53 )
		{
			return 137000;
		}
		if (level == 54 )
		{
			return 142500;
		}
		if (level == 55 )
		{
			return 148200;
		}
		if (level == 56 )
		{
			return 154000;
		}
		if (level == 57 )
		{
			return 159900;
		}
		if (level == 58 )
		{
			return 165800;
		}
		if (level == 59 )
		{
			return 172000;
		}
		if (level == 60 )
		{
			return 290000 ;
		}
		if (level == 61 )
		{
			return 317000 ;
		}
		if (level == 62 )
		{
			return 349000 ;
		}
		if (level == 63 )
		{
			return 386000 ;
		}
		if (level == 64 )
		{
			return 428000 ;
		}
		if (level == 65 )
		{
			return 475000 ;
		}
		if (level == 66 )
		{
			return 527000 ;
		}
		if (level == 67 )
		{
			return 585000 ;
		}
		if (level == 68 )
		{
			return 648000 ;
		}
		if (level == 69 )
		{
			return 717000 ;
		}
		if (level == 70 )
		{
			return 1523800;
		}
		if (level == 71 )
		{
			return 1539000;
		}
		if (level == 72 )
		{
			return 1555700;
		}
		if (level == 73 )
		{
			return 1571800;
		}
		if (level == 74 )
		{
			return 1587900;
		}
		if (level == 75 )
		{
			return 1604200;
		}
		if (level == 76 )
		{
			return 1620700;
		}
		if (level == 77 )
		{
			return 1637400;
		}
		if (level == 78 )
		{
			return 1653900;
		}
		if (level == 79 )
		{
			return 1670800;
		}
		if (level == 80 )
		{
			return 1686300;
		}
		if (level == 81 )
		{
			return 2121500;
		}
		if (level == 82 )
		{
			return 2669000;
		}
		if (level == 83 )
		{
			return 3469000;
		}
		return 1;
	}

	public int getPlayerLevel(Player player)
	{
		PlayerCache p = this.getPlayerCacheByName(player.getName());
		if (p != null)
		{
			int xp = p.experience;

			return this.getLevelFromExp(xp);

		} else {
			return 1;
		}
	}
	
	public int setPlayerHPBonusMax(Player player, int amount)
	{
		PlayerCache p = this.getPlayerCacheByName(player.getName());
		if (p != null)
		{
			p.hitpointsmax = amount;
			return this.getPlayerHPBonusMax(player);

		} else {
			return 0;
		}
	}

	public int getPlayerHPBonusMax(Player player)
	{
		PlayerCache p = this.getPlayerCacheByName(player.getName());
		if (p != null)
		{
			return p.hitpointsmax;

		} else {
			return 0;
		}
	}

	public int setPlayerHPBonus(Player player, int amount)
	{
		PlayerCache p = this.getPlayerCacheByName(player.getName());
		if (p != null)
		{
			p.hitpoints = amount;
			return this.getPlayerHPBonus(player);

		} else {
			return 0;
		}
	}

	public int getPlayerHPBonus(Player player)
	{
		PlayerCache p = this.getPlayerCacheByName(player.getName());
		if (p != null)
		{
			return p.hitpoints;

		} else {
			return 0;
		}
	}

	public void setPlayerCombatExp(Player player, int amount, int changed, boolean groupbonus)
	{
		PlayerCache p = this.getPlayerCacheByName(player.getName());
		if (p != null)
		{
			// get total level currently
			int maxlevel = getLevelFromExp(getMaxExperience(player));
			int cl = getLevelFromExp(p.combatexperience);
			int rl = getLevelFromExp(p.rangedexperience);
			int sl = getLevelFromExp(p.scholarlyexperience);
			int nl = getLevelFromExp(p.naturalexperience);
			int level = cl+rl+sl+nl;
			
			if (level < maxlevel)
			{
				int oldlevel = getLevelFromExp(p.combatexperience);
				int newlevel = getLevelFromExp(amount);
				if (newlevel > oldlevel)
				{
					player.sendMessage("You gained a combat level!");
				}
				p.combatexperience = amount;
				
				// base exp
				// not as efficient instead use the player object we already have 
				// int oldlevel = this.getPlayerLevel(attacker);
				int oldbaselevel = getLevelFromExp(p.experience);
				if (p.experience < getMaxExperience(player))
				{

					int newbaseexperience = p.experience + changed;
					p.experience = newbaseexperience;
					if (groupbonus)
					{
						player.sendMessage(ChatColor.YELLOW + "* You gained group base experience! ("+changed+")");
					} else {
						player.sendMessage(ChatColor.YELLOW + "* You gained base experience! ("+changed+")");
					}
					
					// not as efficient, instead use the player object we already have
					//int newlevel = this.getPlayerLevel(attacker);
					int newbaselevel = getLevelFromExp(newbaseexperience);
					if (newbaselevel > oldbaselevel)
					{
						player.sendMessage(ChatColor.YELLOW + "* You gained a base level ("+newbaselevel+")!");
					}
				}
				
				
				if (groupbonus)
				{
					player.sendMessage(ChatColor.YELLOW + "* You gained group combat experience! ("+changed+")");
				} else {
					player.sendMessage(ChatColor.YELLOW + "* You gained combat experience! ("+changed+")");
				}

			} else {
				// base exp
				// not as efficient instead use the player object we already have 
				// int oldlevel = this.getPlayerLevel(attacker);
				int oldbaselevel = getLevelFromExp(p.experience);
				if (p.experience < getMaxExperience(player))
				{

					int newbaseexperience = p.experience + changed;
					p.experience = newbaseexperience;
					if (groupbonus)
					{
						player.sendMessage(ChatColor.YELLOW + "* You gained group base experience! ("+changed+")");
					} else {
						player.sendMessage(ChatColor.YELLOW + "* You gained base experience! ("+changed+")");
					}
					
					
					// not as efficient, instead use the player object we already have
					//int newlevel = this.getPlayerLevel(attacker);
					int newbaselevel = getLevelFromExp(newbaseexperience);
					if (newbaselevel > oldbaselevel)
					{
						player.sendMessage(ChatColor.YELLOW + "* You gained a base level ("+newbaselevel+")!");
					}
				}

			}
		}
	}

	public void setPlayerRangedExp(Player player, int amount, int changed, boolean groupbonus)
	{
		PlayerCache p = this.getPlayerCacheByName(player.getName());
		if (p != null)
		{
			// get total level currently
			int maxlevel = getLevelFromExp(getMaxExperience(player));
			int cl = getLevelFromExp(p.combatexperience);
			int rl = getLevelFromExp(p.rangedexperience);
			int sl = getLevelFromExp(p.scholarlyexperience);
			int nl = getLevelFromExp(p.naturalexperience);
			int level = cl+rl+sl+nl;
			
			if (level < maxlevel)			
			{
				int oldlevel = getLevelFromExp(p.rangedexperience);
				int newlevel = getLevelFromExp(amount);
				if (newlevel > oldlevel)
				{
					player.sendMessage("You gained a ranged level!");
				}

				p.rangedexperience = amount;
				// base exp
				// not as efficient instead use the player object we already have 
				// int oldlevel = this.getPlayerLevel(attacker);
				int oldbaselevel = getLevelFromExp(p.experience);
				if (p.experience < getMaxExperience(player))
				{

					int newbaseexperience = p.experience + changed;
					p.experience = newbaseexperience;
					if (groupbonus)
					{
						player.sendMessage(ChatColor.YELLOW + "* You gained group base experience! ("+changed+")");
						
					} else {
						player.sendMessage(ChatColor.YELLOW + "* You gained base experience! ("+changed+")");
						
					}
					
					// not as efficient, instead use the player object we already have
					//int newlevel = this.getPlayerLevel(attacker);
					int newbaselevel = getLevelFromExp(newbaseexperience);
					if (newbaselevel > oldbaselevel)
					{
						player.sendMessage(ChatColor.YELLOW + "* You gained a base level ("+newbaselevel+")!");
					}
				}
				if (groupbonus)
				{
					player.sendMessage(ChatColor.YELLOW + "* You gained group ranged experience! ("+changed+")");
				} else {
					player.sendMessage(ChatColor.YELLOW + "* You gained ranged experience! ("+changed+")");					
				}

			} else {
				// base exp
				// not as efficient instead use the player object we already have 
				// int oldlevel = this.getPlayerLevel(attacker);
				int oldbaselevel = getLevelFromExp(p.experience);
				if (p.experience < getMaxExperience(player))
				{

					int newbaseexperience = p.experience + changed;
					p.experience = newbaseexperience;
					if (groupbonus)
					{
						player.sendMessage(ChatColor.YELLOW + "* You gained group base experience! ("+changed+")");
					} else {
						player.sendMessage(ChatColor.YELLOW + "* You gained base experience! ("+changed+")");						
					}
					
					// not as efficient, instead use the player object we already have
					//int newlevel = this.getPlayerLevel(attacker);
					int newbaselevel = getLevelFromExp(newbaseexperience);
					if (newbaselevel > oldbaselevel)
					{
						player.sendMessage(ChatColor.YELLOW + "* You gained a base level ("+newbaselevel+")!");
					}
				}
			}
		}
	}
	public void setPlayerScholarlyExp(Player player, int amount, int changed, boolean groupbonus)
	{
		PlayerCache p = this.getPlayerCacheByName(player.getName());
		if (p != null)
		{
			// get total level currently
			int maxlevel = getLevelFromExp(getMaxExperience(player));
			int cl = getLevelFromExp(p.combatexperience);
			int rl = getLevelFromExp(p.rangedexperience);
			int sl = getLevelFromExp(p.scholarlyexperience);
			int nl = getLevelFromExp(p.naturalexperience);
			int level = cl+rl+sl+nl;
						
			if (level < maxlevel)
			{
				int oldlevel = getLevelFromExp(p.scholarlyexperience);
				int newlevel = getLevelFromExp(amount);
				if (newlevel > oldlevel)
				{
					player.sendMessage("You gained a scholarly magic level!");
				}			  

				p.scholarlyexperience = amount;
				// base exp
				// not as efficient instead use the player object we already have 
				// int oldlevel = this.getPlayerLevel(attacker);
				int oldbaselevel = getLevelFromExp(p.experience);
				if (p.experience < getMaxExperience(player))
				{

					int newbaseexperience = p.experience + changed;
					p.experience = newbaseexperience;
					if (groupbonus)
					{
						player.sendMessage(ChatColor.YELLOW + "* You gained group base experience! ("+changed+")");
					} else {
						player.sendMessage(ChatColor.YELLOW + "* You gained base experience! ("+changed+")");
					}
					
					// not as efficient, instead use the player object we already have
					//int newlevel = this.getPlayerLevel(attacker);
					int newbaselevel = getLevelFromExp(newbaseexperience);
					if (newbaselevel > oldbaselevel)
					{
						player.sendMessage(ChatColor.YELLOW + "* You gained a base level ("+newbaselevel+")!");
					}
				}
				
				if (groupbonus)
				{
					player.sendMessage(ChatColor.YELLOW + "* You gained group scholarly magic experience! ("+changed+")");	
				} else {
					player.sendMessage(ChatColor.YELLOW + "* You gained scholarly magic experience! ("+changed+")");					
				}
			} else {
				// base exp
				// not as efficient instead use the player object we already have 
				// int oldlevel = this.getPlayerLevel(attacker);
				int oldbaselevel = getLevelFromExp(p.experience);
				if (p.experience < getMaxExperience(player))
				{

					int newbaseexperience = p.experience + changed;
					p.experience = newbaseexperience;
					if (groupbonus)
					{
						player.sendMessage(ChatColor.YELLOW + "* You gained group base experience! ("+changed+")");						
					} else {
						player.sendMessage(ChatColor.YELLOW + "* You gained base experience! ("+changed+")");
						
					}
					
					// not as efficient, instead use the player object we already have
					//int newlevel = this.getPlayerLevel(attacker);
					int newbaselevel = getLevelFromExp(newbaseexperience);
					if (newbaselevel > oldbaselevel)
					{
						player.sendMessage(ChatColor.YELLOW + "* You gained a base level ("+newbaselevel+")!");
					}
				};
			}
		}
	}
	
	public void setPlayerNaturalExp(Player player, int amount, int changed, boolean groupbonus)
	{
		PlayerCache p = this.getPlayerCacheByName(player.getName());
		if (p != null)
		{
			// get total level currently
			int maxlevel = getLevelFromExp(getMaxExperience(player));
			int cl = getLevelFromExp(p.combatexperience);
			int rl = getLevelFromExp(p.rangedexperience);
			int sl = getLevelFromExp(p.scholarlyexperience);
			int nl = getLevelFromExp(p.naturalexperience);
			int level = cl+rl+sl+nl;
						
			if (level < maxlevel)
			
			{
				int oldlevel = getLevelFromExp(p.naturalexperience);
				int newlevel = getLevelFromExp(amount);
				if (newlevel > oldlevel)
				{
					player.sendMessage("You gained a natural magic level!");
				}	

				p.naturalexperience = amount;
				// base exp
				// not as efficient instead use the player object we already have 
				// int oldlevel = this.getPlayerLevel(attacker);
				int oldbaselevel = getLevelFromExp(p.experience);
				if (p.experience < getMaxExperience(player))
				{

					int newbaseexperience = p.experience + changed;
					p.experience = newbaseexperience;
					player.sendMessage(ChatColor.YELLOW + "* You gained base experience! ("+changed+")");
					
					// not as efficient, instead use the player object we already have
					//int newlevel = this.getPlayerLevel(attacker);
					int newbaselevel = getLevelFromExp(newbaseexperience);
					if (newbaselevel > oldbaselevel)
					{
						player.sendMessage(ChatColor.YELLOW + "* You gained a base level ("+newbaselevel+")!");
					}
				}
				player.sendMessage(ChatColor.YELLOW + "* You gained Natural/Divine magic experience! ("+changed+")");
			} else {
				p.naturalexperience = getMaxExperience(player);
				// base exp
				// not as efficient instead use the player object we already have 
				// int oldlevel = this.getPlayerLevel(attacker);
				int oldbaselevel = getLevelFromExp(p.experience);
				if (p.experience < getMaxExperience(player))
				{

					int newbaseexperience = p.experience + changed;
					p.experience = newbaseexperience;
					player.sendMessage(ChatColor.YELLOW + "* You gained base experience! ("+changed+")");
					
					// not as efficient, instead use the player object we already have
					//int newlevel = this.getPlayerLevel(attacker);
					int newbaselevel = getLevelFromExp(newbaseexperience);
					if (newbaselevel > oldbaselevel)
					{
						player.sendMessage(ChatColor.YELLOW + "* You gained a base level ("+newbaselevel+")!");
					}
				}
			}
		}
	}
	public int getPlayerCombatExp(Player player)
	{
		PlayerCache p = this.getPlayerCacheByName(player.getName());
		if (p != null)
		{
			return p.combatexperience;
		}
		return 0;
	}

	public int getPlayerRangedExp(Player player)
	{
		PlayerCache p = this.getPlayerCacheByName(player.getName());
		if (p != null)
		{
			return p.rangedexperience;
		}
		return 0;
	}

	public int getPlayerScholarlyExp(Player player)
	{
		PlayerCache p = this.getPlayerCacheByName(player.getName());
		if (p != null)
		{
			return p.scholarlyexperience;
		}
		return 0;
	}

	public int getPlayerNaturalExp(Player player)
	{
		PlayerCache p = this.getPlayerCacheByName(player.getName());
		if (p != null)
		{
			return p.naturalexperience;
		}
		return 0;
	}

	

	public int getPlayerCombatLevel(Player player)
	{
		PlayerCache p = this.getPlayerCacheByName(player.getName());
		if (p != null)
		{
			int xp = p.combatexperience;

			return this.getLevelFromExp(xp);

		} else {
			return 1;
		}
	}

	public int getPlayerRangedLevel(Player player)
	{
		PlayerCache p = this.getPlayerCacheByName(player.getName());
		if (p != null)
		{
			int xp = p.rangedexperience;

			return this.getLevelFromExp(xp);

		} else {
			return 1;
		}
	}

	public int getScholarlyMagicLevel(Player player)
	{
		PlayerCache p = this.getPlayerCacheByName(player.getName());
		if (p != null)
		{
			int xp = p.scholarlyexperience;

			return this.getLevelFromExp(xp);

		} else {
			return 1;
		}
	}
	public int getNaturalMagicLevel(Player player)
	{
		PlayerCache p = this.getPlayerCacheByName(player.getName());
		if (p != null)
		{
			int xp = p.naturalexperience;

			return this.getLevelFromExp(xp);

		} else {
			return 1;
		}
	} 

	public int getPlayerPower(Player player)
	{
		PlayerCache p = this.getPlayerCacheByName(player.getName());
		if (p != null)
		{
			int power = p.power;

			return power;

		} else {
			return 0;
		}
	}

	public boolean isMuted(Player player)
	{
		if (this.isEssentials)
		{
			return this.essentials.getUser(player).isMuted();
		}

		return false;
	}

	public void onDisable()
	{

		System.out.println("[RPChat] Committing all player data");
        commitPlayerCacheAll();

		PluginDescriptionFile desc = getDescription();
		System.out.println(desc.getFullName() + " has been disabled");
	}

	private Boolean setupPermissions()
	{
		RegisteredServiceProvider permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
		if (permissionProvider != null) {
			permission = (Permission)permissionProvider.getProvider();
		}
		if (permission != null) return Boolean.valueOf(true); return Boolean.valueOf(false);
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	public String getAllianceNameShorthand(String race)
	{
		if (race.equals("light")) { return "light"; } 
		if (race.equals("neutral")) { return "neutral"; } 
		if (race.equals("dark")) { return "dark"; } 
		return "unk";
	}

	public List<String> getAllianceNames()
	{
		List<String> alliances = new ArrayList<String>();

		alliances.add("light");
		alliances.add("neutral");
		alliances.add("dark");
		return alliances;
	}

	public boolean hasPerm(Player player, String string)
	{
		return permission.has(player, string);
	}

	public String getPermissionGroups(Player player)
	{
		if (player.getName().toLowerCase().equals("mixxit".toLowerCase()))
		{
			if (player.isOp())
			{
				return ChatColor.LIGHT_PURPLE + "SuperDev" + ChatColor.WHITE;
			}
		}

		if (hasPerm(player, "permissions.tag.emperor"))
		{
			return ChatColor.DARK_PURPLE + "Emperor" + ChatColor.WHITE;
		}

		if (hasPerm(player, "permissions.tag.dev"))
		{
			return ChatColor.LIGHT_PURPLE + "Dev" + ChatColor.WHITE;
		}

		if (hasPerm(player, "permissions.tag.general"))
		{
			return ChatColor.DARK_RED + "General" + ChatColor.WHITE;
		}

		if (hasPerm(player, "permissions.tag.colonel"))
		{
			return ChatColor.RED + "Colonel" + ChatColor.WHITE;
		}

		if (hasPerm(player, "permissions.tag.captain"))
		{
			return ChatColor.DARK_GREEN + "Captain" + ChatColor.WHITE;
		}

		if (hasPerm(player, "permissions.tag.sergeant"))
		{
			return ChatColor.GREEN + "Sergeant" + ChatColor.WHITE;
		}

		if (hasPerm(player, "permissions.tag.goliath"))
		{
			return ChatColor.BLACK + "Goliath" + ChatColor.WHITE;
		}

		if (hasPerm(player, "permissions.tag.immortal"))
		{
			return ChatColor.GOLD + "Immortal" + ChatColor.WHITE;
		}

		if (hasPerm(player, "permissions.tag.elite"))
		{
			return ChatColor.DARK_BLUE + "Elite" + ChatColor.WHITE;
		}

		if (hasPerm(player, "permissions.tag.prestige"))
		{
			return ChatColor.BLUE + "Prestige" + ChatColor.WHITE;
		}

		if (hasPerm(player, "permissions.tag.premium"))
		{
			return ChatColor.AQUA + "Premium" + ChatColor.WHITE;
		}

		if (hasPerm(player, "permissions.tag.juggernaut"))
		{
			return ChatColor.BLUE + "Juggernaut" + ChatColor.WHITE;
		}

		if (hasPerm(player, "permissions.tag.lord"))
		{
			return ChatColor.DARK_GRAY + "Lord" + ChatColor.WHITE;
		}

		if (hasPerm(player, "permissions.tag.citizen"))
		{
			return ChatColor.GRAY + "Citizen" + ChatColor.WHITE;
		}

		if (hasPerm(player, "permissions.tag.nomad"))
		{
			return ChatColor.WHITE + "Nomad" + ChatColor.WHITE;
		}

		return "Nomad";
	}

	public String formatString(String string) {
		String s = string;
		for (ChatColor color : ChatColor.values()) {
			s = s.replaceAll("(&([a-f0-9]))", "§$2");
		}
		return s;
	}

	public void onEnable()
	{
		PluginDescriptionFile desc = getDescription();
		System.out.println(desc.getFullName() + " has been enabled");

		this.pm = getServer().getPluginManager();

		checkPlugins();
		setupPermissions();
		setupDatabase();

		PluginManager pm = getServer().getPluginManager();
		
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			   public void run() {
				   commitPlayerCacheAll();
			   }
			}, 2000L, 3000L);
			

		if (this.towny == null)
		{
			this.pm.disablePlugin(this);
		}

		getCommand("local").setExecutor(new Message("local",this));
		getCommand("racechat").setExecutor(new Message("race",this));
		getCommand("race").setExecutor(new SetRace(this));
		getCommand("name").setExecutor(new SetName(this));
		getCommand("lastname").setExecutor(new SetLastName(this));
		getCommand("findname").setExecutor(new FindName(this));
		getCommand("setelection").setExecutor(new SetElectionCmd(this));
		getCommand("overlevel").setExecutor(new OverLevelCmd(this));
		getCommand("group").setExecutor(new GroupCmd(this));
		getCommand("groupchat").setExecutor(new GroupChatCmd(this));
		getCommand("setlanguage").setExecutor(new SetLanguage(this));
		getCommand("gender").setExecutor(new SetGender(this));
		getCommand("getstats").setExecutor(new GetStatsCmd(this));
		getCommand("infection").setExecutor(new GetInfectionCmd(this));
		getCommand("random").setExecutor(new RandomCmd(this));
		getCommand("meditate").setExecutor(new MeditateCmd(this));
		getCommand("defaultchannel").setExecutor(new SetDefaultChannel(this));
		getCommand("capital").setExecutor(new GotoCapital(this));
		getCommand("alliancechat").setExecutor(new Message("alliance",this));
		getCommand("title").setExecutor(new SetTitle(this));
		getCommand("emote").setExecutor(new EmoteMessage(this));
		getCommand("alliance").setExecutor(new AllianceCmd(this));
		getCommand("promote").setExecutor(new PromoteCmd(this));
		getCommand("reloadquests").setExecutor(new ReloadQuests(this));
		getCommand("demote").setExecutor(new DemoteCmd(this));
		getCommand("nationchat").setExecutor(new Message("nation",this));
		getCommand("townchat").setExecutor(new Message("town",this));
		getCommand("reset").setExecutor(new ResetCmd(this));
		getCommand("who").setExecutor(new Who(this));
		registerEvents();
		loadQuests();

	}


	public String getSectorName(Location location)
	{
		String World = location.getWorld().getName();
		String ChunkX = Integer.toString(location.getChunk().getX());
		String ChunkZ = Integer.toString(location.getChunk().getZ());
		return World + ":" + ChunkX + "," + ChunkZ;
	}

	public void registerEvents()
	{
		this.rpchatPlayerListener = new RPchatPlayerListener(this);
		this.rpchatEntityListener = new RPchatEntityListener(this);
		getServer().getPluginManager().registerEvents(this.rpchatPlayerListener, this);
		getServer().getPluginManager().registerEvents(this.rpchatEntityListener, this);

	}

	public void sendMessageToAll(String message)
	{
		for (World w : getServer().getWorlds())
		{
			for (Player p : w.getPlayers())
			{
				p.sendMessage(message);
			}
		}
	}

	public static String capitalizeFirstLetters(String s)
	{
		for (int i = 0; i < s.length(); i++)
		{
			if (i == 0)
			{
				s = String.format("%s%s", new Object[] { 
						Character.valueOf(Character.toUpperCase(s.charAt(0))), 
						s.substring(1) });
			}

			if ((Character.isLetterOrDigit(s.charAt(i))) || 
					(i + 1 >= s.length())) continue;
			s = String.format("%s%s%s", new Object[] { 
					s.subSequence(0, i + 1), 
					Character.valueOf(Character.toUpperCase(s.charAt(i + 1))), 
					s.substring(i + 2) });
		}

		return s;
	}

	protected Towny getTowny()
	{
		return this.towny;
	}
	


	boolean onlyletters(String string) {
		return string.matches("^[a-zA-Z]+$");
	}
	boolean onlylettersorspace(String string) {
		return string.matches("^[a-zA-Z\\s]+$");
	}

	private void checkPlugins() {
		Plugin testessentials = getServer().getPluginManager().getPlugin("Essentials");
		if (testessentials == null)
		{
			this.isEssentials = false;
		} else {
			this.isEssentials = true;
			this.essentials = ((Essentials)testessentials);
		}

		Plugin test = this.pm.getPlugin("Towny");
		if ((test != null) && ((test instanceof Towny)))
			this.towny = ((Towny)test);
		
		try
		{
			Plugin sen = this.pm.getPlugin("Sentry");
			if ((sen != null) && ((sen instanceof Sentry)))
				this.sentry = ((Sentry)sen);
		} catch (Exception e ) {
			this.sentry = null;
		}
	}

	public String capitalise(String string)
	{
		if (string == null)
		{
			return "";
		}
		if (string.equals(""))
		{
			return "";
		}
		return Character.toUpperCase(string.charAt(0)) + string.substring(1);
	}

	
	public String getPlayerAlliance(Player player)
	{
		try
		{
			PlayerCache sPlayer = this.getPlayerCacheByName(player.getName());
			if (sPlayer == null) {
				return "Unknown";
			}
			return sPlayer.alliance;
		}
		catch (Exception e)
		{
			//System.out.println("[rpchat] Exception: " + e.getMessage());
		}return "Unknown";
	}

	public String getAllianceFlags(String alliance)
	{
		try
		{
			sqlAlliances sAlliance = (sqlAlliances)getDatabase().find(sqlAlliances.class).where().ieq("name", alliance).findUnique();
			if (sAlliance == null) {
				return "";
			}
			return sAlliance.getFlags();
		}
		catch (Exception e)
		{
			//System.out.println("[rpchat] Exception: " + e.getMessage());
		}
		return "";
	}

	public void setAllianceFlags(String alliance, String flags)
	{
		try
		{
			sqlAlliances sAlliance = (sqlAlliances)getDatabase().find(sqlAlliances.class).where().ieq("name", alliance).findUnique();
			if (sAlliance == null) {

			} else {
				sAlliance.setFlags(flags);
				this.getDatabase().save(sAlliance);
			}
		}
		catch (Exception e)
		{
			//System.out.println("[rpchat] Exception: " + e.getMessage());
		}
	}

	public String getPlayerRace(Player player)
	{
		try
		{
			PlayerCache sPlayer = this.getPlayerCacheByName(player.getName());
			if (sPlayer == null) {
				return "Unknown";
			}
			return sPlayer.race;
		}
		catch (Exception e)
		{
			//System.out.println("[rpchat] Exception: " + e.getMessage());
		}return "Unknown";
	}

	private void setupDatabase()
	{
		try
		{
			getDatabase().find(sqlPlayer.class).findRowCount();
			getDatabase().find(sqlRaces.class).findRowCount();
			getDatabase().find(sqlAlliances.class).findRowCount();
		}
		catch (PersistenceException ex) {
			System.out.println("Installing database for " + getDescription().getName() + " due to first time usage");
			removeDDL();
			installDDL();
		}
	}

	public List<Class<?>> getDatabaseClasses()
	{
		List list = new ArrayList();
		list.add(sqlPlayer.class);
		list.add(sqlRaces.class);
		list.add(sqlAlliances.class);

		return list;
	}

	public void sendMessageToRace(String race, String message)
	{
		for (World w : getServer().getWorlds())
		{
			for (Player p : w.getPlayers())
			{
				if (!getPlayerRace(p).equals(race))
					continue;
				p.sendMessage(message);
			}
		}
	}

	public void sendMessageToAlliance(String alliance, String message)
	{
		for (World w : getServer().getWorlds())
		{
			for (Player p : w.getPlayers())
			{
				if (!getPlayerAlliance(p).equals(alliance))
					continue;
				p.sendMessage(message);
			}
		}
	}


	public static String encode(long number) {
		StringBuilder b = new StringBuilder();
		assert (number > 0L);
		do {
			long div = number / 26L;
			int mod = (int)(number % 26L);
			b.append("abcdefghijklmnopqrstuvwxyz".charAt(mod));
			number = div;
		}while (number != 0L);
		return b.toString();
	}
	static int error(String s) {
		throw new RuntimeException(s);
	}
	public static long decode(String string) {
		int l = string.length();
		long answer = 0L;
		long mul = 1L;
		for (int i = 0; i < l; i++) {
			int val = string.charAt(i);
			int num = (val >= 65) && (val <= 90) ? val - 65 : (val >= 97) && (val <= 122) ? val - 97 : error("bad format");
			answer += num * mul; mul *= 26L;
		}
		return answer;
	}

	public void sendMessageToAllianceExcept(Player victim, String victimalliance, String string)
	{
		for (World w : getServer().getWorlds())
		{
			for (Player p : w.getPlayers())
			{
				if (p.equals(victim))
					continue;
				if (!getPlayerAlliance(p).equals(victimalliance))
					continue;
				p.sendMessage(string);
			}
		}
	}

	public void sendMessageToAllExcept(Player victim, String string)
	{
		for (World w : getServer().getWorlds())
		{
			for (Player p : w.getPlayers())
			{
				if (p.equals(victim))
					continue;
				p.sendMessage(string);
			}
		}
	}

	public int getVoteCount(String name)
	{
		List<sqlPlayer> players = getDatabase().find(sqlPlayer.class).where().ieq("vote", name).findList();
		int count = 0;
		for (sqlPlayer p : players){
			count++;
		}
		return count;
	}
	
	public int getPlayersOverCap(Player player)
	{
		List<sqlPlayer> players = getDatabase().find(sqlPlayer.class).findList();
		int count = 0;
		for (sqlPlayer p : players){
			int cl = this.getLevelFromExp(p.getCombatexperience());
			int rl = this.getLevelFromExp(p.getRangedexperience());
			int sl = this.getLevelFromExp(p.getScholarlyexperience());
			int nl = this.getLevelFromExp(p.getNaturalexperience());
			int total = cl+rl+sl+nl;
			if (total > this.getLevelFromExp(getMaxExperience(player)))
			{
				player.sendMessage(p.getName() + "TOTAL: " + total + " C:"+cl+" R:"+rl+" S:"+sl+" N: "+nl);
			}
		}
		return count;
	}

	public static int safeLongToInt(long l) {
		// return at most the largest value of INT
		return (int) Math.min(Integer.MAX_VALUE, l);
	}

	public int getFreeInventorySlotsCount(Player player)
	{
		int count = 0;
		Inventory inventory = player.getInventory();
		for (ItemStack i : inventory.getContents())
		{
			if (i == null)
			{
				count++;
			}
		}
		return count;
	}

	public boolean isKing(Player player) {

		if (player == null)
		{
			return false;
		}
		PlayerCache sPlayerme = this.getPlayerCacheByName(player.getName());
		if (sPlayerme == null) {
			return false;
		}

		if (sPlayerme.election == 2)
		{
			return true;


		}

		return false;
	}

	public boolean isKingByName(String playername) {


		PlayerCache sPlayerme = this.getPlayerCacheByName(playername);
		if (sPlayerme == null) {
			return false;
		}

		if (sPlayerme.election == 2)
		{
			return true;


		}

		return false;
	}

	public String getPlayerTitle(Player player) {
		PlayerCache sPlayerme = this.getPlayerCacheByName(player.getName());
		if (sPlayerme == null) {
			return "";		
		} else {
			if (sPlayerme.title.equals(""))
			{
				return "";
			} else {
				return (sPlayerme.title);			
			}
		}
	}

	public boolean isOnline(String player)
	{
		for (World w : this.getServer().getWorlds())
		{
			for (Player p : w.getPlayers())
			{
				if (p.getName().equals(player))
				{
					return true;
				}
			}
		}
		return false;

	}

	public void setPlayerTitle(Player fromplayer,Player targetplayer, String title) {


		if (title.length() > 32)
		{
			fromplayer.sendMessage("You cannot set a title that is longer than 32 characters");
			return;
		}

		if (!onlylettersorspace(title))
		{
			fromplayer.sendMessage("A title can only contain letters and spaces");
			return;
		}

		if (fromplayer.isOp() || this.hasPerm(fromplayer, "rpchat.admin"))
		{
			PlayerCache sPlayerthem = this.getPlayerCacheByName(targetplayer.getName());
			if (sPlayerthem == null) {
				fromplayer.sendMessage("You cannot grant a title when your targets account is being updated.");
				return;
			} else {
				if (title.equals("clear"))
				{
					sPlayerthem.title = "";
					fromplayer.sendMessage("Title cleared for player");
					sPlayerthem.decoration = this.getDecoration(sPlayerthem);
					return;
				} else {						
					sPlayerthem.title = title;
					fromplayer.sendMessage("Title set for player");
					sPlayerthem.decoration = this.getDecoration(sPlayerthem);
					return;
				}
			}
		}


		PlayerCache sPlayerme = this.getPlayerCacheByName(fromplayer.getName());
		if (sPlayerme == null) {
			fromplayer.sendMessage("You cannot grant a title when your account is being updated.");
			return;
		} else {
			if (isKing(fromplayer))
			{
				PlayerCache sPlayerthem = this.getPlayerCacheByName(targetplayer.getName());
				if (sPlayerthem == null) {
					fromplayer.sendMessage("You cannot grant a title when your targets account is being updated.");
					return;
				} else {
					if (sPlayerme.alliance.equals(sPlayerthem.alliance))
					{
						if (title.equals("clear"))
						{
							sPlayerthem.title = "";
							fromplayer.sendMessage("Title cleared for player");
							sPlayerthem.decoration = this.getDecoration(sPlayerthem);
							return;
						} else {						
							sPlayerthem.title = title;
							fromplayer.sendMessage("Title set for player");
							sPlayerthem.decoration = this.getDecoration(sPlayerthem);
							return;
						}
					} else {
						fromplayer.sendMessage("You cannot set a title of a player who is not your race.");
						return;
					}
				}
			} else {
				if (title.equals("clear"))
				{
					if (fromplayer.equals(targetplayer))
					{
						PlayerCache sPlayerthem = this.getPlayerCacheByName(targetplayer.getName());
						if (sPlayerthem == null) {
							fromplayer.sendMessage("You cannot grant a title when your targets account is being updated.");
							return;
						}

						sPlayerthem.title = "";
						fromplayer.sendMessage("Title cleared for player");
						sPlayerthem.decoration = this.getDecoration(sPlayerthem);
						return;
					} else {
						fromplayer.sendMessage("Only kings can set other peoples titles.");
						return;
					}
				}
				fromplayer.sendMessage("Only a king can set a title.");
				return;
			}
		}
	}

	public boolean isIgnored(Player ignoredby, Player player) {
		// TODO Auto-generated method stub
		return this.essentials.getUser(player).isIgnoredPlayer(ignoredby.getName());
	}

	


	public sqlPlayer getPlayerObject(Player player) {
		// TODO Auto-generated method stub
		sqlPlayer sPlayer = (sqlPlayer)getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();



		return sPlayer;
	}

	public sqlPlayer getPlayerObjectByName(String playername) {
		// TODO Auto-generated method stub
		sqlPlayer sPlayer = (sqlPlayer)getDatabase().find(sqlPlayer.class).where().ieq("name", playername).findUnique();
		return sPlayer;
	}


	public void giveMoney(Player player, int money) {
		// TODO Auto-generated method stub
		this.essentials.getOfflineUser(player.getName()).giveMoney(Double.parseDouble(Integer.toString(money)));
		player.sendMessage("You receive: " + money);

	}

	public void teleport(Player player, Location loc) {
		// TODO Auto-generated method stub
		try
		{
			this.essentials.getOfflineUser(player.getName()).getTeleport().teleport(loc, null);
		} catch (Exception e)
		{

		}
	}


	public int getPlayerExperience(Player player) {
		// TODO Auto-generated method stub
		PlayerCache sPlayerme = this.getPlayerCacheByName(player.getName());
		if (sPlayerme == null) {
			return 0;
		} 

		return sPlayerme.experience;
	}

	public void setPlayerPower(Player player, int newpower) {
		// TODO Auto-generated method stub
		PlayerCache s = this.getPlayerCacheByName(player.getName());
		if (s != null)
		{
			s.power = newpower;
		} 
	}

	public void setPlayerPowerMeditate(Player player, int newpower) {
		// TODO Auto-generated method stub
		PlayerCache s = this.getPlayerCacheByName(player.getName());
		if (s != null)
		{	
			s.power = newpower;
			player.sendMessage("Your mind relaxes as your power increases ("+this.getPlayerPower(player)+"/100)");
		} else {
			player.sendMessage("You cannot meditate when your character is being updated");
		}
	}

	public void setLastMeditateAsNow(Player player) {
		// TODO Auto-generated method stub
		PlayerCache s = this.getPlayerCacheByName(player.getName());
		if (s != null)
		{
			Date now = new Date();
			String timenow = Long.toString(now.getTime());

			s.lastmeditate = timenow;

		} else {
			//System.out.println("[RPChat] ERROR: Could not find player to set last meditate time");
		}
	}

	public int getPlayerTimeSinceLastMeditate(Player player)
	{
		PlayerCache s = this.getPlayerCacheByName(player.getName());
		if (s != null)
		{
			if (s.lastmeditate.equals(""))
			{
				return 30;
			} else {
				Date now = new Date();
				String timenow = Long.toString(now.getTime());
				Date then = new Date(Long.parseLong(s.lastmeditate));
				// elapsed miliseconds
				Long elapsed = now.getTime() - then.getTime();
				// every minute it will generated 0.1 gold nuggets
				Long secondssincelast = (long)(elapsed / 1000   );
				return safeLongToInt(secondssincelast);
			}
		} else {
			return 30;
		}
	}

	public boolean isPlayerAbleToFullMeditate(Player player) {
		// TODO Auto-generated method stub
		if (getPlayerTimeSinceLastMeditate(player) >= 120)
		{
			return true;
		} else {
			return false;
		}

	}

	public boolean isPlayerAbleTo3qMeditate(Player player) {
		// TODO Auto-generated method stub
		if (getPlayerTimeSinceLastMeditate(player) >= 90)
		{
			return true;
		} else {
			return false;
		}

	}

	public boolean isPlayerAbleTo2qMeditate(Player player) {
		// TODO Auto-generated method stub
		if (getPlayerTimeSinceLastMeditate(player) >= 60)
		{
			return true;
		} else {
			return false;
		}

	}

	public boolean isPlayerAbleToMeditate(Player player) {
		// TODO Auto-generated method stub
		if (getPlayerTimeSinceLastMeditate(player) >= 30)
		{
			return true;
		} else {
			return false;
		}

	}

	public boolean playerOptedIn(Player player) {
		// TODO Auto-generated method stub
		PlayerCache splayer = this.getPlayerCacheByName(player.getName());
		if (splayer != null)
		{
			if (splayer.optedin.equals("true"))
			{
				return true;
			}
		}

		return false;
	}

	public String getPlayerGender(Player player) {
		// TODO Auto-generated method stub
		PlayerCache splayer = this.getPlayerCacheByName(player.getName());
		if (splayer != null)
		{
			if (splayer.gender.equals("female"))
			{
				return "female";
			}
		}
		// has no gender set, assume male
		return "male";
	}

	public String getPlayerGenderByName(String playername) {
		// TODO Auto-generated method stub
		PlayerCache splayer = this.getPlayerCacheByName(playername);
		if (splayer != null)
		{
			if (splayer.gender.equals("female"))
			{
				return "female";
			}
		}
		// has no gender set, assume male
		return "male";
	}

	public String getRaceAlliance(String racename) {
		// TODO Auto-generated method stub
		String alliance = "unknown";
		if (racename.toString().toLowerCase().equals("human")) {
			alliance = "neutral";
		}
		if (racename.toString().toLowerCase().equals("fairy")) {
			alliance = "light";
		}
		
		if (racename.toString().toLowerCase().equals("highelf")) {
			alliance = "light";
		}
		if (racename.toString().toLowerCase().equals("woodelf")) {
			alliance = "light";
		}
		if (racename.toString().toLowerCase().equals("halfelf")) {
			alliance = "light";
		}
		if (racename.toString().toLowerCase().equals("darkelf")) {
			alliance = "dark";
		}
		if (racename.toString().toLowerCase().equals("vampire")) {
			alliance = "dark";
		}
		if (racename.toString().toLowerCase().equals("barbarian")) {
			alliance = "neutral";
		}
		if (racename.toString().toLowerCase().equals("orc")) {
			alliance = "dark";
		}
		if (racename.toString().toLowerCase().equals("ogre")) {
			alliance = "dark";
		}
		if (racename.toString().toLowerCase().equals("troll")) {
			alliance = "dark";
		}
		if (racename.toString().toLowerCase().equals("halfdragon")) {
			alliance = "neutral";
		}
		if (racename.toString().toLowerCase().equals("gnome")) {
			alliance = "neutral";
		}
		if (racename.toString().toLowerCase().equals("goblin")) {
			alliance = "dark";
		}
		if (racename.toString().toLowerCase().equals("hobbit")) {
			alliance = "neutral";
		}
		if (racename.toString().toLowerCase().equals("highhuman")) {
			alliance = "neutral";
		}
		if (racename.toString().toLowerCase().equals("undead")) {
			alliance = "dark";
		}
		if (racename.toString().toLowerCase().equals("dwarf")) {
			alliance = "neutral";
		}
		if (racename.toString().toLowerCase().equals("ratman")) {
			alliance = "dark";
		}
		if (racename.toString().toLowerCase().equals("lizardman")) {
			alliance = "neutral";
		}
		if (racename.toString().toLowerCase().equals("elemental")) {
			alliance = "dark";
		}
		if (racename.toString().toLowerCase().equals("kobold")) {
			alliance = "dark";
		}
		if (racename.toString().toLowerCase().equals("angel")) {
			alliance = "light";
		}
		if (racename.toString().toLowerCase().equals("fallenangel")) {
			alliance = "dark";
		}

		if (racename.toString().toLowerCase().equals("clockwork")) {
			alliance = "neutral";
		}

		return alliance;
	}
	
	public void commitPlayerCacheAll()
	{
		int count = 0;
		for (Entry<String, PlayerCache> entry : this.playerdata.entrySet())
		{
			count++;
			this.commitPlayerCache(entry.getKey());
		}
		System.out.println("[RPChat] Committed " + count + " playercache entries to the database");
		
	}

	public void commitPlayerCache(String name)
	{
		// find entry in commit list
		if (this.playerdata.containsKey(name))
		{
			PlayerCache pc = this.playerdata.get(name);
			sqlPlayer sPlayer = this.getPlayerObjectByName(name);
			if (sPlayer == null)
			{
				sPlayer = new sqlPlayer();
			}
			sPlayer.setName(pc.name);
			sPlayer.setDisplay(pc.display);
			sPlayer.setRace(pc.race);
			sPlayer.setLanguage(pc.language);
			sPlayer.setLanguageflags(pc.languageflags);
			sPlayer.setAlliance(pc.alliance);
			sPlayer.setLastname(pc.lastname);
			sPlayer.setPrefix(pc.prefix);
			sPlayer.setChatfocus(pc.chatfocus);
			sPlayer.setTitle(pc.title);
			sPlayer.setGender(pc.gender);
			sPlayer.setFlags(pc.flags);
			sPlayer.setFlagpole(pc.flagpole);
			sPlayer.setKillcount(pc.killcount);
			sPlayer.setVote(pc.vote);
			sPlayer.setBitwise(pc.bitwise);
			sPlayer.setExperience(pc.experience);
			sPlayer.setOptional(pc.optional);
			sPlayer.setElection(pc.election);
			sPlayer.setCombatexperience(pc.combatexperience);
			sPlayer.setRangedexperience(pc.rangedexperience);
			sPlayer.setScholarlyexperience(pc.scholarlyexperience);
			sPlayer.setNaturalexperience(pc.naturalexperience);
			sPlayer.setPower(pc.power);
			sPlayer.setLastmeditate(pc.lastmeditate);
			sPlayer.setBirthstamp(pc.birthstamp);
			sPlayer.setSuffix(pc.suffix);
			sPlayer.setHitpoints(pc.hitpoints);
			// commit the database entry for the player
			this.getDatabase().save(sPlayer);
		}
		
	}
	
	public PlayerCache getPlayerCacheByNameAsync(String name) {
		if (this.playerdata.containsKey(name))
		{
			PlayerCache pc = this.playerdata.get(name);
			return pc;			
		} else {
			return null;
		}
	}

	public PlayerCache getPlayerCacheByName(String name) {
		if (this.playerdata.containsKey(name))
		{
			PlayerCache pc = this.playerdata.get(name);
			return pc;			
		} else {
			// create it
			PlayerCache pc = new PlayerCache();
			pc.name = name;
			
			// now get data from the sqldatabase
			sqlPlayer sPlayer = this.getPlayerObjectByName(name);
			
			// no player object exists
			if (sPlayer == null)
			{
				sPlayer = new sqlPlayer();
				sPlayer.setName(name);
				sPlayer.setDisplay(name);
				sPlayer.setRace("human");
				sPlayer.setLanguage("common");
				sPlayer.setLanguageflags("0,0,0,0,0,0,0,0,0,0,0,0");
				sPlayer.setAlliance("neutral");

				// commit the database entry for the player
				this.getDatabase().save(sPlayer);
			}
			
			pc.display = sPlayer.getDisplay();
			pc.race = sPlayer.getRace();
			pc.language = sPlayer.getLanguage();
			pc.languageflags = sPlayer.getLanguageflags();
			pc.alliance = sPlayer.getAlliance();
			// playercache extension compatibility fields
			pc.lastname = sPlayer.getLastname();
			pc.prefix = sPlayer.getPrefix();
			pc.chatfocus = sPlayer.getChatfocus();
			pc.title = sPlayer.getTitle();
			pc.gender = sPlayer.getGender();
			pc.flags = sPlayer.getFlags();
			pc.flagpole = sPlayer.getFlagpole();
			pc.killcount = sPlayer.getKillcount();
			pc.vote = sPlayer.getVote();
			pc.bitwise = sPlayer.getBitwise();
			pc.experience = sPlayer.getExperience();
			pc.optional = sPlayer.getOptional();
			pc.election = sPlayer.getElection();
			pc.combatexperience = sPlayer.getCombatexperience();
			pc.rangedexperience = sPlayer.getRangedexperience();
			pc.scholarlyexperience = sPlayer.getScholarlyexperience();
			pc.naturalexperience = sPlayer.getNaturalexperience();
			pc.power = sPlayer.getPower();
			pc.lastmeditate = sPlayer.getLastmeditate();
			pc.birthstamp = sPlayer.getBirthstamp();
			pc.suffix = sPlayer.getSuffix();
			pc.hitpoints = sPlayer.getHitpoints();
			// decoration
			pc.decoration = getDecoration(pc);
			// add it to the cache
			this.playerdata.put(name,pc);
			// update their language!
			this.setLanguageSkill(name, this.getLanguageName(pc.race), 100);
			return pc;
		}
	}
	
	public String getDecoration(PlayerCache pc)
	{
		String decoration = "";
		String alliancetitle = ChatColor.BLUE+"N"+ChatColor.RESET;
		if (pc.alliance.equals("light"))
		{
			alliancetitle=ChatColor.RED+"L"+ChatColor.RESET;
		}
		if (pc.alliance.equals("dark"))
		{
			alliancetitle=ChatColor.BLACK+"D"+ChatColor.RESET;
		}
		if (pc.alliance.equals("neutral"))
		{
			alliancetitle=ChatColor.BLUE+"N"+ChatColor.RESET;
		}
		String language = pc.language;
		String gender = pc.gender;
		int election = pc.election;
		String kingstatus = "";
		if (election == 2)
		{
			if (gender.equals("female"))
			{
				kingstatus = "Queen";
			} else {
				kingstatus = "King";
			}
		}
		if (election == 0)
		{
			kingstatus = "Patient";
		}
		String gendertext = "M";
		if (gender.equals("female"))
		{
			gendertext = "F";
		}
		String alliancetext = alliancetitle;
		String classtext =  getClassTitle(pc);
		String displayname = capitalise(pc.display);
		String lastname = capitalise(pc.lastname);
		String title = pc.title;
		String fullname = displayname + " " + lastname;
		String membertype = "*";
		
		String colouredfullname = fullname;
		if (election == 2)
		{
			colouredfullname = ChatColor.RED + colouredfullname + ChatColor.RESET;
		} else {
			if (election == 0)
			{
				colouredfullname = ChatColor.BLUE + colouredfullname + ChatColor.RESET;				
			}
			colouredfullname = ChatColor.GREEN + colouredfullname + ChatColor.RESET;
		}
		
		
		decoration = "["+gendertext+","+alliancetext+","+membertype+"]"+" "+classtext+" "+ kingstatus + " " + colouredfullname+" "+title;
		if (kingstatus.equals("") && title.equals(""))
		{
			decoration = "["+gendertext+","+alliancetext+","+membertype+"]"+" "+classtext+" "+ colouredfullname;
		}
		if (kingstatus.equals("") && !title.equals(""))
		{
			decoration = "["+gendertext+","+alliancetext+","+membertype+"]"+" "+classtext+" "+ colouredfullname + " "+ title;
		}
		if (!kingstatus.equals("") && title.equals(""))
		{
			decoration = "["+gendertext+","+alliancetext+","+membertype+"]"+" "+classtext+" "+ kingstatus + " " + colouredfullname;
		}
		return decoration;
	}


	public void DoAsyncChat(Player player, String playername, String message,Set recipients) {
		// TODO Auto-generated method stub
		// fetch from player cache
		// check if player is online
		if (isMuted(player))
		{
			player.sendMessage("You cannot send a message because you are muted");
			return;
		}
		
		PlayerCache pc = this.playerdata.get(playername);
		
		Date now = new Date();
		Long timenow = now.getTime();
		
		if (!pc.lastmessage.equals("") && (Long.parseLong(pc.lastmessage) + 750) > timenow )
		{
			player.sendMessage("You are sending messages too fast and have been choked");
			System.out.println("[RPChat] WARNING " + player.getName() + " was sending ASYNC messages too fast and has been choked");
			pc.spamcount++;
			this.checkSpamCount(player);
			return;
		} else {
			pc.lastmessage = Long.toString(timenow);
			pc.spamcount = 0;
		}
		if (pc.chatfocus.equals(""))
		{
			
			DoCachedLocalMessage(player,pc.decoration, message,recipients);
			getServer().getScheduler().scheduleSyncDelayedTask(this,new parseChatTask(this,player.getName(),message), 1L);
		}

		if (pc.chatfocus.equals("local"))
		{

			DoCachedLocalMessage(player,pc.decoration, message,recipients);
			getServer().getScheduler().scheduleSyncDelayedTask(this,new parseChatTask(this,player.getName(),message), 1L);

		}

		if (pc.chatfocus.equals("nation"))
		{
			if (this.realisticchat == true)
			{
				player.sendMessage("This chat channel is unavailable while the server setting realisticchat is enabled");
				player.sendMessage("You can set your default channel with /setfocus");
			} else {
			DoCachedNationMessage(player,pc.decoration, message,recipients);
			}

		}


		if (pc.chatfocus.equals("town"))
		{
			if (this.realisticchat == true)
			{
				player.sendMessage("This chat channel is unavailable while the server setting realisticchat is enabled");
				player.sendMessage("You can set your default channel with /setfocus");
			} else {
			DoCachedTownMessage(player,pc.decoration, message,recipients);
			}

		}

		if (pc.chatfocus.equals("alliance"))
		{
			if (this.realisticchat == true)
			{
				player.sendMessage("This chat channel is unavailable while the server setting realisticchat is enabled");
				player.sendMessage("You can set your default channel with /setfocus");
			} else {
			DoCachedAllianceMessage(player,pc.decoration, message,recipients);
			}
		}

		if (pc.chatfocus.equals("race"))
		{
			if (this.realisticchat == true)
			{
				player.sendMessage("This chat channel is unavailable while the server setting realisticchat is enabled");
				player.sendMessage("You can set your default channel with /setfocus");
			} else {
			DoCachedRaceMessage(player,pc.decoration, message,recipients);
			}
		}
	}


	public void checkSpamCount(Player player) {
		// TODO Auto-generated method stub
		PlayerCache pc = this.getPlayerCacheByNameAsync(player.getName());
		if (pc != null)
		{
			if (pc.spamcount > 3)
			{
				System.out.println("Player " + player.getName() + " kicked for spamming");
				player.kickPlayer("Spamming - this has been logged and a ban may follow");
			}
		}
	}

	public void DoCachedRaceMessage(Player player,String decoration, String message, Set<Player> recipients) {
		// TODO Auto-generated method stub
		if (this.isMuted(player))
		{
			return;
		}
		
		//System.out.println("[RPChat-Race] " + player.getName() + ":"+  message);

		int count = 0;
		for (Player p : recipients)
		{
			if (p.equals(player))
			{
				PlayerReceiveMessage(p,player,"race",decoration,message);
			}
			else
			{
				String targetrace = this.getCachedPlayerRace(p.getName());

				if (!targetrace.equals(this.getCachedPlayerRace(player.getName())))
					continue;

				if (!this.isIgnored(player,p))
				{
					PlayerReceiveMessage(p,player,"race",decoration,message);
					count++;
				}
			}

		}

		if (count < 1)
		{
			player.sendMessage(ChatColor.GRAY + "* You speak but nobody hears you (There is no-one from your race online.)");
		}

	}


	public void DoCachedAllianceMessage(Player player,String decoration, String message, Set<Player> recipients) {
		// TODO Auto-generated method stub
		if (this.isMuted(player))
		{
			return;
		}
		//System.out.println("[RPChat-Alliance] " + player.getName() + ":"+  message);

		int count = 0;
		String alliance = getCachedPlayerAlliance(player.getName());
		for (Player p : recipients)
		{
			if (p.equals(player))
			{
				PlayerReceiveMessage(p,player,"alliance",decoration,message);
			}
			else
			{
				String targetalliance = this.getCachedPlayerAlliance(p.getName());

				if (!targetalliance.equals(alliance))
					continue;

				if (!this.isIgnored(player,p))
				{
					PlayerReceiveMessage(p,player,"alliance",decoration,message);
					count++;
				}
			}

		}

		if (count < 1)
		{
			player.sendMessage(ChatColor.GRAY + "* You speak but nobody hears you (There is no-one from your race online.)");
		}
	}


	public void DoCachedTownMessage(Player player,String decoration, String message, Set<Player> recipients) {
		// TODO Auto-generated method stub
		if (this.isMuted(player))
		{
			return;
		}
		
		//System.out.println("[RPChat-Town] " + player.getName() + ":"+  message);
		String race = this.getCachedPlayerRace(player.getName());
		try
		{
			List<Resident> residents = this.towny.getTownyUniverse().getActiveResidents();
			int residentfound = 0;

			for (Resident resident : residents)
			{
				if (resident.getName().equals(player.getName()))
				{
					Resident res = resident;
					residentfound = 1;
					
					String town = "";
					try
					{
						town = res.getTown().getName();
					}
					catch (Exception e)
					{
						town = "";
						player.sendMessage("You cannot send a message to your town as you are not in one.");
						return;
					}

					int count = 0;
					for (Player p : recipients)
					{
						if (p.equals(player))
						{
							PlayerReceiveMessage(p,player,"town",decoration,message);
						} else {
							for (Resident targetresident : residents)
							{

								if (targetresident.getName().equals(p.getName()))
								{
									Resident targetres = targetresident;
									try
									{
										if (!targetres.getTown().equals(res.getTown()))
											continue;
										if (!this.isIgnored(player,p))
										{
											PlayerReceiveMessage(p,player,"town",decoration,message);
											count++;
										}
									}
									catch (Exception localException1)
									{
									}
								}
							}
						}
					}
					if (count < 1)
					{
						player.sendMessage(ChatColor.GRAY + "* You speak but nobody hears you (There is no-one online in this town)");
					}
					if (residentfound == 1)
					{
						break;
					}
				}

			}
			if (residentfound == 0)
			{
				player.sendMessage(ChatColor.GRAY + "* You do not appear to be in a town");
			}
		} catch (Exception e)
		{

		}
	}


	public void DoCachedNationMessage(Player player,String decoration, String message, Set<Player> recipients) {
		// TODO Auto-generated method stub
		if (this.isMuted(player))
		{
			return;
		}
		//System.out.println("[RPChat-Nation] " + player.getName() + ":"+  message);

		String race = "";
		race = this.getCachedPlayerRace(player.getName());
		try
		{
			List<Resident> residents = this.towny.getTownyUniverse().getActiveResidents();

			int residentfound = 0;

			for (Resident resident : residents)
			{
				if (resident.getName().equals(player.getName()))
				{
					Resident res = resident;
					residentfound = 1;
					String town = "";
					try
					{
						town = res.getTown().getName();
					}
					catch (Exception e)
					{
						town = "";
						player.sendMessage("You cannot send a message to your nation as you are not in a town.");
						return;
					}

					String nation = "";
					try
					{
						nation = res.getTown().getNation().getName();
					}
					catch (Exception e)
					{
						nation = "";
						player.sendMessage("You cannot send a message to your nation as you are not in one.");
						return;
					}

					int count = 0;
					for (Player p : recipients)
					{
						if (p.equals(player))
						{
							PlayerReceiveMessage(p,player,"nation",decoration,message);
						} else {
							for (Resident targetresident : residents)
							{

								if (targetresident.getName().equals(p.getName()))
								{
									Resident targetres = targetresident;
									try
									{
										if (!targetres.getTown().getNation().equals(res.getTown().getNation()))
											continue;
										if (!this.isIgnored(player,p))
										{
											PlayerReceiveMessage(p,player,"nation",decoration,message);
											count++;
										}
									}
									catch (Exception localException1)
									{
									}
								}
							}
						}
					}
					if (count < 1)
					{
						player.sendMessage(ChatColor.GRAY + "* You speak but nobody hears you (There is no-one online in this nation)");
					}
				}

				if (residentfound == 1)
				{
					break;
				}
			}
			if (residentfound == 0)
			{
				player.sendMessage(ChatColor.GRAY + "* You do not appear to be in a nation");
			}
		} catch (Exception e)
		{

		}
	}


	public void DoCachedLocalMessage(Player player,String decoration, String message, Set<Player> recipients) {
		// TODO Auto-generated method stub
		if (this.isMuted(player))
		{
			return;
		}
		
		//System.out.println("[RPChat-Local] " + player.getName() + ":"+  message);

		String race = getCachedPlayerRace(player.getName());
		int count = 0;
		
		for (Player p : recipients)
		{
			if (p.equals(player))
			{
				PlayerReceiveMessage(p,player,"local",decoration,message);
			} else {

				if (!this.isIgnored(player,p))
				{
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
					PlayerReceiveMessage(p,player,"local",decoration,message);
					count++;
				}
			}

		}

		if (count < 1)
		{
			player.sendMessage(ChatColor.GRAY + "* You speak but nobody in range can hear you.");
		}
	}
	
	public List<Player> getNearbyPlayers(Player player)
	{
		List<Player> list = new ArrayList<Player>();
		List<Entity> ne = player.getNearbyEntities(20, 20, 20);
		for (Entity e : ne)
		{
			if (e instanceof Player)
			{
				if (!e.hasMetadata("NPC"))
				{
					list.add((Player)e);
				}
			}
		}
		return list;
	}
	
	public List<Player> getNearbyGroupPlayers(Player player)
	{
		PlayerCache pc = this.getPlayerCacheByNameAsync(player.getName());
		List<Player> list = new ArrayList<Player>();
		
		// only do this if they are in a group
		if (!pc.group.equals(""))
		{
			String group = pc.group;
			List<Player> nearbylist = this.getNearbyPlayers(player);
			// only do this if there are nearby players
			if (nearbylist.size() > 0)
			{
				for (Player p : nearbylist)
				{
					PlayerCache tpc = this.getPlayerCacheByName(p.getName());
					if (tpc.group.equals(group))
					{
						list.add(p);
					}
				}
			}
		}
		return list;
		
	}
	
	public void parseQuestText(String player, String message) {
		// TODO Auto-generated method stub
		// well looks like we are nearby and possibly able to handle this text - lets see what we can make of it
		Player p = getServer().getPlayerExact(player);
		message = message.toLowerCase();
		
		if (p != null)
		{
			List<Entity> ne = p.getNearbyEntities(2, 2, 2);
			
			for (Entity e : ne)
			{
				if (e instanceof Player)
				{
					if (e.hasMetadata("NPC"))
					{
						Player s = (Player)e;
						sendPlayerQuestResponse(p,s,message);
						
						
					}
				}
			}
		}
		
	}
		
	private void PlayerReceiveMessage(Player p, Player from, String type, String decoration,String message) {
		// TODO Auto-generated method stub
		
		
		String fromlanguage = this.getCachedPlayerLanguage(from.getName());
		String tolanguage = this.getCachedPlayerLanguage(p.getName());
		
		String scrambledmessage = getScrambled(fromlanguage,message);
		
		if (type.equals("local"))
		{
			
			
			if (fromlanguage.equals(tolanguage))
			{
				p.sendMessage(decoration+" says '"+ChatColor.YELLOW+message+"'");
			} else {
				// check if they have 100 of that language skill
				if (this.getLanguageSkill(p.getName(), fromlanguage) == 100)
				{
					p.sendMessage(decoration+" says '"+ChatColor.YELLOW+message+"'");
				} else {
					p.sendMessage(decoration+" ("+fromlanguage+"): "+ChatColor.YELLOW +scrambledmessage);
				}
			}
		}
		
		if (type.equals("nation"))
		{
			if (fromlanguage.equals(tolanguage))
			{
				p.sendMessage(decoration+" : "+ChatColor.LIGHT_PURPLE +message);
			} else {
				if (this.getLanguageSkill(p.getName(), fromlanguage) == 100)
				{
					p.sendMessage(decoration+" : "+ChatColor.LIGHT_PURPLE +message);
				} else {
					p.sendMessage(decoration+" ("+fromlanguage+"): "+ChatColor.LIGHT_PURPLE +scrambledmessage);
				}
			}
		}
		if (type.equals("town"))
		{
			if (fromlanguage.equals(tolanguage))
			{
				p.sendMessage(decoration+" : " +ChatColor.GREEN+message);
			} else {
				if (this.getLanguageSkill(p.getName(), fromlanguage) == 100)
				{
					p.sendMessage(decoration+" : " +ChatColor.GREEN+message);
				} else {
					p.sendMessage(decoration+" ("+fromlanguage+"): " +ChatColor.GREEN+scrambledmessage);
				}
			}
		}
		if (type.equals("alliance"))
		{
			if (fromlanguage.equals(tolanguage))
			{
				p.sendMessage(decoration+" : " +ChatColor.AQUA+message);
			} else {
				if (this.getLanguageSkill(p.getName(), fromlanguage) == 100)
				{
					p.sendMessage(decoration+" : " +ChatColor.AQUA+message);
				} else {
					p.sendMessage(decoration+" ("+fromlanguage+"): " +ChatColor.AQUA+scrambledmessage);
				}
			}
		}
		if (type.equals("race"))
		{
			if (fromlanguage.equals(tolanguage))
			{
				p.sendMessage(decoration+" : " +ChatColor.GOLD + message);
			} else {
				if (this.getLanguageSkill(p.getName(), fromlanguage) == 100)
				{
					p.sendMessage(decoration+" : " +ChatColor.GOLD + message);
				} else {
					p.sendMessage(decoration+" ("+fromlanguage+"): " +ChatColor.GOLD + scrambledmessage);
				}
			}
		}
		
		if (!fromlanguage.equals(tolanguage))
		{
			Random rand = new Random();
			int result = rand.nextInt(10)+1;
			if (result > 7)
			{
				IncreaseLanguageSkill(p,fromlanguage,1);
			}
		}
		
	}
	
	public boolean isPlayerInPlayersGroup(Player player, Player target)
	{
		PlayerCache pc = this.getPlayerCacheByNameAsync(player.getName());
		PlayerCache tc = this.getPlayerCacheByNameAsync(target.getName());
		
		if (!pc.group.equals(""))
		{
		
			if (pc.group.equals(tc.group))
			{
				return true;
			} else {
				return false;
			}
		} 
		
		return false;
	}
	
	private boolean isPlayerInGroupAsync(Player p) {
		// TODO Auto-generated method stub
		
		PlayerCache pc = this.getPlayerCacheByNameAsync(p.getName());
		if (!pc.group.equals(""))
		{
			return true;
		}
		return false;
	}
	
	public String getPlayerGroupAsync(Player p)
	{
		if (isPlayerInGroupAsync(p))
		{
			PlayerCache pc = this.getPlayerCacheByNameAsync(p.getName());
			return pc.group;
		}
		return "";
	}
	
	public void setPlayerGroup(Player p, String groupname)
	{
		PlayerCache pc = this.getPlayerCacheByNameAsync(p.getName());
		pc.group = groupname;
	}
	
	private boolean isPlayerNPCThreadSafe(Player p) {
		// TODO Auto-generated method stub
		if (p.hasMetadata("NPC"))
		{
			return true;
		}
		return false;
	}

	public int getLanguageSkill(String name,String language)
	{
		String languageflags = getCachedPlayerLanguageFlags(name);
		String[] langArray  = languageflags.split(",");
		
		if (language.equals("common"))
		{
			return 100;
		}
		
		if (language.equals("human"))
		{
			return Integer.parseInt(langArray[0]);
		}
		if (language.equals("naturestongue"))
		{
			return Integer.parseInt(langArray[1]);
		}
		if (language.equals("elvish"))
		{
			return Integer.parseInt(langArray[2]);
		}
		if (language.equals("ancienthuman"))
		{
			return Integer.parseInt(langArray[3]);
		}
		if (language.equals("undertongue"))
		{
			return Integer.parseInt(langArray[4]);
		}
		if (language.equals("gnomish"))
		{
			return Integer.parseInt(langArray[5]);
		}
		if (language.equals("ancientgobel"))
		{
			return Integer.parseInt(langArray[6]);
		}
		if (language.equals("thiefspeak"))
		{
			return Integer.parseInt(langArray[7]);
		}
		if (language.equals("ancientdragon"))
		{
			return Integer.parseInt(langArray[8]);
		}
		if (language.equals("deathspeak"))
		{
			return Integer.parseInt(langArray[9]);
		}
		if (language.equals("lidkish"))
		{
			return Integer.parseInt(langArray[10]);
		}
		if (language.equals("tiktok"))
		{
			return Integer.parseInt(langArray[11]);
		}
		
		return 0;
	}
	
	public void setLanguageSkill(String name,String language,int i)
	{
		int human = getLanguageSkill(name,"human");
		int naturestongue = getLanguageSkill(name,"naturestongue");
		int elvish = getLanguageSkill(name,"elvish");
		int ancienthuman = getLanguageSkill(name,"ancienthuman");
		int undertongue = getLanguageSkill(name,"undertongue");
		int gnomish = getLanguageSkill(name,"gnomish");
		int ancientgobel = getLanguageSkill(name,"ancientgobel");
		int thiefspeak = getLanguageSkill(name,"thiefspeak");
		int ancientdragon = getLanguageSkill(name,"ancientdragon");
		int deathspeak = getLanguageSkill(name,"deathspeak");
		int lidkish = getLanguageSkill(name,"lidkish");
		int tiktok = getLanguageSkill(name,"tiktok");
		
		if (language.equals("human"))
		{
			human = i;
		}
		if (language.equals("naturestongue"))
		{
			naturestongue = i;
		}
		if (language.equals("elvish"))
		{
			elvish = i;
		}
		if (language.equals("ancienthuman"))
		{
			ancienthuman = i;
		}
		if (language.equals("undertongue"))
		{
			undertongue = i;
		}
		if (language.equals("gnomish"))
		{
			gnomish = i;
		}
		if (language.equals("ancientgobel"))
		{
			ancientgobel = i;
		}
		if (language.equals("thiefspeak"))
		{
			thiefspeak = i;
		}
		if (language.equals("ancientdragon"))
		{
			ancientdragon = i;
		}
		if (language.equals("deathspeak"))
		{
			deathspeak = i;
		}
		if (language.equals("lidkish"))
		{
			lidkish = i;
		}
		if (language.equals("tiktok"))
		{
			tiktok = i;
		}
		
		String newarray = human+","+
				naturestongue+","+
				elvish+","+
				ancienthuman+","+
				undertongue+","+
				gnomish+","+
				ancientgobel+","+
				thiefspeak+","+
				ancientdragon+","+
				deathspeak+","+
				lidkish+","+
				tiktok;
		
		setCachedPlayerLanguageFlags(name,newarray);
	}

	private void setCachedPlayerLanguageFlags(String name, String newarray) {
		// TODO Auto-generated method stub
		PlayerCache pc = this.getPlayerCacheByName(name);
		if (pc != null)
		{
			pc.languageflags=newarray;
		}
	}

	private void IncreaseLanguageSkill(Player p, String language, int i) {
		// TODO Auto-generated method stub
		
		// make sure not over 100
		int curskill = this.getLanguageSkill(p.getName(), language);
		if (curskill < 100)
		{
			this.setLanguageSkill(p.getName(), language,(curskill+i));
			p.sendMessage("* You get better at " + language + " (" +(curskill+i)+")");
		}
		
		
	}


	public String getCachedPlayerRace(String name) {
		// TODO Auto-generated method stub
		PlayerCache pc = this.getPlayerCacheByNameAsync(name);
		if (pc != null)
		{
			return pc.race;
		} else {
			return "";
		}

	}
	
	public void promotePlayer(Player player)
	{
		this.permission.playerAddGroup(player, "inviteonly");
		player.sendMessage("Player added to RP Strict Group");
	}
	public void demotePlayer(Player player)
	{
		this.permission.playerRemoveGroup(player, "inviteonly");
		player.sendMessage("Player removed from RP Strict Group");
	}
	
	public String getCachedPlayerLanguageFlags(String name) {
		// TODO Auto-generated method stub
		PlayerCache pc = this.getPlayerCacheByNameAsync(name);
		if (pc != null)
		{
			return pc.languageflags;
		} else {
			return "0,0,0,0,0,0,0,0,0,0,0,0";
		}

	}
	
	public String getCachedPlayerFlags(String name) {
		// TODO Auto-generated method stub
		PlayerCache pc = this.getPlayerCacheByNameAsync(name);
		if (pc != null)
		{
			return pc.flags;
		} else {
			return "";
		}

	}
	
	
	
	public String getCachedPlayerLanguage(String name) {
		// TODO Auto-generated method stub
		PlayerCache pc = this.getPlayerCacheByNameAsync(name);
		if (pc != null)
		{
			return pc.language;
		} else {
			return "common";
		}

	}
	
	public boolean getCachedPlayerOptedIn(String name) {
		// always return true
		
		return true;

	}
	
	public String getCachedPlayerAlliance(String name) {
		// TODO Auto-generated method stub
		PlayerCache pc = this.getPlayerCacheByName(name);
		if (pc != null)
		{
			return pc.alliance;
		} else {
			return "neutral";
		}

	}
	
	public void sendRand(Player player, int sides) {
		// TODO Auto-generated method stub
		Random rand = new Random();
		int result = rand.nextInt(sides)+1;
		
		for (Player p : player.getWorld().getPlayers())
		{
			if (p.equals(player))
			{
				p.sendMessage(ChatColor.GRAY+player.getName() + " rolls a " + sides + " sided dice and gets " + result);
			} else {

				if (!this.isIgnored(player,p))
				{
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

					p.sendMessage(ChatColor.GRAY+player.getName() + " rolls a " + sides + " sided dice and gets " + result);
				}
			}

		}
	}
	
	
	public int getInfectedCount()
	{
		List<sqlPlayer> infected = getDatabase().find(sqlPlayer.class).where().eq("bitwise", 1).findList();
		return infected.size();
		
	}
	
	public boolean isInfected(Player player)
	{
		PlayerCache sPlayer = this.getPlayerCacheByName(player.getName());
		boolean infected = false;
		if (sPlayer.bitwise == 1)
		{
			infected = true;
		}
		return infected;
		
	}
	


	public void PlayerGotoCapital(Player player) {
		// TODO Auto-generated method stub
				PlayerCache sPlayerme = this.getPlayerCacheByName(player.getName());
				if (sPlayerme.race.equals("human"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-5106,66,-198);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("fairy"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-3726,65,2497);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("highelf"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-3787,107,3399);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("woodelf"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-3811,112,3784);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("halfelf"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-4090,67,2377);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("darkelf"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-5121, 76, -1300);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("vampire"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-4955, 83, -743);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("barbarian"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-4971,78,-366);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("orc"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-4803,65,-1450);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("ogre"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-4803,65,-1450);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("troll"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-4976, 66, -620);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("halfdragon"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-5337,83,-389);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("gnome"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-5403,117,611);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("goblin"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-4536,90,-669);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("hobbit"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-5569,72,298);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("highhuman"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-5403,117,611);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("undead"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-4287,67,-862);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("dwarf"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-4356,67,-341);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("ratman"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-5122, 73, -577);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("lizardman"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-4544,64,556);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("elemental"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-5365,66,-1270);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("kobold"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-5365,66,-1270);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("angel"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-3652,68,2876);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("fallenangel"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-5122, 73, -577);
					this.teleport(player, loc);
				}
				if (sPlayerme.race.equals("clockwork"))
				{
					World world = this.getServer().getWorld("world");
					Location loc  = new Location(world,-5403,117,611);
					this.teleport(player, loc);
				}
	}

	public void parseNpcInteract(Player player, Player target) {
		// TODO Auto-generated method stub
		
		if (player.getItemInHand().getData().getItemType() == Material.MONSTER_EGG)
		{
			ItemStack egg = player.getItemInHand();
			ItemMeta im = egg.getItemMeta();
			// Give it a name
			String itemname = im.getDisplayName();
			List<String> lore = im.getLore();
			String questitem = lore.get(0).split(" ")[0];
			
			
			
			int count = 0;
			if (!questitem.equals(""))
			{
				for (Quest q : this.quests)
				{
					if (q.npcname.equals(target.getName()) && questitem.equals(q.req_itemid))
					{
						if (!q.not_flag.equals(""))
						{
							// wait - we have a flag they musnt have lets check it!
							
							if (hasFlag(player, q.not_flag))
							{
								
								// doh he has it - respond
								if (!q.not_flagresponse.equals(""))
								{
									String text = q.npcname + " says " + "'"+ChatColor.YELLOW+q.not_flagresponse+ChatColor.RESET+"'";
									player.sendMessage(text);
								} else {
									String text = q.npcname + " says " + "'"+ChatColor.YELLOW+"Hmm, what?"+ChatColor.RESET+"'";
									player.sendMessage(text);
								}
								return;
							}
						}
						
						if (!q.req_flag.equals(""))
						{
							if (!hasFlag(player, q.req_flag))
							{
								// doesnt have the required flag
								if (!q.noreq_flagresponse.equals(""))
								{
									String text = q.npcname + " says " + "'"+ChatColor.YELLOW+q.noreq_flagresponse+ChatColor.RESET+"'";
									player.sendMessage(text);
								} else {
									String text = q.npcname + " says " + "'"+ChatColor.YELLOW+"I have been watching your progress and you are not ready to give me this just yet"+ChatColor.RESET+"'";
									player.sendMessage(text);
									
								}
								return;
							}
						}

						
						player.sendMessage(ChatColor.LIGHT_PURPLE+"* " + target.getName() + " takes the item from you and thanks you");
						
						player.getInventory().setItemInHand(null);
						player.updateInventory();
						String text = q.npcname + " says " + "'"+ChatColor.YELLOW+q.response+ChatColor.RESET+"'";
						player.sendMessage(text);
						count++;
						
						if (!q.reward.equals(""))
						{
							this.rewardqueue.put(player.getName(), q.reward);
							player.sendMessage(ChatColor.LIGHT_PURPLE + "* An item drops at your feet");
						}
						
						if (!q.reward_flag.equals(""))
						{
							this.giveFlag(player,q.reward_flag);
							player.sendMessage("* You have received a character flag!");
						}
					}
				}
			
			}
			
			if (count == 0)
			{
				String text = target.getName() + " says " + "'"+ChatColor.YELLOW+"I have no use for this item you can have it back";
				player.sendMessage(text);
			}
		} 	
		
	}

	public void invitePlayerToGroup(Player leader, Player member) {
		// TODO Auto-generated method stub
		if (!isPlayerInGroupAsync(member))
		{
			// first check if the group is within size limit or doesnt exist at all (less than 1 players)
			if (getGroupCount(leader.getName()) < 6)
			{
				PlayerCache pc = this.getPlayerCacheByName(member.getName());
				pc.lastgroupinvite = leader.getName();
				leader.sendMessage("Invited " + member.getName() + " to join your group");
				member.sendMessage("You have been invited to join " + leader.getName() + "'s group - /group accept | /group decline");
			} else {
				leader.sendMessage("That group is full");
			}
		} else {
			leader.sendMessage("That player is already in a group");
		}
		
		
	}
	
	private GroupCache getGroup(String groupname)
	{
		if (this.groupdata.size() > 0)
		{
			if (this.groupdata.get(groupname) != null)
			{
				return this.groupdata.get(groupname);
			} else {
				return null;
			}
		}
		return null;
	}

	private int getGroupCount(String groupname) {
		// TODO Auto-generated method stub
		if (getGroup(groupname) != null)
		{
			GroupCache gc = getGroup(groupname);
			int count = 1; // leader
			if (!gc.member1.equals(""))
			{
				count++;
			}
			if (!gc.member2.equals(""))
			{
				count++;
			}
			if (!gc.member3.equals(""))
			{
				count++;
			}
			if (!gc.member4.equals(""))
			{
				count++;
			}
			if (!gc.member5.equals(""))
			{
				count++;
			}
			return count;
		}
		return 0;
	}
	public void sendMessageToGroupFromPlayer(Player player, String message) {
		// TODO Auto-generated method stub
		PlayerCache pc = this.getPlayerCacheByName(player.getName());
		
		if (!pc.group.equals(""))
		{
			if (this.getGroup(pc.group) != null)
			{
				sendMessageToGroup(pc.group,"[group] "+ pc.display + " : " + message);
			} else {
				player.sendMessage("You are not in a group");
			}
		} else {
			player.sendMessage("You are not in a group");
		}
	}

	
	public void sendMessageToGroup(String groupname, String message)
	{
		if (this.getGroup(groupname) != null)
		{
			GroupCache gc = this.getGroup(groupname);
			if (!gc.leadername.equals(""))
			{
				if (getServer().getPlayerExact(gc.leadername) != null)
				{
					Player member = getServer().getPlayerExact(gc.leadername);
					member.sendMessage(message);
				}
			}
			
			if (!gc.member1.equals(""))
			{
				if (getServer().getPlayerExact(gc.member1) != null)
				{
					Player member = getServer().getPlayerExact(gc.member1);
					member.sendMessage(message);
				}
			}
			if (!gc.member2.equals(""))
			{
				if (getServer().getPlayerExact(gc.member2) != null)
				{
					Player member = getServer().getPlayerExact(gc.member2);
					member.sendMessage(message);
				}
			}
			if (!gc.member3.equals(""))
			{
				if (getServer().getPlayerExact(gc.member3) != null)
				{
					Player member = getServer().getPlayerExact(gc.member3);
					member.sendMessage(message);
				}
			}
			if (!gc.member4.equals(""))
			{
				if (getServer().getPlayerExact(gc.member4) != null)
				{
					Player member = getServer().getPlayerExact(gc.member4);
					member.sendMessage(message);
				}
			}
			if (!gc.member5.equals(""))
			{
				if (getServer().getPlayerExact(gc.member5) != null)
				{
					Player member = getServer().getPlayerExact(gc.member5);
					member.sendMessage(message);
				}
			}
		}
	}

	public void removePlayerFromGroup(Player player) {
		// TODO Auto-generated method stub
		// are they in a group?
		PlayerCache pc = this.getPlayerCacheByName(player.getName());
		
		if (!pc.group.equals(""))
		{
			if (this.getGroup(pc.group) != null)
			{
				GroupCache gc = this.getGroup(pc.group);
				if (gc.leadername.equals(player.getName()))
				{
					// remove the group entirely
					// remove members first
					if (!gc.member1.equals(""))
					{
						if (getServer().getPlayerExact(gc.member1) != null)
						{
							// player is online to remove
							
							Player member = getServer().getPlayerExact(gc.member1);
							PlayerCache mpc = this.getPlayerCacheByName(member.getName());
							mpc.group = "";
							mpc.lastgroupinvite = "";
							gc.member1 = "";
							member.sendMessage("Your group has been disbanded");
						} else {
							// player is offline, clear just his group membership
							gc.member1 = "";
						}
					}
					if (!gc.member2.equals(""))
					{
						if (getServer().getPlayerExact(gc.member2) != null)
						{
							// player is online to remove
							
							Player member = getServer().getPlayerExact(gc.member2);
							PlayerCache mpc = this.getPlayerCacheByName(member.getName());
							mpc.group = "";
							mpc.lastgroupinvite = "";
							gc.member2 = "";
							member.sendMessage("Your group has been disbanded");
						} else {
							// player is offline, clear just his group membership
							gc.member2 = "";
						}
					}
					if (!gc.member3.equals(""))
					{
						if (getServer().getPlayerExact(gc.member3) != null)
						{
							// player is online to remove
							
							Player member = getServer().getPlayerExact(gc.member3);
							PlayerCache mpc = this.getPlayerCacheByName(member.getName());
							mpc.group = "";
							mpc.lastgroupinvite = "";
							gc.member3 = "";
							member.sendMessage("Your group has been disbanded");
						} else {
							// player is offline, clear just his group membership
							gc.member3 = "";
						}
					}
					if (!gc.member4.equals(""))
					{
						if (getServer().getPlayerExact(gc.member4) != null)
						{
							// player is online to remove
							
							Player member = getServer().getPlayerExact(gc.member4);
							PlayerCache mpc = this.getPlayerCacheByName(member.getName());
							mpc.group = "";
							mpc.lastgroupinvite = "";
							gc.member4 = "";
							member.sendMessage("Your group has been disbanded");
						} else {
							// player is offline, clear just his group membership
							gc.member4 = "";
						}
					}
					if (!gc.member5.equals(""))
					{
						if (getServer().getPlayerExact(gc.member5) != null)
						{
							// player is online to remove
							
							Player member = getServer().getPlayerExact(gc.member5);
							PlayerCache mpc = this.getPlayerCacheByName(member.getName());
							mpc.group = "";
							mpc.lastgroupinvite = "";
							gc.member5 = "";
							member.sendMessage("Your group has been disbanded");
						} else {
							// player is offline, clear just his group membership
							gc.member5 = "";
						}
					}
					
					// now remove the leader
					player.sendMessage("Your group has been disbanded");
					this.groupdata.remove(player.getName());
				} else {
					// Not the leader, just remove from the member slot
					if (gc.member1.equals(player.getName()))
					{
						// player is online to remove
						sendMessageToGroup(pc.group, gc.member1 + " has left the group");
						gc.member1 = "";
						player.sendMessage("You have left the group");
						
					}
					if (gc.member2.equals(player.getName()))
					{
						// player is online to remove
						sendMessageToGroup(pc.group, gc.member2 + " has left the group");
						gc.member2 = "";
						player.sendMessage("You have left the group");
						
					}
					if (gc.member3.equals(player.getName()))
					{
						// player is online to remove
						sendMessageToGroup(pc.group, gc.member3 + " has left the group");
						gc.member3 = "";
						player.sendMessage("You have left the group");
						
					}
					if (gc.member4.equals(player.getName()))
					{
						// player is online to remove
						sendMessageToGroup(pc.group, gc.member4 + " has left the group");
						gc.member4 = "";
						player.sendMessage("You have left the group");
						
					}
					if (gc.member5.equals(player.getName()))
					{
						// player is online to remove
						sendMessageToGroup(pc.group, gc.member5 + " has left the group");
						gc.member5 = "";
						player.sendMessage("You have left the group");
						
					}
					
					// Is this the last player of the group? If so remove it
					if (this.getGroupCount(pc.group) == 1)
					{
						sendMessageToGroup(pc.group, "The group has been disbanded");
						this.groupdata.remove(pc.group);
						PlayerCache mpl = this.getPlayerCacheByNameAsync(pc.group);
						mpl.group = "";
						mpl.lastgroupinvite = "";
						
					}
						
					
				}
				// now remove personal data for group
				pc.group = "";
				pc.lastgroupinvite = "";
			} else {
				// now remove personal data for group
				pc.group = "";
				pc.lastgroupinvite = "";
				player.sendMessage("You are not in a group");
			}
		} else {
			player.sendMessage("You are not in a group");
		}
	}

	public void acceptGroup(Player player) {
		// TODO Auto-generated method stub
		PlayerCache pc = this.getPlayerCacheByName(player.getName());
		
		if (pc.group.equals(""))
		{
			if (!pc.lastgroupinvite.equals(""))
			{
				if (getGroup(pc.lastgroupinvite) != null)
				{
					if (this.getGroupCount(pc.lastgroupinvite) < 6)
					{
						// find a free slot
						GroupCache gc = getGroup(pc.lastgroupinvite);
						if (gc.member1.equals(""))
						{
							sendMessageToGroup(pc.lastgroupinvite, player.getName() + " has joined the group");
							gc.member1 = player.getName();
							pc.group = gc.leadername;
							pc.lastgroupinvite = "";

							player.sendMessage("You have joined the group, use /g to talk");
							return;
						}
						if (gc.member2.equals(""))
						{
							sendMessageToGroup(pc.lastgroupinvite, player.getName() + " has joined the group");
							gc.member2 = player.getName();
							pc.group = gc.leadername;
							pc.lastgroupinvite = "";

							player.sendMessage("You have joined the group, use /g to talk");
							return;
						}
						if (gc.member3.equals(""))
						{
							sendMessageToGroup(pc.lastgroupinvite, player.getName() + " has joined the group");							
							gc.member3 = player.getName();
							pc.group = gc.leadername;
							pc.lastgroupinvite = "";

							player.sendMessage("You have joined the group, use /g to talk");
							return;

						}
						if (gc.member4.equals(""))
						{
							sendMessageToGroup(pc.lastgroupinvite, player.getName() + " has joined the group");
							gc.member4 = player.getName();
							pc.group = gc.leadername;
							pc.lastgroupinvite = "";

							player.sendMessage("You have joined the group, use /g to talk");
							return;
						}
						if (gc.member5.equals(""))
						{
							sendMessageToGroup(pc.lastgroupinvite, player.getName() + " has joined the group");
							gc.member5 = player.getName();
							pc.group = gc.leadername;
							pc.lastgroupinvite = "";
							player.sendMessage("You have joined the group, use /g to talk");
							return;
						}
						
						
						
					} else {
						player.sendMessage("You cannot join this group as it is now now full");
					}
					
				} else {
					// group does not exist, but the leader may not have yet formed it due to no invites yet
					// so lets check if the leader is without a group
					// first, is the leader online?
					if (getServer().getPlayerExact(pc.lastgroupinvite) != null)
					{
						Player leader = getServer().getPlayerExact(pc.lastgroupinvite);
						// leader is online 
						PlayerCache pcl = this.getPlayerCacheByNameAsync(pc.lastgroupinvite);
						if (!pcl.group.equals(pc.lastgroupinvite) && !pcl.group.equals(""))
						{
							player.sendMessage("That group is no longer available as the leader has joined another group");
						} else {
							if (pcl.group.equals(""))
							{
								// create the group
								GroupCache gc = new GroupCache();
								gc.leadername = pc.lastgroupinvite;
								pcl.lastgroupinvite = "";
								pcl.group = pc.lastgroupinvite;
								leader.sendMessage("You have formed the group");
								
								gc.member1 = player.getName();
								player.sendMessage("You have joined the group");
								pc.group = pc.lastgroupinvite;
								
								this.groupdata.put(pc.lastgroupinvite, gc);
							} else {
								player.sendMessage("That group is no longer available");
							}
						}
						
					} else {
						player.sendMessage("That group is no longer available as the leader is offline");
					}
					
				}
			} else {
				player.sendMessage("You have not been invited to a group");
			}
		} else {
			player.sendMessage("You are already in a group");
		}
		
		// clear last invite as its been processed
		pc.lastgroupinvite = "";
	}

	public void declineGroup(Player player) {
		// TODO Auto-generated method stub
		PlayerCache pc = this.getPlayerCacheByName(player.getName());
		
		if (!pc.group.equals(""))
		{
			if (!pc.lastgroupinvite.equals(""))
			{
				if (getGroup(pc.lastgroupinvite) != null)
				{
					GroupCache gc = getGroup(pc.lastgroupinvite);
					sendMessageToGroup(pc.lastgroupinvite, player.getName() + " has declined the group invitation");
					player.sendMessage("You have declined the group invitation");
				} else {
					player.sendMessage("You have declined the group invitation");
				}
			} else {
				player.sendMessage("You have declined the group invitation");
			}
		} else {
			player.sendMessage("You have declined the group invitation");
		}
		
		// clear last invite as its been processed
		pc.lastgroupinvite = "";
	}

	public void sendPlayerGroupList(Player player) {
		// TODO Auto-generated method stub
		PlayerCache pc = this.getPlayerCacheByName(player.getName());
		if (!pc.group.equals(""))
		{
			if (getGroup(pc.group) != null)
			{
				GroupCache gc = getGroup(pc.group);
				String listtext = "";
				listtext += "[Leader]" + gc.leadername;
				if (!gc.member1.equals(""))
				{
					listtext += " [1] " + gc.member1;
				}
				if (!gc.member2.equals(""))
				{
					listtext += " [2] " + gc.member2;
				}
				if (!gc.member3.equals(""))
				{
					listtext += " [3] " + gc.member3;
				}
				if (!gc.member4.equals(""))
				{
					listtext += " [4] " + gc.member4;
				}
				if (!gc.member5.equals(""))
				{
					listtext += " [5] " + gc.member5;
				}
				player.sendMessage("Group List");
				player.sendMessage(listtext);
			} else {
				player.sendMessage("Your group no longer exists to get a list of");
			}
		} else {
			player.sendMessage("You are not in a group");
		}
	}

	public void sendRaceMessage(String race, String message) {
		// TODO Auto-generated method stub
		for (World w : getServer().getWorlds())
		{
			for (Player p : w.getPlayers())
		     {
				if (!p.hasMetadata("NPC"))
				{
					PlayerCache pc= this.getPlayerCacheByNameAsync(p.getName());
					if (pc.race.equals(race))
					{
						p.sendMessage(ChatColor.GOLD + message);
			     	}
				}
		     }
		}
	    	   
	}

	public void setPlayerElection(Player player, Player playerExact, String args) {
		// TODO Auto-generated method stub
		player.sendMessage("Setting election status for: " + playerExact.getName());
		
		// is args a number?
		if (this.isInteger(args))
		{
			int status = Integer.parseInt(args);
			if (status == 0 || status == 1 || status == 2)
			{
				if (status == 0)
				{
					player.sendMessage("Player set to patient (" + args + ")");
					PlayerCache pc = this.getPlayerCacheByName(playerExact.getName());
					pc.election = status;
					World world = this.getServer().getWorld("world");
					Location loc = new Location(world,166.00,56.00,283.00);
					playerExact.teleport(loc);
					
					player.sendMessage("Player saved, kicking for title update");
					pc.decoration = getDecoration(pc);
					this.commitPlayerCache(playerExact.getName());
					player.sendMessage("Player jailed");
					playerExact.kickPlayer("You are now set as a Patient!");
					this.jailPlayer(playerExact,"infirmary");
				}
				if (status == 1)
				{
					player.sendMessage("Player set Freeman (" + args + ")");
					PlayerCache pc = this.getPlayerCacheByName(playerExact.getName());
					pc.election = status;
					player.sendMessage("Player saved, kicking for title update");
					pc.decoration = getDecoration(pc);
					this.unjailPlayer(playerExact);
					player.sendMessage("Player unjailed");
					this.commitPlayerCache(playerExact.getName());
					playerExact.kickPlayer("Congratulations, you are now Free!");
					
				}
				
				if (status == 2)
				{
					player.sendMessage("Election status set to: " + args);
					PlayerCache pc = this.getPlayerCacheByName(playerExact.getName());
					pc.election = status;
					player.sendMessage("Player saved, kicking for title update");
					pc.decoration = getDecoration(pc);
					this.commitPlayerCache(playerExact.getName());
					playerExact.kickPlayer("Congratulations, you are now King");
				}
				
				

			} else {
				player.sendMessage("Error: Status must be 0, 1 or 2");
			}
			
		} else {
			player.sendMessage("Error: Cannot set election status for "+playerExact+" to " + args + " - must be 0, 1  or 2");			
		}
		
	}

	public Metal getMetal(String type) {
		// TODO Auto-generated method stub
		for (Metal metal : this.metals)
		{
			if (metal.name.equals(type))
			{
				return metal;
			}
		}
		return null;
	}

	
	
}

