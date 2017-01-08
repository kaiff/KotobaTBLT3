package com.github.orgs.kotobaminers.kotobaapi.sentence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.github.orgs.kotobaminers.kotobaapi.database.KotobaSQLData;

public abstract class Sentence implements KotobaSQLData {


	protected static final String JAPANESE_INI = "Enter Japanese";
	protected static final String ENGLISH_INI = "Enter English";
	private static final String TASK_INI = "DEFAULT";


	private Integer id = null;
	private int conversation = 0;
	private int npc = 0;
	private int order = 0;
	private String task = TASK_INI;
	private boolean key = false;
	private Map<Expression, String> lines = new HashMap<>();
	private Optional<UUID> owner = Optional.empty();


	public enum Expression {KANJI, ENGLISH}


	public abstract Sentence create(final Consumer<Sentence> builder);
	public abstract Sentence create(ResultSet result) throws SQLException;
	public abstract Sentence empty(int conversation, int npc);


	public Sentence id(int id) {
		this.id = id;
		return this;
	}

	public Sentence npc(int npc) {
		this.npc = npc;
		return this;
	}

	public Sentence conversation(int conversation) {
		this.conversation = conversation;
		return this;
	}

	public Sentence order(int ordering) {
		this.order = ordering;
		return this;
	}

	public Sentence task(String task) {
		this.task = task;
		return this;
	}

	public Sentence key(boolean key) {
		this.key = key;
		return this;
	}

	public Sentence japanese(String japanese) {
		this.lines.put(Expression.KANJI, japanese);
		return this;
	}

	public Sentence english(String english) {
		this.lines.put(Expression.ENGLISH, english);
		return this;
	}

	public Sentence owner(Optional<UUID> owner) {
		this.owner = owner;
		return this;
	}


	public Integer getId() {
		return id;
	}

	public int getNPC() {
		return npc;
	}

	public List<String> getLines(List<Expression> expressions) {
		return expressions.stream()
			.map(e -> lines.get(e))
			.collect(Collectors.toList());
	}

	public int getConversation() {
		return conversation;
	}

	public int getOrder() {
		return order;
	}

	public String getTask() {
		return task;
	}

	public boolean getKey() {
		return key;
	}

	public Optional<UUID> getOwner() {
		return owner;
	}

	public Sentence editEnglish(String sentence) {
		lines.put(Expression.ENGLISH, sentence);
		return this;
	}

	public Sentence editKanji(String sentence) {
		lines.put(Expression.KANJI, sentence);
		return this;
	}


	@Override
	public String toString() {
		return "Cnv: " + conversation + ", NPC: " + npc + ", Tsk: " + task + ", Jp: " + lines.get(Expression.KANJI) + ", En: " + lines.get(Expression.ENGLISH);
	}


}

