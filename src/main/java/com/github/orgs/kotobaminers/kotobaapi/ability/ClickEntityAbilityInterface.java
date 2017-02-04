package com.github.orgs.kotobaminers.kotobaapi.ability;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;

public interface ClickEntityAbilityInterface extends ItemStackAbilityInterface {
	boolean perform(PlayerInteractEntityEvent event);
	int getConsumption();

	default void consumeInHand(Player player) {
		KotobaItemStack.consume(player.getInventory(), player.getItemInHand(), getConsumption());
	}
}
