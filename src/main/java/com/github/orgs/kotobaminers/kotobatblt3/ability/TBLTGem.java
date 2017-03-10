package com.github.orgs.kotobaminers.kotobatblt3.ability;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockData;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public enum TBLTGem {


	GREEN_GEM(TBLTItemStackIcon.GREEN_GEM, Material.WOOL, (short) 5),
	BLUE_GEM(TBLTItemStackIcon.BLUE_GEM, Material.WOOL, (short) 11),
	RED_GEM(TBLTItemStackIcon.RED_GEM, Material.WOOL, (short) 14),
	;


	private KotobaItemStackIcon icon;
	private Material wireMaterial;
	private short wireData;


	private TBLTGem(KotobaItemStackIcon icon, Material switchMaterial, short wireData) {
		this.icon = icon;
		this.wireMaterial = switchMaterial;
		this.wireData = wireData;
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


	@SuppressWarnings("deprecation")
	public boolean isWire(Block block) {
		if(block.getData() == wireData && block.getType() == wireMaterial) {
			return true;
		}
		return false;
	}


	public Material getSwitchMaterial() {
		return wireMaterial;
	}


	public short getSwitchData() {
		return wireData;
	};


	public KotobaItemStackIcon getIcon() {
		return icon;
	}


}

