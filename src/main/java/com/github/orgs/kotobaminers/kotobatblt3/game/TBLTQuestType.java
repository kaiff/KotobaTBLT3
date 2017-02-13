package com.github.orgs.kotobaminers.kotobatblt3.game;

import com.github.orgs.kotobaminers.kotobaapi.game.KotobaQuest;
import com.github.orgs.kotobaminers.kotobaapi.game.KotobaQuestType;

public enum TBLTQuestType implements KotobaQuestType {
	ABILITY_USE() {
		@Override
		boolean checkAchieved(KotobaQuest quest) {
			return false;
		}
	},
	ITEM_CONTAINS() {
		@Override
		boolean checkAchieved(KotobaQuest quest) {
			return false;
		}
	},
	;


	private TBLTQuestType() {
	}


	abstract boolean checkAchieved(KotobaQuest quest);





}

