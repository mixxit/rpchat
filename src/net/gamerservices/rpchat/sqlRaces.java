/*    */ package net.gamerservices.rpchat;
/*    */ 
/*    */ import com.avaje.ebean.validation.Length;
/*    */ import com.avaje.ebean.validation.NotEmpty;
/*    */ import javax.persistence.Entity;
/*    */ import javax.persistence.Id;
/*    */ import javax.persistence.Table;
/*    */ 
/*    */ @Entity
/*    */ @Table(name="fantasyraces_races")
/*    */ public class sqlRaces
/*    */ {
/*    */ 
/*    */   @Id
/*    */   private int id;
/*    */ 
/*    */   @Length(max=16)
/*    */   @NotEmpty
/*    */   private String name;
/*    */ 
/*    */   @Length(max=32)
/* 22 */   private String capital = "";
/*    */ 
/*    */   @Length(max=64)
/* 25 */   private String capitalloc = "";
/*    */ 
/*    */   @Length(max=32)
/* 29 */   private String description = "";
/*    */ 
/*    */   @Length(max=32)
/* 32 */   private String leader = "";
/*    */ 
/*    */   @Length(max=32)
/* 35 */   private String flags = "";
/*    */ 
/*    */   public void setId(int id)
/*    */   {
/* 39 */     this.id = id;
/*    */   }
/*    */ 
/*    */   public int getId() {
/* 43 */     return this.id;
/*    */   }
/*    */ 
/*    */   public void setName(String name)
/*    */   {
/* 48 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 52 */     return this.name;
/*    */   }
/*    */ 
/*    */   public void setCapital(String capital)
/*    */   {
/* 57 */     this.capital = capital;
/*    */   }
/*    */ 
/*    */   public String getCapital() {
/* 61 */     return this.capital;
/*    */   }
/*    */ 
/*    */   public void setCapitalloc(String loc)
/*    */   {
/* 66 */     this.capitalloc = loc;
/*    */   }
/*    */ 
/*    */   public String getCapitalloc() {
/* 70 */     return this.capitalloc;
/*    */   }
/*    */ 
/*    */   public void setDescription(String description)
/*    */   {
/* 75 */     this.description = description;
/*    */   }
/*    */ 
/*    */   public String getDescription() {
/* 79 */     return this.description;
/*    */   }
/*    */ 
/*    */   public void setLeader(String leader)
/*    */   {
/* 84 */     this.leader = leader;
/*    */   }
/*    */ 
/*    */   public String getLeader() {
/* 88 */     return this.leader;
/*    */   }
/*    */ 
/*    */   public String getFlags() {
/* 92 */     return this.flags;
/*    */   }
/*    */ 
/*    */   public void setFlags(String flags) {
/* 96 */     this.flags = flags;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\end\Desktop\rpchatlite.jar
 * Qualified Name:     net.gamerservices.rpchat.sqlRaces
 * JD-Core Version:    0.6.0
 */