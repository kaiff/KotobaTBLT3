package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum TBLTItem implements ItemAbilityInterface {

	PORTAL_CRYSTAL(
		Material.NETHER_STAR,
		(short) 0,
		"Portal Crystal",
		Arrays.asList("This special item is used to open the time travel portal to your next location.", "Click the portal to open it.")
	) {
	},

	SINGLE_PORTAL(
		Material.NETHER_STAR,
		(short) 0,
		"Single portal key",
		null
	) {
	},

	PREDICTION(
		Material.WRITTEN_BOOK,
		(short) 0,
		"A written prediction",
		null
	) {
	},
	;

	protected Material material;
	protected short data;
	protected String name;
	protected List<String> lore;

	private TBLTItem(Material material, short data, String name, List<String> lore) {
		this.material = material;
		this.data = data;
		this.name = name;
		this.lore = lore;
	}


	public boolean isTBLTItem(ItemStack item) {
		if(item.getType() == this.material && item.getItemMeta().getDisplayName().equalsIgnoreCase(name)) {
			return true;
		}
		return false;
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

