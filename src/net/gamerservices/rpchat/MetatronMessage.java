/*    */ package net.gamerservices.rpchat;
/*    */ 
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class MetatronMessage
/*    */   implements CommandExecutor
/*    */ {
/*    */   rpchat plugin;
/*    */ 
/*    */   public MetatronMessage(rpchat rpchat)
/*    */   {
/* 15 */     this.plugin = rpchat;
/*    */   }
/*    */ 
/*    */   public static String arrayToString(String[] a, String separator) {
/* 19 */     String result = "";
/* 20 */     if (a.length > 0) {
/* 21 */       result = a[0];
/* 22 */       for (int i = 1; i < a.length; i++) {
/* 23 */         result = result + separator + a[i];
/*    */       }
/*    */     }
/* 26 */     return result;
/*    */   }
/*    */ 
/*    */   public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3)
/*    */   {
/* 33 */     if (arg0.isOp())
/*    */     {
/* 35 */       String message = arrayToString(arg3, " ");
/* 36 */       for (World w : arg0.getServer().getWorlds())
/*    */       {
/* 38 */         for (Player p : w.getPlayers())
/*    */         {
/* 40 */           p.sendMessage(ChatColor.RED + "BEHOLD! The Metatron has arrived with a message from " + arg0.getName());
/* 41 */           p.sendMessage("§K" + message);
/* 42 */           p.sendMessage(ChatColor.RED + "With the holy message delivered the Metatron returns on high");
/*    */         }
/*    */       }
/*    */     }
/*    */     else {
/* 47 */       arg0.sendMessage("You may not summon the metatron");
/*    */     }
/*    */ 
/* 50 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\end\Desktop\rpchatlite.jar
 * Qualified Name:     net.gamerservices.rpchat.MetatronMessage
 * JD-Core Version:    0.6.0
 */