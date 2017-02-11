package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.ability.ClickBlockAbilityInterface;
import com.github.orgs.kotobaminers.kotobaapi.ability.ItemStackAbilityManager;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaSound;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArena;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.game.TBLTData;
import com.github.orgs.kotobaminers.kotobatblt3.utility.Utility;

public class ClickAbilityListener implements Listener {
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		List<Action> clicks = Arrays.asList(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);
		if(!clicks.contains(event.getAction())) return;

		if(!Utility.isTBLTPlayer(player)) return;

		List<ClickBlockAbilityInterface> abilities = ItemStackAbilityManager.find(player.getItemInHand())
			.stream()
			.filter(a -> a instanceof ClickBlockAbilityInterface)
			.map(a -> (ClickBlockAbilityInterface) a)
			.collect(Collectors.toList());

		if(0 < abilities.size()) {
			event.setCancelled(true);
			if(canPerformLocation(player)) {

				Optional<List<Boolean>> founds = new TBLTArenaMap().findUnique(player.getLocation())
					.map(storage -> abilities.stream().map(
						a -> {
							boolean successed = false;
							if(a.isCorrectAction(event.getAction())) {
								successed = a.perform(event);//Perform ability here
								if(successed) {
									a.consumeInHand(player);
									TBLTData.getOrDefault(player.getUniqueId()).updateAbilityUsed(a);
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
							TBLTData.getOrDefault(player.getUniqueId()).updateAbilityUsed(a);
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


}

