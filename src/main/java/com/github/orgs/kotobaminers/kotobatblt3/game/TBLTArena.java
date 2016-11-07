package com.github.orgs.kotobaminers.kotobatblt3.game;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.github.orgs.kotobaminers.kotobaapi.worldeditor.BlockStorage;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;

public class TBLTArena extends BlockStorage {

	private static final File DIRECTORY = new File(Setting.getPlugin().getDataFolder().getAbsolutePath() + "/Arena/");
	private static Set<TBLTArena> arenas = new HashSet<>();

	private TBLTArena(String name) {
		setName(name);
	}

	public static TBLTArena create(String name) {
		TBLTArena arena = new TBLTArena(name);
		return arena;
	}

	@Override
	public File getDirectory() {
		return DIRECTORY;
	}

	public static void importAll() {
		arenas = new HashSet<>();
		Stream.of(DIRECTORY.listFiles())
			.map(f -> f.getName().substring(0, f.getName().length()-".yml".length()))
			.forEach(name -> {
				TBLTArena arena = TBLTArena.create(name);
				arena.setDataFromConfig(Bukkit.getWorlds());
				addArena(arena);
			});
	}

	public static void addArena(TBLTArena arena) {
		arenas.removeIf(a -> a.getName().equalsIgnoreCase(arena.getName()));
		arenas.add(arena);
	}

	public static boolean isInArena(Location location) {
		return arenas
			.stream()
			.anyMatch(arena -> arena.isLocationIn(location));
	}

	public static void saveAll() {
		arenas.stream().forEach(BlockStorage::save);
	}

	public static Set<TBLTArena> getArenas() {
		return arenas;
	}

}
