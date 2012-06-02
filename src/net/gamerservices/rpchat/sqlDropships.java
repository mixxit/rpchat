package net.gamerservices.rpchat;

import com.avaje.ebean.validation.Length;
import com.avaje.ebean.validation.NotEmpty;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fantasyraces_dropships")
public class sqlDropships
{

  @Id
  private int id;

  @Length(max=32)
  @NotEmpty
  private String name;

  @Length(max=32)
  @NotEmpty
  private String world = "";

  @Length(max=32)
  private String x = "";

  @Length(max=32)
  private String y = "";

  @Length(max=32)
  @NotEmpty
  private String z = "";

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

  public String getX() {
    return this.x;
  }

  public void setX(String x) {
    this.x = x;
  }

  public String getY() {
    return this.y;
  }

  public void setY(String y) {
    this.y = y;
  }

  public String getZ() {
    return this.z;
  }

  public void setZ(String z) {
    this.z = z;
  }

  public void setWorld(String world)
  {
    this.world = world;
  }
  public String getWorld() {
    return this.world;
  }
}