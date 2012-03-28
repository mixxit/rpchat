/*    */ package net.gamerservices.rpchat;
/*    */ 
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Chunk;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
/*    */ 
/*    */ public class RPchatEntityListener implements Listener
/*    */ {
/*    */   private rpchat plugin;
/*    */ 
/*    */   public RPchatEntityListener(rpchat rpchat)
/*    */   {
/* 18 */     this.plugin = rpchat;
/*    */   }
/*    */   @EventHandler(priority = EventPriority.NORMAL)
/*    */   public void onEntityDamage(EntityDamageEvent event)
/*    */   {
/* 24 */     if (!(event instanceof EntityDamageByEntityEvent))
/*    */     {
/* 26 */       return;
/*    */     }
/*    */ 
/* 29 */     if (!(event.getEntity() instanceof Player))
/*    */     {
/* 31 */       return;
/*    */     }
/*    */ 
/* 35 */     EntityDamageByEntityEvent damagecause = (EntityDamageByEntityEvent)event;
/* 36 */     if (!(damagecause.getDamager() instanceof Player))
/*    */     {
/* 38 */       return;
/*    */     }
/*    */ 
/* 41 */     Player victim = (Player)event.getEntity();
/* 42 */     Player attacker = (Player)damagecause.getDamager();
/*    */ 
/* 45 */     String victimrace = this.plugin.getPlayerRace(victim);
/* 46 */     String attackerrace = this.plugin.getPlayerRace(attacker);
/*    */ 
/* 48 */     if (victimrace.equals(attackerrace))
/*    */     {
/* 50 */       attacker.sendMessage("* You are attacking a player of the same race!");
/*    */ 
/* 52 */       if (victim.getHealth() > damagecause.getDamage())
/*    */       {
/* 54 */         return;
/*    */       }
/*    */ 
/* 57 */       return;
/*    */     }
/*    */ 
/* 62 */     if (victim.getHealth() > damagecause.getDamage())
/*    */     {
/* 64 */       return;
/*    */     }
/*    */ 
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\end\Desktop\rpchatlite.jar
 * Qualified Name:     net.gamerservices.rpchat.RPchatEntityListener
 * JD-Core Version:    0.6.0
 */