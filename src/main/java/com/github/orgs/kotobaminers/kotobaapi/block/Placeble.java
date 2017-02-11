package com.github.orgs.kotobaminers.kotobaapi.block;

import org.bukkit.block.Block;

public interface Placeble {
	boolean canPlace(Block block);
	boolean place(KotobaBlockData data);
}
