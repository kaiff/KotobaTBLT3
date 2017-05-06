package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.event.player.PlayerInteractEvent;

import com.github.orgs.kotobaminers.kotobaapi.block.PlayerBlockInteractive;
import com.github.orgs.kotobaminers.kotobatblt3.block.InteractiveStructure;

public class PlayerInteractiveManager {


	public static List<PlayerBlockInteractive> getBlockInteractive() {
		return Stream.of(
			Stream.of(ClickBlockAbility.values()),
			Stream.of(ClickBlockChestAbility.values()),
			Stream.of(InteractiveStructure.values())
		)
			.flatMap(a -> a)
			.collect(Collectors.toList());
	}


	public static List<PlayerBlockInteractive> find(PlayerInteractEvent event) {
		return getBlockInteractive().stream()
			.filter(a -> a.isSame(event))
			.collect(Collectors.toList());
	}


}

