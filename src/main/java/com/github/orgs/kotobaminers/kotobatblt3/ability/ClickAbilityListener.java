package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
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

				Optional<List<Boolean>> founds = new TBLTArenaMap().findUnique(player.getLocation())
					.map(storage -> abilities.stream().map(
						ability -> {
							boolean successed = false;
							if(ability.isCorrectAction(event.getAction())) {
								successed = ability.perform(event);//Perform ability here
								if(successed) {
									ability.consumeInHand(player);
								}
							}
							return successed;
						}).collect(Collectors.toList()));

				if(founds.isPresent()) {
					if(founds.get().stream().anyMatch(f -> f == true)) {
						return;
					}
				}
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
						boolean successed = false;
						successed = ability.perform(event);//Perform ability here
						if(successed) {
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
