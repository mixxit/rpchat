package net.gamerservices.rpchat;

import com.earth2me.essentials.Essentials;
import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Coord;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.palmergames.bukkit.towny.object.TownyWorld;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockVector;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

public class rpchat extends JavaPlugin
{
  private PluginManager pm;
  private Towny towny = null;
  private Listener rpchatPlayerListener;
  private Listener rpchatEntityListener;
  public static Permission permission = null;
  public static Economy econ = null;
  private Essentials essentials = null;
  public boolean isEssentials = false;
  static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
  
  private static final String LOG_PREFIX = "[Dynmap-RPChat] ";
  private static final String DEF_INFOWINDOW = "<div class=\"infowindow\">Sector Owner: %alliance%<br />Resources: %stored%</div>";
  private static final String ADMIN_ID = "administrator";
  
  // MAX EXPERIENCE (level 15 atm)
  static final int maxexperience = 13600;
  private Map<String, AreaMarker> resareas = new HashMap<String, AreaMarker>();
  private Map<String, Marker> resmark = new HashMap<String, Marker>();
  FileConfiguration cfg;
  MarkerSet set;
  long updperiod;
  boolean use3d;
  String infowindow;
  String admininfowindow;
  AreaStyle defstyle;
  Map<String, AreaStyle> ownerstyle;
  Set<String> visible;
  Set<String> hidden;
  boolean stop; 
  int maxdepth;
  Plugin dynmap;
  DynmapAPI api;
  MarkerAPI markerapi;
  
  private static class AreaStyle {
      String strokecolor;
      double strokeopacity;
      int strokeweight;
      String fillcolor;
      double fillopacity;
      String label;

      AreaStyle(FileConfiguration cfg, String path, AreaStyle def) {
          strokecolor = cfg.getString(path+".strokeColor", def.strokecolor);
          strokeopacity = cfg.getDouble(path+".strokeOpacity", def.strokeopacity);
          strokeweight = cfg.getInt(path+".strokeWeight", def.strokeweight);
          fillcolor = cfg.getString(path+".fillColor", def.fillcolor);
          fillopacity = cfg.getDouble(path+".fillOpacity", def.fillopacity);
          label = cfg.getString(path+".label", null);
      }

      AreaStyle(FileConfiguration cfg, String path) {
          strokecolor = cfg.getString(path+".strokeColor", "#00FF00");
          strokeopacity = cfg.getDouble(path+".strokeOpacity", 0);
          strokeweight = cfg.getInt(path+".strokeWeight", 1);
          fillcolor = cfg.getString(path+".fillColor", "#00FF00");
          fillopacity = cfg.getDouble(path+".fillOpacity", 0.35);
      }
  }
  
  public int getMaxExperience()
  {
	  return this.maxexperience;
  }
  
  public boolean isPlayerInPVPArea(Player player)
  {
	  try {
		Coord coord = Coord.parseCoord(player);
		TownyWorld world = TownyUniverse.getDataSource().getWorld(player.getWorld().getName());
		
		TownBlock townblock = world.getTownBlock(coord);
		
		if (townblock.getTown().isPVP() && !world.isForcePVP() && !townblock.getPermissions().pvp)
		{
			return true;
		} else {
			return false;
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		System.out.println("isPlayerInPVPArea: " + e.getMessage());
		return true;
	}
  }
  
  public int getTotalSkillExperience(Player player)
  {
	  sqlPlayer p = this.getPlayerObject(player);
	  if (p != null)
	  {
		  return p.getCombatexperience() + p.getRangedexperience() + p.getScholarlyexperience() + p.getNaturalexperience();
	  } 
	  return 0;
  }
  
  public void updateSectors()
  {
	  Map<String,AreaMarker> newmap = new HashMap<String,AreaMarker>(); /* Build new map */
	  
	  List<String> alliancenames = this.getAllianceNames();
	  for (String alliance : alliancenames)
	  {
	  
		  List<sqlSector> sectors = getAllianceSectors(alliance);
		  for (sqlSector sector : sectors)
		  {
			  handleSector(sector, alliance, newmap);
		  }
	  
	  }
	  resareas = newmap;
	  //System.out.println("[RPChat-Dynmap] Scheduled to run again in " + updperiod);
	  getServer().getScheduler().scheduleSyncDelayedTask(this, new SectorUpdate(), updperiod);
  }
  
  private Location getGreaterSectorCorner(sqlSector sector)
  {
	  String world = sector.getName().split(":")[0];
	  String chunkpos = sector.getName().split(":")[1];
	  int chunkx = Integer.parseInt(chunkpos.split(",")[0]);
	  int chunky = Integer.parseInt(chunkpos.split(",")[1]);
	  chunkx = chunkx+1;
	  chunky = chunky+1;
	  
	  Location loc = new Location(this.getServer().getWorld(world), (double)((chunkx * 16) - 15), 0, (double)((chunky * 16) - 15));
	  return loc;
	  
  }
  
  private Location getLesserSectorCorner(sqlSector sector)
  {
	  String world = sector.getName().split(":")[0];
	  String chunkpos = sector.getName().split(":")[1];
	  int chunkx = Integer.parseInt(chunkpos.split(",")[0]);
	  int chunky = Integer.parseInt(chunkpos.split(",")[1]);
	  
	  chunkx = chunkx+1;
	  chunky = chunky+1;
	  
	  
	  Location loc = new Location(this.getServer().getWorld(world), (double)(chunkx * 16), 0, (double)(chunky * 16));
	  return loc;
  }
  
  private boolean isVisible(String owner, String worldname) {
      if((visible != null) && (visible.size() > 0)) {
          if((visible.contains(owner) == false) && (visible.contains("world:" + worldname) == false) &&
                  (visible.contains(worldname + "/" + owner) == false)) {
              return false;
          }
      }
      if((hidden != null) && (hidden.size() > 0)) {
          if(hidden.contains(owner) || hidden.contains("world:" + worldname) || hidden.contains(worldname + "/" + owner))
              return false;
      }
      return true;
  }
  
  private void addStyle(String owner, String worldid, AreaMarker m, sqlSector sector) {
      AreaStyle as = null;
      
      if(!ownerstyle.isEmpty()) {
          as = ownerstyle.get(owner.toLowerCase());
      }
      if(as == null)
          as = defstyle;

      int sc = 0x00FF00;
      int fc = 0x00FF00;
      try {
          sc = Integer.parseInt(as.strokecolor.substring(1), 16);
          fc = Integer.parseInt(as.fillcolor.substring(1), 16);
      } catch (NumberFormatException nfx) {
      }
      m.setLineStyle(as.strokeweight, as.strokeopacity, sc);
      m.setFillStyle(as.fillopacity, fc);
      if(as.label != null) {
          m.setLabel(as.label);
      }
  }
  
  private void handleSector(sqlSector sector, String alliance, Map<String, AreaMarker> newmap) 
  {
	  // TODO Auto-generated method stub
	  double[] x = null;
	  double[] z = null;
	  Location l0 = getLesserSectorCorner(sector);
      Location l1 = getGreaterSectorCorner(sector);
      
      if(l0 == null)
          return;
      String wname = l0.getWorld().getName();
      String owner = alliance;
      /* Handle areas */
      if(isVisible(owner, wname)) { 
          /* Make outline */
          x = new double[4];
          z = new double[4];
          
          x[0] = l0.getX(); 
          z[0] = l0.getZ();
          
          x[1] = l0.getX(); 
          z[1] = l1.getZ()-1;
          
          x[2] = l1.getX()-1; 
          z[2] = l1.getZ()-1;
          
          x[3] = l1.getX()-1; 
          z[3] = l0.getZ();
          
          String markerid = sector.getName();
          AreaMarker m = resareas.remove(markerid); /* Existing area? */
          if(m == null) {
              m = set.createAreaMarker(markerid, owner, false, wname, x, z, false);
              if(m == null)
                  return;
          }
          else {
        	  
              m.setCornerLocations(x, z); /* Replace corner locations */
              m.setLabel(owner);   /* Update label */
          }
          if(use3d) { /* If 3D? */
              m.setRangeY(l1.getY()+1.0, l0.getY());
          }            
          /* Set line and fill properties */
          addStyle(owner, wname, m, sector);

          /* Build popup */
          String desc = formatInfoWindow(sector, m, alliance);
          m.setDescription(desc); /* Set popup */

          /* Add to map */
          newmap.put(markerid, m);
      }
  }
  
  private class OurServerListener implements Listener {
      @SuppressWarnings("unused")
      @EventHandler(priority=EventPriority.MONITOR)
      public void onPluginEnable(PluginEnableEvent event) {
          Plugin p = event.getPlugin();
          String name = p.getDescription().getName();
          if(name.equals("dynmap")) {
              if(dynmap.isEnabled())
                  activate();
          }
      }
  }
  
  private void activate() {
      /* Now, get markers API */
	  System.out.println("Activating RPChat Dynmap");
      markerapi = api.getMarkerAPI();
      if(markerapi == null) {
          System.out.println("Error loading dynmap marker API!");
          return;
      }
      /* Load configuration */
      FileConfiguration cfg = getConfig();
      cfg.options().copyDefaults(true);   /* Load defaults, if needed */
      this.saveConfig();  /* Save updates, if needed */
      /* Now, add marker set for mobs (make it transient) */
      set = markerapi.getMarkerSet("sectors.markerset");
      if(set == null)
      {
          set = markerapi.createMarkerSet("sectors.markerset", cfg.getString("layer.name", "Sectors"), null, false);
      } else {
          set.setMarkerSetLabel(cfg.getString("layer.name", "Sectors"));
      }
      
      if(set == null) {
    	  System.out.println("Error creating marker set");
          return;
      }
      int minzoom = cfg.getInt("layer.minzoom", 0);
      if(minzoom > 0)
          set.setMinZoom(minzoom);
      set.setLayerPriority(cfg.getInt("layer.layerprio", 9));
      set.setHideByDefault(cfg.getBoolean("layer.hidebydefault", false));
      use3d = cfg.getBoolean("use3dregions", false);
      infowindow = cfg.getString("infowindow", DEF_INFOWINDOW);
      maxdepth = cfg.getInt("maxdepth", 16);

      System.out.println("Loading styles");
      /* Get style information */
      defstyle = new AreaStyle(cfg, "regionstyle");
      ownerstyle = new HashMap<String, AreaStyle>();
      ConfigurationSection sect = cfg.getConfigurationSection("ownerstyle");
      if(sect != null) {
          Set<String> ids = sect.getKeys(false);
          
          for(String id : ids) {
              ownerstyle.put(id.toLowerCase(), new AreaStyle(cfg, "ownerstyle." + id, defstyle));
          }
      }
      List<String> vis = cfg.getStringList("visibleregions");
      if(vis != null) {
          visible = new HashSet<String>(vis);
      }
      List<String> hid = cfg.getStringList("hiddenregions");
      if(hid != null) {
          hidden = new HashSet<String>(hid);
      }

      /* Set up update job - based on periond */
      int per = cfg.getInt("update.period", 30);
      if(per < 15) per = 15;
      updperiod = (long)(per*200);
      stop = false;
      System.out.println("Initialising ticks");
      getServer().getScheduler().scheduleSyncDelayedTask(this, new SectorUpdate(), 40);   /* First time is 2 seconds */
      
  }
  
  private class SectorUpdate implements Runnable {
      public void run() {
    	  
          if(!stop)
          {
              updateSectors();
          } else {
          }
      }
  }
  
  private int getSectorResource(sqlSector sector)
  {
	  int count = 0;
	  
	  if (!sector.getFlags().equals(""))
	  {
		    Date then = new Date(Long.parseLong(sector.getFlags()));
		    Date now = new Date();
		    String timenow = Long.toString(now.getTime());
		    
		    // elapsed miliseconds
		    Long elapsed = now.getTime() - then.getTime();
		    
		    // every minute it will generated 0.1 gold nuggets
		    Long resourcegained = (long)((elapsed / 60000) * 0.03);
		    count = safeLongToInt(resourcegained);
	  }
	  
	  return count;
  }
  
  private String formatInfoWindow(sqlSector sector, AreaMarker m,String alliance) {
      String v;
      v = "<div class=\"regioninfo\">"+infowindow+"</div>";
      v = v.replace("%alliance%", alliance);
      
      v = v.replace("%stored%", Integer.toString(getSectorResource(sector)));
      
      return v;
  }
  

  static
  {
     permission = null;
  }
  
  public int getLevelFromExp(int xp)
  {
	  if (xp < 400)
	  {
		  return 1;
	  }
	  if (xp < 900)
	  {
		  return 2;
	  }
	  if (xp < 1400)
	  {
		  return 3;
	  }
	  if (xp < 2100)
	  {
		  return 4;
	  }
	  if (xp < 2800)
	  {
		  return 5;
	  }
	  if (xp < 3600)
	  {
		  return 6;
	  }
	  if (xp < 4500)
	  {
		  return 7;
	  }
	  if (xp < 5400)
	  {
		  return 8;
	  }
	  if (xp < 6500)
	  {
		  return 9;
	  }
	  if (xp < 7600)
	  {
		  return 10;
	  }
	  if (xp < 8700)
	  {
		  return 11;
	  }
	  if (xp < 9800)
	  {
		  return 12;
	  }
	  if (xp < 11000)
	  {
		  return 13;
	  }
	  if (xp < 12300)
	  {
		  return 14;
	  }
	  if (xp < 13600)
	  {
		  return 15;
	  }
	  if (xp < 15000)
	  {
		  return 16;
	  }
	  if (xp < 16400)
	  {
		  return 17;
	  }
	  if (xp < 17800)
	  {
		  return 18;
	  }
	  if (xp < 19300)
	  {
		  return 19;
	  }
	  if (xp < 20800)
	  {
		  return 20;
	  }
	  if (xp < 22400)
	  {
		  return 21;
	  }
	  if (xp < 24000)
	  {
		  return 22;
	  }
	  if (xp < 25500)
	  {
		  return 23;
	  }
	  if (xp < 27200)
	  {
		  return 24;
	  }
	  if (xp < 28900)
	  {
		  return 25;
	  }
	  if (xp < 30500)
	  {
		  return 26;
	  }
	  if (xp < 32200)
	  {
		  return 27;
	  }
	  if (xp < 33900)
	  {
		  return 28;
	  }
	  if (xp < 36300)
	  {
		  return 29;
	  }
	  if (xp < 38800)
	  {
		  return 30;
	  }
	  if (xp < 41600)
	  {
		  return 31;
	  }
	  if (xp < 44600)
	  {
		  return 32;
	  }
	  if (xp < 48000)
	  {
		  return 33;
	  }
	  if (xp < 51400)
	  {
		  return 34;
	  }
	  if (xp < 55000)
	  {
		  return 35;
	  }
	  if (xp < 58700)
	  {
		  return 36;
	  }
	  if (xp < 62400)
	  {
		  return 37;
	  }
	  if (xp < 66200)
	  {
		  return 38;
	  }
	  if (xp < 70200)
	  {
		  return 39;
	  }
	  if (xp < 74300)
	  {
		  return 40;
	  }
	  if (xp < 78500)
	  {
		  return 41;
	  }
	  if (xp < 82800)
	  {
		  return 42;
	  }
	  if (xp < 87100)
	  {
		  return 43;
	  }
	  if (xp < 91600)
	  {
		  return 44;
	  }
	  if (xp < 96300)
	  {
		  return 45;
	  }
	  if (xp < 101000)
	  {
		  return 46;
	  }
	  if (xp < 105800)
	  {
		  return 47;
	  }
	  if (xp < 110700)
	  {
		  return 48;
	  }
	  if (xp < 115700)
	  {
		  return 49;
	  }
	  if (xp < 120900)
	  {
		  return 50;
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
	  return 1;
  }
  
  public int getPlayerLevel(Player player)
  {
	  sqlPlayer p = this.getPlayerObject(player);
	  if (p != null)
	  {
		  int xp = p.getExperience();
		  
		  return this.getLevelFromExp(xp);
		  
	  } else {
		  return 1;
	  }
  }
  
  public void setPlayerCombatExp(Player player, int amount)
  {
	  sqlPlayer p = this.getPlayerObject(player);
	  if (p != null)
	  {
		  if (amount < maxexperience)
		  {
			  int oldlevel = getLevelFromExp(p.getCombatexperience());
			  int newlevel = getLevelFromExp(p.getCombatexperience() + amount);
			  if (newlevel > oldlevel)
			  {
				  player.sendMessage("You gained a combat level!");
			  }
			  p.setCombatexperience(amount);
			  this.getDatabase().save(p);

		  } else {
			  p.setCombatexperience(maxexperience);
			  this.getDatabase().save(p);

		  }
	  }
  }
  
  public void setPlayerRangedExp(Player player, int amount)
  {
	  sqlPlayer p = this.getPlayerObject(player);
	  if (p != null)
	  {
		  if (amount < maxexperience)
		  {
			  int oldlevel = getLevelFromExp(p.getRangedexperience());
			  int newlevel = getLevelFromExp(p.getRangedexperience() + amount);
			  if (newlevel > oldlevel)
			  {
				  player.sendMessage("You gained a ranged level!");
			  }
			  
			  p.setRangedexperience(amount);
			  this.getDatabase().save(p);
		  } else {
			  p.setRangedexperience(maxexperience);
			  this.getDatabase().save(p);
		  }
	  }
  }
  public void setPlayerScholarlyExp(Player player, int amount)
  {
	  sqlPlayer p = this.getPlayerObject(player);
	  if (p != null)
	  {
		  if (amount < maxexperience)
		  {
			  int oldlevel = getLevelFromExp(p.getScholarlyexperience());
			  int newlevel = getLevelFromExp(p.getScholarlyexperience() + amount);
			  if (newlevel > oldlevel)
			  {
				  player.sendMessage("You gained a scholarly magic level!");
			  }			  
			  
			  p.setScholarlyexperience(amount);
			  this.getDatabase().save(p);
		  } else {
			  p.setScholarlyexperience(maxexperience);
			  this.getDatabase().save(p);
		  }
	  }
  }
  public int getPlayerCombatExp(Player player)
  {
	  sqlPlayer p = this.getPlayerObject(player);
	  if (p != null)
	  {
		  return p.getCombatexperience();
	  }
	  return 0;
  }
  
  public int getPlayerRangedExp(Player player)
  {
	  sqlPlayer p = this.getPlayerObject(player);
	  if (p != null)
	  {
		  return p.getRangedexperience();
	  }
	  return 0;
  }
  
  public int getPlayerScholarlyExp(Player player)
  {
	  sqlPlayer p = this.getPlayerObject(player);
	  if (p != null)
	  {
		  return p.getScholarlyexperience();
	  }
	  return 0;
  }
  
  public int getPlayerNaturalExp(Player player)
  {
	  sqlPlayer p = this.getPlayerObject(player);
	  if (p != null)
	  {
		  return p.getNaturalexperience();
	  }
	  return 0;
  }
  
  public void setPlayerNaturalExp(Player player, int amount)
  {
	  sqlPlayer p = this.getPlayerObject(player);
	  if (p != null)
	  {
		  if (amount < maxexperience)
		  {
			  int oldlevel = getLevelFromExp(p.getNaturalexperience());
			  int newlevel = getLevelFromExp(p.getNaturalexperience() + amount);
			  if (newlevel > oldlevel)
			  {
				  player.sendMessage("You gained a natural magic level!");
			  }	
			  
			  p.setNaturalexperience(amount);
			  this.getDatabase().save(p);
		  } else {
			  p.setNaturalexperience(maxexperience);
			  this.getDatabase().save(p);
		  }
	  }
  }
  
  public int getPlayerCombatLevel(Player player)
  {
	  sqlPlayer p = this.getPlayerObject(player);
	  if (p != null)
	  {
		  int xp = p.getCombatexperience();
		  
		  return this.getLevelFromExp(xp);
		  
	  } else {
		  return 1;
	  }
  }
  
  public int getPlayerRangedLevel(Player player)
  {
	  sqlPlayer p = this.getPlayerObject(player);
	  if (p != null)
	  {
		  int xp = p.getRangedexperience();
		  
		  return this.getLevelFromExp(xp);
		  
	  } else {
		  return 1;
	  }
  }
  
  public int getScholarlyMagicLevel(Player player)
  {
	  sqlPlayer p = this.getPlayerObject(player);
	  if (p != null)
	  {
		  int xp = p.getScholarlyexperience();
		  
		  return this.getLevelFromExp(xp);
		  
	  } else {
		  return 1;
	  }
  }
  public int getNaturalMagicLevel(Player player)
  {
	  sqlPlayer p = this.getPlayerObject(player);
	  if (p != null)
	  {
		  int xp = p.getNaturalexperience();
		  
		  return this.getLevelFromExp(xp);
		  
	  } else {
		  return 1;
	  }
  } 
  
  public int getPlayerPower(Player player)
  {
	  sqlPlayer p = this.getPlayerObject(player);
	  if (p != null)
	  {
		  int power = p.getPower();
		  
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
	  
	 if(set != null) {
         set.deleteMarkerSet();
         set = null;
     }
     resareas.clear();
     stop = true;
      
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
	  if (race.equals("combine")) { return "cmb"; } 
	  if (race.equals("collective")) { return "col"; } 
	  if (race.equals("realm")) { return "rlm"; } 
	  if (race.equals("dominion")) { return "dom"; } 
	  if (race.equals("legacy")) { return "lgc"; } 
	  if (race.equals("legion")) { return "lgn"; } 
	  if (race.equals("foresworn")) { return "fsw"; } 
	  if (race.equals("forsaken")) { return "fsk"; } 	  
	  return "unk";
  }
  
  public List<String> getAllianceNames()
  {
	  List<String> alliances = new ArrayList<String>();
	  
	  alliances.add("combine");
	  alliances.add("collective");
	  alliances.add("realm");
	  alliances.add("dominion");
	  alliances.add("legacy");
	  alliances.add("legion");
	  alliances.add("foresworn");
	  alliances.add("forsaken");
	  
	  
	  return alliances;
  }
  
  public boolean hasPerm(Player player, String string)
  {
     return permission.has(player, string);
  }

  public String getGroups(Player player)
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
     /* Get dynmap */
     dynmap = pm.getPlugin("dynmap");
     if(dynmap == null) {
         System.out.println("Cannot find dynmap!");
         return;
     }
     api = (DynmapAPI)dynmap; /* Get API */

     getServer().getPluginManager().registerEvents(new OurServerListener(), this);        
     /* If both enabled, activate */
     if(dynmap.isEnabled())
         activate();
     

     if ((this.towny == null) || (getServer().getScheduler().scheduleSyncDelayedTask(this, new onLoadedTask(this), 1L) == -1))
     {
       System.out.println("SEVERE - Could not schedule onLoadedTask.");
       this.pm.disablePlugin(this);
     }
        
     getCommand("local").setExecutor(new LocalMessage(this));
     getCommand("racechat").setExecutor(new RaceMessage(this));
     getCommand("race").setExecutor(new SetRace(this));
     getCommand("metatron").setExecutor(new MetatronMessage(this));
     getCommand("globalchat").setExecutor(new GlobalMessage(this));
	 getCommand("name").setExecutor(new SetName(this));
	 getCommand("lastname").setExecutor(new SetLastName(this));
	 getCommand("findname").setExecutor(new FindName(this));
	 getCommand("vote").setExecutor(new SetVote(this));
	 getCommand("doelection").setExecutor(new SetElection(this));
	 getCommand("getstats").setExecutor(new GetStatsCmd(this));
	 getCommand("meditate").setExecutor(new MeditateCmd(this));
	 getCommand("defaultchannel").setExecutor(new SetDefaultChannel(this));
	 getCommand("setcapital").setExecutor(new SetCapital(this));
	 getCommand("capital").setExecutor(new GotoCapital(this));
	 getCommand("alliancechat").setExecutor(new AllianceMessage(this));
	 getCommand("title").setExecutor(new SetTitle(this));
	 getCommand("emote").setExecutor(new EmoteMessage(this));
	 getCommand("teleport").setExecutor(new DoDropship(this));
	 getCommand("sector").setExecutor(new Sector(this));	 
	 getCommand("plantflag").setExecutor(new DoPlantFlag(this));
	 getCommand("grantflag").setExecutor(new DoGrantFlag(this));
	 getCommand("alliance").setExecutor(new AllianceCmd(this));
	 getCommand("optin").setExecutor(new OptedinCmd(this));
	 
	 getCommand("reset").setExecutor(new ResetCmd(this));
	 getCommand("who").setExecutor(new Who(this));
	 registerEvents();
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
  }

  public String capitalise(String string)
  {
     if (string == null)
       throw new NullPointerException("string");
     if (string.equals(""))
       throw new NullPointerException("string");
     return Character.toUpperCase(string.charAt(0)) + string.substring(1);
  }

  private void setupDatabase()
  {
    try
    {
       getDatabase().find(sqlPlayer.class).findRowCount();
       getDatabase().find(sqlRaces.class).findRowCount();
       getDatabase().find(sqlAlliances.class).findRowCount();
       getDatabase().find(sqlDropships.class).findRowCount();
       getDatabase().find(sqlSector.class).findRowCount();
       
    }
    catch (PersistenceException ex) {
       System.out.println("Installing database for " + getDescription().getName() + " due to first time usage");
       removeDDL();
       installDDL();
    }
  }

  public String getPlayerAlliance(Player player)
  {
    try
    {
       sqlPlayer sPlayer = (sqlPlayer)getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
       if (sPlayer == null) {
        return "Unknown";
      }
       return sPlayer.getAlliance();
    }
    catch (Exception e)
    {
       System.out.println("[rpchat] Exception: " + e.getMessage());
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
	       System.out.println("[rpchat] Exception: " + e.getMessage());
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
	       System.out.println("[rpchat] Exception: " + e.getMessage());
	     }
  }
  
  public String getPlayerRace(Player player)
  {
    try
    {
       sqlPlayer sPlayer = (sqlPlayer)getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
       if (sPlayer == null) {
        return "Unknown";
      }
       return sPlayer.getRace();
    }
    catch (Exception e)
    {
       System.out.println("[rpchat] Exception: " + e.getMessage());
     }return "Unknown";
  }
  
  

  public List<Class<?>> getDatabaseClasses()
  {
     List list = new ArrayList();
     list.add(sqlPlayer.class);
     list.add(sqlRaces.class);
     list.add(sqlAlliances.class);
     list.add(sqlSector.class);
     list.add(sqlDropships.class);
     
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

	public String getPlayerLastName(Player player) {
		
		sqlPlayer sPlayerme = (sqlPlayer)this.getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
		if (sPlayerme == null) {
			return "";		
		} else {
			if (sPlayerme.getLastname().equals(""))
			{
				return "";
			} else {
				return capitalise(sPlayerme.getLastname());			
			}
		}
	}
	
	public String getPlayerDisplayName(Player player) {
		
		
		if (isKing(player))
	    {
			return "King " + capitalise(player.getDisplayName());

	    }

		
		if (isSteward(player))
	    {
			return "Steward " + capitalise(player.getDisplayName());

	    }
		return capitalise(player.getDisplayName());
	}
	
	public String getColouredName(Player player) {
		
		
		if (player.getName().toLowerCase().equals("mixxit".toLowerCase()))
	    {
	      if (player.isOp())
	      {
	         return ChatColor.LIGHT_PURPLE + player.getName() + ChatColor.WHITE;
	      }
	    }
		
		if (isKing(player))
	    {
	       return ChatColor.RED + player.getName() + ChatColor.WHITE;
	    }
		
		if (isSteward(player))
	    {
	       return ChatColor.GOLD + player.getName() + ChatColor.WHITE;
	    }
		
		return ChatColor.GREEN + player.getName() + ChatColor.WHITE;
	}
	
	public List<sqlSector> getAllianceSectors(String alliance)
	{
		List<sqlSector> sectors = getDatabase().find(sqlSector.class).where().ieq(alliance, "1").findList();
		return sectors;
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
	
	public void clearSectorResources(List<sqlSector> sectors)
	{
		Date now = new Date();
	    String timenow = Long.toString(now.getTime());
		for (sqlSector s : sectors){
			if (!s.getFlags().equals(""))
			{
				s.setFlags(timenow);
			}
		}
		this.getDatabase().save(sectors);
	}
	
	public int getAllianceResourcesValue(String alliance)
	{
		int value = 0;
		List<sqlSector> sectors = getDatabase().find(sqlSector.class).where().ieq(alliance, "1").findList();
		return getSectorsResourcesValue(sectors);
		
	}
	
	public int getSectorsResourcesValue(List<sqlSector> sectors)
	{
		int value = 0;
		Long count = 0L;
		
		for (sqlSector s : sectors){
			
			if (!s.getFlags().equals(""))
			{
				value = value + getSectorResource(s);
			}
		}
		
		
		return safeLongToInt(value);
	}
	
	public int getAndClearAllianceResources(String alliance)
	{
		List<sqlSector> sectors = getDatabase().find(sqlSector.class).where().ieq(alliance, "1").findList();
		int count = 0;
		count = getSectorsResourcesValue(sectors);
		
		if (count != 0)
		{
			clearSectorResources(sectors);
		} else {
			
		}
		return count;
	}
	
	private String getAllianceStewardName(String alliancename)
	{
		
		String leadername = "none";
		List<sqlPlayer> players = getDatabase().find(sqlPlayer.class).where().ieq("bitwise", "1").findList();
		String highestleadername = "";
		int highestleadercount = 0;
		for (sqlPlayer p : players){
			if (p.getAlliance().equals(alliancename))
			{
				int curplayersvotecount = getVoteCount(p.getName().toLowerCase());
				if (curplayersvotecount > highestleadercount)
				{
					highestleadername = p.getName().toLowerCase();
					highestleadercount = curplayersvotecount;
				}
			}
		}
		
		if (highestleadername.equals(""))
		{
			return leadername;
		} else {
			return highestleadername;
		}
		
	}
	
	public boolean isKing(Player player) {
		
	
		sqlPlayer sPlayerme = (sqlPlayer)this.getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
		if (sPlayerme == null) {
			return false;
		}
		
		if (sPlayerme.getElection() == 2)
		{
			return true;
			
						
		}
		
		return false;
	}
	
	private boolean isSteward(Player player) {
		
	
		sqlPlayer sPlayerme = (sqlPlayer)this.getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
		if (sPlayerme == null) {
			return false;
		}
		
		if (sPlayerme.getElection() == 1)
		{
			if (getAllianceStewardName(sPlayerme.getAlliance()).equals(player.getName().toLowerCase()))
			{
				return true;
			}
				
			
						
		}
		
		return false;
	}

	public void giveExperience(Player attacker, int i) {
		
		sqlPlayer sPlayerme = (sqlPlayer)this.getDatabase().find(sqlPlayer.class).where().ieq("name", attacker.getName()).findUnique();
		if (sPlayerme == null) {
			return;
		}
		
		int oldlevel = this.getPlayerLevel(attacker);
		
		if (sPlayerme.getExperience() < maxexperience)
		{
			
			sPlayerme.setExperience(sPlayerme.getExperience() + 1);
			this.getDatabase().save(sPlayerme);
			System.out.println("[RPChat] Player " + attacker.getDisplayName() + "("+attacker.getName()+") earned experience!");
			attacker.sendMessage(ChatColor.YELLOW + "* You gained experience!");
			int newlevel = this.getPlayerLevel(attacker);
			if (newlevel > oldlevel)
			{
				attacker.sendMessage(ChatColor.YELLOW + "* You gained a level ("+newlevel+")!");
			}
		}
	}

	public String getPlayerTitle(Player player) {
		
		
		sqlPlayer sPlayerme = (sqlPlayer)this.getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
		if (sPlayerme == null) {
			return "";		
		} else {
			if (sPlayerme.getTitle().equals(""))
			{
				return "";
			} else {
				return (sPlayerme.getTitle());			
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
		
		if (fromplayer.isOp())
		{
			sqlPlayer sPlayerthem = (sqlPlayer)this.getDatabase().find(sqlPlayer.class).where().ieq("name", targetplayer.getName()).findUnique();
			if (sPlayerthem == null) {
				fromplayer.sendMessage("You cannot grant a title when your targets account is being updated.");
				return;
			} else {
				if (title.equals("clear"))
				{
					sPlayerthem.setTitle("");
					this.getDatabase().save(sPlayerthem);
					fromplayer.sendMessage("Title cleared for player");
					return;
				} else {						
					sPlayerthem.setTitle(title);
					this.getDatabase().save(sPlayerthem);
					fromplayer.sendMessage("Title set for player");
					return;
				}
			}
		}
		
		
		sqlPlayer sPlayerme = (sqlPlayer)this.getDatabase().find(sqlPlayer.class).where().ieq("name", fromplayer.getName()).findUnique();
		if (sPlayerme == null) {
			fromplayer.sendMessage("You cannot grant a title when your account is being updated.");
			return;
		} else {
			if (isKing(fromplayer))
			{
				sqlPlayer sPlayerthem = (sqlPlayer)this.getDatabase().find(sqlPlayer.class).where().ieq("name", targetplayer.getName()).findUnique();
				if (sPlayerthem == null) {
					fromplayer.sendMessage("You cannot grant a title when your targets account is being updated.");
					return;
				} else {
					if (sPlayerme.getAlliance().equals(sPlayerthem.getAlliance()))
					{
						if (title.equals("clear"))
						{
							sPlayerthem.setTitle("");
							this.getDatabase().save(sPlayerthem);
							fromplayer.sendMessage("Title cleared for player");
							return;
						} else {						
							sPlayerthem.setTitle(title);
							this.getDatabase().save(sPlayerthem);
							fromplayer.sendMessage("Title set for player");
							return;
						}
					} else {
						fromplayer.sendMessage("You cannot set a title of a player who is not your race.");
						return;
					}
				}
			} else {
				fromplayer.sendMessage("Only a king can set a title.");
				return;
			}
		}
	}

	public boolean isIgnored(Player ignoredby, Player player) {
		// TODO Auto-generated method stub
		return this.essentials.getUser(player).isIgnoredPlayer(ignoredby.getName());
	}

	public void clearVotes(Player player) {
		// TODO Auto-generated method stub
		
		List<sqlPlayer> players = getDatabase().find(sqlPlayer.class).where().ieq("vote", player.getName()).findList();
		for (sqlPlayer p : players){
			p.setVote("");
			this.getDatabase().save(p);
		}
	}
	
	public String addDropship(String world, double x, double y, double z)
	  {
	    try
	    {
	      sqlDropships sDropship = new sqlDropships();
	      String x1 = Double.toString(x);
	      String y1 = Double.toString(y);
	      String z1 = Double.toString(z);
	      Date now = new Date();
	      Long time26now = Long.valueOf(now.getTime());
	      String timenow = Long.toString(now.getTime());

	      sDropship.setName(timenow);
	      sDropship.setWorld(world);

	      sDropship.setX(x1);
	      sDropship.setY(y1);
	      sDropship.setZ(z1);

	      getDatabase().save(sDropship);

	      return encode(time26now.longValue());
	    }
	    catch (Exception e)
	    {
	      System.out.println("[rpchat] Exception: " + e.getMessage());
	    }

	    return null;
	  }
	
	public void setSectorDominator(String sector,String dominator)
	{
		sqlSector sSector = (sqlSector)getDatabase().find(sqlSector.class).where().ieq("name", sector).findUnique();
		if (sSector == null) {
	        sSector = new sqlSector();
	        sSector.setName(sector);

	        getDatabase().save(sSector);
	    }
		
		sSector.setCombine(0);
		sSector.setCollective(0);
		sSector.setRealm(0);
		sSector.setDominion(0);
		sSector.setLegacy(0);
		sSector.setLegion(0);
		sSector.setForesworn(0);
		sSector.setForsaken(0);
	    Date now = new Date();
	    String timenow = Long.toString(now.getTime());
	    sSector.setFlags(timenow);
		
	    if (dominator.equals("combine"))
	    {
	    	sSector.setCombine(1);
	    }
	    if (dominator.equals("collective"))
	    {
	    	sSector.setCollective(1);
	    }
	    if (dominator.equals("realm"))
	    {
	    	sSector.setRealm(1);
	    }
	    if (dominator.equals("dominion"))
	    {
	    	sSector.setDominion(1);
	    }
	    if (dominator.equals("legacy"))
	    {
	    	sSector.setLegacy(1);
	    }
	    if (dominator.equals("legion"))
	    {
	    	sSector.setLegion(1);
	    }
	    if (dominator.equals("foresworn"))
	    {
	    	sSector.setForesworn(1);
	    }
	    if (dominator.equals("forsaken"))
	    {
	    	sSector.setForsaken(1);
	    }

	    this.getDatabase().save(sSector);
		
	}

	public String getSectorDominator(String sector)
	  {
	    String dominator = "none";
	    try
	    {
	      sqlSector sSector = (sqlSector)getDatabase().find(sqlSector.class).where().ieq("name", sector).findUnique();
	      if (sSector == null) {
	        sSector = new sqlSector();
	        sSector.setName(sector);

	        getDatabase().save(sSector);
	      }

	      Map map = new HashMap();
	      map.put("combine", Integer.valueOf(sSector.getCombine()));
	      map.put("collective", Integer.valueOf(sSector.getCollective()));
	      map.put("realm", Integer.valueOf(sSector.getRealm()));
	      map.put("dominion", Integer.valueOf(sSector.getDominion()));
	      map.put("legacy", Integer.valueOf(sSector.getLegacy()));
	      map.put("legion", Integer.valueOf(sSector.getLegion()));
	      map.put("foresworn", Integer.valueOf(sSector.getForesworn()));
	      map.put("forsaken", Integer.valueOf(sSector.getForsaken()));

	      Set s = map.entrySet();

	      Iterator it = s.iterator();

	      String highestrace = "none";
	      Integer highestvalue = Integer.valueOf(0);

	      while (it.hasNext())
	      {
	        Map.Entry m = (Map.Entry)it.next();

	        String key = (String)m.getKey();

	        Integer value = (Integer)m.getValue();
	        if (value.intValue() <= highestvalue.intValue())
	          continue;
	        highestrace = key;
	        highestvalue = value;
	      }

	      dominator = highestrace;
	    }
	    catch (Exception e)
	    {
	      System.out.println("[RPchatError] " + e.getMessage());
	      dominator = "none";
	    }

	    return dominator;
	  }

	public sqlPlayer getPlayerObject(Player player) {
		// TODO Auto-generated method stub
		sqlPlayer sPlayer = (sqlPlayer)getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
		
		
		
		return sPlayer;
	}

	public boolean hasFlag(Player targetplayer) {
		// TODO Auto-generated method stub
		
		if (getPlayerObject(targetplayer).getFlagpole() == 1)
		{ 
			return true;
		}
		
		return false;
	}

	public void grantFlag(Player targetplayer) {
		// TODO Auto-generated method stub
		sqlPlayer player = getPlayerObject(targetplayer);
		player.setFlagpole(1);
		this.getDatabase().save(player);
		targetplayer.sendMessage("You have been granted a flag!");
	}

	public int getAllianceSectorCount(String alliance) {
		// TODO Auto-generated method stub
		List<sqlSector> sectors = getDatabase().find(sqlSector.class).where().ieq(alliance, "1").findList();
		int count = 0;
		for (sqlSector s : sectors){
            count++;
		}
		return count;
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

	public boolean isAdjacentToAllianceSector(String sector, String alliance) {
		// TODO Auto-generated method stub
		String[] data = sector.split(":");
		String world = data[0];
		String[] coord = data[1].split(",");
		String x = coord[0];
		String z = coord[1];
		
		int actx = Integer.parseInt(x);
		int actz = Integer.parseInt(z);
		
		int startx = -1;
		int startz = -1;
		
		while (startx < 2)
		{
			while (startz < 2)
			{
				String dominator = this.getSectorDominator(world + ":" + (actx + startx) + "," + (actz + startz));
				if (dominator.equals(alliance))
				{
					return true;
				}
				startz++;
			}
			
			startx++;
			startz = -1;
		}
		
		return false;
	}

	public int getPlayerExperience(Player player) {
		// TODO Auto-generated method stub
		sqlPlayer sPlayerme = (sqlPlayer)this.getDatabase().find(sqlPlayer.class).where().ieq("name", player.getName()).findUnique();
		if (sPlayerme == null) {
			return 0;
		} 
		
		return sPlayerme.getExperience();
	}

	public void setPlayerPower(Player player, int newpower) {
		// TODO Auto-generated method stub
		sqlPlayer s = this.getPlayerObject(player);
		if (s != null)
		{
			s.setPower(newpower);
			this.getDatabase().save(s);
		} 
	}
	
	public void setPlayerPowerMeditate(Player player, int newpower) {
		// TODO Auto-generated method stub
		sqlPlayer s = this.getPlayerObject(player);
		if (s != null)
		{	
			s.setPower(newpower);
			this.getDatabase().save(s);
			player.sendMessage("Your mind relaxes as your power increases ("+this.getPlayerPower(player)+"/100)");
		} else {
			player.sendMessage("You cannot meditate when your character is being updated");
		}
	}

	public void setLastMeditateAsNow(Player player) {
		// TODO Auto-generated method stub
		sqlPlayer s = this.getPlayerObject(player);
		if (s != null)
		{
			Date now = new Date();
		    String timenow = Long.toString(now.getTime());
		    
			s.setLastmeditate(timenow);
			this.getDatabase().save(s);

		} else {
			System.out.println("[RPChat] ERROR: Could not find player to set last meditate time");
		}
	}
	
	public int getPlayerTimeSinceLastMeditate(Player player)
	{
		sqlPlayer s = this.getPlayerObject(player);
		if (s != null)
		{
			if (s.getLastmeditate().equals(""))
			{
				return 30;
			} else {
				Date now = new Date();
			    String timenow = Long.toString(now.getTime());
			    Date then = new Date(Long.parseLong(s.getLastmeditate()));
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
		sqlPlayer splayer = this.getPlayerObject(player);
		if (splayer != null)
		{
			if (splayer.getOptedin().equals("true"))
			{
				return true;
			}
		}
		
		return false;
	}
}
