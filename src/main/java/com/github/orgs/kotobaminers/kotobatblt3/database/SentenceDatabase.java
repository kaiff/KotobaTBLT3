package com.github.orgs.kotobaminers.kotobatblt3.database;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;

import com.github.orgs.kotobaminers.kotobaapi.database.DatabaseManager;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence.Expression;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;

public class SentenceDatabase extends DatabaseManager {


	private static String database;
	private static String user;
	private static String pass;
	private static YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(Setting.getPlugin().getDataFolder().getAbsoluteFile() + "/Config/Database.yml"));
	private static String sentenceTable;


	protected synchronized void update(Sentence sentence) {
		String update = "";
		PreparedStatement prepared = null;
		Connection connection = null;
		try {
			connection = openConnection();
			if(connection != null) {
				if(sentence.getId() == null) {
					update = "INSERT INTO " + sentenceTable + " "
						+ "(npc, conversation, ordering, task, keyBool, japanese, english, owner) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
					prepared = connection.prepareStatement(update);
					prepared.setInt(1, sentence.getNPC());
					prepared.setInt(2, sentence.getConversation());
					prepared.setInt(3, sentence.getOrder());
					prepared.setString(4, sentence.getTask());
					prepared.setBoolean(5, sentence.getKey());
					prepared.setString(6, sentence.getLines(Arrays.asList(Expression.KANJI)).get(0).replace("'", "''"));
					prepared.setString(7, sentence.getLines(Arrays.asList(Expression.ENGLISH)).get(0).replace("'", "''"));
					prepared.setString(8, sentence.getOwner().map(UUID::toString).orElse(""));
					prepared.executeUpdate();
				} else {
					update = "INSERT INTO " + sentenceTable + " "
						+ "(id, npc, conversation, ordering, task, keyBool, japanese, english, owner) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) "
						+ "ON DUPLICATE KEY UPDATE "
							+ "id = VALUES(id), "
							+ "npc = VALUES(npc), "
							+ "conversation = VALUES(conversation), "
							+ "ordering = VALUES(ordering), "
							+ "task = VALUES(task), "
							+ "keyBool = VALUES(keyBool), "
							+ "japanese = VALUES(japanese), "
							+ "english = VALUES(english), "
							+ "owner = VALUES(owner);";
					prepared = connection.prepareStatement(update);
					prepared.setInt(1, sentence.getId());
					prepared.setInt(2, sentence.getNPC());
					prepared.setInt(3, sentence.getConversation());
					prepared.setInt(4, sentence.getOrder());
					prepared.setString(5, sentence.getTask());
					prepared.setBoolean(6, sentence.getKey());
					prepared.setString(7, sentence.getLines(Arrays.asList(Expression.KANJI)).get(0).replace("'", "''"));
					prepared.setString(8, sentence.getLines(Arrays.asList(Expression.ENGLISH)).get(0).replace("'", "''"));
					prepared.setString(9, sentence.getOwner().map(UUID::toString).orElse(""));
					prepared.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(prepared != null) prepared.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			closeConnection(connection);
		}
	}


	public synchronized void deleteConversation(int conversation) {
		String delete = "DELETE FROM " + sentenceTable + " WHERE conversation = ?;";
		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = openConnection();
			ps = connection.prepareStatement(delete);
			ps.setInt(1, conversation);
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatement(ps);
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
				sentence = Optional.ofNullable(new TBLTSentence().create(result));
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
					list.add(new TBLTSentence().create(result));
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

	public synchronized Optional<Integer> getMaxConversation() {
		String sql = "SELECT MAX(conversation) as maxConversation FROM tblt_sentence;";
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;

		try {
			connection = openConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(sql);
			if(result.next()) {
				return Optional.ofNullable(result.getInt("maxConversation"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(result);
			closeStatement(statement);
			closeConnection(connection);
		}
		return Optional.empty();
	}

	public synchronized Optional<Integer> findConversation(int npc) {
		String find = "SELECT conversation FROM " + sentenceTable + " WHERE npc = ?;";
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		try {
			connection = openConnection();
			ps = connection.prepareStatement(find);
			ps.setInt(1, npc);
			result = ps.executeQuery();
			if(result.next()) {
				return Optional.ofNullable(result.getInt("conversation"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(result);
			closeStatement(ps);
			closeConnection(connection);
		}
		return Optional.empty();
	}

	public synchronized Set<Integer> getNPCIds() {
		String find = "SELECT npc FROM " + sentenceTable + ";";
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;

		Set<Integer> ids = new HashSet<Integer>();
		try {
			connection = openConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(find);
			while(result.next()) {
				ids.add(result.getInt("npc"));
			}
			if(result.next()) {
				return ids;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(result);
			closeStatement(statement);
			closeConnection(connection);
		}
		return ids;
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

