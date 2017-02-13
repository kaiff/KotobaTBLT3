package com.github.orgs.kotobaminers.kotobaapi.game;

import org.bukkit.entity.Player;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;

public abstract class KotobaQuest {


	protected boolean achieved = false;


	protected abstract boolean checkAchieve(Player player);
	public abstract boolean isSame(KotobaQuest quest);
	public abstract String toString();


	public boolean tryFirstAchieve(Player player) {
		if(achieved) return false;
		if(checkAchieve(player)) {
			this.achieved = true;
			playAchievedEffect(player);
			return true;
		}
		return false;
	}

	protected void playAchievedEffect(Player player) {
		KotobaEffect.MAGIC_MIDIUM.playEffect(player.getLocation());
		KotobaEffect.MAGIC_MIDIUM.playSound(player.getLocation());
	}



}

