package com.github.orgs.kotobaminers.kotobatblt3.quest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.block.Chest;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobatblt3.utility.ChestReader;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

import net.md_5.bungee.api.ChatColor;

public abstract class KotobaQuest {


	private boolean done = false;


	public abstract Optional<KotobaQuest> tryCreate(Chest chest);

	abstract boolean checkQuest(Event event);

	abstract Class<? extends Event> getEventClass();

	abstract void playCorrectEffect(Event event);

	abstract void playWrongEffect(Event event);;

	public abstract TBLTItemStackIcon getIcon();


	public boolean isSameEvent(Event event) {
		return event.getClass() == getEventClass();
	}


	public abstract ItemStack createProgress();


	public void setDone(boolean done) {
		this.done = done;
	}
	public boolean getDone() {
		return done;
	}
	protected List<ItemStack> extractOptions(Chest chest) {
		return ChestReader.extractItems(chest).stream()
			.filter(i -> !getIcon().isIconItemStack(i))
			.collect(Collectors.toList());
	}
	protected List<String> getQuestStatus() {
		List<String> lore = new ArrayList<>();
		if(done) {
			lore.add(ChatColor.GREEN + "Completed!");
		} else {
			lore.add(ChatColor.DARK_RED + "Not Yet");
		}
		return lore;
	}



}
