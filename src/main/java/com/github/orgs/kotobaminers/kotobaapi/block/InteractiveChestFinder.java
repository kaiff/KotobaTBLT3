package com.github.orgs.kotobaminers.kotobaapi.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;

public interface InteractiveChestFinder extends KotobaConfigChestFinder {


	default List<Chest> findChests(PlayerInteractEvent event, KotobaItemStackIcon icon) {
		return findTargetBlock(event)
			.map(b -> findChests(b.getLocation()))
			.orElse(new ArrayList<>()).stream()
			.filter(c ->
				Stream.of(c.getInventory().getContents())
					.filter(i -> i != null)
					.anyMatch(i -> icon.isIconItemStack(i)))
			.collect(Collectors.toList());
	}


	default Optional<Block> findTargetBlock(PlayerInteractEvent event) {
		return getInteractType().getTargetBlock(event);
	}


	InteractType getInteractType();


}

