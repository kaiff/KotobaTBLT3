package com.github.orgs.kotobaminers.kotobatblt3.userinterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArena;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public class IconCreatorUtility {
	public static List<ItemStack> createArenaInfo(TBLTArena arena) {
		List<ItemStack> icons = new ArrayList<ItemStack>();

		ItemStack info = TBLTGUIIcon.INFORMATION.createItemStack(Arrays.asList(arena.getName(), String.valueOf(arena.getId())));

		Location center = arena.getCenter();
		ItemStack teleport = TBLTGUIIcon.TELEPORT.createItemStack(Arrays.asList(arena.getWorld().getName(), String.valueOf(center.getBlockX()) + "," + String.valueOf(center.getBlockY()) + "," + String.valueOf(center.getBlockZ())));

		ItemStack id = TBLTItemStackIcon.ARENA_ID.create(1);
		ItemMeta idMeta = id.getItemMeta();
		idMeta.setLore(Arrays.asList(String.valueOf(arena.getId())));
		id.setItemMeta(idMeta);

		ItemStack next = TBLTItemStackIcon.ARENA_NEXT.create(1);
		ItemMeta nextMeta = next.getItemMeta();
		nextMeta.setLore(Arrays.asList(String.valueOf(arena.getArenaMeta().getNext())));
		next.setItemMeta(nextMeta);

		icons.addAll(Arrays.asList(info, teleport, id, next));

		return icons;
	}


	public static List<ItemStack> createArenaMenu(TBLTArena arena) {
		ItemStack hint = TBLTGUIIcon.SEE_HINTS.createItemStack();
		ItemStack restart = TBLTGUIIcon.RESTART.createItemStack();
		return Arrays.asList(hint, restart);
	}
}

