package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.block.InteractiveChest;
import com.github.orgs.kotobaminers.kotobaapi.block.KotobaConfigChestInterface;
import com.github.orgs.kotobaminers.kotobaapi.block.KotobaStructure;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public enum InteractiveStructure implements KotobaStructure, InteractiveChest {
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
		TBLTConfigChest.TWO_BY_SEVEN
	) {
		@Override
		public boolean interact(PlayerInteractEvent event) {
			if(0 == findOrigins(event.getClickedBlock().getLocation()).size()) return false;
			List<ItemStack> options = findOptions(event);
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


	private KotobaItemStackIcon icon;
	private Map<Vector, Material> structure;
	private TBLTConfigChest chest;


	private InteractiveStructure(KotobaItemStackIcon icon, Map<Vector, Material> structure, TBLTConfigChest chest) {
		this.icon = icon;
		this.structure = structure;
		this.chest = chest;
	}


	@Override
	public Map<Vector, Material> getStructure() {
		return structure;
	}


	@Override
	public KotobaConfigChestInterface getChest() {
		return chest;
	}

	@Override
	public KotobaItemStackIcon getIcon() {
		return icon;
	}


}

