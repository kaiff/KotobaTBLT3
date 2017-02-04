package com.github.orgs.kotobaminers.kotobaapi.ability;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobatblt3.ability.ClickBlockAbility;
import com.github.orgs.kotobaminers.kotobatblt3.ability.ClickBlockChestAbility;
import com.github.orgs.kotobaminers.kotobatblt3.ability.ClickEntityAbility;
import com.github.orgs.kotobaminers.kotobatblt3.ability.ProjectileAbility;

public class ItemStackAbilityManager {


	private static List<ItemStackAbilityInterface> getAbilities() {
		return Arrays.asList(
			Stream.of(ClickBlockAbility.values()),
			Stream.of(ClickEntityAbility.values()),
			Stream.of(ClickBlockChestAbility.values()),
			Stream.of(ProjectileAbility.values())
		).stream()
			.flatMap(a -> a)
			.collect(Collectors.toList());
	}


	public static List<ItemStackAbilityInterface> find(ItemStack itemStack) {
		return getAbilities().stream()
			.filter(a -> a.getIcon().isSame(itemStack))
			.collect(Collectors.toList());
	}



}
