package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ChestReader {


	public static boolean checkPattern3By3(Chest chest, Vector offset) {
		Location origin = chest.getLocation().clone().add(offset);
		return findPattern3By3(chest)
			.map(pattern -> pattern.entrySet().stream().map(e -> origin.clone().add(e.getKey()).getBlock().getType() == e.getValue()).allMatch(b -> b == true))
			.orElse(false);
	}

	private static Optional<Map<Vector, Material>> findPattern3By3(Chest chest) {
		boolean is3x3 = Stream.of(chest.getInventory().getContents())
			.filter(i -> i != null)
			.anyMatch(i -> ChestKey.GEM_PORTAL_KEY_3X3.getIcon().isIconItemStack(i));

		if(!is3x3) return Optional.empty();

		Map<Vector, Material> pattern = new HashMap<Vector, Material>();
		Map<Vector, Integer> index = new HashMap<Vector, Integer>() {{
			put(new Vector(-1,0,1), 3);
			put(new Vector(0,0,1), 4);
			put(new Vector(1,0,1), 5);
			put(new Vector(-1,0,0), 12);
			put(new Vector(0,0,0), 13);
			put(new Vector(1,0,0), 14);
			put(new Vector(-1,0,-1), 21);
			put(new Vector(0,0,-1), 22);
			put(new Vector(1,0,-1), 23);
		}};

		Inventory inventory = chest.getInventory();
		index.entrySet().stream()
			.forEach(e -> {
				ItemStack item = inventory.getItem(e.getValue());
				if(item != null) {
					pattern.put(e.getKey(), item.getType());
				}
			});

		if(pattern.size() == index.size()) {
			return Optional.of(pattern);
		}

		return Optional.empty();
	}


}

