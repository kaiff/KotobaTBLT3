package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.github.orgs.kotobaminers.kotobaapi.worldeditor.BlockStorage;
import com.github.orgs.kotobaminers.kotobaapi.worldeditor.BlockStorageMap;

public class BlockReplacerMap  extends BlockStorageMap {


	private static Map<String, BlockStorage> replacers = new TreeMap<String, BlockStorage>();


	@Override
	public Map<String, BlockStorage> getMap() {
		return replacers;
	}

	@Override
	public List<BlockStorage> getStorages() {
		return getMap().values().stream().collect(Collectors.toList());
	}

	@Override
	public void importAll() {
		replacers = new TreeMap<String, BlockStorage>();//Initialize
		(new BlockReplacer("dummy")).importAll().stream()
			.forEach(storage -> put(storage));
		return;
	}

}

