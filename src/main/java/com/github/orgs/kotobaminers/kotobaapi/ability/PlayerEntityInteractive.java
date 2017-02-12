package com.github.orgs.kotobaminers.kotobaapi.ability;

import org.bukkit.event.player.PlayerInteractEntityEvent;

public interface PlayerEntityInteractive {
	boolean interact(PlayerInteractEntityEvent event);
	boolean isSame(PlayerInteractEntityEvent event);
}
