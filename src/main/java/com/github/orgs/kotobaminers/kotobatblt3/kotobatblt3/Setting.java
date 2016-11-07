package com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3;

import com.github.orgs.kotobaminers.kotobaapi.sentence.HologramsManager;
import com.github.orgs.kotobaminers.kotobatblt3.database.PlayerDatabase;
import com.github.orgs.kotobaminers.kotobatblt3.database.SentenceDatabase;
import com.github.orgs.kotobaminers.kotobatblt3.game.BlockReplacer;
import com.github.orgs.kotobaminers.kotobatblt3.game.TBLTArena;

public class Setting {
	private static KotobaTBLT3 plugin;

	public static void initialize(KotobaTBLT3 plugin) {
		Setting.plugin = plugin;

		TBLTArena.importAll();
		BlockReplacer.importAll();

		new PlayerDatabase().loadConfig();
		new SentenceDatabase().loadConfig();

		HologramsManager.removeAllHologram(plugin);

	}

	public static KotobaTBLT3 getPlugin() {
		return plugin;
	}

	public static void saveAll() {
		TBLTArena.saveAll();
	}
}
