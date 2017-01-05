package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.github.orgs.kotobaminers.kotobaapi.worldeditor.BlockStorage;
import com.github.orgs.kotobaminers.kotobaapi.worldeditor.BlockStorageMap;

public class TBLTArenaMap extends BlockStorageMap {


	private static Map<String, BlockStorage> arenas = new TreeMap<String, BlockStorage>();


	@Override
	public Map<String, BlockStorage> getMap() {
		return arenas;
	}

	@Override
	public List<BlockStorage> getStorages() {
		return getMap().values().stream().collect(Collectors.toList());
	}

	@Override
	public void importAll() {
		arenas = new TreeMap<String, BlockStorage>();//Initialize
		(new TBLTArena("dummy")).importAll().stream()
			.forEach(storage -> put(storage));
		return;
	}


}

