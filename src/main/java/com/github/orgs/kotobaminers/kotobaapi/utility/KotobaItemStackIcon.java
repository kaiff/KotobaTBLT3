package com.github.orgs.kotobaminers.kotobaapi.utility;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public interface KotobaItemStackIcon {


	static final List<ItemFlag> FLAGS = Arrays.asList(ItemFlag.HIDE_ATTRIBUTES);
	static final int LORE_LENGTH = 25;


	Material getMaterial();
	short getData();
	String getName();
	List<String> getLore();


	default boolean isSame(ItemStack itemStack) {
		return getFindType().isSame(create(1), itemStack);
	}


	default ItemStack create(int amount) {
		ItemStack item = new ItemStack(getMaterial(), amount, getData());
		if(getLore() != null) {
			List<String> splited = getLore().stream().flatMap(line -> KotobaUtility.splitSentence(line, LORE_LENGTH).stream()).collect(Collectors.toList());
			item = KotobaItemStack.setColoredLore(item, ChatColor.RESET, splited);
		}

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getName());

		FLAGS.stream().forEach(flag -> meta.addItemFlags(flag));

		item.setItemMeta(meta);
		return item;
	}

	IconFindType getFindType();
	enum IconFindType {
		SIMILAR {
			@Override
			boolean isSame(ItemStack itemStack1, ItemStack itemStack2) {
				return itemStack1.isSimilar(itemStack2);
			}
		},
		EXCEPT_LORE {
			@Override
			boolean isSame(ItemStack itemStack1, ItemStack itemStack2) {
				ItemMeta itemMeta1 = itemStack1.getItemMeta();
				ItemMeta itemMeta2 = itemStack2.getItemMeta();

				boolean type = itemStack1.getType() == itemStack2.getType();
				boolean data= itemStack1.getDurability() == itemStack2.getDurability();
				boolean name = itemMeta1.getDisplayName() == itemMeta2.getDisplayName();

				return type && data && name;
			}
		}
		;

		abstract boolean isSame(ItemStack itemStack1, ItemStack itemStack2);


	}


}

