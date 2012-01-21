/*    */ package net.gamerservices.rpchat;
/*    */ 
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class Sector
/*    */   implements CommandExecutor
/*    */ {
/*    */   rpchat plugin;
/*    */ 
/*    */   public Sector(rpchat rpchat)
/*    */   {
/* 14 */     this.plugin = rpchat;
/*    */   }
/*    */ 
/*    */   public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3)
/*    */   {
/* 22 */     Player player = (Player)arg0;
/*    */ 
/* 24 */     String sectorname = this.plugin.getSectorName(player.getLocation());
/*    */ 
/* 26 */     player.sendMessage(ChatColor.GREEN + "Territory breakdown for: " + sectorname);
/* 27 */     player.sendMessage(ChatColor.GREEN + "Dominating race at this location: " + this.plugin.getSectorDominator(this.plugin.getSectorName(player.getLocation())));
/* 28 */     player.sendMessage(ChatColor.GREEN + "Race gains resource per tick: null");
/*    */ 
/* 30 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\end\Desktop\rpchatlite.jar
 * Qualified Name:     net.gamerservices.rpchat.Sector
 * JD-Core Version:    0.6.0
 */