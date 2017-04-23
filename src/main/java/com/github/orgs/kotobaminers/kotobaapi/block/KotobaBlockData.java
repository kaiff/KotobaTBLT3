package com.github.orgs.kotobaminers.kotobaapi.block;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class KotobaBlockData {


	private Location location;
	private Material material;
	private int data;


	private static final List<Material> SECOND = Arrays.asList(
		Material.STATIONARY_WATER,
		Material.WATER,
		Material.STATIONARY_LAVA,
		Material.LAVA,
		Material.TORCH,
		Material.REDSTONE_TORCH_OFF,
		Material.REDSTONE_TORCH_ON
	);


	public KotobaBlockData(Location location, Material material, int data) {
		this.location = location;
		this.material = material;
		this.data = data;
	}


	public static void placeBlockSafe(List<KotobaBlockData> blocksData) {
		List<KotobaBlockData> first = blocksData.stream()
				.filter(data -> !SECOND.contains(data.getMaterial()))
				.collect(Collectors.toList());
		List<KotobaBlockData> second = blocksData.stream()
				.filter(data -> SECOND.contains(data.getMaterial()))
				.collect(Collectors.toList());

		blocksData.stream()
			.map(data -> data.getLocation().getBlock())
			.filter(block -> SECOND.contains(block.getType()))
			.forEach(block -> block.setType(Material.AIR));

		first.forEach(KotobaBlockData::placeBlock);
		second.forEach(KotobaBlockData::placeBlock);
	}


	protected Block getCurrentBlock() {
		return location.getBlock();
	}

	@SuppressWarnings("deprecation")
	public void placeBlock() {
		Block block = location.getBlock();
		block.setTypeIdAndData(material.getId(), (byte) data, false);
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	public Location getLocation() {
		return location;
	}
	public Material getMaterial() {
		return material;
	}
	public int getData() {
		return data;
	}


}

