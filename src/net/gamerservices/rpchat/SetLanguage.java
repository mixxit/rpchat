package net.gamerservices.rpchat;

import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLanguage implements CommandExecutor {

	rpchat parent;
	public SetLanguage(rpchat rpchat) {
		
		this.parent = rpchat;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		CommandSender sender = arg0;
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player)sender;
		
		PlayerCache sPlayer = this.parent.getPlayerCacheByName(player.getName());
		
		if (arg3.length == 0) {
			player.sendMessage(ChatColor.LIGHT_PURPLE + "Your current language is: " + sPlayer.language);
			player.sendMessage(ChatColor.LIGHT_PURPLE + "To set a new language use the: '/setlanguage language' command");
			
			int human = this.parent.getLanguageSkill(player.getName(),"human");
			int naturestongue = this.parent.getLanguageSkill(player.getName(),"naturestongue");
			int elvish = this.parent.getLanguageSkill(player.getName(),"elvish");
			int ancienthuman = this.parent.getLanguageSkill(player.getName(),"ancienthuman");
			int undertongue = this.parent.getLanguageSkill(player.getName(),"undertongue");
			int gnomish = this.parent.getLanguageSkill(player.getName(),"gnomish");
			int ancientgobel = this.parent.getLanguageSkill(player.getName(),"ancientgobel");
			int thiefspeak = this.parent.getLanguageSkill(player.getName(),"thiefspeak");
			int ancientdragon = this.parent.getLanguageSkill(player.getName(),"ancientdragon");
			int deathspeak = this.parent.getLanguageSkill(player.getName(),"deathspeak");
			int lidkish = this.parent.getLanguageSkill(player.getName(),"lidkish");
			int tiktok = this.parent.getLanguageSkill(player.getName(),"tiktok");
			
			
			
			String languagestring = "common";
			if (human == 100)
			{
				languagestring = languagestring + " human ";
			}
			
			if (naturestongue == 100)
			{
				languagestring = languagestring + " naturestongue ";
			}
			if (elvish == 100)
			{
				languagestring = languagestring + " elvish ";
			}
			if (ancienthuman == 100)
			{
				languagestring = languagestring + " ancienthuman ";
			}
			if (undertongue == 100)
			{
				languagestring = languagestring + " undertongue ";
			}
			if (gnomish == 100)
			{
				languagestring = languagestring + " gnomish ";
			}
			if (ancientgobel == 100)
			{
				languagestring = languagestring + " ancientgobel ";
			}
			if (thiefspeak == 100)
			{
				languagestring = languagestring + " thiefspeak ";
			}
			if (ancientdragon == 100)
			{
				languagestring = languagestring + " ancientdragon ";
			}
			if (deathspeak == 100)
			{
				languagestring = languagestring + " deathspeak ";
			}
			if (lidkish == 100)
			{
				languagestring = languagestring + " lidkish ";
			}
			if (tiktok == 100)
			{
				languagestring = languagestring + " tiktok ";
			}
			
			player.sendMessage("The languages you have learned are: "  + languagestring);
			
			return true;
			
		}
		
		if (arg3[0].matches("^[a-zA-Z]+$"))
		{
			String newlang = arg3[0];
			newlang = newlang.toLowerCase();
			if (this.parent.getLanguageSkill(player.getName(), newlang) == 100)
			{
				sPlayer.language = newlang;
				this.parent.updatePlayerScore(player);
				player.sendMessage("Your language is now: " + arg3[0]);
				return true;
			} else {
				player.sendMessage("You cannot make this your default language, as you have not mastered it (100 skill required)");
				return true;
			}
			
		} else {
			player.sendMessage("Your language can only contain letters");
			return false;
		}
	}

}
