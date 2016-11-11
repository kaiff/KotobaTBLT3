package com.github.orgs.kotobaminers.kotobaapi.utility;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KotobaAPIItemStack extends ItemStack {
	private enum MaterialConverter {
		IRONDOOR(Arrays.asList(Material.IRON_DOOR_BLOCK), Arrays.asList(Material.IRON_DOOR)),
		;
		private List<Material> blocks;
		private List<Material> items;

		private MaterialConverter(List<Material> blocks, List<Material> items) {
			this.blocks = blocks;
			this.items = items;
		}

		static Material toBlockMaterial(Material material) {
			return Stream.of(MaterialConverter.values())
				.filter(mc -> mc.items.contains(material))
				.findAny()
				.map(mc-> mc.blocks.get(0))
				.orElse(material);
		}
		static Material toItemMaterial(Material material) {
			return Stream.of(MaterialConverter.values())
				.filter(mc -> mc.blocks.contains(material))
				.findAny()
				.map(mc-> mc.items.get(0))
				.orElse(material);
		}

	}

	private KotobaAPIItemStack(Material material, int amount, short data) {
		super(material, amount, data);
	}

	public static KotobaAPIItemStack create(Material material, int amount, short data, String name, List<String> lore) {
		KotobaAPIItemStack itemStack = new KotobaAPIItemStack(MaterialConverter.toItemMaterial(material), amount, data);
		ItemMeta itemMeta = itemStack.getItemMeta();
		if(itemMeta != null) {
			itemMeta.setDisplayName(name);
			if(lore != null) {
				itemMeta.setLore(lore);
			}
			itemStack.setItemMeta(itemMeta);
			return itemStack;
		}
		return null;
	}

	public List<String> getLore() {
		return getItemMeta().getLore();
	}
	public void setLore(List<String> lore) {
		ItemMeta itemMeta = getItemMeta();
		itemMeta.setLore(lore);
		setItemMeta(itemMeta);
	}
	public String getDisplayName() {
		return getItemMeta().getDisplayName();
	}
	public void setLore(String name) {
		ItemMeta itemMeta = getItemMeta();
		itemMeta.setDisplayName(name);
		setItemMeta(itemMeta);
	}
}
