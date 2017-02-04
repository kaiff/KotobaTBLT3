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
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.ability.ClickBlockAbilityInterface;
import com.github.orgs.kotobaminers.kotobaapi.ability.ItemStackAbilityManager;
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

		ItemStackAbilityManager.find(player.getItemInHand());


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

		ItemStackAbilityManager.find(event.getPlayer().getItemInHand())
			.stream()
			.filter(a -> a instanceof ClickEntityAbility)
			.map(a -> (ClickEntityAbility) a)
			.forEach(a -> {
				event.setCancelled(true);
				new TBLTArenaMap().findUnique(player.getLocation())
					.map(arena -> (TBLTArena) arena)
					.ifPresent(arena -> {
						boolean successed = false;
						successed = a.perform(event);//Perform ability here
						if(successed) {
							a.consumeInHand(player);
						} else {
							KotobaSound.SHEAR.play(player.getLocation());
							return;
						}
					});
		});
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
