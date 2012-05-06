package net.gamerservices.rpchat;

import com.avaje.ebean.validation.Length;
import com.avaje.ebean.validation.NotEmpty;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fantasyraces_player")
public class sqlPlayer
{

	@Id
	private int id;
	
	@Length(max=16)
	@NotEmpty
	private String name = "";
	
	@Length(max=32)
	@NotEmpty
	private String display = "";
	
	@Length(max=32)
	private String lastname = "";
	
	@Length(max=32)
	private String prefix = "";
	
	@Length(max=32)
	private String chatfocus = "";
	
	@Length(max=32)
	private String title = "";
	
	@Length(max=32)
	@NotEmpty
	private String race = "";
	
	@Length(max=32)
	@NotEmpty
	private String language = "";
	
	@Length(max=32)
	private String flags = "";
	private int killcount;
	
	@Length(max=32)
	private String vote = "";
				
	private int bitwise;
	private int experience;
	
	@Length(max=32)
	private String optional;
	
	private int election;
	
	public void setId(int id) { this.id = id; }
	
	public int getId()
	{
		return this.id;
	}
	
	public void setKillcount(int count)
	{
		this.killcount = count;
	}
	
	public int getKillcount() {
		return this.killcount;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTitle() {
		if (this.title == null)
		{
		return "";
		}
		return this.title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getPrefix() {
		return this.prefix;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getLastname() {
		return this.lastname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public String getDisplay() {
		return this.display;
	}
	
	public void setDisplay(String displayname) {
		this.display = displayname;
	}
	
	public String getFlags() {
		return this.flags;
	}
	
	public void setFlags(String flags) {
		this.flags = flags;
	}
	
	public String getRace()
	{
		return this.race;
	}
	
	public void setRace(String race) {
		this.race = race;
	}
	
	public String getLanguage() {
		return this.language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
	
	public String getVote() {
		return this.vote;
	}
	
	public void setVote(String minecraftname) {
		this.vote = minecraftname;
	}
	
	public String getChatfocus() {
		return this.chatfocus;
	}
	
	public void setChatfocus(String chatroomname) {
		this.chatfocus = chatroomname;
	}
	
	
	public void setBitwise(int bits)
	{
		this.bitwise = bits;
	}
	
	public int getBitwise() {
		return this.bitwise;
	}
	
	public void setExperience(int exp)
	{
		this.experience = exp;
	}
	
	public int getExperience() {
		return this.experience;
	}
	
	public void setElection(int val)
	{
		this.election = val;
	}
	
	public int getElection() {
		return this.election;
	}
	
	public void setOptional(String optional)
	{
		this.optional = optional;
	}
	
	public String getOptional() {
		return this.optional;
	}

}
