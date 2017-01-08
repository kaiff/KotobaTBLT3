package com.github.orgs.kotobaminers.kotobatblt3.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.github.orgs.kotobaminers.kotobaapi.sentence.ConversationEditor;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence;

public class TBLTConversationEditor extends ConversationEditor {


	private static Map<Integer, ConversationEditor> editors = new TreeMap<>();


	private TBLTConversationEditor() {
	}

	public TBLTConversationEditor(List<Sentence> sentences, UUID author, int select) {
		this.sentences = sentences;
		this.author = author;
		this.select = select;
	}

	public static TBLTConversationEditor empty() {
		return new TBLTConversationEditor();
	}


	@Override
	public Map<Integer, ConversationEditor> getEditors() {
		return editors;
	}

	@Override
	public ConversationEditor getConversationEditorOrDefault(int conversation, UUID author, int select) {
		ConversationEditor editor = null;
		if(getEditors().containsKey(conversation)) {
			editor = getEditors().get(conversation);
			if(editor.author.equals(author)) {
				editors.put(conversation, editor);
				return editor;
			}
		}
		List<Sentence> list = new SentenceDatabase().findSentencesByConversation(conversation)
			.orElse(new ArrayList<>());
		editor = new TBLTConversationEditor(list, author, select);
		editors.put(conversation,  editor);
		return editor;
	}



	@Override
	public void deleteConversation() {
		new SentenceDatabase().deleteConversation(getConversation());
	}


	@Override
	public void createConversation(int npc, UUID author) {
		List<Sentence> list = new ArrayList<Sentence>();
		list.add(new TBLTSentence().empty(new SentenceDatabase().getMaxConversation() + 1, npc));
		new TBLTConversationEditor(list, author, 0).updateConversation();
	}


}
