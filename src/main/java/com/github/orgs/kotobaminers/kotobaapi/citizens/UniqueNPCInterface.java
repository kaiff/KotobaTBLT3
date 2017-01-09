package com.github.orgs.kotobaminers.kotobaapi.citizens;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobatblt3.database.SentenceDatabase;

import net.citizensnpcs.api.npc.NPC;

public interface UniqueNPCInterface {


	EntityType getType();
	String getName();


	abstract void become(NPC npc);
	abstract void despawnAll();
	abstract void spawn(int id);
	abstract void playDespawnEffect(Location location);
	abstract void playSpawnEffect(Location location);
	abstract ItemStack createKey(int id);
	abstract Optional<NPC> findNPCByKey(ItemStack key);


	abstract void setStatus(NPC npc);


	default boolean isThisNPC(NPC npc) {
		if(!npc.getName().equalsIgnoreCase(getName())) return false;

		EntityType type;
		if(npc.getEntity() == null) {
			npc.spawn(npc.getStoredLocation());
			type = npc.getEntity().getType();
			npc.despawn();
		} else {
			type = npc.getEntity().getType();
		}
		return type == getType();
	}


	default List<NPC> getNPCs() {
		return new SentenceDatabase().getNPCIds().stream()
			.map(id -> CitizensManager.findNPC(id))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.filter(this::isThisNPC)
			.collect(Collectors.toList());
	}


}

