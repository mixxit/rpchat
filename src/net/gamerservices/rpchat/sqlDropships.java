/*    */ package net.gamerservices.rpchat;
/*    */ 
/*    */ import com.avaje.ebean.validation.Length;
/*    */ import com.avaje.ebean.validation.NotEmpty;
/*    */ import javax.persistence.Entity;
/*    */ import javax.persistence.Id;
/*    */ import javax.persistence.Table;
/*    */ 
/*    */ @Entity
/*    */ @Table(name="fantasyraces_dropships")
/*    */ public class sqlDropships
/*    */ {
/*    */ 
/*    */   @Id
/*    */   private int id;
/*    */ 
/*    */   @Length(max=32)
/*    */   @NotEmpty
/*    */   private String name;
/*    */ 
/*    */   @Length(max=32)
/*    */   @NotEmpty
/* 24 */   private String world = "";
/*    */ 
/*    */   @Length(max=32)
/* 27 */   private String x = "";
/*    */ 
/*    */   @Length(max=32)
/* 30 */   private String y = "";
/*    */ 
/*    */   @Length(max=32)
/*    */   @NotEmpty
/* 34 */   private String z = "";
/*    */ 
/*    */   public void setId(int id)
/*    */   {
/* 38 */     this.id = id;
/*    */   }
/*    */ 
/*    */   public int getId() {
/* 42 */     return this.id;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 46 */     return this.name;
/*    */   }
/*    */ 
/*    */   public void setName(String name) {
/* 50 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public String getX() {
/* 54 */     return this.x;
/*    */   }
/*    */ 
/*    */   public void setX(String x) {
/* 58 */     this.x = x;
/*    */   }
/*    */ 
/*    */   public String getY() {
/* 62 */     return this.y;
/*    */   }
/*    */ 
/*    */   public void setY(String y) {
/* 66 */     this.y = y;
/*    */   }
/*    */ 
/*    */   public String getZ() {
/* 70 */     return this.z;
/*    */   }
/*    */ 
/*    */   public void setZ(String z) {
/* 74 */     this.z = z;
/*    */   }
/*    */ 
/*    */   public void setWorld(String world)
/*    */   {
/* 79 */     this.world = world;
/*    */   }
/*    */   public String getWorld() {
/* 82 */     return this.world;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\end\Desktop\rpchatlite.jar
 * Qualified Name:     net.gamerservices.rpchat.sqlDropships
 * JD-Core Version:    0.6.0
 */