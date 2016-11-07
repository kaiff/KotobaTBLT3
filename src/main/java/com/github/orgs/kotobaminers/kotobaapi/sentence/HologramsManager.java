package com.github.orgs.kotobaminers.kotobaapi.sentence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class HologramsManager {
	
	private static Map<Integer, Holograms> map = new HashMap<>();
	
	public static void updateHologram(JavaPlugin plugin, Holograms holograms, int conversation, List<String> lines, int duration)  {
		Optional.ofNullable(map.getOrDefault(conversation, null)).ifPresent(h -> h.remove());
		map.put(conversation, holograms);
		holograms.displayTemporarily(plugin, lines, duration);
	}
	
	public static void removeAllHologram(JavaPlugin plugin) {
		plugin.getServer().getWorlds().stream()
			.flatMap(w -> w.getEntities().stream().filter(e -> e.getType().equals(EntityType.ARMOR_STAND)))
			.map(e -> (ArmorStand) e)
			.filter(a -> !a.isVisible() && !a.hasGravity() && a.isCustomNameVisible())
			.forEach(a -> a.remove());
	}
}
