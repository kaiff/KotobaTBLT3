package com.github.orgs.kotobaminers.kotobaapi.userinterface;

import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;

public interface IconListGUI {
	public static final int PAGE_FAILED = -1;
	public static final int INVENGTORY_SIZE = 9 * 6;
	public static final int ICON_NUMBER = 9 * 5;
	public static final String PREVIOUS = "<= Previous =";
	public static final String NEXT = "= Next =>";

	abstract List<ItemStack> getIcons(Player player);
	abstract String getTitle();
	abstract void onIconLeftClickEvent(InventoryClickEvent event);
	abstract void onIconRightClickEvent(InventoryClickEvent event);

	default public Inventory createInventory(Player player, int page) {
		List<ItemStack> items = getIcons(player);
		int start = (page - 1) * ICON_NUMBER;
		int limit = Math.min(ICON_NUMBER + start, items.size());
		Inventory inventory = Bukkit.createInventory(null, INVENGTORY_SIZE, getTitle() + String.valueOf(page));
		if(items.size() == 0) {
			Inventory inventory2 = Bukkit.createInventory(null, INVENGTORY_SIZE, getTitle() + String.valueOf(1));
			return inventory2;
		}
		Stream.iterate(start, i -> i + 1)
			.limit(limit - start)
			.forEach(i -> inventory.setItem(i - start, items.get(i)));
		inventory.setItem(INVENGTORY_SIZE - 9, KotobaItemStack.create(Material.COOKIE, (short) 0, 1, PREVIOUS, null));
		inventory.setItem(INVENGTORY_SIZE - 1, KotobaItemStack.create(Material.CAKE, (short) 0, 1, NEXT, null));
		return inventory;
	}

	default Integer getCurrentPage(Inventory inventory) {
		String title2 = inventory.getTitle();
		if(title2.startsWith(getTitle())) {
			String str = title2.substring(getTitle().length(), getTitle().length() + 1);
			if(KotobaUtility.isNumber(str)) {
				return Integer.parseInt(str);
			}
		}
		return PAGE_FAILED;
	}
	default Integer getMaxPage(Player player) {
		if(getIcons(player).size() % ICON_NUMBER == 0) {
			return getIcons(player).size() / ICON_NUMBER;
		}
		return getIcons(player).size() / ICON_NUMBER + 1;
	}
	default Integer getPreviousPage(Player player, Inventory inventory) {
		int page = getCurrentPage(inventory);
		if(page == 1) {
			return getMaxPage(player);
		} else {
			return page - 1;
		}
	}
	default Integer getNextPage(Player player, Inventory inventory) {
		int page = getCurrentPage(inventory);
		if(page == getMaxPage(player)) {
			return 1;
		} else {
			return page + 1;
		}
	}

	default boolean isIconClicked(int rawSlot) {
		if(0 <=rawSlot && rawSlot < ICON_NUMBER) {
			return true;
		}
		return false;
	}
	default boolean isPreviousClicked(int rawSlot) {
		if(rawSlot == INVENGTORY_SIZE - 9) {
			return true;
		}
		return false;
	}
	default boolean isNextClicked(int rawSlot) {
		if(rawSlot == INVENGTORY_SIZE - 1) {
			return true;
		}
		return false;
	}

	default boolean isIconListGUI(Inventory inventory) {
		if(getCurrentPage(inventory) != PAGE_FAILED) {
			return true;
		}
		return false;
	}


}