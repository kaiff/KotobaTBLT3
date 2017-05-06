package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

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


	public Optional<TBLTArena> findPlayingMap(Player player) {
		if(player.getGameMode() == GameMode.ADVENTURE) {
			return findUnique(player.getLocation()).map(a -> (TBLTArena) a);
		}
		return Optional.empty();
	}


}

