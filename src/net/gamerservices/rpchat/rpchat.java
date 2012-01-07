package net.gamerservices.rpchat;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
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


public class rpchat extends JavaPlugin
{

  public void onDisable()
  {
	PluginDescriptionFile desc = getDescription();
	System.out.println(desc.getFullName() + " has been disabled");
  }

  public void onEnable()
  {
	PluginDescriptionFile desc = getDescription();
	System.out.println(desc.getFullName() + " has been enabled");
	
	getCommand("local").setExecutor(new LocalMessage(this));

  }

  boolean onlyletters(String string) {
	  return string.matches("^[a-zA-Z]+$");
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
