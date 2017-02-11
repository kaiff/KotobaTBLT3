package com.github.orgs.kotobaminers.kotobaapi.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;


public interface KotobaStructure {
	Map<Vector, Material> getStructure();

	default List<Map<Vector, Material>> getStructures() {
		Map<Vector, Material> rotated = new HashMap<Vector, Material>();
		getStructure().entrySet().stream()
			.forEach(e -> rotated.put(new Vector(e.getKey().getZ(), e.getKey().getY(), e.getKey().getX()), e.getValue()));
		return Arrays.asList(getStructure(), rotated);
	}

	default List<Location> findOrigins(Location location) {
		List<Location> origins = new ArrayList<Location>();

		Block block = location.getBlock();

		getStructures().forEach(s ->
			s.entrySet().stream()
				.filter(e -> e.getValue() == block.getType())
				.filter(e -> {
					Location origin = block.getLocation().clone().subtract(e.getKey());
					return s.entrySet().stream()
						.allMatch(e2 -> origin.clone().add(e2.getKey()).getBlock().getType() == e2.getValue());
				})
				.forEach(e -> origins.add(block.getLocation().clone().subtract(e.getKey()))
			)
		);
		return origins;
	}

}


