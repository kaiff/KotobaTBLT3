package com.github.orgs.kotobaminers.kotobatblt3.quest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaSound;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaTitle;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaTitle.TitleOption;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

import net.md_5.bungee.api.ChatColor;

public class ClickQuest extends KotobaQuest {


	private static final Material AFTER = Material.OBSIDIAN;
	private Material material;


	@Override
	public TBLTItemStackIcon getIcon() {
		return TBLTItemStackIcon.CLICK_QUEST_CREATOR;
	}


	@Override
	public Optional<KotobaQuest> tryCreate(Chest chest) {
		List<ItemStack> options = extractOptions(chest);
		ClickQuest quest = new ClickQuest();
		if(0 < options.size()) {
			ItemStack o1 = options.get(0);
			quest.material = o1.getType();
			return Optional.of(quest);
		}
		return Optional.empty();
	}


	private Optional<PlayerInteractEvent> castEvent(Event event) {
		if(event instanceof PlayerInteractEvent) {
			return Optional.of((PlayerInteractEvent) event);
		}
		return Optional.empty();
	}


	@Override
	public boolean checkQuest(Event event) {
		if(event instanceof PlayerInteractEvent) {
			PlayerInteractEvent e = (PlayerInteractEvent) event;
			if(e.getClickedBlock() == null) return false;
			if(e.getClickedBlock().getType() == material) {
				playCorrectEffect(event);
				return true;
			}
		}

		return false;
	}


	@Override
	Class<? extends Event> getEventClass() {
		return PlayerInteractEvent.class;
	}


	@Override
	void playCorrectEffect(Event event) {
		castEvent(event)
			.ifPresent(e -> {
				KotobaTitle.displayTitle(e.getPlayer(), "Correct", ChatColor.GREEN, Arrays.asList(TitleOption.BOLD));
				KotobaSound.GOOD.play(e.getPlayer().getLocation());
				Optional.of(e.getClickedBlock()).ifPresent(b -> b.setType(AFTER));
				setDone(true);
			});
	}


	@Override
	void playWrongEffect(Event event) {
		castEvent(event)
		.ifPresent(e -> {
			KotobaTitle.displayTitle(e.getPlayer(), "Wrong", ChatColor.RED, Arrays.asList(TitleOption.BOLD));
		});
	}


	@Override
	public ItemStack createProgress() {
		List<String> lore = getQuestStatus();
		lore.addAll(KotobaUtility.splitSentence("Click on the target block."));
		return KotobaItemStack.create(material, (short) 0, 1, this.getClass().getSimpleName(), lore);
	}


}

