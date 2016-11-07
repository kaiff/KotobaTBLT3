package com.github.orgs.kotobaminers.kotobaapi.kotobaapi;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.orgs.kotobaminers.kotobaapi.kotobaapi.KotobaAPICommandExecutor.CommandEnum;

public class KotobaAPI extends JavaPlugin {

	@Override
	public void onEnable() {
		this.getCommand(CommandEnum.label).setExecutor(new KotobaAPICommandExecutor(this));

	}

}
