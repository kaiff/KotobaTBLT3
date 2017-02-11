package com.github.orgs.kotobaminers.kotobatblt3.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.github.orgs.kotobaminers.kotobaapi.ability.ItemStackAbilityInterface;
import com.github.orgs.kotobaminers.kotobaapi.quest.KotobaQuest;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.quest.AbilityUseQuest;

public class TBLTData {


	private static Map<UUID, TBLTData> data = new HashMap<UUID, TBLTData>();


	private UUID uuid;
	private Block target;
	private Map<ItemStackAbilityInterface, Integer> abilityUsed = new HashMap<>();
	private Set<KotobaQuest> quests = new HashSet<>();


	private TBLTData(UUID uuid) {
		this.uuid = uuid;
	}


	public TBLTData target(Block target) {
		this.target = target;
		return this;
	}


	public void initialize() {
		data.put(uuid, new TBLTData(uuid));
	}

	public static TBLTData getOrDefault(UUID uuid) {
		data.putIfAbsent(uuid, new TBLTData(uuid));
		return data.get(uuid);
	}


	public void registerQuest(KotobaQuest quest) {
		if(!quests.stream().anyMatch(q -> q.isSame(quest))) {
			quests.add(quest);
		}
	}


	public void updateAbilityUsed(ItemStackAbilityInterface ability) {
		abilityUsed.putIfAbsent(ability, 0);
		abilityUsed.compute(ability, (key, old) -> old + 1);
		Bukkit.getOnlinePlayers().stream().filter(p -> p.getUniqueId().equals(uuid))
			.findAny()
			.ifPresent(p -> findAbilityUseQuest(ability).ifPresent(q -> q.tryFirstAchieve(p)));
	}


	private Optional<AbilityUseQuest> findAbilityUseQuest(ItemStackAbilityInterface ability) {
		AbilityUseQuest dummy = AbilityUseQuest.create(ability, 0);
		return quests.stream().filter(q -> q instanceof AbilityUseQuest)
			.map(q -> (AbilityUseQuest) q)
			.filter(q -> q.isSame(dummy))
			.findFirst();
	}


	public int getAbilityUsed(ItemStackAbilityInterface ability) {
		abilityUsed.putIfAbsent(ability, 0);
		return abilityUsed.get(ability);
	}


	public Block getTarget() {
		return target;
	}

	public Optional<Location> findTarget(Player player) {
		return new TBLTArenaMap().findUnique(player.getLocation())
			.filter(arena -> arena.isIn(target.getLocation()))
			.map(arena -> target.getLocation());
	}


	@Override
	public String toString() {
		List<String> elements = new ArrayList<String>();
		elements.addAll(quests.stream()
			.map(q -> q.toString())
			.collect(Collectors.toList()));
		return String.join(", ", elements);
	}
}

