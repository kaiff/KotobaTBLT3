package com.github.orgs.kotobaminers.kotobaapi.userinterface;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaAPIItemStack;

public interface GUIIcon {
	default KotobaAPIItemStack createItemStack() {
		return KotobaAPIItemStack.create(getMaterial(), getAmount(), getData(), getDisplayName(), getLore());
	}

	default boolean isIcon(ItemStack itemStack) {
		if(itemStack == null) return false;
		if(itemStack.getType().equals(getMaterial()) && itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(getDisplayName())) {
			return true;
		}
		return false;
	}

	void onClickEvent(InventoryClickEvent event);
	Material getMaterial();
	void setMaterial(Material material);

	int getAmount();
	void setAmount(int amount);

	short getData();
	void setData(short data);

	String getDisplayName();
	void setDisplayName(String displayName);

	List<String> getLore();
	void setLore(List<String> lore);

}
