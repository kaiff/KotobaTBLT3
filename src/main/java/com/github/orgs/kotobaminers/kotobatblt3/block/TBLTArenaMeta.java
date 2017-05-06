package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.TBLTPlayer;
import com.github.orgs.kotobaminers.kotobatblt3.quest.KotobaQuest;

public class TBLTArenaMeta {


	private int next = 0;
	private Map<TBLTPlayer, Location> jobSpawn = new HashMap<>();
	private Map<TBLTPlayer, List<ItemStack>> jobItems = new HashMap<>();
	private List<KotobaQuest> quests = new ArrayList<>();


	public int getNext() {
		return next;
	}
	public void setNext(int next) {
		this.next = next;
	}
	public Map<TBLTPlayer, List<ItemStack>> getJobItems() {
		return jobItems;
	}
	public Map<TBLTPlayer, Location> getJobSpawn() {
		return jobSpawn;
	}
	public List<KotobaQuest> getQuests() {
		return quests;
	}
	public void setQuests(List<KotobaQuest> quests) {
		this.quests = quests;
	}


	public void giveJobItems(Player player) {
		TBLTPlayer job = TBLTPlayer.findJob(player);
		jobItems.getOrDefault(job, Arrays.asList())
			.forEach(i -> player.getInventory().addItem(i));
	}

}

