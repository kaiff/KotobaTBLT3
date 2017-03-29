package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.Arrays;
import java.util.List;
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

import com.github.orgs.kotobaminers.kotobaapi.block.PlayerBlockInteractive;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaSound;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArena;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.database.TBLTData;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTCooldown;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTUtility;

public class ClickAbilityListener implements Listener {
	@EventHandler
	void onPlayerInteractBlock(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		List<Action> clicks = Arrays.asList(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);
		if(!clicks.contains(event.getAction())) return;

		if(TBLTCooldown.INTERACTIVE.isCooldown(player.getUniqueId())) return;
		if(!TBLTUtility.isTBLTPlayer(player)) return;

		List<PlayerBlockInteractive> interactives = PlayerInteractiveManager.find(event);


		if(0 < interactives.size()) {
			event.setCancelled(true);
			List<Boolean> success = interactives.stream().map(i -> i.interact(event)).collect(Collectors.toList());
			if(!success.contains(true)) {
				playFailureEffect(player.getLocation());
			} else {
				TBLTCooldown.INTERACTIVE.updateCooldown(player.getUniqueId());
			}
		}
	}


	private void playFailureEffect(Location location) {
		KotobaSound.SHEAR.play(location);
	}


	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		ItemStack itemStack = player.getItemInHand();

		if(itemStack.getType() == Material.AIR) return;
		if(!((Entity) player).isOnGround()) return;
		if(!TBLTUtility.isTBLTPlayer(player)) return;

		PlayerInteractiveManager.find(event)
			.stream()
			.filter(a -> a instanceof ClickEntityAbility)
			.map(a -> (ClickEntityAbility) a)
			.forEach(a -> {
				event.setCancelled(true);
				new TBLTArenaMap().findUnique(player.getLocation())
					.map(arena -> (TBLTArena) arena)
					.ifPresent(arena -> {
						boolean successed = false;
						successed = a.interact(event);//Perform ability here
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

