package com.github.orgs.kotobaminers.kotobatblt3.game;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaAPIUtility;
import com.github.orgs.kotobaminers.kotobatblt3.gui.TBLTGUI;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Utility;

public class TBLTArenaListener implements Listener {
	@EventHandler
	void onItemSpawn(ItemSpawnEvent event) {
		if(TBLTArena.isInArena(event.getLocation())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	void onItemPickup(PlayerPickupItemEvent event) {
		if(Utility.isTBLTPlaying(event.getPlayer())) {
			event.setCancelled(true);
			event.getItem().remove();
		}
	}

	@EventHandler
	void onItemDrop(PlayerDropItemEvent event) {
		if(Utility.isTBLTPlaying(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	void onInventoryOpen(InventoryOpenEvent event) {
		if(event.getPlayer() instanceof Player) {
			Player player = (Player) event.getPlayer();
			if(Utility.isTBLTPlaying(player)) {
				if(!TBLTGUI.find(event.getInventory()).isPresent()) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	void onInventoryClick(InventoryClickEvent event) {
		if(event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			if(Utility.isTBLTPlaying(player)) {
				if(!TBLTGUI.find(event.getInventory()).isPresent()) {
					event.getWhoClicked().closeInventory();
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onEntityPortalTeleport(EntityPortalEvent event) {
		if(event.getEntityType().equals(EntityType.DROPPED_ITEM)) {
			if(!TBLTArena.isInArena(event.getEntity().getLocation())) return;
			if(event.getEntity() instanceof Item) {
				Item item = (Item) event.getEntity();
				ItemMeta itemMeta = item.getItemStack().getItemMeta();
				if(itemMeta.getDisplayName().equalsIgnoreCase("Portal Bomb")) {
					World world = item.getWorld();
					Location location = item.getLocation();
					for(int i = 0; i < 10; i++) {
						final int data = i;
						Setting.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Setting.getPlugin(), new Runnable() {
							@Override
							public void run() {
								world.playEffect(KotobaAPIUtility.randomizeLocation(location, 3), Effect.ENDER_SIGNAL, data);
								world.playSound(location, Sound.PORTAL_TRAVEL, 0.5F, 0.5F);
							}
						}, 20 + data * 8L);
					}
					for(int i = 0; i < 100; i++) {
						final int data = i;
						Setting.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Setting.getPlugin(), new Runnable() {
							@Override
							public void run() {
								world.playEffect(KotobaAPIUtility.randomizeLocation(location, 3), Effect.SMOKE, data);
								world.playSound(location, Sound.FIZZ, 0.5F, 1);
							}
						}, 10 * 20L);
					}
					for(int i = 0; i < 3; i++) {
						final int delay = i;
						Setting.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Setting.getPlugin(), new Runnable() {
							@Override
							public void run() {
								world.createExplosion(KotobaAPIUtility.randomizeLocation(location, 2), 6);
							}
						}, 4 * delay + 12 * 20L);
					}
				}
			}
		}
	}
}
