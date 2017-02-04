package com.github.orgs.kotobaminers.kotobatblt3.ability;

import org.bukkit.event.player.PlayerInteractEntityEvent;

public interface ClickEntityAbilityInterface extends ItemAbilityInterface {
	boolean perform(PlayerInteractEntityEvent event);
}
