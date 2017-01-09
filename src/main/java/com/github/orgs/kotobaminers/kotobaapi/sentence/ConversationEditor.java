package com.github.orgs.kotobaminers.kotobaapi.sentence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public abstract class ConversationEditor {


	public List<Sentence> sentences = new ArrayList<>();
	public UUID author;
	public int select = 0;


	public abstract void deleteConversation();
	public abstract ConversationEditorMap getMap();
	protected abstract void createConversation(int npc, UUID author);
	public abstract void insertEmpty(int index);


	public void updateConversation() {
		deleteConversation();
		Stream.iterate(0, i -> i + 1)
			.limit(sentences.size())
			.map(i -> sentences.get(i).order(i))
			.forEach(Sentence::update);
		getMap().remove(getConversation());
	}


	protected int getConversation() {
		if(0 < sentences.size()) {
			return sentences.get(0).getConversation();
		}
		return -1;
	}


	protected Optional<Sentence> getSelectedSentence() {
		if(select < sentences.size()) {
			return Optional.of(sentences.get(select));
		}
		return Optional.empty();
	}


	public void removeSentence() {
		if(select < sentences.size()) {
			if(sentences.size() == 1) {
				deleteConversation();
				return;
			}
			sentences.remove(select);
			updateConversation();
		}
	}


	protected void insertSentence(Sentence sentence, int index) {
		if(index <= sentences.size()) {
			sentences.add(index, sentence);
			updateConversation();
		}
	}

	protected void prependSentence(Sentence sentence) {
		insertSentence(sentence, select);
	}
	public void prependEmpty() {
		insertEmpty(select);
	}

	protected void appendSentence(Sentence sentence) {
		insertSentence(sentence, select + 1);
	}
	public void appendEmpty() {
		insertEmpty(select + 1);
	}

	public ConversationEditor select(int select) {
		this.select = select;
		return this;
	}

	public void editEnglish(String line) {
		getSelectedSentence().ifPresent(s -> s.editEnglish(line));
		updateConversation();
	}

	public void editKanji(String line) {
		getSelectedSentence().ifPresent(s -> s.editKanji(line));
		updateConversation();
	}

	public void editNPC(int npc) {
		if(canAddNPC(npc)) {
			getSelectedSentence().ifPresent(s -> s.npc(npc));
			updateConversation();
		}
	}

	protected abstract boolean canAddNPC(int npc);


}

