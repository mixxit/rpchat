/*     */ package net.gamerservices.rpchat;
/*     */ 
/*     */ import com.palmergames.bukkit.towny.Towny;
/*     */ import java.io.PrintStream;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandExecutor;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public class GlobalMessage
/*     */   implements CommandExecutor
/*     */ {
/*     */   private Towny towny;
/*     */   private rpchat parent;
/*     */ 
/*     */   public GlobalMessage(rpchat rpchat)
/*     */   {
/*  25 */     this.parent = rpchat;
/*  26 */     this.towny = this.parent.getTowny();
/*     */   }
/*     */ 
/*     */   public static String arrayToString(String[] a, String separator) {
/*  30 */     String result = "";
/*  31 */     if (a.length > 0) {
/*  32 */       result = a[0];
/*  33 */       for (int i = 1; i < a.length; i++) {
/*  34 */         result = result + separator + a[i];
/*     */       }
/*     */     }
/*  37 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3)
/*     */   {
/*  44 */     String message = arrayToString(arg3, " ");
/*     */     try
/*     */     {
/*  48 */       Player player = this.parent.getServer().getPlayer(arg0.getName());
/*  49 */       if (message.compareTo("") > 0)
/*     */       {
/*  51 */         sendGlobal(player, message);
/*  52 */         return true;
/*     */       }
/*  54 */       return false;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  61 */       System.out.println("[RPChat Error]: " + e.getMessage());
/*     */     }
/*     */ 
/*  64 */     return false;
/*     */   }
/*     */ 
/*     */   public void sendGlobal(Player player, String message)
/*     */   {
/*  70 */     if (this.parent.isMuted(player))
/*     */     {
/*  73 */       return;
/*     */     }
/*     */ 
/*  76 */     String race = "";
/*  77 */     race = this.parent.getPlayerRace(player);
/*     */ 
/*  79 */     String tag = "";
/*     */     try
/*     */     {
/*  82 */       tag = this.parent.getGroups(player);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  86 */       System.out.println(e.getMessage());
/*  87 */       tag = "Refugee";
/*     */     }
/*     */ 
/*  91 */     int count = 0;
/*     */ 
/*  93 */     for (World w : player.getServer().getWorlds())
/*     */     {
/*  95 */       for (Player p : w.getPlayers())
/*     */       {
/*  97 */         if (p.equals(player))
/*     */         {
/* 100 */           p.sendMessage("[" + tag + "][" + race + "] " + ChatColor.WHITE + player.getDisplayName() + ChatColor.WHITE + ": " + message);
/*     */         } else {
/* 102 */           p.sendMessage("[" + tag + "][" + race + "] " + ChatColor.WHITE + player.getDisplayName() + ChatColor.WHITE + ": " + message);
/*     */ 
/* 105 */           count++;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 111 */     if (count < 1)
/*     */     {
/* 113 */       player.sendMessage(ChatColor.GRAY + "* You speak but nobody is online.");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\end\Desktop\rpchatlite.jar
 * Qualified Name:     net.gamerservices.rpchat.GlobalMessage
 * JD-Core Version:    0.6.0
 */