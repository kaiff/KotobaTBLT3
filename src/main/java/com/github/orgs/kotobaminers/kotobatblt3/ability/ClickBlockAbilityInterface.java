package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.orgs.kotobaminers.kotobatblt3.resource.ResourceConsumer;

import net.md_5.bungee.api.ChatColor;

public interface ClickBlockAbilityInterface extends ItemAbilityInterface, ResourceConsumer {
	default boolean isCorrectAction(Action action) {
		return getTriggers().contains(action);
	}
	List<Action> getTriggers();
	boolean perform(PlayerInteractEvent event);

	default List<String> getResourceLore(Block block) {
		return getResourceConsumption(block).entrySet().stream()
			.map(entry -> "" + ChatColor.RESET + entry.getValue() + " " + entry.getKey().getColoredName())
			.collect(Collectors.toList());
	}
}