package com.github.orgs.kotobaminers.kotobaapi.block;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;

public enum InteractType {
	RELATIVE {
		@Override
		public Optional<Block> getTargetBlock(PlayerInteractEvent event) {
			Block block = event.getClickedBlock().getRelative(event.getBlockFace());
			if(block.getType() == Material.AIR) {
				return Optional.of(block);
			}
			return Optional.empty();
		}
	},
	CLICKED {
		@Override
		public Optional<Block> getTargetBlock(PlayerInteractEvent event) {
			return Optional.ofNullable(event.getClickedBlock());
		}
	},
	;

	public abstract Optional<Block> getTargetBlock(PlayerInteractEvent event);

}
