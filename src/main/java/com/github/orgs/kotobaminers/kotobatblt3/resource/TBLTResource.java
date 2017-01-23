package com.github.orgs.kotobaminers.kotobatblt3.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public enum TBLTResource {
	MAGIC_MANA(
		Material.EMERALD,
		(short) 0,
		"Magic Mana",
		null,
		ChatColor.GREEN
	),

//	LIFE_MANA(
//		Material.INK_SACK,
//		(short) 2,
//		"Life Mana",
//		null,
//		ChatColor.GREEN
//	),
//
//	METAL_MANA(
//		Material.IRON_INGOT,
//		(short) 0,
//		"Metal Mana",
//		null,
//		ChatColor.GRAY
//	),
//
//	WATER_MANA(
//		Material.INK_SACK,
//		(short) 4,
//		"Water Mana",
//		null,
//		ChatColor.AQUA
//	),
//
//	FLAME_MANA(
//		Material.BLAZE_POWDER,
//		(short) 0,
//		"Flame Mana",
//		null,
//		ChatColor.RED
//	),
//
//	ENDER_MANA(
//		Material.EYE_OF_ENDER,
//		(short) 0,
//		"Ender Mana",
//		null,
//		ChatColor.DARK_GRAY
//	),
	;

	private Material material;
	private short data;
	private String name;
	private List<String> lore;
	private ChatColor color;

	private TBLTResource(Material material, short data, String name, List<String> lore, ChatColor color) {
		this.material = material;
		this.data = data;
		this.name = name;
		this.lore = lore;
		this.color = color;
	}

	public ItemStack create(int amount) {
		ItemStack itemStack = new ItemStack(material, amount, data);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(lore);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public ItemStack create() {
		return create(1);
	}

	public static Map<TBLTResource, Integer> getResources(List<ItemStack> items) {
		Map<TBLTResource, Integer> resources = new HashMap<>();
		Stream.of(TBLTResource.values())
			.forEach(resource -> {
				int sum = items.stream()
					.filter(item -> resource.create().isSimilar(item))
					.mapToInt(i -> i.getAmount()).sum();
				resources.put(resource, sum);
			});
		return resources;
	}

	public String getColoredName() {
		return color + name;
	}

}
