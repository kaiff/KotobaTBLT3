package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface ItemAbilityInterface {
	boolean canPerform(Player player, Action action);
	void perform(PlayerInteractEvent event);

	short getData();
	Material getMaterial();
	String getDisplayName();
	List<String> getLore();
	List<Action> getTriggers();

	default ItemStack createItem(int amount) {
		ItemStack item = new ItemStack(getMaterial(), amount, getData());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getDisplayName());
		meta.setLore(getLore());
		item.setItemMeta(meta);
		return item;
	}
}
