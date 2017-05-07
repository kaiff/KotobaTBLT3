package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.orgs.kotobaminers.kotobaapi.block.PlayerBlockInteractive;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaSound;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTCooldown;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTUtility;

public class InteractListener implements Listener {
	private static final List<Action> CLICKS = Arrays.asList(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);

	@EventHandler
	void onPlayerInteractBlock(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if(!CLICKS.contains(event.getAction())) return;

		if(TBLTCooldown.INTERACTIVE.isCooldown(player.getUniqueId())) return;
		if(!TBLTUtility.isTBLTPlayer(player)) return;

		List<PlayerBlockInteractive> interactives = PlayerInteractiveManager.find(event);

		if (0 < interactives.size()) {
			event.setCancelled(true);
			List<Boolean> success = interactives.stream().map(i -> i.interact(event)).collect(Collectors.toList());
			if(!success.contains(true)) {
				playFailureEffect(player.getLocation());
			} else {
				TBLTCooldown.INTERACTIVE.updateCooldown(player.getUniqueId());
			}
		}
	}


	private void playFailureEffect(Location location) {
		KotobaSound.SHEAR.play(location);
	}


}

