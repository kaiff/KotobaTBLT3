package com.github.orgs.kotobaminers.kotobatblt3.quest;

import org.bukkit.entity.Player;

import com.github.orgs.kotobaminers.kotobaapi.ability.ItemStackAbilityInterface;
import com.github.orgs.kotobaminers.kotobaapi.quest.KotobaQuest;
import com.github.orgs.kotobaminers.kotobatblt3.game.TBLTData;

public class AbilityUseQuest extends KotobaQuest {


	private ItemStackAbilityInterface ability;
	private int goal = 0;


	public static AbilityUseQuest create(ItemStackAbilityInterface ability, int goal) {
		AbilityUseQuest quest = new AbilityUseQuest();
		quest.ability = ability;
		quest.goal = goal;
		return quest;
	}


	@Override
	protected boolean checkAchieve(Player player) {
		TBLTData data = TBLTData.getOrDefault(player.getUniqueId());
		return goal <= data.getAbilityUsed(ability) ;
	}


	@Override
	public boolean isSame(KotobaQuest quest) {
		if(quest instanceof AbilityUseQuest) {
			AbilityUseQuest q = (AbilityUseQuest) quest;
			if(q.ability == this.ability) {
				return true;
			}
		}
		return false;
	}


	@Override
	public String toString() {
		return "Class: " + getClass().getName() + ", Achieved: " + achieved + ", Ability: " + ability.getIcon().getName() + ", Goal: " + goal;
	}


}

