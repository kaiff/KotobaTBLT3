package com.github.orgs.kotobaminers.kotobatblt3.citizens;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Ocelot.Type;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.citizens.CitizensManager;
import com.github.orgs.kotobaminers.kotobaapi.citizens.UniqueNPCInterface;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobatblt3.database.SentenceDatabase;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;

public enum UniqueNPC implements UniqueNPCInterface {


	CAT(EntityType.OCELOT, "Chiro") {
		@Override
		public void setStatus(NPC npc) {
			LookClose lookClose = new LookClose();
			lookClose.lookClose(true);
			npc.addTrait(lookClose);

			if(npc.getEntity() instanceof Ocelot) {
				Ocelot ocelot = (Ocelot) npc.getEntity();
				ocelot.setCatType(Type.BLACK_CAT);
			}
		}
	},


	SLIME(EntityType.SLIME, "Sticky") {
		@Override
		public void setStatus(NPC npc) {
			if(npc.getEntity() instanceof Slime) {
				Slime slime = (Slime) npc.getEntity();
				slime.setSize(1);
			}
			LookClose lookClose = new LookClose();
			lookClose.lookClose(true);
			npc.addTrait(lookClose);
		}
	},

	;


	private EntityType type;
	private String name;


	private UniqueNPC(EntityType type, String name) {
		this.type = type;
		this.name = name;
	}


	@Override
	public void become(NPC npc) {
		npc.setBukkitEntityType(type);
		npc.setName(name);
		setStatus(npc);
	}

	@Override
	public void spawn(int id) {
		Optional<NPC> optional = CitizensManager.findNPC(id);
		if(optional.isPresent()) {
			NPC npc = optional.get();
			if(npc.getEntity() != null) return;//#NPC.isSpawn() is not reliable for some reason

			if(isThisNPC(npc)) {
				npc.spawn(npc.getStoredLocation());
				setStatus(npc);
				playSpawnEffect(npc.getStoredLocation());
			}
		}
		return;
	}

	@Override
	public void despawnAll() {
		new SentenceDatabase().getNPCIds().stream()
			.map(id -> CitizensManager.findNPC(id))
			.filter(optional -> optional.isPresent())
			.map(optional -> optional.get())
			.filter(npc -> npc.getEntity() != null)
			.filter(npc -> isThisNPC(npc))
			.forEach(npc -> {
				npc.despawn();
				playDespawnEffect(npc.getStoredLocation());
			});
	}

	@Override
	public void playSpawnEffect(Location location) {
		KotobaEffect.ENDER_SIGNAL.playEffect(location);
		Stream.iterate(0, i -> i)
			.limit(10)
			.forEach(i -> location.getWorld().playEffect(location, Effect.MOBSPAWNER_FLAMES, 0));
		KotobaEffect.ENDER_SIGNAL.playSound(location);
	}

	@Override
	public void playDespawnEffect(Location location) {
		KotobaEffect.ENDER_SIGNAL.playEffect(location);
		Stream.iterate(0, i -> i)
			.limit(10)
			.forEach(i -> location.getWorld().playEffect(location, Effect.MOBSPAWNER_FLAMES, 0));
		KotobaEffect.ENDER_SIGNAL.playSound(location);
	}


	@Override
	public EntityType getType() {
		return type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemStack createKey(int id) {
		return KotobaItemStack.create(Material.MONSTER_EGG, (short) 0, 1, "Servant key", Arrays.asList(name(), Integer.toString(id)));
	}

	@Override
	public Optional<NPC> findNPCByKey(ItemStack item) {
		ItemStack key = createKey(0);
		if(key.getType() == item.getType() &&
			key.getDurability() == item.getDurability() &&
			key.getItemMeta().getDisplayName().equalsIgnoreCase(item.getItemMeta().getDisplayName())) {

			List<String> lore = item.getItemMeta().getLore();
			if(lore != null) {
				if(1 < lore.size()) {
					if(!name().equalsIgnoreCase(lore.get(0))) return Optional.empty();
					try {
						int id = Integer.parseInt(lore.get(1));
						return CitizensManager.findNPC(id);
					} catch(NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return Optional.empty();
	}

}

