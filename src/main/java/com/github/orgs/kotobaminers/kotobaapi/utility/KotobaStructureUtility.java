package com.github.orgs.kotobaminers.kotobaapi.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class KotobaStructureUtility {


	private static final List<Integer> ROTATER = Arrays.asList(1, 2, 3);


	public static Vector rotateRight(Vector v) {
		return new Vector(-v.getBlockZ(), v.getBlockY(), v.getBlockX());
	}

	public static Vector rotateRight(Vector v, int times) {
		for(int i = 0; i < times; i++) {
			v = rotateRight(v);
		}
		return v;
	}


	public static List<Map<Vector, Material>> getRotations(Map<Vector, Material> pattern) {
		List<Map<Vector, Material>> rotations = ROTATER.stream().map(r -> {
			Map<Vector, Material> rotated = new HashMap<Vector, Material>();
			pattern.entrySet().stream()
				.forEach(e -> rotated.put(rotateRight(e.getKey().clone(), r), e.getValue()));
			return rotated;
		}).collect(Collectors.toList());
		rotations.add(pattern);

		return rotations;
	}


	private static List<List<Vector>> getRotations(List<Vector> vectors) {
		List<List<Vector>> rotations = ROTATER.stream()
			.map(r -> vectors.stream().map(v -> rotateRight(v.clone(), r)).collect(Collectors.toList()))
			.collect(Collectors.toList());
		rotations.add(vectors);
		return rotations;
	}


	public static List<Block> findOrigins(Map<Vector, Material> pattern, Location location, boolean hasRotations) {
		List<Block> origins = new ArrayList<>();

		Block block = location.getBlock();

		if(hasRotations) {
			getRotations(pattern).forEach(s ->
				s.entrySet().stream()
					.filter(e -> e.getValue() == block.getType())
					.filter(e -> {
						Location origin = block.getLocation().clone().subtract(e.getKey());
						return s.entrySet().stream()
								.allMatch(e2 -> origin.clone().add(e2.getKey()).getBlock().getType() == e2.getValue());
					})
					.forEach(e -> origins.add(block.getLocation().clone().subtract(e.getKey()).getBlock())
				)
			);
		} else {
			pattern.entrySet().stream()
				.filter(e -> e.getValue() == block.getType())
				.filter(e -> {
					Location origin = block.getLocation().clone().subtract(e.getKey());
					return pattern.entrySet().stream()
							.allMatch(e2 -> origin.clone().add(e2.getKey()).getBlock().getType() == e2.getValue());
				})
				.forEach(e -> origins.add(block.getLocation().clone().subtract(e.getKey()).getBlock()));
		}
		return origins;
	}


	public static List<List<Block>> findExistings(Map<Vector, Material> pattern, Location location, boolean hasRotations) {
		return findOrigins(pattern, location, hasRotations).stream()
			.flatMap(o -> getRotations(pattern).stream()
				.filter(s -> s.entrySet().stream().allMatch(e -> o.getLocation().clone().add(e.getKey()).getBlock().getType() == e.getValue()))
				.map(s -> s.entrySet().stream().map(e -> o.getLocation().clone().add(e.getKey()).getBlock()).collect(Collectors.toList()))
			)
			.collect(Collectors.toList());
	}


	public static List<Block> findOrigins(List<Vector> vectors, Location location, boolean hasRotations) {
		if(hasRotations) {
			return getRotations(vectors).stream()
				.flatMap(list -> list.stream().map(p -> location.clone().subtract(p).getBlock()))
				.collect(Collectors.toList());
		}
		return vectors.stream()
			.map(p -> location.clone().subtract(p).getBlock())
			.collect(Collectors.toList());
	}



}

