package com.github.orgs.kotobaminers.kotobatblt3.quest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaSound;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaTitle;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaTitle.TitleOption;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

import net.md_5.bungee.api.ChatColor;

public class WalkOnQuest extends KotobaQuest {


	private static final Material AFTER = Material.OBSIDIAN;
	private Location location;


	@Override
	public Optional<KotobaQuest> tryCreate(Chest chest) {
		WalkOnQuest quest = new WalkOnQuest();
		quest.location = chest.getLocation().add(0, 2, 0);
		return Optional.of(quest);
	}


	private Optional<PlayerMoveEvent> castEvent(Event event) {
		if(event instanceof PlayerMoveEvent) {
			return Optional.of((PlayerMoveEvent) event);
		}
		return Optional.empty();
	}


	@Override
	public boolean checkQuest(Event event) {
		return castEvent(event)
			.map(e -> {
				Location position = e.getPlayer().getLocation().add(0, -1, 0).getBlock().getLocation();
				if(position.distance(location) == 0) {
					playCorrectEffect(event);
					return true;
				}
				return false;
			}).orElse(false);
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
				location.getBlock().setType(AFTER);
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
	public TBLTItemStackIcon getIcon() {
		return TBLTItemStackIcon.WALK_ON_QUEST_CREATOR;
	}


	@SuppressWarnings("deprecation")
	@Override
	public ItemStack createProgress() {
		Block block = location.getBlock();
		List<String> lore = getQuestStatus();
		lore.addAll(KotobaUtility.splitSentence("Walk on the target block."));
		return KotobaItemStack.create(block.getType(), block.getData(), 1, this.getClass().getSimpleName(), lore);
	}


}
