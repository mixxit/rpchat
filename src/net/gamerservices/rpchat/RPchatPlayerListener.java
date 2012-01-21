/*    */ package net.gamerservices.rpchat;
/*    */ 
/*    */ import com.avaje.ebean.EbeanServer;
/*    */ import com.avaje.ebean.ExpressionList;
/*    */ import com.avaje.ebean.Query;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.player.PlayerChatEvent;
/*    */ import org.bukkit.event.player.PlayerJoinEvent;
/*    */ import org.bukkit.event.player.PlayerListener;
/*    */ 
/*    */ public class RPchatPlayerListener extends PlayerListener
/*    */ {
/*    */   private rpchat plugin;
/*    */ 
/*    */   public void onPlayerJoin(PlayerJoinEvent event)
/*    */   {
/* 19 */     sqlPlayer sPlayer = (sqlPlayer)this.plugin.getDatabase().find(sqlPlayer.class).where().ieq("name", event.getPlayer().getName()).findUnique();
/* 20 */     if (sPlayer == null) {
/* 21 */       sPlayer = new sqlPlayer();
/* 22 */       sPlayer.setName(event.getPlayer().getName());
/*    */ 
/* 24 */       sPlayer.setDisplay(event.getPlayer().getName());
/* 25 */       sPlayer.setRace("human");
/* 26 */       sPlayer.setLanguage("human");
/*    */ 
/* 29 */       this.plugin.getDatabase().save(sPlayer);
/*    */     }
/*    */ 
/* 33 */     if (!sPlayer.getFlags().equals("done"))
/*    */     {
/* 35 */       event.getPlayer().sendMessage(ChatColor.RED + "*********************************************************");
/* 36 */       event.getPlayer().sendMessage(ChatColor.RED + "You currently have NO race set - use /race to set one");
/* 37 */       event.getPlayer().sendMessage(ChatColor.RED + "*********************************************************");
/*    */     }
/*    */   }
/*    */ 
/*    */   public RPchatPlayerListener(rpchat rpchat)
/*    */   {
/* 44 */     this.plugin = rpchat;
/*    */   }
/*    */ 
/*    */   public void onPlayerChat(PlayerChatEvent event)
/*    */   {
/* 50 */     DoGlobalMessage(event.getPlayer(), event.getMessage());
/* 51 */     event.setCancelled(true);
/*    */   }
/*    */ 
/*    */   public void DoGlobalMessage(Player playername, String message)
/*    */   {
/* 57 */     GlobalMessage gm = new GlobalMessage(this.plugin);
/* 58 */     gm.sendGlobal(playername, message);
/*    */   }
/*    */ 
/*    */   public void DoOOCMessage(Player playername, String message)
/*    */   {
/* 64 */     OOCMessage ooc = new OOCMessage(this.plugin);
/* 65 */     ooc.sendOOC(playername, message);
/*    */   }
/*    */ 
/*    */   public void DoLocalMessage(Player playername, String message)
/*    */   {
/* 71 */     LocalMessage lm = new LocalMessage(this.plugin);
/* 72 */     lm.sendLocal(playername, message);
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\end\Desktop\rpchatlite.jar
 * Qualified Name:     net.gamerservices.rpchat.RPchatPlayerListener
 * JD-Core Version:    0.6.0
 */