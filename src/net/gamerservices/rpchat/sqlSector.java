package net.gamerservices.rpchat;

import com.avaje.ebean.validation.Length;
import com.avaje.ebean.validation.NotEmpty;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fantasyraces_sector")
public class sqlSector
{

  @Id
  private int id;

  @Length(max=32)
  @NotEmpty
  private String name = "";
  
  private int combine = 0;

  private int collective = 0;

  private int realm = 0;

  private int dominion = 0;

  private int legacy = 0;

  private int legion = 0;

  private int foresworn = 0;

  private int forsaken = 0;

  @Length(max=32)
  private String flags = "";

  public void setId(int id)
  {
    this.id = id;
  }

  public int getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCombine(int id)
  {
    this.combine = id;
  }

  public int getCombine() {
    return this.combine;
  }

  public void setCollective(int id)
  {
    this.collective = id;
  }

  public int getCollective() {
    return this.collective;
  }

  public void setRealm(int id)
  {
    this.realm = id;
  }

  public int getRealm() {
    return this.realm;
  }

  public void setDominion(int id)
  {
    this.dominion = id;
  }

  public int getDominion() {
    return this.dominion;
  }

  public void setLegacy(int id)
  {
    this.legacy = id;
  }

  public int getLegacy() {
    return this.legacy;
  }

  public void setLegion(int id)
  {
    this.legion = id;
  }

  public int getLegion() {
    return this.legion;
  }

  public void setForesworn(int id)
  {
    this.foresworn = id;
  }

  public int getForesworn() {
    return this.foresworn;
  }

  public void setForsaken(int id)
  {
    this.forsaken = id;
  }

  public int getForsaken() {
    return this.forsaken;
  }
  
  public String getFlags() {
	  return this.flags;
  }
	 
  public void setFlags(String flags) {
	  this.flags = flags;
  }

}