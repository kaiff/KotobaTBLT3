package com.github.orgs.kotobaminers.kotobaapi.userinterface;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface ChestGUI {
	public enum ChestSize {
		MINIMUM {
			@Override
			int getSize(int iconSize) {
				if(6 * 9 < iconSize) return 54;
				return (iconSize / 9 + 1) * 9;
			}
		},
		LARGE {
			@Override
			int getSize(int iconSize) {
				return 54;
			}
		},
		;
		abstract int getSize(int iconSize);
	}

	String getTitle();
	ChestSize getChestSize();

	void onInventoryClick(InventoryClickEvent event);
	default Optional<Inventory> create(List<ItemStack> icons) {
		Inventory inventory = Bukkit.createInventory(null, getChestSize().getSize(icons.size()), getTitle());
		Stream.iterate(0, i -> i + 1)
			.limit(icons.size())
			.forEach(i -> inventory.setItem(i, icons.get(i)));
		return Optional.ofNullable(inventory);
	}
}