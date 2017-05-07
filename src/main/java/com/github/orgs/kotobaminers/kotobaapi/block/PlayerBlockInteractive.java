package com.github.orgs.kotobaminers.kotobaapi.block;

import org.bukkit.event.player.PlayerInteractEvent;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaPriority;

public interface PlayerBlockInteractive extends KotobaPriority {


	boolean interact(PlayerInteractEvent event);
	boolean isSame(PlayerInteractEvent event);


}

