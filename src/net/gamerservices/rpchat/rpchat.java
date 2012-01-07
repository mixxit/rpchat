package net.gamerservices.rpchat;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import com.palmergames.bukkit.towny.Towny;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.PersistenceException;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class rpchat extends JavaPlugin
{
  private PluginManager pm;
  private Towny towny = null;
	
  public void onDisable()
  {
	PluginDescriptionFile desc = getDescription();
	System.out.println(desc.getFullName() + " has been disabled");
  }

  public void onEnable()
  {
	PluginDescriptionFile desc = getDescription();
	System.out.println(desc.getFullName() + " has been enabled");
	
	
	pm = getServer().getPluginManager();
	Plugin test;

  
	checkPlugins();
	
	/*
	 *  This executes the task with a 1 tick delay
	 *  avoiding the bukkit depends bug.
	 */
	if ((towny == null) || (getServer().getScheduler().scheduleSyncDelayedTask(this, new onLoadedTask(this),1) == -1)){
		/*
		 *  We either failed to find Towny
		 *  or the Scheduler failed to register the task.
		 */
		System.out.println("SEVERE - Could not schedule onLoadedTask.");
		pm.disablePlugin(this);
	}

	
	getCommand("local").setExecutor(new LocalMessage(this));
	getCommand("ooc").setExecutor(new OOCMessage(this));
	
		
  }
  protected Towny getTowny() {
		return towny;
  }
	
  boolean onlyletters(String string) {
	  return string.matches("^[a-zA-Z]+$");
  }
  
  private void checkPlugins() {
      Plugin test;

      test = pm.getPlugin("Towny");
      if (test != null && test instanceof Towny)
      	towny = (Towny)test;

	}

  public String capitalise(String string)
  {
	if (string == null)
		throw new NullPointerException("string");
	if (string.equals(""))
		throw new NullPointerException("string");
	return Character.toUpperCase(string.charAt(0)) + string.substring(1);
  }
}
