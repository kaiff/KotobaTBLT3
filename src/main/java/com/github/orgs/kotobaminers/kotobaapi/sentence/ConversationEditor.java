package com.github.orgs.kotobaminers.kotobaapi.sentence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public abstract class ConversationEditor {


	public List<Sentence> sentences = new ArrayList<>();
	public UUID author;
	public int select = 0;


	public abstract ConversationEditor getConversationEditorOrDefault(int conversation, UUID author, int select);
	public abstract Map<Integer, ConversationEditor> getEditors();
	public abstract void createConversation(int npc, UUID author);
	public abstract void deleteConversation();


	public void updateConversation() {
		deleteConversation();
		Stream.iterate(0, i -> i + 1)
			.limit(sentences.size())
			.map(i -> sentences.get(i).order(i))
			.forEach(Sentence::update);
		if(getEditors().containsKey(getConversation())) {
			getEditors().remove(getConversation());
		}
	}


	protected int getConversation() {
		if(0 < sentences.size()) {
			return sentences.get(0).getConversation();
		}
		return -1;
	}


	public Optional<Sentence> getSelectedSentence() {
		if(select < sentences.size()) {
			return Optional.of(sentences.get(select));
		}
		return Optional.empty();
	}


	public void removeSentence(int order) {
		if(order < sentences.size()) {
			if(sentences.size() == 1) {
				deleteConversation();
				return;
			}
			sentences.remove(order);
			updateConversation();
		}
	}


	public void insertSentence(Sentence sentence, int order) {
		if(order <= sentences.size()) {
			sentences.add(order, sentence);
			updateConversation();
		}
	}


}

