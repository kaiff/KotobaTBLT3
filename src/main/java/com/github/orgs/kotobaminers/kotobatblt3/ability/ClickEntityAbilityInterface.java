package com.github.orgs.kotobaminers.kotobatblt3.ability;

import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.github.orgs.kotobaminers.kotobatblt3.resource.ResourceConsumer;

public interface ClickEntityAbilityInterface extends ItemAbilityInterface, ResourceConsumer {
	boolean perform(PlayerInteractEntityEvent event);
}
