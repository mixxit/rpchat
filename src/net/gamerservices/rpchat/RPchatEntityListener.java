/*    */ package net.gamerservices.rpchat;
/*    */ 
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Chunk;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ import org.bukkit.event.entity.EntityDamageEvent;
/*    */ import org.bukkit.event.entity.EntityListener;
/*    */ 
/*    */ public class RPchatEntityListener extends EntityListener
/*    */ {
/*    */   private rpchat plugin;
/*    */ 
/*    */   public RPchatEntityListener(rpchat rpchat)
/*    */   {
/* 18 */     this.plugin = rpchat;
/*    */   }
/*    */ 
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
/* 68 */     Location location = attacker.getLocation();
/* 69 */     String sector = location.getWorld().getName() + ":" + location.getChunk().getX() + "," + location.getChunk().getZ();
/*    */ 
/* 74 */     String tidyvictimrace = rpchat.capitalizeFirstLetters(victimrace);
/* 75 */     String tidyattackerrace = rpchat.capitalizeFirstLetters(attackerrace);
/*    */ 
/* 80 */     String dominance = this.plugin.getSectorDominator(sector);
/*    */ 
/* 82 */     if (dominance.equals("none"))
/*    */     {
/* 84 */       this.plugin.sendMessageToAll(ChatColor.RED + "Blood has been shed! " + attacker.getName() + " of The " + tidyattackerrace + " Empire has initiated a war against The " + tidyvictimrace + " Empire in sector " + sector + "!");
/*    */ 
/* 87 */       String dropshipid = this.plugin.addDropship(victim.getLocation().getWorld().getName(), victim.getLocation().getX(), victim.getLocation().getY(), victim.getLocation().getZ());
/*    */ 
/* 90 */       if (!dropshipid.equals(null))
/*    */       {
/* 92 */         this.plugin.sendMessageToAllExcept(victim, ChatColor.RED + "To be dropshipped use /dropship " + dropshipid + "'");
/*    */       }
/* 94 */       this.plugin.addSectorKill(sector, attackerrace);
/*    */     }
/*    */     else
/*    */     {
/* 98 */       if (!dominance.equals(victimrace)) {
/* 99 */         return;
/*    */       }
/* 101 */       this.plugin.sendMessageToRace(victimrace, ChatColor.GOLD + attacker.getName() + " of The " + tidyattackerrace + " Empire has initiated an invasion of one of your sectors!!! Sector: (" + sector + ")");
/*    */ 
/* 104 */       String dropshipid = this.plugin.addDropship(victim.getLocation().getWorld().getName(), victim.getLocation().getX(), victim.getLocation().getY(), victim.getLocation().getZ());
/*    */ 
/* 106 */       if (!dropshipid.equals(null))
/*    */       {
/* 108 */         this.plugin.sendMessageToRaceExcept(victim, victimrace, ChatColor.GOLD + "To be dropshipped use '/dropship " + dropshipid + "'");
/*    */       }
/* 110 */       this.plugin.addSectorKill(sector, attackerrace);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\end\Desktop\rpchatlite.jar
 * Qualified Name:     net.gamerservices.rpchat.RPchatEntityListener
 * JD-Core Version:    0.6.0
 */