package com.github.orgs.kotobaminers.kotobaapi.utility;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Sound;

public enum KotobaSound {
	GOOD(Sound.LEVEL_UP, 1, 1),
	FAILED(Sound.ITEM_BREAK, 1, 1),
	ATTENTION(Sound.NOTE_PIANO, 1, 1),
	POP_UP(Sound.CHICKEN_EGG_POP, 1, 1),
	FORGE(Sound.ANVIL_USE, 1, 1),
	CLICK(Sound.CLICK, 1, 1),
	SHEAR(Sound.SHEEP_SHEAR, 1, 1),
	WARP(Sound.ENDERMAN_TELEPORT, 1, 1),
	FIZZ(Sound.FIZZ, 1, 1),
	;

	private float volume;
	private float pitch;
	private Sound sound;

	private KotobaSound(Sound sound, float volume, float pitch) {
		this.sound = sound;
		this.volume = volume;
		this.pitch = pitch;
	}

	public void play(Location location) {
		location.getWorld().playSound(location, sound, volume, pitch);
	}

	public static void playRandomPitch(Location location, Sound sound, float volume, float min, float max, int number) {
		Random random = new Random();
		float pitch = min + (max - min) / number * random.nextInt(number + 1);
		location.getWorld().playSound(location, sound, volume, pitch);
	}
}
