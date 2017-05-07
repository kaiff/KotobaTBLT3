package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.event.player.PlayerInteractEvent;

import com.github.orgs.kotobaminers.kotobaapi.block.PlayerBlockInteractive;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaPriority;
import com.github.orgs.kotobaminers.kotobatblt3.block.InteractiveBlock;
import com.github.orgs.kotobaminers.kotobatblt3.block.InteractiveStructure;

public class PlayerInteractiveManager {


	public static List<PlayerBlockInteractive> getBlockInteractive() {
		return Stream.of(
			Stream.of(ClickBlockAbility.values()),
			Stream.of(ClickBlockChestAbility.values()),
			Stream.of(InteractiveStructure.values()),
			Stream.of(InteractiveBlock.values())
		)
			.flatMap(a -> a)
			.collect(Collectors.toList());
	}


	public static List<PlayerBlockInteractive> find(PlayerInteractEvent event) {
		List<KotobaPriority> interactives = getBlockInteractive().stream()
			.filter(a -> a.isSame(event))
			.filter(a -> a instanceof KotobaPriority)
			.map(a -> (KotobaPriority) a)
			.collect(Collectors.toList());
		if(0 < interactives.size()) {
			return interactives.get(0).findHighetPriorities(interactives).stream()
				.filter(a -> a instanceof PlayerBlockInteractive)
				.map(a -> (PlayerBlockInteractive) a)
				.collect(Collectors.toList());
		}
		return new ArrayList<>();
	}


}

