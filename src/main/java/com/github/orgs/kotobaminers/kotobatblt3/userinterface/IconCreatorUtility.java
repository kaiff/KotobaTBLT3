package com.github.orgs.kotobaminers.kotobatblt3.userinterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobatblt3.block.BlockReplacer;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArena;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public class IconCreatorUtility {
	public static List<ItemStack> getIcons(TBLTArena arena) {
		List<ItemStack> icons = new ArrayList<ItemStack>();

		ItemStack information = TBLTIcon.INFORMATION.createItemStack(Arrays.asList(arena.getName()));

		Location center = arena.getCenter();
		ItemStack teleport = TBLTIcon.TELEPORT.createItemStack(Arrays.asList(arena.getWorld().getName(), String.valueOf(center.getBlockX()) + "," + String.valueOf(center.getBlockY()) + "," + String.valueOf(center.getBlockZ())));

		ItemStack warp = TBLTItemStackIcon.PORTAL_CRYSTAL.create(1);
		ItemMeta warpMeta = warp.getItemMeta();
		warpMeta.setLore(Arrays.asList(arena.getName()));
		warp.setItemMeta(warpMeta);

		List<ItemStack> checkPoints =
			arena.getCheckPoints().stream()
				.map(loc -> TBLTIcon.CHECK_POINT.createItemStack(Arrays.asList(arena.getWorld().getName(), String.valueOf(loc.getBlockX()) + "," + String.valueOf(loc.getBlockY()) + "," + String.valueOf(loc.getBlockZ()))))
				.collect(Collectors.toList());

		icons.addAll(Arrays.asList(information, teleport, warp));
		icons.addAll(checkPoints);

		return icons;
	}

	public static List<ItemStack> getIcons(BlockReplacer replacer) {
		List<ItemStack> icons = new ArrayList<ItemStack>();

		ItemStack information = TBLTIcon.INFORMATION.createItemStack(Arrays.asList(replacer.getName()));

		Location center = replacer.getCenter();
		ItemStack teleport = TBLTIcon.TELEPORT.createItemStack(Arrays.asList(replacer.getWorld().getName(), String.valueOf(center.getBlockX()) + "," + String.valueOf(center.getBlockY()) + "," + String.valueOf(center.getBlockZ())));

		icons.addAll(Arrays.asList(information, teleport));

		if(replacer.getTrigger() != null) {
			icons.add(KotobaItemStack.create(replacer.getTrigger(), (short) 0, 1, "Trigger", null));
		}
		if(replacer.getTarget() != null) {
			icons.add(KotobaItemStack.create(replacer.getTarget(), (short) 0, 1, "Target", null));
		}
		return icons;
	}


}
