/*    */ package net.gamerservices.rpchat;
/*    */ 
/*    */ import com.palmergames.bukkit.towny.NotRegisteredException;
/*    */ import com.palmergames.bukkit.towny.Towny;
/*    */ import com.palmergames.bukkit.towny.object.Nation;
/*    */ import com.palmergames.bukkit.towny.object.Resident;
/*    */ import com.palmergames.bukkit.towny.object.Town;
/*    */ import com.palmergames.bukkit.towny.object.TownyUniverse;
/*    */ import java.io.PrintStream;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class OOCMessage
/*    */   implements CommandExecutor
/*    */ {
/*    */   private Towny towny;
/*    */   private rpchat parent;
/*    */ 
/*    */   public OOCMessage(rpchat rpchat)
/*    */   {
/* 23 */     this.parent = rpchat;
/* 24 */     this.towny = this.parent.getTowny();
/*    */   }
/*    */ 
/*    */   public static String arrayToString(String[] a, String separator) {
/* 28 */     String result = "";
/* 29 */     if (a.length > 0) {
/* 30 */       result = a[0];
/* 31 */       for (int i = 1; i < a.length; i++) {
/* 32 */         result = result + separator + a[i];
/*    */       }
/*    */     }
/* 35 */     return result;
/*    */   }
/*    */ 
/*    */   public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3)
/*    */   {
/* 42 */     String message = arrayToString(arg3, " ");
/*    */     try
/*    */     {
/* 46 */       Player player = this.parent.getServer().getPlayer(arg0.getName());
/* 47 */       if (message.compareTo("") > 0)
/*    */       {
/* 49 */         sendOOC(player, message);
/* 50 */         return true;
/*    */       }
/* 52 */       return false;
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 59 */       System.out.println("[RPChat Error]: " + e.getMessage());
/*    */     }
/*    */ 
/* 62 */     return false;
/*    */   }
/*    */ 
/*    */   public void sendOOC(Player player, String message)
/*    */   {
/* 72 */     if (this.parent.isMuted(player))
/*    */     {
/* 75 */       return;
/*    */     }
/*    */ 
/* 78 */     String race = "";
/* 79 */     race = this.parent.getPlayerRace(player);
/*    */     try
/*    */     {
/* 83 */       Resident res = this.towny.getTownyUniverse().getResident(player.getName());
/* 84 */       String tag = "";
/*    */       try
/*    */       {
/* 87 */         tag = this.parent.getGroups(player);
/*    */       }
/*    */       catch (Exception e)
/*    */       {
/* 92 */         tag = "Refugee";
/*    */       }
/*    */ 
/* 96 */       String town = "";
/*    */       try
/*    */       {
/* 100 */         town = res.getTown().getName();
/*    */       }
/*    */       catch (Exception e)
/*    */       {
/* 104 */         town = "";
/*    */       }
/*    */ 
/* 107 */       String nation = "";
/*    */       try
/*    */       {
/* 111 */         nation = res.getTown().getNation().getName();
/*    */       }
/*    */       catch (Exception e)
/*    */       {
/* 115 */         nation = "";
/*    */       }
/*    */ 
/* 119 */       int count = 0;
/*    */ 
/* 121 */       for (Player p : player.getWorld().getPlayers())
/*    */       {
/* 123 */         if (p.equals(player))
/*    */         {
/* 126 */           p.sendMessage("[" + tag + "][" + race + "] " + ChatColor.WHITE + player.getDisplayName() + ": " + ChatColor.LIGHT_PURPLE + message);
/*    */         }
/*    */         else
/*    */         {
/* 131 */           p.sendMessage("[" + tag + "][" + race + "] " + ChatColor.WHITE + player.getDisplayName() + ": " + ChatColor.LIGHT_PURPLE + message);
/* 132 */           count++;
/*    */         }
/*    */       }
/*    */ 
/* 136 */       if (count < 1)
/*    */       {
/* 138 */         player.sendMessage(ChatColor.GRAY + "* You speak but nobody hears you (There is no-one in this world, try another world or use global chat)");
/*    */       }
/*    */     }
/*    */     catch (NotRegisteredException localNotRegisteredException)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\end\Desktop\rpchatlite.jar
 * Qualified Name:     net.gamerservices.rpchat.OOCMessage
 * JD-Core Version:    0.6.0
 */