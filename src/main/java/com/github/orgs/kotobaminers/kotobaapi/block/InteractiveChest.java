package com.github.orgs.kotobaminers.kotobaapi.block;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Chest;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;

public interface InteractiveChest {


	boolean interact(PlayerInteractEvent event);


	KotobaConfigChestInterface getChest();


	default List<Chest> findChests(PlayerInteractEvent event) {
		return getChest().findTargetBlock(event)
			.map(b -> getChest().findChests(b.getLocation()))
			.orElse(new ArrayList<>());
	}


	default List<ItemStack> findOptions(PlayerInteractEvent event) {
		return getChest().findTargetBlock(event)
			.map(b -> getChest().findOptions(b.getLocation(), getIcon()))
			.orElse(new ArrayList<>());
	}


	KotobaItemStackIcon getIcon();


}

