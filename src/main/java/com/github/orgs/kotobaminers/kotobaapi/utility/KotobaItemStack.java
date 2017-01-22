package com.github.orgs.kotobaminers.kotobaapi.utility;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class KotobaItemStack extends ItemStack {
	private enum MaterialConverter {
		IRONDOOR(Arrays.asList(Material.IRON_DOOR_BLOCK), Arrays.asList(Material.IRON_DOOR)),
		;
		private List<Material> blocks;
		private List<Material> items;

		private MaterialConverter(List<Material> blocks, List<Material> items) {
			this.blocks = blocks;
			this.items = items;
		}

//		static Material toBlockMaterial(Material material) {
//			return Stream.of(MaterialConverter.values())
//				.filter(mc -> mc.items.contains(material))
//				.findAny()
//				.map(mc-> mc.blocks.get(0))
//				.orElse(material);
//		}
		static Material toItemMaterial(Material material) {
			return Stream.of(MaterialConverter.values())
				.filter(mc -> mc.blocks.contains(material))
				.findAny()
				.map(mc-> mc.items.get(0))
				.orElse(material);
		}
	}

	private KotobaItemStack(Material material, int amount, short data) {
		super(material, amount, data);
	}

	public static ItemStack create(Material material, short data, int amount, String name, List<String> lore) {
		ItemStack itemStack = new KotobaItemStack(MaterialConverter.toItemMaterial(material), amount, data);
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

	public static ItemStack create(ItemStack itemStack) {
		ItemStack kotobaItem = new KotobaItemStack(MaterialConverter.toItemMaterial(itemStack.getType()), itemStack.getAmount(), itemStack.getDurability());
		ItemMeta itemMeta = itemStack.getItemMeta();
		kotobaItem.setItemMeta(itemMeta);
		return kotobaItem;
	}

	public static void consume(Inventory inventory, ItemStack item, int amount) {
		if(item.getAmount() - amount < 1) {
			item.setAmount(-1);//Magic value
			Stream.iterate(0, i -> i + 1)
				.limit(inventory.getSize())
				.filter(i -> inventory.getItem(i) != null)
				.filter(i -> inventory.getItem(i).getAmount() < 1)
				.forEach(i -> inventory.setItem(i, new ItemStack(Material.AIR)));
		} else {
			item.setAmount(item.getAmount() - amount);
		}
	}

	public static ItemStack setColoredLore(ItemStack itemStack, ChatColor color, List<String> lore) {
		ItemStack clone = itemStack.clone();
		lore = lore.stream()
			.map(s -> "" + ChatColor.RESET + color + s)
			.collect(Collectors.toList());
		ItemMeta meta = clone.getItemMeta();
		meta.setLore(lore);
		clone.setItemMeta(meta);
		return clone;
	}

}
