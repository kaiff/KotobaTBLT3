package com.github.orgs.kotobaminers.kotobatblt3.quest;

import com.github.orgs.kotobaminers.kotobaapi.quest.KotobaQuest;
import com.github.orgs.kotobaminers.kotobaapi.quest.KotobaQuestType;

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

