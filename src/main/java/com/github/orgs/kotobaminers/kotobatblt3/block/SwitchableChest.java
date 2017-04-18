package com.github.orgs.kotobaminers.kotobatblt3.block;

import org.bukkit.block.Chest;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;

public interface SwitchableChest {
	void turnOn(Chest chest);
	void turnOff(Chest chest);
	KotobaItemStackIcon getIcon();
}
