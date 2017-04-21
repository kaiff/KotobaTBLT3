package com.github.orgs.kotobaminers.kotobatblt3.utility;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobatblt3.userinterface.TBLTPlayerGUI;


public enum ChestChecker {


	HAS_ITEM_AT_LEAST(TBLTItemStackIcon.TRIGGER_ITEM_AT_LEAST) {
		@Override
		boolean checkItems(List<ItemStack> items, Player player) {
			List<ItemStack> contents = Stream.of(player.getInventory().getContents())
				.filter(i -> i != null)
				.collect(Collectors.toList());
			boolean allMatch = items.stream()
				.allMatch(item -> contents.stream().anyMatch(c -> c.isSimilar(item)));
			if(!allMatch) {
				TBLTPlayerGUI.YOU_NEED.create(items.stream().map(i -> {
						ItemStack i2 = i.clone();
						i2.setAmount(1);
						return i2;
					})
					.collect(Collectors.toList())
				).ifPresent(i -> player.openInventory(i));
			}
			return allMatch;
		}
	},


	HAS_ITEM_SAME_AMOUNT(TBLTItemStackIcon.TRIGGER_ITEM_SAME_AMOUNT) {
		@Override
		boolean checkItems(List<ItemStack> items, Player player) {
			List<ItemStack> contents = Stream.of(player.getInventory().getContents())
				.filter(i -> i != null)
				.collect(Collectors.toList());

			return items.stream()
				.map(item -> {
						int amount = contents.stream()
							.filter(c -> c.isSimilar(item))
							.mapToInt(c -> c.getAmount())
							.sum();
						return amount == item.getAmount();
					}
				).allMatch(b -> b == true);
		}
	}
	,
	;


	private TBLTItemStackIcon icon;


	private ChestChecker(TBLTItemStackIcon icon) {
		this.icon = icon;
	}


	abstract boolean checkItems(List<ItemStack> item, Player player);


	public static boolean checkTriggers(Chest chest, Player player) {
		return Stream.of(ChestChecker.values())
			.map(checker -> {
				List<ItemStack> items = checker.findTriggersItem(chest);
				if(0 < items.size()) {
					return checker.checkItems(items, player);
				}
				return true;
			})
			.allMatch(b -> b == true);
	}


	private List<ItemStack> findTriggersItem(Chest chest) {
		return Stream.iterate(0, i -> i + 1)
			.limit(3)
			.map(i -> ChestReader.findItemsInRow(chest, i))
			.filter(list ->
				list.stream()
					.anyMatch(i -> icon.isIconItemStack(i))
			)
			.flatMap(list -> list.stream())
			.filter(i -> Stream.of(ChestChecker.values()).map(checker -> checker.icon).none	Match(icon -> icon.isIconItemStack(i)))
			.collect(Collectors.toList());
	}


}
