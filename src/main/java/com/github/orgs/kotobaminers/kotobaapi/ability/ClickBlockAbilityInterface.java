package com.github.orgs.kotobaminers.kotobaapi.ability;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.block.PlayerBlockInteractive;
import com.github.orgs.kotobaminers.kotobatblt3.game.TBLTData;

public interface ClickBlockAbilityInterface extends ItemStackAbilityInterface, PlayerBlockInteractive {


	List<Action> getTriggers();
	boolean perform(PlayerInteractEvent event);


	default boolean isCorrectAction(Action action) {
		return getTriggers().contains(action);
	}


	@Override
	default boolean isSame(PlayerInteractEvent event) {
		ItemStack itemStack = event.getPlayer().getItemInHand();
		if(itemStack != null) {
			return getIcon().isIconItemStack(itemStack);
		}
		return false;
	}


	@Override
	default boolean interact(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if(!canPerformLocation(player)) return false;

		boolean success = false;
		if(isCorrectAction(event.getAction())) {
			success = perform(event);
			if(success) {
				consumeInHand(player);
				TBLTData.getOrDefault(player.getUniqueId()).updateAbilityUsed(this);
				return true;
			}
		}

		return false;
	}

	default boolean canPerformLocation(Player player) {
		boolean onGround = ((Entity) player).isOnGround();
		boolean atLadder = player.getLocation().getBlock().getType() == Material.LADDER;
		if(onGround || atLadder) {
			return true;
		}
		return false;
	}


}

