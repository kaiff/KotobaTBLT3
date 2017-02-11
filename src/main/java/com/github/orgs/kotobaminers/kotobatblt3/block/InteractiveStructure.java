package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.block.ConfigChest;
import com.github.orgs.kotobaminers.kotobaapi.block.Interactive;
import com.github.orgs.kotobaminers.kotobaapi.block.KotobaStructure;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public enum InteractiveStructure implements KotobaStructure, ConfigChest, Interactive {
	MAGIC_MIRROR_1(new HashMap<Vector, Material>() {{
		put(new Vector(0,0,0), Material.SEA_LANTERN);
		put(new Vector(1,0,0), Material.SEA_LANTERN);
		put(new Vector(0,1,0), Material.STAINED_GLASS_PANE);
		put(new Vector(1,1,0), Material.STAINED_GLASS_PANE);
		put(new Vector(0,2,0), Material.STAINED_GLASS_PANE);
		put(new Vector(1,2,0), Material.STAINED_GLASS_PANE);
		put(new Vector(0,3,0), Material.STAINED_GLASS_PANE);
		put(new Vector(1,3,0), Material.STAINED_GLASS_PANE);
		put(new Vector(0,4,0), Material.STAINED_GLASS_PANE);
		put(new Vector(1,4,0), Material.STAINED_GLASS_PANE);
		put(new Vector(0,5,0), Material.SEA_LANTERN);
		put(new Vector(1,5,0), Material.SEA_LANTERN);
	}},
		TBLTItemStackIcon.PLAYER_GATE_KEY
	) {
		@Override
		public boolean interact(PlayerInteractEvent event) {
			List<ItemStack> options = findOptions(event.getClickedBlock().getLocation());
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
	;


	private static final Vector ORIGIN_TO_CHEST = new Vector(0,-2,0);


	private Map<Vector, Material> structure;
	private KotobaItemStackIcon key;


	private InteractiveStructure(Map<Vector, Material> structure, KotobaItemStackIcon key) {
		this.structure = structure;
		this.key = key;
	}

	@Override
	public Map<Vector, Material> getStructure() {
		return structure;
	}

	@Override
	public List<Chest> findChests(Location location) {
		return findOrigin(location).stream()
			.map(l -> l.clone().add(ORIGIN_TO_CHEST).getBlock())
			.filter(b -> b.getState() instanceof Chest)
			.map(b -> (Chest) b.getState())
			.filter(c -> Stream.of(c.getInventory().getContents()).filter(i -> i != null).anyMatch(i -> getKey().isIconItemStack(i)))
			.collect(Collectors.toList());
	}

	@Override
	public KotobaItemStackIcon getKey() {
		return key;
	}


}

