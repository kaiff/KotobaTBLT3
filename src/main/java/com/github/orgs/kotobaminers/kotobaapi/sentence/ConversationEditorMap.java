package com.github.orgs.kotobaminers.kotobaapi.sentence;

import java.util.Map;
import java.util.UUID;

public abstract class ConversationEditorMap {


	protected abstract Map<Integer, ConversationEditor> getMap();
	protected abstract void createConversation(int npc, UUID author);
	protected abstract ConversationEditor registerConversationEditorOrDefault(int conversation, UUID author);
	protected abstract ConversationEditor registerConversationEditorOrDefault(int conversation, UUID author, int select);


	public void remove(int conversation) {
		if(getMap().containsKey(conversation)) {
			getMap().remove(conversation);
		}
	}


}

