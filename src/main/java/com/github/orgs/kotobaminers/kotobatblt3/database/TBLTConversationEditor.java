package com.github.orgs.kotobaminers.kotobatblt3.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.github.orgs.kotobaminers.kotobaapi.citizens.CitizensManager;
import com.github.orgs.kotobaminers.kotobaapi.sentence.ConversationEditor;
import com.github.orgs.kotobaminers.kotobaapi.sentence.ConversationEditorMap;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence;

import net.citizensnpcs.api.npc.NPC;

public class TBLTConversationEditor extends ConversationEditor {


	private static final ConversationEditorMap MAP = new TBLTConversationEditorMap();


	TBLTConversationEditor(List<Sentence> sentences, UUID author) {
		this.sentences = sentences;
		this.author = author;
	}


	@Override
	public void deleteConversation() {
		new SentenceDatabase().deleteConversation(getConversation());
	}


	@Override
	public ConversationEditorMap getMap() {
		return MAP;
	}


	@Override
	protected void createConversation(int npc, UUID author) {
		List<Sentence> list = new ArrayList<Sentence>();
		new SentenceDatabase().getMaxConversation()
			.ifPresent(conversation -> list.add(new TBLTSentence().empty(conversation + 1, npc)));
		new TBLTConversationEditor(list, author).updateConversation();
	}


	@Override
	protected boolean canAddNPC(int npc) {
		Optional<NPC> entity = CitizensManager.findNPC(npc);
		if(!entity.isPresent()) return false;
		if(sentences.stream().map(Sentence::getNPC).collect(Collectors.toSet()).contains(npc)) return true;
		if(entity.filter(e -> !new SentenceDatabase().getNPCIds().contains(e.getId())).isPresent()) return true;

		return false;
	}


	@Override
	public void insertEmpty(int index) {
		getSelectedSentence().ifPresent(selected -> insertSentence(new TBLTSentence().empty(getConversation(), selected.getNPC()), index));
	}


}
