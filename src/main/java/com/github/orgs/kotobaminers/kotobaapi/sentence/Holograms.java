package com.github.orgs.kotobaminers.kotobaapi.sentence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public abstract class Holograms {


	private double height = 0.1;
	private double space = 0.25;
	private int duplicate =  3;
	private Location base = null;
	private List<ArmorStand> holos = new ArrayList<ArmorStand>();
	public BukkitTask task = null;


	public void display(List<String> lines) {
		Collections.reverse(lines);
		if(0 < lines.size()){
			cancelTask();
			removeNear();
			Stream.iterate(0, i -> i + 1)
				.limit(lines.size())
				.forEach(i -> updateArmorStand(lines.get(i), base.clone().add(0, height + space * i, 0)));
		}
	}


	@Deprecated
	public void displayTemporarily(JavaPlugin plugin, List<String> lines, Integer duration){
		if(0 < lines.size()){
			cancelTask();
			this.base.setY((this.base.getY() + this.height) - 1.25);
			for(int i = lines.size(); 0 < i; i--) {
				for(int j = 0; j < 3; j++) {
				final ArmorStand hologram =
						(ArmorStand) this.base.getWorld().spawnEntity(this.base, EntityType.ARMOR_STAND);
				hologram.setVisible(false);
				hologram.setGravity(false);
				hologram.setCustomName(new String(lines.get(i-1)));
				hologram.setCustomNameVisible(true);
				holos.add(hologram);
				}

				this.base.setY(this.base.getY() + 0.25);
			}
			task = Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				@Override
				public void run() {
					remove();
				}
			}, duration);
		}
	}


	private void updateArmorStand(String line, Location location) {
		Stream.iterate(0, i -> i)
			.limit(duplicate)
			.forEach(i -> {
				ArmorStand hologram = (ArmorStand) this.base.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
				hologram.setVisible(false);
				hologram.setGravity(false);
				hologram.setCustomName(line);
				hologram.setCustomNameVisible(true);
			});
	}


	public void remove() {
		cancelTask();
		holos.forEach(e -> e.remove());
		holos = new ArrayList<>();
	}

	private void cancelTask() {
		if (task != null) {
			task.cancel();
		}
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

	public void removeNear() {
		base.getWorld().getNearbyEntities(base, 0.1, 2, 0.1).stream()
			.filter(e -> isHologramArmorStand(e))
			.forEach(Entity::remove);
	}


	public static void removeAllHologram(JavaPlugin plugin) {
		plugin.getServer().getWorlds().stream()
			.flatMap(w -> w.getEntities().stream().filter(e -> e.getType().equals(EntityType.ARMOR_STAND)))
			.map(e -> (ArmorStand) e)
			.filter(a -> !a.isVisible() && !a.hasGravity() && a.isCustomNameVisible())
			.forEach(a -> a.remove());
	}

	protected void setLocation(Location base) {
		this.base = base;
	}
}

