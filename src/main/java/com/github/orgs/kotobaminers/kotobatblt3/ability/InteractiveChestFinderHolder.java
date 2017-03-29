package com.github.orgs.kotobaminers.kotobatblt3.ability;

import org.bukkit.event.player.PlayerInteractEvent;

import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTInteractiveChestFinder;

public interface InteractiveChestFinderHolder {


	TBLTInteractiveChestFinder getChestFinder();
	boolean interactWithChests(PlayerInteractEvent event);


}

