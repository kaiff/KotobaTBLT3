package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum TBLTItem implements ItemAbilityInterface {

	WARP_CRYSTAL(Material.NETHER_STAR, (short) 0, "Warp crystal", null) {
		@Override
		public boolean isTBLTItem(ItemStack item) {
			if(item.getType() == this.material && item.getItemMeta().getDisplayName().equalsIgnoreCase(name)) {
				return true;
			}
			return false;
		}
	},

	PREDICTION(Material.WRITTEN_BOOK, (short) 0, "A written prediction", null) {
		@Override
		public boolean isTBLTItem(ItemStack item) {
			if(item.getType() == this.material && item.getItemMeta().getDisplayName().equalsIgnoreCase(name)) {
				return true;
			}
			return false;
		}
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


	public abstract boolean isTBLTItem(ItemStack item);


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

