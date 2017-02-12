package com.github.orgs.kotobaminers.kotobaapi.block;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaStructureUtility;

public interface KotobaConfigChestFinder {


	static final Material MATERIAL = Material.TRAPPED_CHEST;


	List<Vector> getPositions();
	boolean hasRotations();


	default List<Chest> findChests(Location location) {
		List<Chest> collect = KotobaStructureUtility.findOrigins(getPositions(), location, hasRotations()).stream()
			.filter(b -> b.getType() == MATERIAL)
			.map(b -> (Chest) b.getState())
			.collect(Collectors.toList());
		return collect;
	}


	default boolean hasChests(Location location) {
		if(0 < findChests(location).size()) return true;
		return false;
	}


	default List<ItemStack> findOptions(Location location, List<KotobaItemStackIcon> excetions) {
		List<ItemStack> contents = findChests(location).stream()
			.flatMap(c -> Stream.of(c.getInventory().getContents()).filter(i -> i != null))
			.collect(Collectors.toList());
		return contents.stream()
			.filter(i -> !excetions.stream().anyMatch(e -> e.isIconItemStack(i)))
			.collect(Collectors.toList());
	}


}

