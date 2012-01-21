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
/*    */ public class TownMessage
/*    */   implements CommandExecutor
/*    */ {
/*    */   private Towny towny;
/*    */   private rpchat parent;
/*    */ 
/*    */   public TownMessage(rpchat rpchat)
/*    */   {
/* 24 */     this.parent = rpchat;
/* 25 */     this.towny = this.parent.getTowny();
/*    */   }
/*    */ 
/*    */   public static String arrayToString(String[] a, String separator) {
/* 29 */     String result = "";
/* 30 */     if (a.length > 0) {
/* 31 */       result = a[0];
/* 32 */       for (int i = 1; i < a.length; i++) {
/* 33 */         result = result + separator + a[i];
/*    */       }
/*    */     }
/* 36 */     return result;
/*    */   }
/*    */ 
/*    */   public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3)
/*    */   {
/* 43 */     String message = arrayToString(arg3, " ");
/*    */     try
/*    */     {
/* 47 */       Player player = this.parent.getServer().getPlayer(arg0.getName());
/* 48 */       if (message.compareTo("") > 0)
/*    */       {
/* 50 */         sendTownChat(player, message);
/* 51 */         return true;
/*    */       }
/* 53 */       return false;
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 60 */       System.out.println("[RPChat Error]: " + e.getMessage());
/*    */     }
/*    */ 
/* 63 */     return false;
/*    */   }
/*    */ 
/*    */   public void sendTownChat(Player player, String message)
/*    */   {
/* 73 */     if (this.parent.isMuted(player))
/*    */     {
/* 76 */       return;
/*    */     }
/*    */ 
/* 79 */     String race = "";
/* 80 */     race = this.parent.getPlayerRace(player);
/*    */     try
/*    */     {
/* 84 */       Resident res = this.towny.getTownyUniverse().getResident(player.getName());
/* 85 */       String tag = "";
/*    */       try
/*    */       {
/* 88 */         tag = this.parent.getGroups(player);
/*    */       }
/*    */       catch (Exception e)
/*    */       {
/* 93 */         tag = "Refugee";
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
/* 105 */         player.sendMessage("You cannot send a message to your town as you are not in one.");
/* 106 */         return;
/*    */       }
/*    */ 
/* 109 */       String nation = "";
/*    */       try
/*    */       {
/* 113 */         nation = res.getTown().getNation().getName();
/*    */       }
/*    */       catch (Exception e)
/*    */       {
/* 117 */         nation = "";
/*    */       }
/*    */ 
/* 121 */       int count = 0;
/*    */ 
/* 123 */       for (World w : player.getServer().getWorlds())
/*    */       {
/* 125 */         for (Player p : w.getPlayers())
/*    */         {
/* 127 */           if (p.equals(player))
/*    */           {
/* 130 */             p.sendMessage("[" + tag + "][" + race + "] " + ChatColor.WHITE + player.getDisplayName() + ChatColor.AQUA + ": " + message);
/*    */           }
/*    */           else {
/* 133 */             Resident targetres = this.towny.getTownyUniverse().getResident(p.getName());
/*    */             try
/*    */             {
/* 137 */               if (!targetres.getTown().equals(res.getTown()))
/*    */                 continue;
/* 139 */               p.sendMessage("[" + tag + "][" + race + "] " + ChatColor.WHITE + player.getDisplayName() + ChatColor.AQUA + ": " + message);
/*    */ 
/* 141 */               count++;
/*    */             }
/*    */             catch (Exception localException1)
/*    */             {
/*    */             }
/*    */ 
/*    */           }
/*    */ 
/*    */         }
/*    */ 
/*    */       }
/*    */ 
/* 156 */       if (count < 1)
/*    */       {
/* 158 */         player.sendMessage(ChatColor.GRAY + "* You speak but nobody hears you (There is no-one in this world, try another world or use global chat)");
/*    */       }
/*    */     }
/*    */     catch (NotRegisteredException localNotRegisteredException)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\end\Desktop\rpchatlite.jar
 * Qualified Name:     net.gamerservices.rpchat.TownMessage
 * JD-Core Version:    0.6.0
 */