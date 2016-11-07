package com.github.orgs.kotobaminers.kotobatblt3.gui;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaAPISound;

public class TBLTIconListGUIListener implements Listener {
	@EventHandler
	void onInventoryInteract(InventoryClickEvent event) {
		TBLTIconListGUI.find(event.getInventory())
			.ifPresent(gui -> {
				if(event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) {
					return;
				}

				event.setCancelled(true);
				HumanEntity clicker = event.getWhoClicked();
				int rawSlot = event.getRawSlot();
				Inventory inventory = event.getInventory();

				if(gui.isPreviousClicked(rawSlot)) {
					KotobaAPISound.CLICK.play((Player) event.getWhoClicked());
					clicker.openInventory(gui.createInventory(gui.getPreviousPage(inventory)));
					return;
				}
				if(gui.isNextClicked(rawSlot)) {
					KotobaAPISound.CLICK.play((Player) event.getWhoClicked());
					clicker.openInventory(gui.createInventory(gui.getNextPage(inventory)));
					return;
				}
				if(gui.isIconClicked(rawSlot)) {
					ClickType click = event.getClick();
					if(Arrays.asList(ClickType.LEFT, ClickType.SHIFT_LEFT).contains(click)) {
						KotobaAPISound.CLICK.play((Player) event.getWhoClicked());
						gui.onIconLeftClickEvent(event);
						return;
					}
					if(Arrays.asList(ClickType.RIGHT, ClickType.SHIFT_RIGHT).contains(click)) {
						KotobaAPISound.CLICK.play((Player) event.getWhoClicked());
						gui.onIconRightClickEvent(event);
						return;
					}
				}
				return;
			});
	}
}
