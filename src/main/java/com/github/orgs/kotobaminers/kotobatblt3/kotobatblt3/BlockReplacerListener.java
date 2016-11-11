package com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.orgs.kotobaminers.kotobatblt3.game.BlockReplacer;

public class BlockReplacerListener implements Listener {
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(block == null) return;
		if(isReplacerEvent(player, block.getLocation())) {
			BlockReplacer.getReplacers().stream()
				.filter(r -> r.isLocationIn(block.getLocation()))
				.findAny()
				.ifPresent(r -> {
					if(r.isValidBlockAndTrigger(player, block)) {
//						BlockReplacerEventEnum.EXPLODE_EFFECT.perform(r);
						r.load(Bukkit.getWorlds());
						return;
					}
				});
		}
	}

	private static boolean isReplacerEvent(Player player, Location location) {
		if(player.getGameMode().equals(GameMode.ADVENTURE) && BlockReplacer.isInReplacer(location)) {
			return true;
		}
		return false;
	}

}
