package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.TBLTPlayer;
import com.github.orgs.kotobaminers.kotobatblt3.quest.KotobaQuest;

public class TBLTArenaMeta {


	private int next = 0;
	private Map<TBLTPlayer, Location> jobSpawn = new HashMap<>();
	private Map<TBLTPlayer, List<ItemStack>> jobItems = new HashMap<>();
	private List<KotobaQuest> quests = new ArrayList<>();
	private Map<UUID, List<Block>> getOwners = new HashMap<>();


	private PlayerNumber playerNumber = PlayerNumber.SINGLE;


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

	public void setMultiplePlayers() {
		this.playerNumber = PlayerNumber.MULTIPLE;
	}


	private enum PlayerNumber {
		SINGLE,
		MULTIPLE,
		;
	}


	public void giveJobItems(Player player) {
		TBLTPlayer job = TBLTPlayer.findJob(player);
		jobItems.getOrDefault(job, Arrays.asList())
			.forEach(i -> player.getInventory().addItem(i));
	}


	public boolean isSingle() {
		switch(playerNumber) {
		case SINGLE:
			return true;
		case MULTIPLE:
		default:
			return false;
		}
	}


	public boolean canOperateGem(Player player, Block block) {
		Predicate<Block> isRegisteredGem = (b) -> b.getLocation().distance(block.getLocation()) == 0 && b.getType() == block.getType();
		boolean isMine = getOwners.getOrDefault(player.getUniqueId(), new ArrayList<>()).stream()
			.filter(isRegisteredGem)
			.findFirst()
			.isPresent();
		boolean hasOwner = getOwners.entrySet().stream()
			.filter(e -> !e.getKey().equals(player.getUniqueId()))
			.flatMap(e -> e.getValue().stream())
			.anyMatch(isRegisteredGem);
		return isMine || !hasOwner;
	}


	public void addOwningGem(Player player, Block block) {
		UUID uuid = player.getUniqueId();
		List<Block> ownings = getOwners.getOrDefault(uuid, new ArrayList<>());
		ownings.add(block);
		getOwners.put(uuid, ownings);
	}


	public void removeOwningGem(Player player, Block block) {
		UUID uuid = player.getUniqueId();
		List<Block> ownings = getOwners.getOrDefault(uuid, new ArrayList<>());
		ownings.remove(block);
		getOwners.put(uuid, ownings);
	}


}

