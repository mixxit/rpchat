package net.gamerservices.rpchat;

public class SkillEntry implements Comparable<SkillEntry> {
	public SkillEntry(String string, int experience)  {
		// TODO Auto-generated constructor stub
		this.name = string;
		this.experience = experience;
	}
	String name;
	int experience;
	@Override
	public int compareTo(SkillEntry arg0) {
		// TODO Auto-generated method stub
		if (this.experience < arg0.experience)
		{
			return -1;
		}
		if (this.experience > arg0.experience)
		{
			return 1;
		}
		return 0;
	}

}
