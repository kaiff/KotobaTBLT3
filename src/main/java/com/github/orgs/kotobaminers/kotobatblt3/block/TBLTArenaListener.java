package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.TBLTPlayer;
import com.github.orgs.kotobaminers.kotobatblt3.userinterface.TBLTPlayerGUI;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTUtility;

public class TBLTArenaListener implements Listener {


	@EventHandler
	void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(TBLTUtility.isTBLTPlayer(player)) {
			TBLTPlayer.updatePotionEffects(player);
		}
	}


	@EventHandler
	void onHitEnemy(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		Entity damaged = event.getEntity();
		if(new TBLTArenaMap().isInAny(damaged.getLocation())) {
			if(damager.getType() == EntityType.PLAYER) {
				event.setCancelled(true);
			}
		}
	}


	@EventHandler
	void onEntityDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(TBLTUtility.isTBLTPlayer(player)) {
				event.setCancelled(true);
			}
		}
	}


	@EventHandler
	void onPlayerRegainHealth(EntityRegainHealthEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(!TBLTUtility.isTBLTPlayer(player)) return;

			if(event.getRegainReason() == RegainReason.SATIATED) {
				int rate = 5;
				event.setCancelled(true);
				double health = player.getHealth() + event.getAmount() * rate;
				health = Math.min(health, player.getMaxHealth());
				player.setHealth(health);
			}
		}
	}


	@EventHandler
	void onPlayerChangeFoodLevel(FoodLevelChangeEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(TBLTUtility.isTBLTPlayer(player)) {
				event.setCancelled(true);
				player.setFoodLevel(20);
				player.setSaturation(20);
			}
		}
	}


	@EventHandler
	void onPlayerFallBottom(PlayerInteractEvent event) {
		Player player = (Player) event.getPlayer();

		Location location = player.getLocation();

		if(TBLTUtility.isTBLTPlayer(player)) {
			new TBLTArenaMap().findUnique(location)
			.ifPresent(storage -> {
				TBLTArena arena = (TBLTArena) storage;
				if(arena.getYMin() == location.getBlockY() - 1) {
					event.setCancelled(true);
					arena.restart();
				}
			});
		}
	}


	@EventHandler
	void onVehicleExit(VehicleExitEvent event) {
		LivingEntity exited = event.getExited();
		if(exited instanceof Player) {
			Player player = (Player) exited;
			if(TBLTUtility.isTBLTPlayer(player)) {
				Vehicle vehicle = event.getVehicle();
				Location location = exited.getLocation();
				KotobaEffect.MAGIC_MIDIUM.playEffect(location);
				KotobaEffect.MAGIC_MIDIUM.playSound(location);
				vehicle.remove();
			}
		}
	}


	@EventHandler
	void onItemPickup(PlayerPickupItemEvent event) {
		if(TBLTUtility.isTBLTPlayer(event.getPlayer())) {
			event.setCancelled(true);
		}
	}


	@EventHandler
	void onItemDrop(PlayerDropItemEvent event) {
		if(TBLTUtility.isTBLTPlayer(event.getPlayer())) {
			event.setCancelled(true);
		}
	}


	@EventHandler
	void cancelSplashPotion(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		List<Action> actions = Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);
		if(actions.contains(event.getAction())) {
			ItemStack itemStack = player.getItemInHand();
			if(itemStack.getType() == Material.POTION) {
				Potion potion = Potion.fromItemStack(itemStack);
				if(potion.isSplash()) {
					if(TBLTUtility.isTBLTPlayer(player)) {
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}


	@EventHandler
	void onInventoryOpen(InventoryOpenEvent event) {
		if(event.getPlayer() instanceof Player) {
			Player player = (Player) event.getPlayer();
			if(TBLTUtility.isTBLTPlayer(player)) {
				if(!TBLTPlayerGUI.find(event.getInventory()).isPresent()) {
					event.setCancelled(true);
				}
			}
		}
	}


	@EventHandler
	void onInventoryClick(InventoryClickEvent event) {
		if(event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			if(TBLTUtility.isTBLTPlayer(player)) {
				if(!TBLTPlayerGUI.find(event.getInventory()).isPresent()) {
					event.getWhoClicked().closeInventory();
					event.setCancelled(true);
				}
			}
		}
	}


	@EventHandler
	public void onPlayerPortalEvent(PlayerPortalEvent event) {
		if(TBLTUtility.isTBLTPlayer(event.getPlayer())) {
			event.setCancelled(true);
			Stream.of(ChestPortal.values())
				.forEach(p -> p.findChests(event.getFrom()).forEach(c -> p.goThroughPortal(event)));
		}
	}


}

