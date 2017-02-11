package com.github.orgs.kotobaminers.kotobaapi.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface Takable {
	boolean canTake(Block block, Player player);
	boolean take(Block block, Player player);
}
