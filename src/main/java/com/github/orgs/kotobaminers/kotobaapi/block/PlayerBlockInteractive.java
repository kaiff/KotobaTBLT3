package com.github.orgs.kotobaminers.kotobaapi.block;

import org.bukkit.event.player.PlayerInteractEvent;

public interface PlayerBlockInteractive {


	boolean interact(PlayerInteractEvent event);
	boolean isSame(PlayerInteractEvent event);


}

