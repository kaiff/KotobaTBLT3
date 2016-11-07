package com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.github.orgs.kotobaminers.kotobatblt3.game.TBLTArena;

public class Utility {
	public static void playJumpEffect(Player player) {
		player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 4);
		for(int i = 1; i < 6; i++) {
	        Setting.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Setting.getPlugin(), new Runnable() {
	            @Override
	            public void run() {
					player.getWorld().playEffect(player.getLocation(), Effect.SMOKE, 4);
	            }
	        }, i);
		}
	}

	public static String patternProgress(String base, String unique, int length, int index, ChatColor color) {
		String progress = "" + color;
		for(int i = 0; i < length; i++) {
			if(i == index) {
				progress += unique;
			} else {
				progress += base;
			}
		}
		progress += ChatColor.RESET;
		return progress;
	}

	public static boolean isTBLTPlaying(Player player) {
		if(player.getGameMode().equals(GameMode.ADVENTURE)) {
			if(TBLTArena.isInArena(player.getLocation())) {
				return true;
			}
		}
		return false;
	}

}
