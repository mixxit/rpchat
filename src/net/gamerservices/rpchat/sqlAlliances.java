 package net.gamerservices.rpchat;
 
 import com.avaje.ebean.validation.Length;
 import com.avaje.ebean.validation.NotEmpty;
 import javax.persistence.Entity;
 import javax.persistence.Id;
 import javax.persistence.Table;
 
 @Entity
 @Table(name="fantasyraces_alliances")
 public class sqlAlliances
 {
 
   @Id
   private int id;
 
   @Length(max=16)
   @NotEmpty
   private String name;
 
   @Length(max=32)
   private String capital = "";
 
   @Length(max=64)
   private String capitalloc = "";
 
   @Length(max=32)
   private String description = "";
 
   @Length(max=32)
   private String leader = "";
 
   @Length(max=32)
   private String flags = "";
 
   public void setId(int id)
   {
     this.id = id;
   }
 
   public int getId() {
     return this.id;
   }
 
   public void setName(String name)
   {
     this.name = name;
   }
 
   public String getName() {
     return this.name;
   }
 
   public void setCapital(String capital)
   {
     this.capital = capital;
   }
 
   public String getCapital() {
     return this.capital;
   }
 
   public void setCapitalloc(String loc)
   {
     this.capitalloc = loc;
   }
 
   public String getCapitalloc() {
     return this.capitalloc;
   }
 
   public void setDescription(String description)
   {
     this.description = description;
   }
 
   public String getDescription() {
     return this.description;
   }
 
   public void setLeader(String leader)
   {
     this.leader = leader;
   }
 
   public String getLeader() {
     return this.leader;
   }
 
   public String getFlags() {
     return this.flags;
   }
 
   public void setFlags(String flags) {
     this.flags = flags;
   }
 }
