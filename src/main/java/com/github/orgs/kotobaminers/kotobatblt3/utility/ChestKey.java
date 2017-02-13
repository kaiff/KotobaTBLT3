package com.github.orgs.kotobaminers.kotobatblt3.utility;

import com.github.orgs.kotobaminers.kotobaapi.ability.ItemStackAbilityInterface;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;

public enum ChestKey implements ItemStackAbilityInterface {

	PORTAL_CRYSTAL(TBLTItemStackIcon.PORTAL_CRYSTAL),
	SINGLE_PORTAL(TBLTItemStackIcon.SINGLE_PORTAL),
	GEM_PORTAL_KEY_3X3(TBLTItemStackIcon.GEM_PORTAL_KEY_3x3),
	PREDICTION(TBLTItemStackIcon.WRITTEN_PREDICTION),//TODO: Not ChestKey
	;

	protected KotobaItemStackIcon icon;

	private ChestKey(KotobaItemStackIcon icon) {
		this.icon = icon;
	}


	@Override
	public KotobaItemStackIcon getIcon() {
		return icon;
	}

	@Override
	public int getConsumption() {
		return 0;
	}


}

