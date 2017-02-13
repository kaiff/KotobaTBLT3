package com.github.orgs.kotobaminers.kotobaapi.userinterface;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockStorage;

public class Holograms {


	private static final double SPACE = 0.25;
	private static final int DUPLICATE =  3;
	private static final double ARMOR_STAND_HEIGHT = 1.5;


	public boolean display(List<String> lines, Location base) {
		Collections.reverse(lines);
		if(0 < lines.size()){
			removeNear(base);
			Stream.iterate(0, i -> i + 1)
				.limit(lines.size())
				.forEach(i -> updateArmorStand(lines.get(i), base.clone().add(0, SPACE * i - ARMOR_STAND_HEIGHT, 0)));
			return true;
		}
		return false;
	}


	private void updateArmorStand(String line, Location base) {
		Stream.iterate(0, i -> i)
			.limit(DUPLICATE)
			.forEach(i -> {
				ArmorStand hologram = (ArmorStand) base.getWorld().spawnEntity(base, EntityType.ARMOR_STAND);
				hologram.setVisible(false);
				hologram.setGravity(false);
				hologram.setCustomName(line);
				hologram.setCustomNameVisible(true);
			});
	}


	public void removeNear(Location location) {
		location.getWorld().getNearbyEntities(location, 0.1, 2, 0.1).stream()
			.filter(e -> isHologramArmorStand(e))
			.forEach(Entity::remove);
	}


	public static void removeAllHolograms(JavaPlugin plugin) {
		plugin.getServer().getWorlds().stream()
			.forEach(world -> world.getEntities().stream().filter(Holograms::isHologramArmorStand).forEach(e -> e.remove()));
	}


	public static void removeHolograms(JavaPlugin plugin, KotobaBlockStorage storage) {
		plugin.getServer().getWorlds().stream()
			.forEach(world -> world.getEntities().stream().filter(Holograms::isHologramArmorStand).filter(e -> storage.isIn(e.getLocation())).forEach(e -> e.remove()));
	}


	private static boolean isHologramArmorStand(Entity entity) {
		if(entity instanceof ArmorStand) {
			ArmorStand stand = (ArmorStand) entity;
			if(stand.hasGravity() == false && stand.isVisible() == false && stand.isCustomNameVisible() == true) {
				return true;
			}
		}
		return false;
	}


}

