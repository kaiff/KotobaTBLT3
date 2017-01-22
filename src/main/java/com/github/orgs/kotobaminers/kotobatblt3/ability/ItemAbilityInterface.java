package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;


public interface ItemAbilityInterface {
	Material getMaterial();
	short getData();
	String getName();
	List<String> getLore();
	int getConsumption();

	static final List<ItemFlag> FLAGS = Arrays.asList(ItemFlag.HIDE_ATTRIBUTES);
	static final int LORE_LENGTH = 25;

	default ItemStack createItem(int amount) {
		ItemStack item = new ItemStack(getMaterial(), amount, getData());
		if(getLore() != null) {
			List<String> splited = getLore().stream().flatMap(line -> KotobaUtility.splitSentence(line, LORE_LENGTH).stream()).collect(Collectors.toList());
			item = KotobaItemStack.setColoredLore(item, ChatColor.RESET, splited);
		}

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getName());

		FLAGS.stream().forEach(flag -> meta.addItemFlags(flag));

		item.setItemMeta(meta);
		return item;
	}
	default void consumeInHand(Player player) {
		KotobaItemStack.consume(player.getInventory(), player.getItemInHand(), getConsumption());
	}
}
