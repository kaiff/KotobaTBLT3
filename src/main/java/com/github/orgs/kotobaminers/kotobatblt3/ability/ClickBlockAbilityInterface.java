package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.List;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public interface ClickBlockAbilityInterface extends ItemAbilityInterface {
	default boolean isCorrectAction(Action action) {
		return getTriggers().contains(action);
	}
	List<Action> getTriggers();
	boolean perform(PlayerInteractEvent event);

}