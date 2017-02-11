package com.github.orgs.kotobaminers.kotobaapi.block;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;

public interface ConfigChest {

	List<Chest> findChests(Location location);
	KotobaItemStackIcon getKey();

	default boolean hasChests(Location location) {
		if(0 < findChests(location).size()) return true;
		return false;
	}

	default List<ItemStack> findOptions(Location location) {
		List<ItemStack> contents = findChests(location).stream()
			.flatMap(c -> Stream.of(c.getInventory().getContents()).filter(i -> i != null))
			.collect(Collectors.toList());
		if(contents.stream().anyMatch(i -> getKey().isIconItemStack(i))) {
			return contents.stream()
				.filter(i -> !getKey().isIconItemStack(i))
				.collect(Collectors.toList());
		}
		return new ArrayList<ItemStack>();

	}


}
