package com.github.orgs.kotobaminers.kotobatblt3.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaAPISound;

public class TBLTGUIListener implements Listener {
	@EventHandler
	void onInventoryClick(InventoryClickEvent event) {
		TBLTGUI.find(event.getInventory())
			.ifPresent(e -> {
				event.setCancelled(true);
				if(e.isIconClicked(event.getRawSlot())) {
					KotobaAPISound.CLICK.play((Player) event.getWhoClicked());
					e.onInventoryClick(event);
					return;
				} else {
					return;
				}
			});
	}
}
