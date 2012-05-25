package net.gamerservices.rpchat;

import com.earth2me.essentials.Essentials;
import com.palmergames.bukkit.towny.Towny;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.PersistenceException;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class rpchat extends JavaPlugin
{
  private PluginManager pm;
  private Towny towny = null;
  private Listener rpchatPlayerListener;
  private Listener rpchatEntityListener;
  public static Permission permission;
  private Essentials essentials = null;
  public boolean isEssentials = false;
  static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

  static
  {
     permission = null;
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

     if ((this.towny == null) || (getServer().getScheduler().scheduleSyncDelayedTask(this, new onLoadedTask(this), 1L) == -1))
     {
       System.out.println("SEVERE - Could not schedule onLoadedTask.");
       this.pm.disablePlugin(this);
     }

     getCommand("local").setExecutor(new LocalMessage(this));
     getCommand("ooc").setExecutor(new OOCMessage(this));
     getCommand("global").setExecutor(new GlobalMessage(this));
     getCommand("townchat").setExecutor(new TownMessage(this));
     getCommand("racechat").setExecutor(new RaceMessage(this));
     getCommand("race").setExecutor(new SetRace(this));
     getCommand("metatron").setExecutor(new MetatronMessage(this));
	 getCommand("name").setExecutor(new SetName(this));
	 getCommand("lastname").setExecutor(new SetLastName(this));
	 getCommand("findname").setExecutor(new FindName(this));
	 getCommand("vote").setExecutor(new SetVote(this));
	 getCommand("doelection").setExecutor(new SetElection(this));
	 getCommand("defaultchannel").setExecutor(new SetDefaultChannel(this));
	 getCommand("setcapital").setExecutor(new SetCapital(this));
	 getCommand("capital").setExecutor(new GotoCapital(this));
	 getCommand("alliancechat").setExecutor(new AllianceMessage(this));
	 getCommand("title").setExecutor(new SetTitle(this));
	 getCommand("emote").setExecutor(new EmoteMessage(this));
	 registerEvents();
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
	
	public int getVoteCount(String name)
	{
		List<sqlPlayer> players = getDatabase().find(sqlPlayer.class).where().ieq("vote", name).findList();
		int count = 0;
		for (sqlPlayer p : players){
            count++;
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
	
	private boolean isKing(Player player) {
		
	
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
		if (sPlayerme.getExperience() < 100000000)
		{
			sPlayerme.setExperience(sPlayerme.getExperience() + 1);
			this.getDatabase().save(sPlayerme);
			System.out.println("[RPChat] Player " + attacker.getDisplayName() + "("+attacker.getName()+") earned experience!");
			attacker.sendMessage(ChatColor.YELLOW + "* You gained experience!");
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
}
