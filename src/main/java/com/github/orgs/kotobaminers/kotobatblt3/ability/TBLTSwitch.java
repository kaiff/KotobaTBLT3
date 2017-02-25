package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;

public class TBLTSwitch {


	private static final int RANGE = 16;


	@SuppressWarnings("deprecation")
	private static List<Block> findPowered(Chest origin, Gems gem) {
		List<Block> powered = new ArrayList<>();
		List<Block> heads = new ArrayList<>();
		heads.add(origin.getBlock());

		List<Block> targets = KotobaUtility.getBlocks(origin.getLocation(), RANGE).stream()
				.filter(b -> b.getType() == gem.getSwitchMaterial() && b.getData() == gem.getSwitchData())
				.collect(Collectors.toList());

		Stream.iterate(0, i -> i)
			.limit(RANGE)
			.forEach(i -> {
				List<Block> newHeads = targets.stream()
					.filter(t -> heads.stream().anyMatch(h -> h.getLocation().distance(t.getLocation()) == 1))
					.filter(t -> powered.stream().noneMatch(p -> p.getLocation().distance(t.getLocation()) == 0))
					.collect(Collectors.toList());
				heads.clear();
				heads.addAll(newHeads);
				powered.addAll(heads);
			});
		return powered;
	}

	public static List<Chest> findPoweredChests(Chest origin, Gems gem) {
		return findPowered(origin, gem).stream()
			.map(b -> b.getRelative(BlockFace.DOWN))
			.filter(b -> b.getState() instanceof Chest)
			.map(b -> (Chest) b.getState())
			.collect(Collectors.toList());
	}

}
