package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.List;

import org.bukkit.Material;

public enum TBLTItem implements ItemAbilityInterface {
	WARP_CRYSTAL(Material.NETHER_STAR, (short) 0, "Warp crystal", null),
	;

	private Material material;
	private short data;
	private String name;
	private List<String> lore;

	private TBLTItem(Material material, short data, String name, List<String> lore) {
		this.material = material;
		this.data = data;
		this.name = name;
		this.lore = lore;
	}

	@Override
	public Material getMaterial() {
		return material;
	}
	@Override
	public short getData() {
		return data;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public List<String> getLore() {
		return lore;
	}
	@Override
	public int getConsumption() {
		return 0;
	}
}
