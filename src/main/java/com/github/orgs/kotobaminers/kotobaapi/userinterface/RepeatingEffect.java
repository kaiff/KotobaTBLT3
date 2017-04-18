package com.github.orgs.kotobaminers.kotobaapi.userinterface;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;


public class RepeatingEffect {


	private int period;
	private KotobaEffect effect;
	private KotobaEffect sound;
	private boolean repeat;
	private Location blockLocation;


	private RepeatingEffect() {
	}


	public static RepeatingEffect create(int period, KotobaEffect effect, KotobaEffect sound, boolean repeat, Location blockLocation) {
		RepeatingEffect periodicEffect = new RepeatingEffect();
		periodicEffect.period = period;
		periodicEffect.effect = effect;
		periodicEffect.sound = sound;
		periodicEffect.repeat = repeat;
		periodicEffect.blockLocation = blockLocation.clone();
		return periodicEffect;
	}


	public void startRepeating() {
		if(repeat == false) return;
		Location center = blockLocation.clone().add(0.5,0.5,0.5);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Setting.getPlugin(), new Runnable() {
			@Override
			public void run() {
				if(repeat == false) return;
				effect.playEffect(center);
				sound.playSound(center);
				startRepeating();
			}
		}, period * 20);
	}


	public RepeatingEffect setRepeat(boolean repeat) {
		this.repeat = repeat;
		return this;
	}

	public Location getBlockLocation() {
		return blockLocation;
	}


}

