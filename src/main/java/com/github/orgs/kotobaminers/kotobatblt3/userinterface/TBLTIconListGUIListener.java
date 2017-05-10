package com.github.orgs.kotobaminers.kotobatblt3.userinterface;

import java.util.Arrays;

import org.bukkit.Material;
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
				int rawSlot = event.getRawSlot();
				Inventory inventory = event.getInventory();
				if(event.getWhoClicked() instanceof Player) {
					Player player = (Player) event.getWhoClicked();
					if(gui.isPreviousClicked(rawSlot)) {
						KotobaSound.CLICK.play(player.getLocation());
						player.openInventory(gui.createInventory(player, gui.getPreviousPage(player, inventory)));
						return;
					}
					if(gui.isNextClicked(rawSlot)) {
						KotobaSound.CLICK.play(player.getLocation());
						player.openInventory(gui.createInventory(player, gui.getNextPage(player, inventory)));
						return;
					}
					if(gui.isIconClicked(rawSlot)) {
						ClickType click = event.getClick();
						if(Arrays.asList(ClickType.LEFT, ClickType.SHIFT_LEFT).contains(click)) {
							KotobaSound.CLICK.play(player.getLocation());
							gui.onIconLeftClickEvent(event);
							return;
						}
						if(Arrays.asList(ClickType.RIGHT, ClickType.SHIFT_RIGHT).contains(click)) {
							KotobaSound.CLICK.play(player.getLocation());
							gui.onIconRightClickEvent(event);
							return;
						}
					}
				}

				return;
			});
	}
}
