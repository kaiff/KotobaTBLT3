package com.github.orgs.kotobaminers.kotobaapi.utility;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class KotobaBlockData {


	private Location location;
	private Material material;
	private int data;


	public KotobaBlockData(Location location, Material material, int data) {
		this.location = location;
		this.material = material;
		this.data = data;
	}


	protected Block getCurrentBlock() {
		return location.getBlock();
	}

	@SuppressWarnings("deprecation")
	public void placeBlock() {
		Block block = location.getBlock();
		block.setTypeIdAndData(material.getId(), (byte) data, false);
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

