package com.github.orgs.kotobaminers.kotobaapi.block;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;

public abstract class KotobaPortalManager {


	protected abstract List<KotobaPortalInterface> getAllPortals();


	public List<KotobaPortalInterface> find(Location location) {
		return getAllPortals().stream().filter(p -> p.isThis(location)).collect(Collectors.toList());
	}


}

