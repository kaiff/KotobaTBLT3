package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class SwitchableChestManager {
	public static List<SwitchableChest> getSwitchableChests() {
		return Stream.of(
			Stream.of(SwitchableStructure.values()),
			Stream.of(new ReplacerSwitchChest())
		)
			.flatMap(a -> a)
			.collect(Collectors.toList());
	}


	public static List<SwitchableChest> find(Chest chest) {
		List<ItemStack> contents = Stream.of(chest.getInventory().getContents())
			.filter(i -> i != null)
			.collect(Collectors.toList());
		return getSwitchableChests().stream()
			.filter(s -> contents.stream().anyMatch(c -> s.getIcon().isIconItemStack(c)))
			.collect(Collectors.toList());
	}


}
