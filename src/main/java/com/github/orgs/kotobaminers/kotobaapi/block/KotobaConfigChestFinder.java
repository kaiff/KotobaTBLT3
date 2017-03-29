package com.github.orgs.kotobaminers.kotobaapi.block;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaStructureUtility;

public interface KotobaConfigChestFinder {


	List<Vector> getPositions();
	boolean hasRotations();


	default List<Chest> findChests(Location location) {
		List<Chest> collect = KotobaStructureUtility.findOrigins(getPositions(), location, hasRotations()).stream()
			.filter(b -> b.getState() instanceof Chest)
			.map(b -> (Chest) b.getState())
			.collect(Collectors.toList());
		return collect;
	}


}

