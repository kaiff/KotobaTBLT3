package com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.orgs.kotobaminers.kotobatblt3.ability.ClickAbilityListener;
import com.github.orgs.kotobaminers.kotobatblt3.ability.GeneratableBlockListener;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaListener;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.TBLTCommandExecutor.PlayerCommand;
import com.github.orgs.kotobaminers.kotobatblt3.userinterface.TBLTIconListGUIListener;
import com.github.orgs.kotobaminers.kotobatblt3.userinterface.TBLTPlayerGUIListener;


public class KotobaTBLT3 extends JavaPlugin {

	@Override
	public void onEnable() {

		this.getCommand(PlayerCommand.LABEL).setExecutor(new TBLTCommandExecutor(this));

		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new TBLTArenaListener(), this);
		pluginManager.registerEvents(new CitizensListener(), this);
		pluginManager.registerEvents(new ClickAbilityListener(), this);
		pluginManager.registerEvents(new GeneratableBlockListener(), this);
		pluginManager.registerEvents(new TBLTPlayerGUIListener(), this);
		pluginManager.registerEvents(new TBLTIconListGUIListener(), this);

		Setting.initialize(this);

	}

	@Override
	public void onDisable() {
		Setting.saveAll();
	}
}
