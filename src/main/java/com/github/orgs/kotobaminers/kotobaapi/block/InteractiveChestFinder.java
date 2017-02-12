package com.github.orgs.kotobaminers.kotobaapi.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;

public interface InteractiveChestFinder extends KotobaConfigChestFinder {


	default List<Chest> findChests(PlayerInteractEvent event, KotobaItemStackIcon icon) {
		return findTargetBlock(event)
			.map(b -> findChests(b.getLocation()))
			.orElse(new ArrayList<>()).stream()
			.filter(c ->
				Stream.of(c.getInventory().getContents())
					.filter(i -> i != null)
					.anyMatch(i -> icon.isIconItemStack(i)))
			.collect(Collectors.toList());
	}


	default List<ItemStack> findOptions(PlayerInteractEvent event, List<KotobaItemStackIcon> exceptions) {
		return findTargetBlock(event)
			.map(b -> findOptions(b.getLocation(), exceptions))
			.orElse(new ArrayList<>());
	}


	default Optional<Block> findTargetBlock(PlayerInteractEvent event) {
		return getTargetType().getTargetBlock(event);
	}


	InteractType getTargetType();


	enum InteractType {
		AIR {
			@Override
			Optional<Block> getTargetBlock(PlayerInteractEvent event) {
				Block block = event.getClickedBlock().getRelative(event.getBlockFace());
				if(block.getType() == Material.AIR) {
					return Optional.of(block);
				}
				return Optional.empty();
			}
		},
		BLOCK {
			@Override
			Optional<Block> getTargetBlock(PlayerInteractEvent event) {
				return Optional.ofNullable(event.getClickedBlock());
			}
		},
		;

		abstract Optional<Block> getTargetBlock(PlayerInteractEvent event);

	}


}

