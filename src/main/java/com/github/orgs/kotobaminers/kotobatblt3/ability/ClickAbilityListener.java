package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaSound;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArena;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.utility.Utility;

public class ClickAbilityListener implements Listener {
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();

		if(!Utility.isTBLTPlayer(player)) return;

		List<ClickBlockAbilityInterface> abilities = new ArrayList<>();
		ClickBlockChestAbility.find(event).stream()
			.forEach(ability -> abilities.add(ability));

		ClickBlockAbility.find(player.getItemInHand()).stream()
			.forEach(ability -> abilities.add(ability));
		ProjectileAbility.find(player.getItemInHand()).stream()
			.forEach(ability -> abilities.add(ability));

		if(0 < abilities.size()) {
			event.setCancelled(true);
			if(canPerformLocation(player)) {
			new TBLTArenaMap().findUnique(player.getLocation())
				.ifPresent(storage -> abilities.stream().forEach(
					ability -> {
						if(ability.isCorrectAction(event.getAction())) {
							TBLTArena arena = (TBLTArena) storage;
							if(!arena.hasResources(ability.getResourceConsumption(block))) {
								player.openInventory(arena.getResourceConsumptionInventory(ability.getResourceConsumption(block)));
							} else {
								boolean successed = false;
								successed = ability.perform(event);//Perform ability here
								if(successed) {
									arena.consumeResources(ability.getResourceConsumption(block));
									ability.consumeInHand(player);
									return;
								}
							}
						}
					}));
			}
			//Case: Found any ability but perfomed
			performFailureEffect(player.getLocation());
		}
	}

	private boolean canPerformLocation(Player player) {
		boolean onGround = ((Entity) player).isOnGround();
		boolean atLadder = player.getLocation().getBlock().getType() == Material.LADDER;
		if(onGround || atLadder) {
			return true;
		}
		return false;
	}
	private void performFailureEffect(Location location) {
		KotobaSound.SHEAR.play(location);
	}

	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		ItemStack itemStack = player.getItemInHand();

		if(itemStack.getType() == Material.AIR) return;
		if(!((Entity) player).isOnGround()) return;
		if(!Utility.isTBLTPlayer(player)) return;

		ClickEntityAbility.find(event.getPlayer().getItemInHand())
			.ifPresent(ability -> {
				event.setCancelled(true);
				new TBLTArenaMap().findUnique(player.getLocation())
					.map(arena -> (TBLTArena) arena)
					.ifPresent(arena -> {
						if(!arena.hasResources(ability.getResourceConsumption(null))) {
							player.openInventory(arena.getResourceConsumptionInventory(ability.getResourceConsumption(null)));
							KotobaSound.SHEAR.play(player.getLocation());
							return;
						}

						boolean successed = false;
						successed = ability.perform(event);//Perform ability here
						if(successed) {
							arena.consumeResources(ability.getResourceConsumption(null));
							ability.consumeInHand(player);
						} else {
							KotobaSound.SHEAR.play(player.getLocation());
							return;
						}
					});
		});
	}

	@EventHandler
	public void onPotionSplash(PotionSplashEvent event) {
		if(event.getPotion().getEffects().size() == 0) {
			if(!new TBLTArenaMap().isInAny(event.getEntity().getLocation())) return;
			TBLTPotion.find(event).eventSplash(event);
		}
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		Location location = event.getEntity().getLocation();
		if(!new TBLTArenaMap().isInAny(location)) return;
		ProjectileAbility.find(event.getEntity())
			.ifPresent(p -> p.onHit(event));
	}

	@EventHandler
	public void onEntityDamagedByEntity(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Snowball) {
		}
	}
}
