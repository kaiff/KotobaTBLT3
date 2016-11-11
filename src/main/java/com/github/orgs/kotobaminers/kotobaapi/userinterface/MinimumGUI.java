package com.github.orgs.kotobaminers.kotobaapi.userinterface;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface MinimumGUI {
	int getSize();
	void setSize(int size);
	String getTitle();
	void onInventoryClick(InventoryClickEvent event);
	default Optional<Inventory> create(List<ItemStack> icons) {
		setSize(icons.size());
		if(6 * 9 < getSize()) return Optional.empty();
		Inventory inventory = Bukkit.createInventory(null, (getSize() / 9 + 1) * 9, getTitle());

		Stream.iterate(0, i -> i + 1)
			.limit(icons.size())
			.forEach(i -> inventory.setItem(i, icons.get(i)));
		return Optional.ofNullable(inventory);
	}
	default boolean isMinimumGUI(Inventory inventory) {
		if(inventory.getTitle().equalsIgnoreCase(getTitle())) {
			return true;
		}
		return false;
	}
	default boolean isIconClicked(int rawSlot) {
		if(0 <=rawSlot && rawSlot < getSize()) {
			return true;
		}
		return false;
	}
}