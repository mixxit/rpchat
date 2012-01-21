/*     */ package net.gamerservices.rpchat;
/*     */ 
/*     */ import com.avaje.ebean.validation.Length;
/*     */ import com.avaje.ebean.validation.NotEmpty;
/*     */ import javax.persistence.Entity;
/*     */ import javax.persistence.Id;
/*     */ import javax.persistence.Table;
/*     */ 
/*     */ @Entity
/*     */ @Table(name="fantasyraces_player")
/*     */ public class sqlPlayer
/*     */ {
/*     */ 
/*     */   @Id
/*     */   private int id;
/*     */ 
/*     */   @Length(max=16)
/*     */   @NotEmpty
/*  19 */   private String name = "";
/*     */ 
/*     */   @Length(max=32)
/*     */   @NotEmpty
/*  23 */   private String display = "";
/*     */ 
/*     */   @Length(max=32)
/*  26 */   private String lastname = "";
/*     */ 
/*     */   @Length(max=32)
/*  29 */   private String prefix = "";
/*     */ 
/*     */   @Length(max=32)
/*  32 */   private String title = "";
/*     */ 
/*     */   @Length(max=32)
/*     */   @NotEmpty
/*  36 */   private String race = "";
/*     */ 
/*     */   @Length(max=32)
/*     */   @NotEmpty
/*  40 */   private String language = "";
/*     */ 
/*     */   @Length(max=32)
/*  43 */   private String flags = "";
/*     */   private int killcount;
/*     */ 
/*  49 */   public void setId(int id) { this.id = id; }
/*     */ 
/*     */   public int getId()
/*     */   {
/*  53 */     return this.id;
/*     */   }
/*     */ 
/*     */   public void setKillcount(int count)
/*     */   {
/*  58 */     this.killcount = this.killcount;
/*     */   }
/*     */ 
/*     */   public int getKillcount() {
/*  62 */     return this.killcount;
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  66 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String name) {
/*  70 */     this.name = name;
/*     */   }
/*     */ 
/*     */   public String getTitle() {
/*  74 */     if (this.title == null)
/*     */     {
/*  76 */       return "";
/*     */     }
/*  78 */     return this.title;
/*     */   }
/*     */ 
/*     */   public void setTitle(String title)
/*     */   {
/*  83 */     this.title = title;
/*     */   }
/*     */ 
/*     */   public String getPrefix() {
/*  87 */     return this.prefix;
/*     */   }
/*     */ 
/*     */   public void setPrefix(String prefix) {
/*  91 */     this.prefix = prefix;
/*     */   }
/*     */ 
/*     */   public String getLastname() {
/*  95 */     return this.lastname;
/*     */   }
/*     */ 
/*     */   public void setLastname(String lastname) {
/*  99 */     this.lastname = lastname;
/*     */   }
/*     */ 
/*     */   public String getDisplay() {
/* 103 */     return this.display;
/*     */   }
/*     */ 
/*     */   public void setDisplay(String displayname) {
/* 107 */     this.display = displayname;
/*     */   }
/*     */ 
/*     */   public String getFlags() {
/* 111 */     return this.flags;
/*     */   }
/*     */ 
/*     */   public void setFlags(String flags) {
/* 115 */     this.flags = flags;
/*     */   }
/*     */ 
/*     */   public String getRace()
/*     */   {
/* 120 */     return this.race;
/*     */   }
/*     */ 
/*     */   public void setRace(String race) {
/* 124 */     this.race = race;
/*     */   }
/*     */ 
/*     */   public String getLanguage() {
/* 128 */     return this.language;
/*     */   }
/*     */ 
/*     */   public void setLanguage(String language) {
/* 132 */     this.language = language;
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\end\Desktop\rpchatlite.jar
 * Qualified Name:     net.gamerservices.rpchat.sqlPlayer
 * JD-Core Version:    0.6.0
 */