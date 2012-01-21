/*     */ package net.gamerservices.rpchat;
/*     */ 
/*     */ import com.avaje.ebean.validation.Length;
/*     */ import com.avaje.ebean.validation.NotEmpty;
/*     */ import javax.persistence.Entity;
/*     */ import javax.persistence.Id;
/*     */ import javax.persistence.Table;
/*     */ 
/*     */ @Entity
/*     */ @Table(name="fantasyraces_sector")
/*     */ public class sqlSector
/*     */ {
/*     */ 
/*     */   @Id
/*     */   private int id;
/*     */ 
/*     */   @Length(max=32)
/*     */   @NotEmpty
/*  19 */   private String name = "";
/*     */ 
/*  21 */   private int human = 0;
/*     */ 
/*  23 */   private int chelok = 0;
/*     */ 
/*  25 */   private int vishim = 0;
/*     */ 
/*  27 */   private int chaotic = 0;
/*     */ 
/*  29 */   private int terrix = 0;
/*     */ 
/*  31 */   private int cybran = 0;
/*     */ 
/*  33 */   private int gray = 0;
/*     */ 
/*  35 */   private int sylik = 0;
/*     */ 
/*  37 */   private int mysmaal = 0;
/*     */ 
/*  39 */   private int triume = 0;
/*     */ 
/*  41 */   private int draconic = 0;
/*     */ 
/*     */   public void setId(int id)
/*     */   {
/*  45 */     this.id = id;
/*     */   }
/*     */ 
/*     */   public int getId() {
/*  49 */     return this.id;
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  53 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String name) {
/*  57 */     this.name = name;
/*     */   }
/*     */ 
/*     */   public void setHuman(int id)
/*     */   {
/*  62 */     this.human = id;
/*     */   }
/*     */ 
/*     */   public int getHuman() {
/*  66 */     return this.human;
/*     */   }
/*     */ 
/*     */   public void setChelok(int id)
/*     */   {
/*  73 */     this.chelok = id;
/*     */   }
/*     */ 
/*     */   public int getChelok() {
/*  77 */     return this.chelok;
/*     */   }
/*     */ 
/*     */   public void setVishim(int id)
/*     */   {
/*  84 */     this.vishim = id;
/*     */   }
/*     */ 
/*     */   public int getVishim() {
/*  88 */     return this.vishim;
/*     */   }
/*     */ 
/*     */   public void setChaotic(int id)
/*     */   {
/*  95 */     this.chaotic = id;
/*     */   }
/*     */ 
/*     */   public int getChaotic() {
/*  99 */     return this.chaotic;
/*     */   }
/*     */ 
/*     */   public void setTerrix(int id)
/*     */   {
/* 107 */     this.terrix = id;
/*     */   }
/*     */ 
/*     */   public int getTerrix() {
/* 111 */     return this.terrix;
/*     */   }
/*     */ 
/*     */   public void setCybran(int id)
/*     */   {
/* 118 */     this.cybran = id;
/*     */   }
/*     */ 
/*     */   public int getCybran() {
/* 122 */     return this.cybran;
/*     */   }
/*     */ 
/*     */   public void setGray(int id)
/*     */   {
/* 129 */     this.gray = id;
/*     */   }
/*     */ 
/*     */   public int getGray() {
/* 133 */     return this.gray;
/*     */   }
/*     */ 
/*     */   public void setSylik(int id)
/*     */   {
/* 142 */     this.sylik = id;
/*     */   }
/*     */ 
/*     */   public int getSylik() {
/* 146 */     return this.sylik;
/*     */   }
/*     */ 
/*     */   public void setMysmaal(int id)
/*     */   {
/* 154 */     this.mysmaal = id;
/*     */   }
/*     */ 
/*     */   public int getMysmaal() {
/* 158 */     return this.mysmaal;
/*     */   }
/*     */ 
/*     */   public void setTriume(int id)
/*     */   {
/* 165 */     this.triume = id;
/*     */   }
/*     */ 
/*     */   public int getTriume() {
/* 169 */     return this.triume;
/*     */   }
/*     */ 
/*     */   public void setDraconic(int id)
/*     */   {
/* 177 */     this.draconic = id;
/*     */   }
/*     */ 
/*     */   public int getDraconic() {
/* 181 */     return this.draconic;
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\end\Desktop\rpchatlite.jar
 * Qualified Name:     net.gamerservices.rpchat.sqlSector
 * JD-Core Version:    0.6.0
 */