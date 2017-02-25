package com.github.orgs.kotobaminers.kotobatblt3.ability;

import org.bukkit.Location;
import org.bukkit.Material;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockData;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public enum Gems {


	GREEN_GEM(TBLTItemStackIcon.GREEN_GEM, Material.WOOL, (short) 5),
	BLUE_GEM(TBLTItemStackIcon.BLUE_GEM, Material.WOOL, (short) 11),
	RED_GEM(TBLTItemStackIcon.RED_GEM, Material.WOOL, (short) 14),
	;


	private KotobaItemStackIcon icon;
	private Material switchMaterial;
	private short switchData;


	private Gems(KotobaItemStackIcon icon, Material switchMaterial, short switchData) {
		this.icon = icon;
		this.switchMaterial = switchMaterial;
		this.switchData = switchData;
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


	public Material getSwitchMaterial() {
		return switchMaterial;
	}


	public short getSwitchData() {
		return switchData;
	};


	public KotobaItemStackIcon getIcon() {
		return icon;
	}


}

