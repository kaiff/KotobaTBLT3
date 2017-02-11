package com.github.orgs.kotobaminers.kotobaapi.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;

public interface KotobaConfigChestInterface {


	List<Vector> getPositions();


	InteractType getTargetType();


	default Optional<Block> findTargetBlock(PlayerInteractEvent event) {
		return getTargetType().getTargetBlock(event);
	}


	default List<Chest> findChests(Location location) {
		return getPositions().stream()
			.map(p -> location.clone().subtract(p).getBlock())
			.filter(b -> b.getType() == Material.TRAPPED_CHEST)
			.map(b -> (Chest) b.getState())
			.collect(Collectors.toList());
	}


	default boolean hasChests(Location location) {
		if(0 < findChests(location).size()) return true;
		return false;
	}


	default List<ItemStack> findOptions(Location location, KotobaItemStackIcon key) {
		List<ItemStack> contents = findChests(location).stream()
			.flatMap(c -> Stream.of(c.getInventory().getContents()).filter(i -> i != null))
			.collect(Collectors.toList());
		if(contents.stream().anyMatch(i -> key.isIconItemStack(i))) {
			return contents.stream()
				.filter(i -> !key.isIconItemStack(i))
				.collect(Collectors.toList());
		}
		return new ArrayList<ItemStack>();

	}


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
				return Optional.of(event.getClickedBlock());
			}
		},
		;

		abstract Optional<Block> getTargetBlock(PlayerInteractEvent event);

	}


}

