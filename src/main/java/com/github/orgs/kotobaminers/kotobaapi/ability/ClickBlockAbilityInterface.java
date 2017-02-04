package com.github.orgs.kotobaminers.kotobaapi.ability;

import java.util.List;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public interface ClickBlockAbilityInterface extends ItemStackAbilityInterface {

	List<Action> getTriggers();

	default boolean isCorrectAction(Action action) {
		return getTriggers().contains(action);
	}

	boolean perform(PlayerInteractEvent event);

}