package net.gamerservices.rpchat;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

public class RPchatPlayerListener extends PlayerListener {
	
	 @Override
	 public void onPlayerChat(PlayerChatEvent event) {
		event.getPlayer().sendMessage("You must use /local <msg> for RP chat, /ooc <msg> for world chat or /g <msg> for global chat");
		event.setCancelled(true);
	}
}
