package com.github.orgs.kotobaminers.kotobatblt3.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.github.orgs.kotobaminers.kotobaapi.ability.ItemStackAbilityInterface;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;

public class TBLTData {


	private static Map<UUID, TBLTData> data = new HashMap<UUID, TBLTData>();


	private UUID uuid;
	private Block target;
	private Map<ItemStackAbilityInterface, Integer> abilityUsed = new HashMap<>();


	private TBLTData(UUID uuid) {
		this.uuid = uuid;
	}


	public TBLTData target(Block target) {
		this.target = target;
		return this;
	}


	public void initialize() {
		data.put(uuid, new TBLTData(uuid));
	}

	public static TBLTData getOrDefault(UUID uuid) {
		data.putIfAbsent(uuid, new TBLTData(uuid));
		return data.get(uuid);
	}


	public int getAbilityUsed(ItemStackAbilityInterface ability) {
		abilityUsed.putIfAbsent(ability, 0);
		return abilityUsed.get(ability);
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

