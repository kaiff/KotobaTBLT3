package com.github.orgs.kotobaminers.kotobatblt3.userinterface;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaSound;

public class TBLTPlayerGUIListener implements Listener {
	@EventHandler
	void onInventoryClick(InventoryClickEvent event) {
		TBLTPlayerGUI.find(event.getInventory())
			.ifPresent(e -> {
				event.setCancelled(true);
				KotobaSound.CLICK.play(((Player) event.getWhoClicked()).getLocation());
				e.onInventoryClick(event);
			});
	}
}
