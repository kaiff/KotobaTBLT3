package com.github.orgs.kotobaminers.kotobatblt3.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;

public class TBLTData {

	private static Map<UUID, TBLTData> data = new HashMap<UUID, TBLTData>();
	private Block target;

	private TBLTData() {
	}
	public TBLTData target(Block target) {
		this.target = target;
		return this;
	}

	public static TBLTData getOrDefault(UUID uuid) {
		data.putIfAbsent(uuid, new TBLTData());
		return data.get(uuid);
	}

	public Block getTarget() {
		return target;
	}

	public Optional<Location> findTarget(Player player) {
		return new TBLTArenaMap().findUnique(player.getLocation())
			.filter(arena -> arena.isIn(target.getLocation()))
			.map(arena -> target.getLocation());
	}

}
