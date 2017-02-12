package com.github.orgs.kotobaminers.kotobatblt3.utility;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.orgs.kotobaminers.kotobatblt3.ability.ClickBlockChestAbility;
import com.github.orgs.kotobaminers.kotobatblt3.block.InteractiveStructure;

public class RepeatingEffectHolderManager {


	private static List<RepeatingEffectHolder> getHolders() {
		return Arrays.asList(ClickBlockChestAbility.values(), InteractiveStructure.values()).stream()
			.flatMap(holders -> Stream.of(holders))
			.collect(Collectors.toList());
	}


	public static List<RepeatingEffectHolder> findHolders(TBLTItemStackIcon icon) {
		return getHolders().stream()
			.filter(h -> h.getIcon() == icon)
			.collect(Collectors.toList());
	}


}

