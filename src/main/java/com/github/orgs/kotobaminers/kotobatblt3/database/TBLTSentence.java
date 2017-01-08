package com.github.orgs.kotobaminers.kotobatblt3.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import com.github.orgs.kotobaminers.kotobaapi.database.DatabaseManager;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence;

public class TBLTSentence extends Sentence {


	private static final DatabaseManager DATABASE = new SentenceDatabase();


	@Override
	public Sentence create(final Consumer<Sentence> builder) {
		Sentence sentence = new TBLTSentence();
		builder.accept(sentence);
		return sentence;
	}


	@Override
	public Sentence create(ResultSet result) throws SQLException {
		final int id = result.getInt("id");
		final int npc = result.getInt("npc");
		final int conversation = result.getInt("conversation");
		final int order = result.getInt("ordering");
		final String task = result.getString("task");
		final boolean key = result.getBoolean("keyBool");
		final String japanese = result.getString("japanese");
		final String english = result.getString("english");
		final Optional<UUID> owner = Optional.ofNullable(result.getString("owner")).filter(o -> o.length() == 36).map(UUID::fromString);
		return new TBLTSentence().create(s ->
			s.id(id)
			.npc(npc)
			.conversation(conversation)
			.order(order)
			.task(task)
			.key(key)
			.japanese(japanese)
			.english(english)
			.owner(owner)
			);
	}


	public Sentence empty(int conversation, int npc) {
		return new TBLTSentence().create(s ->
			s.conversation(conversation)
				.npc(npc)
				.japanese(JAPANESE_INI)
				.english(ENGLISH_INI)
		);
	}


	@Override
	public DatabaseManager getDatabaseManager() {
		return DATABASE;
	}


	@Override
	public void update() {
		((SentenceDatabase) DATABASE).update(this);
	}


}

