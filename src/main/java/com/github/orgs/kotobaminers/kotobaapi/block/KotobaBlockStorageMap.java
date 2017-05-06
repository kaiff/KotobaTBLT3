package com.github.orgs.kotobaminers.kotobaapi.block;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Location;

public abstract class KotobaBlockStorageMap {


	protected abstract Map<String, KotobaBlockStorage> getMap();
	public abstract List<KotobaBlockStorage> getStorages();


	public void put(KotobaBlockStorage storage) {
		getMap().put(storage.getName(), storage);
	}

	public void remove(KotobaBlockStorage storage) {
		getMap().remove(storage.getName());
	}


	public Optional<KotobaBlockStorage> find(int id) {
		return getMap().values().stream().filter(s -> s.getId() == id).findFirst();
	}


	public Optional<KotobaBlockStorage> findUnique(String name) {
		return Optional.ofNullable(getMap().get(name));
	}

	public Optional<KotobaBlockStorage> findUnique(Location location) {
		List<KotobaBlockStorage> storages = getMap().values().stream()
			.filter(storage -> storage.isIn(location))
			.collect(Collectors.toList());
		if(storages.size() == 1) {
			return Optional.of(storages.get(0));
		}
		return Optional.empty();
	}

	public List<KotobaBlockStorage> find(Location location) {
		return getMap().values().stream()
			.filter(storage -> storage.isIn(location))
			.collect(Collectors.toList());
	}


	public boolean isInAny(Location location) {
		return getMap().values()
			.stream()
			.anyMatch(arena -> arena.isIn(location));
	}

	public void saveAll() {
		getMap().values().stream().forEach(KotobaBlockStorage::save);
	}

	public abstract void importAll();


}

