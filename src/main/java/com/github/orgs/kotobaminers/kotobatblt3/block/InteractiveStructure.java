package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockData;
import com.github.orgs.kotobaminers.kotobaapi.block.KotobaStructure;
import com.github.orgs.kotobaminers.kotobaapi.block.PlayerBlockInteractive;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaStructureUtility;
import com.github.orgs.kotobaminers.kotobatblt3.game.TBLTData;
import com.github.orgs.kotobaminers.kotobatblt3.gui.TBLTPlayerGUI;
import com.github.orgs.kotobaminers.kotobatblt3.utility.RepeatingEffect;
import com.github.orgs.kotobaminers.kotobatblt3.utility.RepeatingEffectHolder;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public enum InteractiveStructure implements KotobaStructure, PlayerBlockInteractive, RepeatingEffectHolder {


	MAGIC_MIRROR(
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
				boolean isTarget = Stream.of(player.getInventory().getContents())
					.filter(i -> i != null)
					.anyMatch(i -> options.stream().anyMatch(o -> o.isSimilar(i)));
				if(isTarget && event.getClickedBlock().getType() == Material.STAINED_GLASS_PANE) {
					Block opposite = event.getClickedBlock().getRelative(event.getBlockFace().getOppositeFace());
					if(opposite.getType() == Material.AIR) {
						Location from = player.getLocation();
						Location to = opposite.getLocation().clone().add(0.5,0,0.5);
						Vector direction = from.clone().subtract(to).toVector();
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
							Vector direction = from.clone().subtract(to).toVector();
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
			TBLTItemStackIcon.CHOOS_ONE_KEY,
			new HashMap<Vector, Material>() {{
				put(new Vector(0,2,0), Material.ENCHANTMENT_TABLE);
			}},
			TBLTInteractiveChestType.VERTICAL,
			false
		) {
			@Override
			public boolean interact(PlayerInteractEvent event) {
				TBLTPlayerGUI.ITEM_EXCHANGER.create(getChest().findOptions(event.getClickedBlock().getLocation(), Arrays.asList(TBLTItemStackIcon.CHOOS_ONE_KEY)))
					.ifPresent(i -> {
						TBLTData.getOrDefault(event.getPlayer().getUniqueId()).target(event.getClickedBlock());
						event.getPlayer().openInventory(i);
					});
				return true;
			}
		}
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
		return 0 < getChest().findChests(event, getIcon()).size();
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

