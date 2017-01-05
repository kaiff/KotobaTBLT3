package com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.orgs.kotobaminers.kotobaapi.sentence.Holograms;
import com.github.orgs.kotobaminers.kotobaapi.sentence.HologramsManager;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence.Expression;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaSound;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.database.PlayerData;
import com.github.orgs.kotobaminers.kotobatblt3.database.PlayerDatabase;
import com.github.orgs.kotobaminers.kotobatblt3.database.SentenceDatabase;
import com.github.orgs.kotobaminers.kotobatblt3.utility.NPCManager;
import com.github.orgs.kotobaminers.kotobatblt3.utility.Utility;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;

public class CitizensListener implements Listener {
	@EventHandler
	public void onRightClickNPC(NPCRightClickEvent event) {
		Player player = event.getClicker();

		NPC npc = event.getNPC();
		PlayerDatabase playerDatabase = new PlayerDatabase();
		PlayerData data = playerDatabase.getOrDefault(player.getUniqueId()).npc(npc.getId());
		SentenceDatabase sentenceDatabase = new SentenceDatabase();
		List<Expression> expressions = data.getExpressions();

		playerDatabase.updateDisplay(data, npc.getId())
			.ifPresent(d ->sentenceDatabase.find(d.getDisplay())
			.ifPresent(s ->NPCManager.findNPC(s.getNPC()).map(n -> n.getStoredLocation())
				.ifPresent(loc -> {
					List<String> lines = s.getLines(expressions);
					sentenceDatabase.findSentencesByConversation(s.getConversation())
						.ifPresent(sentences -> lines.add(0, Utility.patternProgress("◇", "◆", sentences.size(), sentences.stream().map(Sentence::getId).collect(Collectors.toList()).indexOf(s.getId()), ChatColor.GREEN)));
					HologramsManager.updateHologram(Setting.getPlugin(), Holograms.create(loc), s.getConversation(), lines, 20 * 10);
					KotobaUtility.lookAt(player, loc.add(0, -1, 0));
					KotobaSound.ATTENTION.play(player.getLocation());
				})));
	}

}
