package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockData;
import com.github.orgs.kotobaminers.kotobaapi.block.KotobaStructure;
import com.github.orgs.kotobaminers.kotobaapi.block.PlayerBlockInteractive;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaStructureUtility;
import com.github.orgs.kotobaminers.kotobatblt3.citizens.UniqueNPC;
import com.github.orgs.kotobaminers.kotobatblt3.database.TBLTData;
import com.github.orgs.kotobaminers.kotobatblt3.userinterface.TBLTPlayerGUI;
import com.github.orgs.kotobaminers.kotobatblt3.utility.RepeatingEffect;
import com.github.orgs.kotobaminers.kotobatblt3.utility.RepeatingEffectHolder;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

import net.citizensnpcs.api.npc.NPC;

public enum InteractiveStructure implements KotobaStructure, PlayerBlockInteractive, RepeatingEffectHolder {


	ITEM_GATE(
		TBLTItemStackIcon.PLAYER_GATE_KEY,
		new HashMap<Vector, Material>() {{
			put(new Vector(0,2,0), Material.SEA_LANTERN);
			put(new Vector(1,2,0), Material.SEA_LANTERN);
			put(new Vector(0,3,0), Material.STAINED_GLASS_PANE);
			put(new Vector(1,3,0), Material.STAINED_GLASS_PANE);
			put(new Vector(0,4,0), Material.STAINED_GLASS_PANE);
			put(new Vector(1,4,0), Material.STAINED_GLASS_PANE);
			put(new Vector(0,5,0), Material.STAINED_GLASS_PANE);
			put(new Vector(1,5,0), Material.STAINED_GLASS_PANE);
			put(new Vector(0,6,0), Material.STAINED_GLASS_PANE);
			put(new Vector(1,6,0), Material.STAINED_GLASS_PANE);
			put(new Vector(0,7,0), Material.SEA_LANTERN);
			put(new Vector(1,7,0), Material.SEA_LANTERN);
		}},
		TBLTInteractiveChestType.TWO_BY_SIX,
		true
	) {
		@Override
		public boolean interact(PlayerInteractEvent event) {
			if(0 == KotobaStructureUtility.findOrigins(getStructure(), event.getClickedBlock().getLocation(), hasRotations()).size()) return false;
			List<ItemStack> options = getChest().findOptions(event, Arrays.asList(getIcon()));
			if(0 < options.size()) {
				Player player = event.getPlayer();
				boolean checkItem = checkItem(options, player.getInventory());
				if(checkItem && event.getClickedBlock().getType() == Material.STAINED_GLASS_PANE) {
					Block opposite = event.getClickedBlock().getRelative(event.getBlockFace().getOppositeFace());
					if(opposite.getType() == Material.AIR) {
						Location from = player.getLocation();
						Location to = opposite.getLocation().clone().add(0.5,0,0.5);
						Vector direction = from.getDirection();
						to.setDirection(direction);
						player.teleport(to);
						KotobaEffect.ENDER_SIGNAL.playEffect(from);
						KotobaEffect.ENDER_SIGNAL.playEffect(to);
						to.getWorld().playSound(to, Sound.PORTAL_TRIGGER, 1, 1);
					}
				}
				return true;
			}
			return false;
		}

		private boolean checkItem(List<ItemStack> targets, Inventory playerInventory) {
			if(0 < targets.size()) {
				List<ItemStack> stuff = Stream.of(playerInventory.getContents()).filter(i -> i != null).collect(Collectors.toList());
				if(targets.stream().anyMatch(i -> TBLTItemStackIcon.ANY_MATCH.isIconItemStack(i))) {
					return targets.stream()
						.filter(t -> !TBLTItemStackIcon.ANY_MATCH.isIconItemStack(t))
						.anyMatch(t -> stuff.stream().anyMatch(s -> s.isSimilar(t)));
				} else {
					return targets.stream()
						.allMatch(t -> stuff.stream().anyMatch(s -> s.isSimilar(t) && s.getAmount() >= t.getAmount()));
				}

			}


			return false;
		}

	},


	ONE_TIME_GATE(
			TBLTItemStackIcon.ONE_TIME_GATE_KEY,
			new HashMap<Vector, Material>() {{
				put(new Vector(0,2,0), Material.SEA_LANTERN);
				put(new Vector(1,2,0), Material.SEA_LANTERN);
				put(new Vector(0,3,0), Material.STAINED_GLASS_PANE);
				put(new Vector(1,3,0), Material.STAINED_GLASS_PANE);
				put(new Vector(0,4,0), Material.STAINED_GLASS_PANE);
				put(new Vector(1,4,0), Material.STAINED_GLASS_PANE);
				put(new Vector(0,5,0), Material.STAINED_GLASS_PANE);
				put(new Vector(1,5,0), Material.STAINED_GLASS_PANE);
				put(new Vector(0,6,0), Material.STAINED_GLASS_PANE);
				put(new Vector(1,6,0), Material.STAINED_GLASS_PANE);
				put(new Vector(0,7,0), Material.SEA_LANTERN);
				put(new Vector(1,7,0), Material.SEA_LANTERN);
			}},
			TBLTInteractiveChestType.TWO_BY_SIX,
			true
		) {
			@Override
			public boolean interact(PlayerInteractEvent event) {
				if(0 == KotobaStructureUtility.findOrigins(getStructure(), event.getClickedBlock().getLocation(), hasRotations()).size()) return false;
					if(event.getClickedBlock().getType() == Material.STAINED_GLASS_PANE) {
						Block opposite = event.getClickedBlock().getRelative(event.getBlockFace().getOppositeFace());
						if(opposite.getType() == Material.AIR) {
							Player player = event.getPlayer();
							Location from = player.getLocation();
							Location to = opposite.getLocation().clone().add(0.5,0,0.5);
							Vector direction = from.getDirection();
							to.setDirection(direction);
							player.teleport(to);
							KotobaEffect.ENDER_SIGNAL.playEffect(from);
							KotobaEffect.ENDER_SIGNAL.playEffect(to);
							to.getWorld().playSound(to, Sound.PORTAL_TRIGGER, 1, 1);
							KotobaStructureUtility.findExistings(getStructure(), event.getClickedBlock().getLocation(), hasRotations())
								.forEach(structure -> structure.stream().filter(b -> b.getType() == Material.STAINED_GLASS_PANE).forEach(b -> new KotobaBlockData(b.getLocation(), Material.OBSIDIAN, 0).placeBlock()));
						}
					return true;
				}
				return false;
			}
		},


	CHOOSE_ONE(
			TBLTItemStackIcon.CHOOSE_ONE_KEY,
			new HashMap<Vector, Material>() {{
				put(new Vector(0,2,0), Material.ENCHANTMENT_TABLE);
			}},
			TBLTInteractiveChestType.VERTICAL,
			false
		) {
			@Override
			public boolean interact(PlayerInteractEvent event) {
				TBLTPlayerGUI.ITEM_EXCHANGER.create(getChest().findOptions(event.getClickedBlock().getLocation(), Arrays.asList(TBLTItemStackIcon.CHOOSE_ONE_KEY)))
					.ifPresent(i -> {
						TBLTData.getOrDefault(event.getPlayer().getUniqueId()).target(event.getClickedBlock());
						event.getPlayer().openInventory(i);
					});
				return true;
			}
		},


	SUMMON_CAT(
			TBLTItemStackIcon.SUMMON_SERVENT_KEY,
			new HashMap<Vector, Material>() {{
				put(new Vector(0,2,0), Material.PUMPKIN);
			}},
			TBLTInteractiveChestType.VERTICAL,
			false
		) {

			private List<UniqueNPC> uniqueNPCs = Arrays.asList(UniqueNPC.CAT_1, UniqueNPC.CAT_2);

			private boolean checkJob(Player player) {
				return true;//TODO
			}

			@Override
			public boolean interact(PlayerInteractEvent event) {
				if(!checkJob(event.getPlayer())) return false;
				Block block = event.getClickedBlock();
				Long summoned = new TBLTArenaMap().findUnique(block.getLocation())
					.map(arena ->
						uniqueNPCs.stream().map(u -> {
							u.getNPCs().stream()
								.filter(n -> arena.isIn(n.getStoredLocation()))
								.forEach(n -> u.despawn(n.getId()));

							List<NPC> npcs = getChest().findOptions(event, Arrays.asList(getIcon())).stream()
								.flatMap(item -> uniqueNPCs.stream().map(n -> n.findNPCByKey(item)))
								.filter(npc -> npc.isPresent())
								.map(Optional::get)
								.collect(Collectors.toList());
							Stream.iterate(0, i -> i + 1)
								.limit(npcs.size())
								.forEach(i -> u.spawn(npcs.get(i).getId(), block.getLocation().clone().add(0.5 + i, 1.5, 0.5)));
								return npcs.size();
						}).count()
					).orElse(0L);
				return 0 < summoned;
			}
		},


	SUMMON_SLIME(
			TBLTItemStackIcon.SUMMON_SERVENT_KEY,
			new HashMap<Vector, Material>() {{
				put(new Vector(0,2,0), Material.MELON_BLOCK);
			}},
			TBLTInteractiveChestType.VERTICAL,
			false
		) {
			private List<UniqueNPC> uniqueNPCs = Arrays.asList(UniqueNPC.SLIME_1, UniqueNPC.SLIME_2);

			private boolean checkJob(Player player) {
				return true;//TODO
			}

			@Override
			public boolean interact(PlayerInteractEvent event) {
				if(!checkJob(event.getPlayer())) return false;
				Block block = event.getClickedBlock();
				Long summoned = new TBLTArenaMap().findUnique(block.getLocation())
					.map(arena ->
						uniqueNPCs.stream().map(u -> {
							u.getNPCs().stream()
								.filter(n -> arena.isIn(n.getStoredLocation()))
								.forEach(n -> u.despawn(n.getId()));

							List<NPC> npcs = getChest().findOptions(event, Arrays.asList(getIcon())).stream()
								.flatMap(item -> uniqueNPCs.stream().map(n -> n.findNPCByKey(item)))
								.filter(npc -> npc.isPresent())
								.map(Optional::get)
								.collect(Collectors.toList());
							Stream.iterate(0, i -> i + 1)
								.limit(npcs.size())
								.forEach(i -> u.spawn(npcs.get(i).getId(), block.getLocation().clone().add(0.5 + i, 1.5, 0.5)));
								return npcs.size();
						}).count()
					).orElse(0L);
				return 0 < summoned;
			}
		},


	;


	private KotobaItemStackIcon icon;
	private Map<Vector, Material> structure;
	private TBLTInteractiveChestType chest;
	private boolean hasRotaions;

	private InteractiveStructure(KotobaItemStackIcon icon, Map<Vector, Material> structure, TBLTInteractiveChestType chest, boolean hasRotaions) {
		this.icon = icon;
		this.structure = structure;
		this.chest = chest;
		this.hasRotaions = hasRotaions;
	}


	@Override
	public KotobaItemStackIcon getIcon() {
		return icon;
	}


	@Override
	public Map<Vector, Material> getStructure() {
		return structure;
	}

	public TBLTInteractiveChestType getChest() {
		return chest;
	}


	@Override
	public boolean isSame(PlayerInteractEvent event) {
		return getChest().getTargetType().getTargetBlock(event)
			.filter(b -> getStructure().values().contains(b.getType()))
			.map(b -> 0 < getChest().findChests(event, getIcon()).size())
			.orElse(false);
	}


	@Override
	public int getPeriod() {
		return 5;
	}
	@Override
	public KotobaEffect getEffect() {
		return KotobaEffect.TWINCLE_SMALL;
	}
	@Override
	public KotobaEffect getSound() {
		return KotobaEffect.MUTE;
	}


	@Override
	public List<RepeatingEffect> createPeriodicEffects(Location origin) {
		return KotobaStructureUtility.findExistings(getStructure(), origin.clone().add(getChest().getPositions().get(0)), hasRotaions).stream()
			.flatMap(list -> list.stream().map(b -> RepeatingEffect.create(getPeriod(), getEffect(), getSound(), true, b.getLocation())))
			.collect(Collectors.toList());
	}


	@Override
	public boolean hasRotations() {
		return hasRotaions;
	}


}

