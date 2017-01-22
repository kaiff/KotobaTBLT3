package com.github.orgs.kotobaminers.kotobatblt3.database;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.citizens.KotobaCitizensManager;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Holograms;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence.Expression;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaSound;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.utility.Utility;

import net.citizensnpcs.api.npc.NPC;

public class TBLTNPCHolograms extends Holograms {


	private static final List<Expression> EXPRESSIONS = Arrays.asList(Expression.ENGLISH);


	private List<String> getLines(Sentence sentence) {
		List<String> lines = sentence.getLines(EXPRESSIONS);
		List<Sentence> sentences = new SentenceDatabase().getSentencesByConversation(sentence.getConversation());
		lines.add(0, Utility.patternProgress("◇", "◆", sentences.size(), sentences.stream().map(Sentence::getId).collect(Collectors.toList()).indexOf(sentence.getId()), ChatColor.GREEN));
		return lines;
	}


	private void removeConversationHolograms(int conversation) {
		new SentenceDatabase().getSentencesByConversation(conversation).stream()
			.map(Sentence::getNPC)
			.collect(Collectors.toSet())
			.stream()
			.forEach(id -> KotobaCitizensManager.findNPC(id).ifPresent(n -> new TBLTNPCHolograms().removeNear(KotobaUtility.getBase(n.getEntity())))
		);
	}


	public Optional<Sentence> findSentence(NPC npc, Player player) {
		PlayerDatabase playerDatabase = new PlayerDatabase();
		PlayerData data = playerDatabase.getOrDefault(player.getUniqueId()).npc(npc.getId());
		return playerDatabase.updateDisplay(data, npc.getId())
			.map(d -> new SentenceDatabase().find(d.getDisplay()))
			.orElse(Optional.empty());
	}


	private void playEffect(Player player, Sentence sentence) {
		KotobaCitizensManager.findNPC(sentence.getNPC())
			.ifPresent(npc -> {
				Location lookAt = npc.getStoredLocation();
				if(npc.getEntity() instanceof LivingEntity) {
					lookAt = ((LivingEntity) npc.getEntity()).getEyeLocation();
				}
				Vector vector = lookAt.subtract(player.getEyeLocation()).toVector();
				player.teleport(player.getLocation().setDirection(vector));
				KotobaSound.ATTENTION.play(npc.getStoredLocation());
			});
	}


	public boolean display(NPC npc, Player player) {
		return findSentence(npc, player)
			.map(sentence -> {
				removeConversationHolograms(sentence.getConversation());

				Location base = KotobaCitizensManager.findNPC(sentence.getNPC()).map(n -> KotobaUtility.getBase(n.getEntity())).orElse(null);
				if(base == null) return false;

				boolean success = new TBLTNPCHolograms().display(getLines(sentence), base);
				if(success) playEffect(player, sentence);

				return success;
			}).orElse(false);
	}


}

