package com.github.orgs.kotobaminers.kotobatblt3.userinterface;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaSound;

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
					KotobaSound.CLICK.play(((Player) event.getWhoClicked()).getLocation());
					clicker.openInventory(gui.createInventory(gui.getPreviousPage(inventory)));
					return;
				}
				if(gui.isNextClicked(rawSlot)) {
					KotobaSound.CLICK.play(((Player) event.getWhoClicked()).getLocation());
					clicker.openInventory(gui.createInventory(gui.getNextPage(inventory)));
					return;
				}
				if(gui.isIconClicked(rawSlot)) {
					ClickType click = event.getClick();
					if(Arrays.asList(ClickType.LEFT, ClickType.SHIFT_LEFT).contains(click)) {
						KotobaSound.CLICK.play(((Player) event.getWhoClicked()).getLocation());
						gui.onIconLeftClickEvent(event);
						return;
					}
					if(Arrays.asList(ClickType.RIGHT, ClickType.SHIFT_RIGHT).contains(click)) {
						KotobaSound.CLICK.play(((Player) event.getWhoClicked()).getLocation());
						gui.onIconRightClickEvent(event);
						return;
					}
				}
				return;
			});
	}
}
