package com.github.orgs.kotobaminers.kotobatblt3.database;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.orgs.kotobaminers.kotobaapi.sentence.Holograms;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence.Expression;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaSound;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.utility.NPCManager;
import com.github.orgs.kotobaminers.kotobatblt3.utility.Utility;

import net.citizensnpcs.api.npc.NPC;

public class TBLTSentenceHolograms extends Holograms {


	public TBLTSentenceHolograms(Location loc) {
		this.setLocation(loc);
	}


	public void display(NPC npc, Player player) {
		SentenceDatabase sentenceDatabase = new SentenceDatabase();
		sentenceDatabase.findSentencesByNPCId(npc.getId())
			.ifPresent(sentences ->
				sentences.stream()
					.map(Sentence::getId)
					.collect(Collectors.toSet())
					.stream()
					.forEach(id -> NPCManager.findNPC(id).ifPresent(n -> new TBLTSentenceHolograms(n.getStoredLocation()).removeNear()))
			);

		PlayerDatabase playerDatabase = new PlayerDatabase();
		PlayerData data = playerDatabase.getOrDefault(player.getUniqueId()).npc(npc.getId());
		List<Expression> expressions = Arrays.asList(Expression.ENGLISH);
		playerDatabase.updateDisplay(data, npc.getId())
			.ifPresent(d ->sentenceDatabase.find(d.getDisplay())
				.ifPresent(s -> NPCManager.findNPC(s.getNPC()).map(n -> n.getStoredLocation())
					.ifPresent(loc -> {
						List<String> lines = s.getLines(expressions);
						sentenceDatabase.findSentencesByConversation(s.getConversation())
							.ifPresent(sentences -> lines.add(0, Utility.patternProgress("◇", "◆", sentences.size(), sentences.stream().map(Sentence::getId).collect(Collectors.toList()).indexOf(s.getId()), ChatColor.GREEN)));
						new TBLTSentenceHolograms(loc).display(lines);
						KotobaUtility.lookAt(player, loc.clone().add(0, -0.25, 0));
						KotobaSound.ATTENTION.play(loc);
					})));
	}

}

