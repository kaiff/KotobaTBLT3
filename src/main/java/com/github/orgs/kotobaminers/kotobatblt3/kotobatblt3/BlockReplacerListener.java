package com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.orgs.kotobaminers.kotobatblt3.game.BlockReplacer;
import com.github.orgs.kotobaminers.kotobatblt3.game.BlockReplacer.BlockReplacerEvent;

public class BlockReplacerListener implements Listener {
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(block == null) return;
		if(!isReplacerEvent(player, block.getLocation())) return;
		BlockReplacer.getReplacers().stream()
			.filter(r -> r.isLocationIn(block.getLocation()))
			.filter(r -> r.isValidBlockAndTrigger(event.getPlayer(), block))
			.findAny()
			.ifPresent(r -> BlockReplacerEvent.REPLACE.perform(r))//TODO: temporary
			;
	}

	public static boolean isReplacerEvent(Player player, Location location) {
		if(player.getGameMode().equals(GameMode.ADVENTURE) && BlockReplacer.isInReplacer(location)) {
			return true;
		}
		return false;
	}

}
