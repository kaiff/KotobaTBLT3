package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;

public class Drone {
	private static Map<UUID, Integer> scheduler = new HashMap<>();
	private static Map<UUID, Location> location = new HashMap<>();

	public static void initializePlayer(Player player) {
		returnToLocation(player);
		removeLocation(player);
		refleshDrone(player);
		removeTask(player);
	}

	private static void removeLocation(Player player) {
		if(location.containsKey(player.getUniqueId())) {
			location.remove(player.getUniqueId());
		}
	}

	private static void setTask(Player player, int task) {
		scheduler.put(player.getUniqueId(), task);
	}

	private static void removeTask(Player player) {
		if(scheduler.containsKey(player.getUniqueId())) {
			scheduler.remove(player.getUniqueId());
		}
	}

	private static void setLocation(Player player) {
		location.put(player.getUniqueId(), player.getLocation());
	}

	private static void returnToLocation(Player player) {
		if(location.containsKey(player.getUniqueId())) {
			player.setNoDamageTicks(20);
			player.teleport(location.get(player.getUniqueId()));
			location.remove(player.getUniqueId());
		}
	}

	public static void performDrone(Player player, int second) {
		if(!isDrone(player)) {
			setLocation(player);
			setDrone(player, second);
			removeTask(player);
			int task = Setting.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Setting.getPlugin(), new Runnable() {
				@Override
				public void run() {
					refleshDrone(player);
					returnToLocation(player);
					removeTask(player);
				}
			}, second * 20L);
			setTask(player, task);
			List<Entity> entities = Stream.iterate(0, i -> i)
				.limit(1)
				.map(i -> player.getWorld().spawnEntity(player.getLocation(), EntityType.BAT))
				.collect(Collectors.toList());
			scheduleDroneEffect(player, second, entities);
			KotobaUtility.playCountDown(Setting.getPlugin(), Arrays.asList(player), second);
		}
	}

	private static void setDrone(Player player, int second) {
		player.setAllowFlight(true);
		player.setVelocity(new Vector(0, 0, 0));
		player.setVelocity(new Vector(0, 2, 0));
		player.setFlying(true);
		player.setFlySpeed(0.05F);
		player.setWalkSpeed(0F);
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 100, 1, true, false));
	}

	private static boolean isDrone(Player player) {
		return scheduler.containsKey(player.getUniqueId());
	}

	private static void refleshDrone(Player player) {
		player.setAllowFlight(false);
		player.setFlying(false);
		player.setFlySpeed(0.1F);
		player.setWalkSpeed(0.2F);
		player.removePotionEffect(PotionEffectType.INVISIBILITY);
	}
	private static void scheduleDroneEffect(Player player, int second, List<Entity> entities) {
		player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 4);
		for(int i = 1; i < second * 20; i++) {
			Setting.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Setting.getPlugin(), new Runnable() {
				@Override
				public void run() {
					player.getWorld().playEffect(player.getLocation(), Effect.SMOKE, 4);
					entities.stream().forEach(entity -> entity.teleport(player.getLocation().clone().add(player.getLocation().getDirection().clone().multiply(1.5))));
				}
			}, i);
		}
		Setting.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Setting.getPlugin(), new Runnable() {
			@Override
			public void run() {
				entities.stream().forEach(Entity::remove);
			}
		}, second * 20);
	}
}
