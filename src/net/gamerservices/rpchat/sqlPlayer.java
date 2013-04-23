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
	private String gender = "";
	
	@Length(max=32)
	@NotEmpty
	private String language = "";
	
	@Length(max=512000)
	private String flags = "";
	private int flagpole = 0;
	private int killcount;
	
	@Length(max=32)
	private String vote = "";
				
	private int bitwise;
	private int experience;
	
	@Length(max=32)
	private String optional = "";
	
	private int election;
	
	@Length(max=32)
	private String alliance = "";
	
	private int combatexperience;
	private int rangedexperience;
	private int scholarlyexperience;
	private int naturalexperience;
	private int power;
	
	@Length(max=128)
	private String languageflags = "";
	
	@Length(max=32)
	private String lastmeditate = "";
	
	@Length(max=32)
	private String optedin = "";
	
	@Length(max=32)
	private String birthstamp = "";
	
	@Length(max=32)
	private String suffix = "";
	
	private int hitpoints;
	
	public void setId(int id) { this.id = id; }
	
	public int getId()
	{
		return this.id;
	}
	
	public void setFlagpole(int flagpole) { this.flagpole = flagpole; }
	
	public int getFlagpole()
	{
		return this.flagpole;
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
	
	public void setAlliance(String alliance)
	{
		this.alliance = alliance;
	}
	
	public String getAlliance() {
		return this.alliance;
	}

	public void setCombatexperience(int exp)
	{
		this.combatexperience = exp;
	}
	
	public int getCombatexperience() {
		return this.combatexperience;
	}
	public void setRangedexperience(int exp)
	{
		this.rangedexperience = exp;
	}
	
	public int getRangedexperience() {
		return this.rangedexperience;
	}
	public void setScholarlyexperience(int exp)
	{
		this.scholarlyexperience = exp;
	}
	
	public int getScholarlyexperience() {
		return this.scholarlyexperience;
	}
	public void setNaturalexperience(int exp)
	{
		this.naturalexperience = exp;
	}
	
	public int getNaturalexperience() {
		return this.naturalexperience;
	}


	public void setPower(int power)
	{
		this.power = power;
	}
	
	public int getPower() {
		return this.power;
	}
	public void setLastmeditate(String timestamp)
	{
		this.lastmeditate = timestamp;
	}
	
	public String getLastmeditate() {
		return this.lastmeditate;
	}
	
	public void setOptedin(String optedin)
	{
		this.optedin = optedin;
	}
	
	public String getOptedin() {
		return this.optedin;
	}
	
	public void setGender(String gender)
	{
		this.gender = gender;
	}
	
	public String getGender() {
		return this.gender;
	}
	
	public void setBirthstamp(String timestamp)
	{
		this.birthstamp = birthstamp;
	}
	
	public String getBirthstamp() {
		return this.birthstamp;
	}
	
	
	public void setSuffix(String suffix)
	{
		this.suffix = suffix;
	}
	
	public String getSuffix() {
		return this.suffix;
	}

	public void setHitpoints(int amount) {
		// TODO Auto-generated method stub
		this.hitpoints = amount;
	}
	public int getHitpoints() {
		// TODO Auto-generated method stub
		return this.hitpoints;
	}
	
	public void setLanguageflags(String languageflags)
	{
		this.languageflags = languageflags;
	}
	
	public String getLanguageflags() {
		return this.languageflags;
	}
	
}
