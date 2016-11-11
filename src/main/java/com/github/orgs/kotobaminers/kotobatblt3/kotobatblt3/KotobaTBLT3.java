package com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.orgs.kotobaminers.kotobatblt3.ability.ClickAbilityListener;
import com.github.orgs.kotobaminers.kotobatblt3.game.TBLTArenaListener;
import com.github.orgs.kotobaminers.kotobatblt3.gui.TBLTGUIListener;
import com.github.orgs.kotobaminers.kotobatblt3.gui.TBLTIconListGUIListener;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.TBLTCommandExecutor.CommandEnum;


public class KotobaTBLT3 extends JavaPlugin {

	@Override
	public void onEnable() {

		this.getCommand(CommandEnum.label).setExecutor(new TBLTCommandExecutor(this));

		getServer().getPluginManager().registerEvents(new ClickAbilityListener(), this);
		getServer().getPluginManager().registerEvents(new TBLTGUIListener(), this);
		getServer().getPluginManager().registerEvents(new TBLTArenaListener(), this);
		getServer().getPluginManager().registerEvents(new BlockReplacerListener(), this);
		getServer().getPluginManager().registerEvents(new TBLTIconListGUIListener(), this);

		Setting.initialize(this);

	}

	@Override
	public void onDisable() {
		Setting.saveAll();
	}
}
