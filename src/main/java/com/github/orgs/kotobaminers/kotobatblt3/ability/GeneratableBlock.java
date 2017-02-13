package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTUtility;

public enum GeneratableBlock {
	BLOCK(
		new HashMap<Vector, Material>() {{
			put(new Vector(0,0,0), Material.WOOL);
		}},
		new Vector(0,0,0)
			) {
		@Override
		void perform(PlayerInteractEvent event) {
		}
	},

	JUMPER(
		new HashMap<Vector, Material>() {{
			put(new Vector(0,1,0), Material.GOLD_PLATE);
			put(new Vector(0,0,0), Material.GOLD_BLOCK);
		}},
		new Vector(0,1,0)
	) {
		@Override
		void perform(PlayerInteractEvent event) {
			Player player = event.getPlayer();
			player.setVelocity(new Vector(0, 0.8, 0));
			TBLTUtility.playJumpEffect(player);
		}
	},

	WEB(
		new HashMap<Vector, Material>() {{
			put(new Vector(0,0,0), Material.WEB);
		}},
		new Vector(0,0,0)
	) {
		@Override
		void perform(PlayerInteractEvent event) {
		}
	},

	;
	private Map<Vector ,Material> blocks = new HashMap<>();
	private Vector trigger;
	private GeneratableBlock(HashMap<Vector, Material> blocks, Vector trigger) {
		this.blocks = blocks;
		this.trigger = trigger;
	}
	boolean canGenerate(Location base) {
		return blocks.keySet().stream()
			.map(v -> base.clone().add(v))
			.allMatch(l -> base.getWorld().getBlockAt(l).getType() == Material.AIR);
	}
	public void generate(Location base) {
		if(!canGenerate(base)) return;
		blocks.entrySet().stream()
			.forEach(e -> base.getWorld().getBlockAt(base.clone().add(e.getKey())).setType(e.getValue()));
	}
	public void delete(Location base, int second) {
		Setting.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Setting.getPlugin(), new Runnable() {
			@Override
			public void run() {
				blocks.keySet().stream()
					.map(v -> base.clone().add(v))
					.forEach(l -> {
						l.getWorld().getBlockAt(l).setType(Material.AIR);
						l.getWorld().playEffect(l, Effect.POTION_BREAK, 0);
					});
			}
		}, second * 20L);
	}
	public static Optional<GeneratableBlock> find(Location base) {
		return Stream.of(GeneratableBlock.values())
			.filter(b -> b.blocks.entrySet().stream().allMatch(e -> base.getWorld().getBlockAt(base.clone().subtract(b.trigger).add(e.getKey())).getType() == e.getValue()))
			.findAny();
	}
	abstract void perform(PlayerInteractEvent event);
}


