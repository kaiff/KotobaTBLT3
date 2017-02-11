package com.github.orgs.kotobaminers.kotobatblt3.ability;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockData;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public enum Gems {


	GREEN_GEM(TBLTItemStackIcon.GREEN_GEM) {},
	BLUE_GEM(TBLTItemStackIcon.BLUE_GEM) {},
	RED_GEM(TBLTItemStackIcon.RED_GEM) {},
	;


	private KotobaItemStackIcon icon;


	private Gems(KotobaItemStackIcon icon) {
		this.icon = icon;
	}


	public KotobaItemStackIcon getIcon() {
		return icon;
	}


	public boolean place(Location location) {
		if(location.getBlock().getType() == Material.AIR) {
			new KotobaBlockData(location, icon.getMaterial(), icon.getData()).placeBlock();
			KotobaEffect.MAGIC_MIDIUM.playEffect(location);
			KotobaEffect.MAGIC_MIDIUM.playSound(location);
			return true;
		}
		return false;
	}


	public ItemStack create(int amount) {
		return icon.create(amount);
	}


}

