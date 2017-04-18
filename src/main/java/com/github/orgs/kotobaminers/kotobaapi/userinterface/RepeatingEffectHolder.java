package com.github.orgs.kotobaminers.kotobaapi.userinterface;

import java.util.List;

import org.bukkit.Location;

import com.github.orgs.kotobaminers.kotobaapi.ability.ItemStackIconHolder;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;

public interface RepeatingEffectHolder extends ItemStackIconHolder {


	int getPeriod();
	KotobaEffect getEffect();
	KotobaEffect getSound();


	List<RepeatingEffect> createPeriodicEffects(Location origin);


}

