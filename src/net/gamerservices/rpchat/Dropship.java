/*    */ package net.gamerservices.rpchat;
/*    */ 
/*    */ import com.avaje.ebean.EbeanServer;
/*    */ import com.avaje.ebean.ExpressionList;
/*    */ import com.avaje.ebean.Query;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class Dropship
/*    */   implements CommandExecutor
/*    */ {
/*    */   rpchat plugin;
/*    */ 
/*    */   public Dropship(rpchat rpchat)
/*    */   {
/* 14 */     this.plugin = rpchat;
/*    */   }
/*    */ 
/*    */   public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3)
/*    */   {
/* 23 */     if (arg3.length == 0)
/*    */     {
/* 25 */       return false;
/*    */     }
/*    */ 
/* 28 */     String dropshipcode = arg3[0];
/* 29 */     String dropshipid = Long.toString(rpchat.decode(dropshipcode));
/*    */ 
/* 31 */     sqlDropships sDropship = (sqlDropships)this.plugin.getDatabase().find(sqlDropships.class).where().ieq("name", dropshipid).findUnique();
/* 32 */     if (sDropship == null) {
/* 33 */       arg0.sendMessage(" * Dropship ID does not exist");
/* 34 */       return true;
/*    */     }
/* 36 */     Player player = (Player)arg0;
/* 37 */     arg0.sendMessage(" * You are drop shipped by the fleet!");
/* 38 */     Location loc = new Location(this.plugin.getServer().getWorld(sDropship.getWorld()), Double.parseDouble(sDropship.getX()), Double.parseDouble(sDropship.getY()), Double.parseDouble(sDropship.getZ()));
/* 39 */     player.teleport(loc);
/* 40 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\end\Desktop\rpchatlite.jar
 * Qualified Name:     net.gamerservices.rpchat.Dropship
 * JD-Core Version:    0.6.0
 */