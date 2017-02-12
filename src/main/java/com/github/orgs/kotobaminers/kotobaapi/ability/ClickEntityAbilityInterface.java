package com.github.orgs.kotobaminers.kotobaapi.ability;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;

public interface ClickEntityAbilityInterface extends ItemStackAbilityInterface, PlayerEntityInteractive {


	int getConsumption();


	default void consumeInHand(Player player) {
		KotobaItemStack.consume(player.getInventory(), player.getItemInHand(), getConsumption());
	}


	@Override
	default boolean isSame(PlayerInteractEntityEvent event) {
		ItemStack itemStack = event.getPlayer().getItemInHand();
		if(itemStack != null) {
			return getIcon().isIconItemStack(itemStack);
		}
		return false;
	}

}
