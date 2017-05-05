package com.github.orgs.kotobaminers.kotobatblt3.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;

public class TBLTUtility {
	//TODO: Integrate with KotobaUtility

	public static Location getRandomLocation(Location location, double radius) {
		Random random = new Random();
		double x = (random.nextDouble() - 0.5) * radius * 2;
		double y = (random.nextDouble() - 0.5) * radius * 2;
		double z = (random.nextDouble() - 0.5) * radius * 2;
		return location.clone().add(x, y, z);
	}

	public static boolean isTBLTPlayer(Player player) {
		return new TBLTArenaMap().isInAny(player.getLocation()) && player.getGameMode() == GameMode.ADVENTURE;
	}

	public static Optional<World> findWorld(String name) {
		return	Bukkit.getWorlds()
			.stream()
			.filter(w -> w.getName().equalsIgnoreCase(name))
			.findAny();
	}

	public static List<Location> getSpherePositions(Location center, int radius) {
		List<Integer> length = Stream.iterate(0, r -> r + 1)
			.limit(radius + 1)
			.collect(Collectors.toList());
		List<int[]> vectors = new ArrayList<int[]>();
		for(int l1 : length) {
			for(int l2 : length) {
				for(int l3 : length) {
					int[] vector = {l1, l2, l3};
					vectors.add(vector);
				}
			}
		}
		int x = center.getBlockX();
		int y = center.getBlockY();
		int z = center.getBlockZ();

		List<Location> result = new ArrayList<>();
		int[][] swaps = {{-1,-1,-1}, {-1,-1,1}, {-1,1,-1}, {-1,1,1}, {1,-1,-1}, {1,-1,1}, {1,1,-1}, {1,1,1}};
		vectors.stream()
			.filter(v -> v[0]*v[0] + v[1]*v[1] + v[2]*v[2] < radius*radius)
			.forEach(v -> Stream.of(swaps).forEach(s -> result.add(new Location(center.getWorld(), x+ v[0]*s[0], y + v[1]*s[1], z + v[2]*s[2]))));
		return result;
	}

	public static void repeatRunnable(int delay, int interval, int times, Runnable runnable) {
		int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Setting.getPlugin(), runnable, delay, interval);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Setting.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Bukkit.getScheduler().cancelTask(task);
			}
		}, interval * times);
	}


	public static void playJumpEffect(Entity entity) {
		entity.getWorld().playEffect(entity.getLocation(), Effect.BLAZE_SHOOT, 4);

		int delay = 0;
		int interval = 1;
		int times = 10;
		repeatRunnable(delay, interval, times, new Runnable() {
			@Override
			public void run() {
				entity.getWorld().playEffect(entity.getLocation(), Effect.SMOKE, 4);
			}
		});
	}

	public static String patternProgress(String base, String unique, int length, int index, ChatColor color) {
		String progress = "" + color;
		for(int i = 0; i < length; i++) {
			if(i == index) {
				progress += unique;
			} else {
				progress += base;
			}
		}
		progress += ChatColor.RESET;
		return progress;
	}

	public static void scheduleBlockEffect(Player player, int second, Location location) {
		for(int i = 1; i < second * 20; i++) {
			Setting.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Setting.getPlugin(), new Runnable() {
				@Override
				public void run() {
					player.getWorld().playEffect(location, Effect.ENDER_SIGNAL, 4);
				}
			}, i);
		}
	}
	public static void stopPlayer(Player player, int duration) {
		Location location = player.getLocation();
		for(int i = 1; i <= duration * 20; i++) {
			Setting.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Setting.getPlugin(), new Runnable() {
				@Override
				public void run() {
					player.teleport(location);
				}
			}, i);
		}
	}
}
