package com.github.orgs.kotobaminers.kotobatblt3.ability;

import com.github.orgs.kotobaminers.kotobaapi.ability.ItemStackAbilityInterface;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public enum TBLTItem implements ItemStackAbilityInterface {

	PORTAL_CRYSTAL(TBLTItemStackIcon.PORTAL_CRYSTAL),
	SINGLE_PORTAL(TBLTItemStackIcon.SINGLE_PORTAL),
	PREDICTION(TBLTItemStackIcon.WRITTEN_PREDICTION),
	;

	protected KotobaItemStackIcon icon;

	private TBLTItem(KotobaItemStackIcon icon) {
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

