package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.orgs.kotobaminers.kotobaapi.sentence.Holograms;
import com.github.orgs.kotobaminers.kotobaapi.sentence.HologramsManager;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence.Expression;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaAPISound;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaAPIUtility;
import com.github.orgs.kotobaminers.kotobatblt3.database.PlayerData;
import com.github.orgs.kotobaminers.kotobatblt3.database.PlayerDatabase;
import com.github.orgs.kotobaminers.kotobatblt3.database.SentenceDatabase;
import com.github.orgs.kotobaminers.kotobatblt3.game.TBLTArena;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.NPCManager;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Utility;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;

public class ClickAbilityListener implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(player.getItemInHand().getType() == Material.AIR) return;
		if(!Utility.isTBLTPlaying(player)) return;
		Action action = event.getAction();
		ClickAbility.find(player.getItemInHand())
			.filter(a -> a.canPerform(player, action))
			.ifPresent(ability -> {
				event.setCancelled(true);
				ability.perform(event);
		});
	}

	@EventHandler
	public void onPotionSplash(PotionSplashEvent event) {
		if(event.getPotion().getEffects().size() == 0) {
			if(!TBLTArena.isInArena(event.getEntity().getLocation())) return;
			TBLTPotion.find(event).eventSplash(event);
		}
	}

	@EventHandler
	public void onRightClickNPC(NPCRightClickEvent event) {
		NPC npc = event.getNPC();
		Player player = event.getClicker();

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
						.ifPresent(sentences -> lines.add(0, Utility.patternProgress("", "", sentences.size(), sentences.stream().map(Sentence::getId).collect(Collectors.toList()).indexOf(s.getId()), ChatColor.GREEN)));
					HologramsManager.updateHologram(Setting.getPlugin(), Holograms.create(loc), s.getConversation(), lines, 20 * 10);
					KotobaAPIUtility.lookAt(player, loc.add(0, -1, 0));
					KotobaAPISound.ATTENTION.play(player);
				})));
	}

}
