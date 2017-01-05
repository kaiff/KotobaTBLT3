package com.github.orgs.kotobaminers.kotobaapi.worldeditor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Location;

public abstract class BlockStorageMap {


	protected abstract Map<String, BlockStorage> getMap();
	public abstract List<BlockStorage> getStorages();


	public void put(BlockStorage storage) {
		getMap().put(storage.getName(), storage);
	}

	public void remove(BlockStorage storage) {
		getMap().remove(storage.getName());
	}

	public Optional<BlockStorage> findUnique(String name) {
		return Optional.ofNullable(getMap().get(name));
	}

	public Optional<BlockStorage> findUnique(Location location) {
		List<BlockStorage> storages = getMap().values().stream()
			.filter(storage -> storage.isIn(location))
			.collect(Collectors.toList());
		if(storages.size() == 1) {
			return Optional.of(storages.get(0));
		}
		return Optional.empty();
	}

	public boolean isInAny(Location location) {
		return getMap().values()
			.stream()
			.anyMatch(arena -> arena.isIn(location));
	}

	public void saveAll() {
		getMap().values().stream().forEach(BlockStorage::save);
	}

	public abstract void importAll();


}

