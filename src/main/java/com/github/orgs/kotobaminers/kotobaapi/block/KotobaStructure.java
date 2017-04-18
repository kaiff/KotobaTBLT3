package com.github.orgs.kotobaminers.kotobaapi.block;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


public interface KotobaStructure {


	Map<Vector, Material> getStructure();
	boolean hasRotations();
	default void generate(Player player) {
		getStructure()
			.forEach((v, m) -> new KotobaBlockData(player.getLocation().clone().add(v), m, 0).placeBlock());
	}
	String getName();


}

