package com.github.orgs.kotobaminers.kotobaapi.ability;

import org.bukkit.entity.Player;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;

public interface ItemStackConsumer {
	int getConsumption();
	default void consumeInHand(Player player) {
		KotobaItemStack.consume(player.getInventory(), player.getItemInHand(), getConsumption());
	}
}
