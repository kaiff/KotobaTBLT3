package com.github.orgs.kotobaminers.kotobatblt3.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.github.orgs.kotobaminers.kotobaapi.sentence.ConversationEditor;
import com.github.orgs.kotobaminers.kotobaapi.sentence.ConversationEditorMap;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence;

public class TBLTConversationEditorMap extends ConversationEditorMap {


	private static Map<Integer, ConversationEditor> editors = new TreeMap<>();


	@Override
	protected Map<Integer, ConversationEditor> getMap() {
		return editors;
	}

	@Override
	public ConversationEditor registerConversationEditorOrDefault(int conversation, UUID author) {
		ConversationEditor editor = null;
		if(getMap().containsKey(conversation)) {
			editor = getMap().get(conversation);
			if(editor.author.equals(author)) {
				getMap().put(conversation, editor);
				return editor;
			}
		}
		List<Sentence> list = new SentenceDatabase().findSentencesByConversation(conversation)
			.orElse(new ArrayList<>());
		editor = new TBLTConversationEditor(list, author);
		getMap().put(conversation,  editor);
		return editor;
	}

	@Override
	public ConversationEditor registerConversationEditorOrDefault(int conversation, UUID author, int select) {
		return registerConversationEditorOrDefault(conversation, author).select(select);
	}

	@Override
	public void createConversation(int npc, UUID author) {
		new TBLTConversationEditor(null, null).createConversation(npc, author);
	}



}

