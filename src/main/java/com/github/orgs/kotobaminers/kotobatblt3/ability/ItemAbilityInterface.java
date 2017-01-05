package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;

public interface ItemAbilityInterface {
	Material getMaterial();
	short getData();
	String getName();
	List<String> getLore();
	int getConsumption();

	List<ItemFlag> flags = Arrays.asList(ItemFlag.HIDE_ATTRIBUTES);

	default ItemStack createItem(int amount) {
		ItemStack item = new ItemStack(getMaterial(), amount, getData());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getName());
		if(getLore() != null) {
			meta.setLore(getLore());
		}

		flags.stream().forEach(flag -> meta.addItemFlags(flag));

		item.setItemMeta(meta);
		return item;
	}
	default void consumeInHand(Player player) {
		KotobaItemStack.consume(player.getInventory(), player.getItemInHand(), getConsumption());
	}
}
