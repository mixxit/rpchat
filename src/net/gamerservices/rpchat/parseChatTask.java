package net.gamerservices.rpchat;

import org.bukkit.entity.Player;

public class parseChatTask implements Runnable{
	private rpchat rpchat;
	private String playerfrom;
	private String message;
	   public parseChatTask(rpchat rpchat, String from, String message) {
		   this.rpchat = rpchat;
		   this.playerfrom = from;
		   this.message = message;
		// TODO Auto-generated constructor stub
	}

	public void run()
	{
		this.rpchat.parseQuestText(playerfrom,message);
	}
}
