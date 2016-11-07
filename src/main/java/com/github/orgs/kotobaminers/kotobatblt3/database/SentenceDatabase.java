package com.github.orgs.kotobaminers.kotobatblt3.database;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang.BooleanUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.orgs.kotobaminers.kotobaapi.database.DatabaseManager;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence.Expression;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;

public class SentenceDatabase extends DatabaseManager {

	public SentenceDatabase() {
		super();
	}

	private static String database;
	private static String user;
	private static String pass;
	private static YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(Setting.getPlugin().getDataFolder().getAbsoluteFile() + "/Config/Database.yml"));
	private static String sentenceTable;

	public synchronized void update(Sentence sentence) {
		String update = "";
		Connection connection = null;
		Statement statement = null;
		if(sentence.getId() == null) {
			update = "INSERT INTO " + sentenceTable + " "
				+ "(npc, conversation, ordering, task, keyBool, japanese, english, owner) "
				+ "VALUES "
					+ "('" + sentence.getNPC() + "', '"
					+ sentence.getConversation() + "', '"
					+ sentence.getOrder() + "', '"
					+ sentence.getTask() + "', '"
					+ BooleanUtils.toInteger(sentence.getKey()) + "', '"
					+ sentence.getLines(Arrays.asList(Expression.KANJI)).get(0).replace("'", "''") + "', '"
					+ sentence.getLines(Arrays.asList(Expression.ENGLISH)).get(0).replace("'", "''") + "', '"
					+ sentence.getOwner().map(UUID::toString).orElse("") + "') ";
		} else {
			update = "INSERT INTO " + sentenceTable + " "
				+ "(id, npc, conversation, ordering, task, keyBool, japanese, english, owner) "
				+ "VALUES "
					+ "('" + sentence.getId() + "', '"
					+ sentence.getNPC() + "', '"
					+ sentence.getConversation() + "', '"
					+ sentence.getOrder() + "', '"
					+ sentence.getTask() + "', '"
					+ BooleanUtils.toInteger(sentence.getKey()) + "', '"
					+ sentence.getLines(Arrays.asList(Expression.KANJI)).get(0).replace("'", "''") + "', '"
					+ sentence.getLines(Arrays.asList(Expression.ENGLISH)).get(0).replace("'", "''") + "', '"
					+ sentence.getOwner().map(UUID::toString).orElse("") + "') "
				+ "ON DUPLICATE KEY UPDATE "
					+ "id = '" + sentence.getId() + "', "
					+ "npc = '" + sentence.getNPC() + "', "
					+ "conversation = '" + sentence.getConversation() + "', "
					+ "ordering = '" + sentence.getOrder() + "', "
					+ "task = '" + sentence.getTask() + "', "
					+ "keyBool = '" + BooleanUtils.toInteger(sentence.getKey()) + "', "
					+ "japanese = '" + sentence.getLines(Arrays.asList(Expression.KANJI)).get(0).replace("'", "''") + "', "
					+ "english = '" + sentence.getLines(Arrays.asList(Expression.ENGLISH)).get(0).replace("'", "''") + "', "
					+ "owner = '" + sentence.getOwner().map(UUID::toString).orElse("") + "';";
		}
		try {
			connection = openConnection();
			if(connection != null) {
				statement = connection.createStatement();
				statement.executeUpdate(update);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(statement != null) statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			closeConnection(connection);
		}
	}

	public synchronized Optional<Sentence> find(int id) {
 		Optional<Sentence> sentence = Optional.empty();
		String select = "SELECT * FROM " + sentenceTable + " WHERE id = '" + id + "' LIMIT 1;";
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;

		try {
			connection = openConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(select);
			if(result.next()) {
				sentence = Optional.ofNullable(Sentence.create(result));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(result);
			closeStatement(statement);
			closeConnection(connection);
		}
		return sentence;
	}

	public synchronized Optional<List<Sentence>> findSentencesByNPCId(int npc) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;

		String select = "SELECT * FROM "  + sentenceTable + " WHERE npc = " + npc + " LIMIT 1;";
		List<Sentence> list = new ArrayList<>();

		try {
			connection = openConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(select);

			if(result.next()) {
				int conversation = result.getInt("conversation");
				list = findSentencesByConversation(conversation).orElse(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(result);
			closeStatement(statement);
			closeConnection(connection);
		}
		if(0 < list.size()) {
			return Optional.ofNullable(list);
		} else {
			return Optional.empty();
		}
	}

	public synchronized Optional<List<Sentence>> findSentencesByConversation(int conversation) {
		List<Sentence> list = new ArrayList<>();
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;

		try {
			connection = openConnection();
			statement = connection.createStatement();
			String select = "SELECT * FROM " + sentenceTable + " WHERE conversation = " + conversation + " ORDER BY ordering ASC;";
			result = statement.executeQuery(select);

			if(result.next()) {
				result.previous();
				while(result.next()) {
					list.add(Sentence.create(result));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(result);
			closeStatement(statement);
			closeConnection(connection);
		}
		if(0 < list.size()) {
			return Optional.ofNullable(list);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public String getDatabase() {
		return database;
	}
	@Override
	public String getUser() {
		return user;
	}
	@Override
	public String getPass() {
		return pass;
	}
	@Override
	public void loadConfig() {
		SentenceDatabase.database = config.getString("DATABASE");
		SentenceDatabase.user = config.getString("USER");
		SentenceDatabase.pass = config.getString("PASS");
		SentenceDatabase.sentenceTable = config.getString("SENTENCE_TABLE");
	}
}
