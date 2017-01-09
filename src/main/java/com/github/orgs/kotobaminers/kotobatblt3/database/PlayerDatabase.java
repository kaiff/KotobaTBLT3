package com.github.orgs.kotobaminers.kotobatblt3.database;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.BooleanUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.orgs.kotobaminers.kotobaapi.database.DatabaseManager;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence;
import com.github.orgs.kotobaminers.kotobatblt3.database.PlayerData.EditMode;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.TBLTCommandExecutor.PermissionEnum;

public class PlayerDatabase extends DatabaseManager {

	public PlayerDatabase() {
		super();
	}

	private static String database;
	private static String user;
	private static String pass;
	private static YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(Setting.getPlugin().getDataFolder().getAbsoluteFile() + "/Config/Database.yml"));
	private static String playerTable;

	@Override
	public void loadConfig() {
		PlayerDatabase.database = config.getString("DATABASE");
		PlayerDatabase.user = config.getString("USER");
		PlayerDatabase.pass = config.getString("PASS");
		PlayerDatabase.playerTable = config.getString("PLAYER_TABLE");
	}

	public synchronized PlayerData getOrDefault(UUID uuid) {
		String select = "SELECT * FROM " + playerTable + " WHERE uuid = '" + uuid.toString() + "' LIMIT 1;";
		PlayerData data = PlayerData.initial(uuid);
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		try {
			connection = openConnection();
			if(connection != null) {
				statement = connection.createStatement();
				result = statement.executeQuery(select);
				if(result.next()) {
					final int npc = result.getInt("npc");
					final int sentence = result.getInt("sentence");
					final int display = result.getInt("display");
					final int edit = result.getInt("edit");
					final EditMode mode = EditMode.valueOf(result.getString("editMode"));
					final PermissionEnum permission = PermissionEnum.valueOf(result.getString("permission"));
					final boolean english = result.getBoolean("english");
					final boolean kanji = result.getBoolean("kanji");
					data = PlayerData.create(d ->
					d.uuid(uuid)
					.npc(npc)
					.sentence(sentence)
					.display(display)
					.edit(edit)
					.editMode(mode)
					.permission(permission)
					.english(english)
					.kanji(kanji)
							);
				}
				update(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(result);
			closeStatement(statement);
			closeConnection(connection);
		}
		return data;
	}

	public synchronized void update(PlayerData data) {
		Connection connection = null;
		Statement statement = null;

		String update = "INSERT INTO " + playerTable + " "
			+ "(uuid, npc, sentence, display, edit, editMode, permission, english, kanji)"
			+ " VALUES"
				+ " ('" + data.getUuid().toString() + "', '"
				+ data.getNPC() + "', '"
				+ data.getSentence() + "', '"
				+ data.getDisplay() + "', '"
				+ data.findEdit().orElse(0) + "', '"
				+ data.getEditMode().name() + "', '"
				+ data.getPermission().name() + "', '"
				+ BooleanUtils.toInteger(data.getEnglish()) + "', '"
				+ BooleanUtils.toInteger(data.getKanji()) +
				"') "
			+ "ON DUPLICATE KEY UPDATE "
				+ "uuid = '" + data.getUuid().toString() + "', "
				+ "npc = '" + data.getNPC() + "', "
				+ "sentence = '" + data.getSentence() + "', "
				+ "display = '" + data.getDisplay() + "', "
				+ "edit = '" + data.findEdit().orElse(0) + "', "
				+ "editMode = '" + data.getEditMode().name() + "', "
				+ "permission = '" + data.getPermission().name() + "', "
				+ "english = '" + BooleanUtils.toInteger(data.getEnglish()) + "', "
				+ "kanji = '" + BooleanUtils.toInteger(data.getKanji()) + "';";
		try {
			connection = openConnection();
			if(connection != null) {
				statement = connection.createStatement();
				if(statement != null) statement.executeUpdate(update);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatement(statement);
			closeConnection(connection);
		}
	}

	public synchronized Optional<PlayerData> updateDisplay(PlayerData data, int npc) {
		List<Sentence> sentences = new SentenceDatabase().findSentencesByNPCId(npc);

		List<Integer> ids = sentences.stream()
			.map(s -> s.getId())
			.collect(Collectors.toList());
		if(ids.size() < 1) return Optional.empty();

		if(ids.contains(data.getDisplay())) {
			int index = ids.indexOf(data.getDisplay());
			if(index < ids.size() - 1) {
				data.display(ids.get(index + 1));
			} else {
				data.display(ids.get(0));
			}
			update(data);
		} else {
			data.display(ids.get(0));
			update(data);
		}
		return Optional.of(data);
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
	public YamlConfiguration getConfig() {
		return config;
	}

}
