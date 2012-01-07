package net.gamerservices.rpchat;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

public class RPchatPlayerListener extends PlayerListener {
	
	private rpchat plugin;
	
	public RPchatPlayerListener(rpchat rpchat) {
		// TODO Auto-generated constructor stub
		this.plugin = rpchat;
	}

	@Override
	public void onPlayerChat(PlayerChatEvent event) {
		//event.getPlayer().sendMessage("You must use /local <msg> for RP chat, /ooc <msg> for world chat or /g <msg> for global chat");
		
		// route it through local
		DoOOCMessage(event.getPlayer(),event.getMessage());		
		event.setCancelled(true);
	}
	
	public void DoOOCMessage(Player playername, String message)
	{
		 OOCMessage ooc = new OOCMessage(this.plugin);
		 ooc.sendOOC(playername,message);
		 
	}
}
