package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockStorage;
import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockStorageMap;

public class TBLTArenaMap extends KotobaBlockStorageMap {


	private static Map<String, KotobaBlockStorage> arenas = new TreeMap<String, KotobaBlockStorage>();


	@Override
	public Map<String, KotobaBlockStorage> getMap() {
		return arenas;
	}

	@Override
	public List<KotobaBlockStorage> getStorages() {
		return getMap().values().stream().collect(Collectors.toList());
	}

	@Override
	public void importAll() {
		arenas = new TreeMap<String, KotobaBlockStorage>();//Initialize
		(new TBLTArena("dummy")).importAll().stream()
			.forEach(storage -> put(storage));
		return;
	}


}

