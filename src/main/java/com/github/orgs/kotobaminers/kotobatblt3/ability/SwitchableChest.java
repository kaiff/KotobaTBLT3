package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.stream.Stream;

import org.bukkit.block.Chest;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public interface SwitchableChest {
	void turnOn(Chest chest);
	void turnOff(Chest chest);
	KotobaItemStackIcon getIcon();

	default boolean hasReverse(Chest chest) {
		return Stream.of(chest.getInventory().getContents())
			.filter(i -> i != null)
			.anyMatch(i -> TBLTItemStackIcon.SWITCH_REVERSE.isIconItemStack(i));
	}

}

