package com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3;

import com.github.orgs.kotobaminers.kotobaapi.sentence.Holograms;
import com.github.orgs.kotobaminers.kotobatblt3.block.BlockReplacerMap;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.database.PlayerDatabase;
import com.github.orgs.kotobaminers.kotobatblt3.database.SentenceDatabase;

public class Setting {
	private static KotobaTBLT3 plugin;

	public static void initialize(KotobaTBLT3 plugin) {
		Setting.plugin = plugin;

		new TBLTArenaMap().importAll();
		new BlockReplacerMap().importAll();

		new PlayerDatabase().loadConfig();
		new SentenceDatabase().loadConfig();

		Holograms.removeAllHologram(plugin);

	}

	public static KotobaTBLT3 getPlugin() {
		return plugin;
	}

	public static void saveAll() {
	}
}
