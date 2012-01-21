/*     */ package net.gamerservices.rpchat;
/*     */ 
/*     */ import com.palmergames.bukkit.towny.Towny;
/*     */ import java.io.PrintStream;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandExecutor;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public class LocalMessage
/*     */   implements CommandExecutor
/*     */ {
/*     */   private rpchat parent;
/*     */   private Towny towny;
/*     */ 
/*     */   public LocalMessage(rpchat rpchat)
/*     */   {
/*  20 */     this.parent = rpchat;
/*  21 */     this.towny = this.parent.getTowny();
/*     */   }
/*     */ 
/*     */   public static String arrayToString(String[] a, String separator) {
/*  25 */     String result = "";
/*  26 */     if (a.length > 0) {
/*  27 */       result = a[0];
/*  28 */       for (int i = 1; i < a.length; i++) {
/*  29 */         result = result + separator + a[i];
/*     */       }
/*     */     }
/*  32 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3)
/*     */   {
/*  39 */     String message = arrayToString(arg3, " ");
/*     */     try
/*     */     {
/*  43 */       Player player = this.parent.getServer().getPlayer(arg0.getName());
/*     */ 
/*  46 */       if (message.compareTo("") == 0)
/*     */       {
/*  48 */         return false;
/*     */       }
/*     */ 
/*  51 */       sendLocal(player, message);
/*  52 */       return true;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  58 */       System.out.println("[RPChatError]: " + e.getMessage());
/*     */     }
/*     */ 
/*  62 */     return false;
/*     */   }
/*     */ 
/*     */   public void sendLocal(Player player, String message)
/*     */   {
/*  67 */     if (this.parent.isMuted(player))
/*     */     {
/*  70 */       return;
/*     */     }
/*     */ 
/*  73 */     String race = "";
/*  74 */     race = this.parent.getPlayerRace(player);
/*     */ 
/*  76 */     String tag = "";
/*     */     try
/*     */     {
/*  79 */       tag = this.parent.getGroups(player);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  84 */       tag = "Refugee";
/*     */     }
/*     */ 
/*  88 */     int count = 0;
/*     */ 
/*  90 */     for (Player p : player.getWorld().getPlayers())
/*     */     {
/*  92 */       if (p.equals(player))
/*     */       {
/*  95 */         if (p.getWorld().getName().compareTo("Redstone") == 0)
/*     */         {
/*  98 */           p.sendMessage("[" + tag + "][" + race + "] " + ChatColor.WHITE + player.getDisplayName() + ChatColor.YELLOW + " says '" + message + "'");
/*     */         }
/* 100 */         else p.sendMessage("[" + tag + "][" + race + "] " + ChatColor.WHITE + player.getDisplayName() + ChatColor.YELLOW + " says '" + message + "'");
/*     */ 
/*     */       }
/* 107 */       else if (p.getWorld().getName().compareTo("Redstone") == 0)
/*     */       {
/* 110 */         p.sendMessage("[" + tag + "][" + race + "] " + ChatColor.WHITE + player.getDisplayName() + ChatColor.YELLOW + " says '" + message + "'");
/* 111 */         count++;
/*     */       }
/*     */       else
/*     */       {
/* 116 */         double x1 = p.getLocation().getX();
/* 117 */         double y1 = p.getLocation().getY();
/* 118 */         double z1 = p.getLocation().getZ();
/*     */ 
/* 120 */         double x2 = player.getLocation().getX();
/* 121 */         double y2 = player.getLocation().getY();
/* 122 */         double z2 = player.getLocation().getZ();
/*     */ 
/* 124 */         int xdist = (int)(x1 - x2);
/* 125 */         int ydist = (int)(y1 - y2);
/* 126 */         int zdist = (int)(z1 - z2);
/*     */ 
/* 128 */         if ((xdist < -100) || (xdist > 100) || (ydist < -100) || (ydist > 100) || (zdist < -100) || (zdist > 100))
/*     */         {
/*     */           continue;
/*     */         }
/*     */ 
/* 133 */         p.sendMessage("[" + tag + "][" + race + "] " + ChatColor.WHITE + player.getDisplayName() + ChatColor.YELLOW + " says '" + message + "'");
/* 134 */         count++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 141 */     if (count < 1)
/*     */     {
/* 143 */       player.sendMessage(ChatColor.GRAY + "* You speak but nobody hears you (Use worldwide /ooc <msg> instead.)");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\end\Desktop\rpchatlite.jar
 * Qualified Name:     net.gamerservices.rpchat.LocalMessage
 * JD-Core Version:    0.6.0
 */