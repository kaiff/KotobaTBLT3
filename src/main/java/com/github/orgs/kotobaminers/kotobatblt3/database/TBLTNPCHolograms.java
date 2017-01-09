package com.github.orgs.kotobaminers.kotobatblt3.database;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.github.orgs.kotobaminers.kotobaapi.citizens.CitizensManager;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Holograms;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence.Expression;
import com.github.orgs.kotobaminers.kotobatblt3.utility.Utility;

import net.citizensnpcs.api.npc.NPC;

public class TBLTNPCHolograms extends Holograms {


	private static final List<Expression> EXPRESSIONS = Arrays.asList(Expression.ENGLISH);


	public boolean display(NPC npc, Player player) {
		//Remove previous sentences TODO check if this working
		SentenceDatabase sentenceDatabase = new SentenceDatabase();
		sentenceDatabase.findSentencesByNPCId(npc.getId())
			.ifPresent(sentences ->
				sentences.stream()
					.map(Sentence::getId)
					.collect(Collectors.toSet())
					.stream()
					.forEach(id -> CitizensManager.findNPC(id).ifPresent(n -> new TBLTNPCHolograms().removeNear(getBase(n.getEntity()))))
			);


		PlayerDatabase playerDatabase = new PlayerDatabase();
		PlayerData data = playerDatabase.getOrDefault(player.getUniqueId()).npc(npc.getId());

		PlayerData data2 = playerDatabase.updateDisplay(data, npc.getId()).orElse(null);
		if(data2 == null) return false;

		Sentence sentence = sentenceDatabase.find(data2.getDisplay()).orElse(null);
		if(sentence == null) return false;

		Location b = CitizensManager.findNPC(sentence.getNPC()).map(n -> getBase(n.getEntity())).orElse(null);
		if(b == null) return false;

		List<String> lines = sentence.getLines(EXPRESSIONS);
		sentenceDatabase.findSentencesByConversation(sentence.getConversation())
			.ifPresent(sentences -> lines.add(0, Utility.patternProgress("◇", "◆", sentences.size(), sentences.stream().map(Sentence::getId).collect(Collectors.toList()).indexOf(sentence.getId()), ChatColor.GREEN)));
		return new TBLTNPCHolograms().display(lines, b);
	}


	private Location getBase(Entity entity) {
		if(entity instanceof LivingEntity) {
			LivingEntity le = (LivingEntity) entity;
			return le.getEyeLocation();
		}
		return entity.getLocation();
	}


}

