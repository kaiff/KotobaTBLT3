package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockStorage;
import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockStorageMap;

public class BlockReplacerMap  extends KotobaBlockStorageMap {


	private static Map<String, KotobaBlockStorage> replacers = new TreeMap<String, KotobaBlockStorage>();


	public List<BlockReplacer> getReplacers(KotobaBlockStorage storage) {
		return replacers.values().stream()
			.filter(s -> storage.isIn(s.getCenter()))
			.map(s -> (BlockReplacer) s)
			.collect(Collectors.toList());
	}


	@Override
	public Map<String, KotobaBlockStorage> getMap() {
		return replacers;
	}

	@Override
	public List<KotobaBlockStorage> getStorages() {
		return getMap().values().stream().collect(Collectors.toList());
	}

	@Override
	public void importAll() {
		replacers = new TreeMap<String, KotobaBlockStorage>();//Initialize
		(new BlockReplacer("dummy")).importAll().stream()
			.forEach(storage -> put(storage));
		return;
	}

}

