package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobatblt3.ability.ProjectileAbility;
import com.github.orgs.kotobaminers.kotobatblt3.gui.TBLTPlayerGUI;
import com.github.orgs.kotobaminers.kotobatblt3.utility.Utility;

public class TBLTArenaListener implements Listener {


	@EventHandler
	void onPlayerInteractStructure(PlayerInteractEvent event) {
//		Player player = event.getPlayer();
//		Block block = event.getClickedBlock();
//		if(block == null) return;
//		ItemStack itemStack = player.getItemInHand();
//		if(itemStack == null) return;
//		if(itemStack.getType() != Material.AIR) return;
//
//		PlayerInteractiveManager.find(event).stream()
//			.filter(i -> i instanceof InteractiveStructure)
//			.map(i -> (InteractiveStructure) i)
//			.forEach(i -> i.interact(event));

	}


	@EventHandler
	void onBlockReplacerPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(block == null) return;
		if(Utility.isTBLTPlayer(player)) {
			new BlockReplacerMap().findUnique(block.getLocation())
				.ifPresent(replacer -> {
					boolean success = ((BlockReplacer) replacer).replace(player, block);
					if(success) KotobaItemStack.consume(player.getInventory(), player.getItemInHand(), 1);
				});
		}
	}


	@EventHandler
	void onHitEnemy(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		Entity damaged = event.getEntity();
		if(new TBLTArenaMap().isInAny(damaged.getLocation())) {
			if(damager.getType() == EntityType.PLAYER) {
				event.setCancelled(true);
			} else if(damager instanceof Projectile) {
				Projectile projectile =(Projectile) damager;
				ProjectileAbility.find(projectile)
					.ifPresent(a -> event.setCancelled(true));
			}
		}
	}


	@EventHandler
	void onEntityDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(!Utility.isTBLTPlayer(player)) return;

			double damage = 0;
			//Fall damage multiply
			switch(event.getCause()) {
			case FALL:
				int rate = 5;
				damage = event.getDamage() * rate;
				event.setCancelled(true);
				break;
			default:
				break;
			}

			if(player.getHealth() - damage <= 0) {
				Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(player.getName() + " died"));
				event.setCancelled(true);
				player.setHealth(20);
				new TBLTArenaMap().findUnique(player.getLocation())
					.map(arena -> (TBLTArena) arena)
					.ifPresent(arena -> Bukkit.getOnlinePlayers().stream().filter(p -> arena.isIn(p.getLocation()) && p.getGameMode() == GameMode.ADVENTURE).forEach(p -> arena.continueFromCurrent(p)));
			} else {
				player.damage(damage);
			}
		}
	}


	@EventHandler
	void onPlayerRegainHealth(EntityRegainHealthEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(!Utility.isTBLTPlayer(player)) return;

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
			if(Utility.isTBLTPlayer(player)) {
				event.setCancelled(true);
				player.setFoodLevel(20);
				player.setSaturation(20);
			}
		}
	}


	@EventHandler
	void onPlayerFallBottom(EntityChangeBlockEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();

			Block block = event.getBlock();
			if(block.getType() == null) return;
			if(block.getType() != Material.REDSTONE_ORE) return;
			if(event.getTo() != Material.GLOWING_REDSTONE_ORE) return;

			if(Utility.isTBLTPlayer(player)) {
				new TBLTArenaMap().findUnique(block.getLocation())
					.ifPresent(storage -> {
						TBLTArena arena = (TBLTArena) storage;
						if(arena.getYMin() == block.getLocation().getBlockY()) {
							event.setCancelled(true);
							arena.load();
							arena.getTBLTPlayers().forEach(p -> arena.continueFromCurrent(p));
						}
					});
			}
		}
	}


	@EventHandler
	void onVehicleExit(VehicleExitEvent event) {
		LivingEntity exited = event.getExited();
		if(exited instanceof Player) {
			Player player = (Player) exited;
			if(Utility.isTBLTPlayer(player)) {
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
		if(Utility.isTBLTPlayer(event.getPlayer())) {
			event.setCancelled(true);
		}
	}


	@EventHandler
	void onItemDrop(PlayerDropItemEvent event) {
		if(Utility.isTBLTPlayer(event.getPlayer())) {
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
					if(Utility.isTBLTPlayer(player)) {
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
			if(Utility.isTBLTPlayer(player)) {
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
			if(Utility.isTBLTPlayer(player)) {
				if(!TBLTPlayerGUI.find(event.getInventory()).isPresent()) {
					event.getWhoClicked().closeInventory();
					event.setCancelled(true);
				}
			}
		}
	}


	@EventHandler
	public void onPlayerPortalEvent(PlayerPortalEvent event) {
		if(Utility.isTBLTPlayer(event.getPlayer())) {
			event.setCancelled(true);
			Stream.of(ChestPortal.values())
				.filter(p -> 0 < p.chest.findChests(event.getFrom()).size())
				.forEach(p -> p.enterPortal(event));
		}
	}


}

