package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.orgs.kotobaminers.kotobaapi.block.PlayerBlockInteractive;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaPriorityValue;
import com.github.orgs.kotobaminers.kotobatblt3.ability.TBLTGem;

public enum InteractiveBlock implements PlayerBlockInteractive {


	GREEN_GEM(Material.EMERALD_BLOCK) {
		@Override
		public boolean interact(PlayerInteractEvent event) {
			return TBLTGem.GREEN_GEM.take(event.getPlayer(), event.getClickedBlock());
		}

	},


	BLUE_GEM(Material.DIAMOND_BLOCK) {
		@Override
		public boolean interact(PlayerInteractEvent event) {
			return TBLTGem.BLUE_GEM.take(event.getPlayer(), event.getClickedBlock());
		}
	},


	RED_GEM(Material.REDSTONE_BLOCK) {
		@Override
		public boolean interact(PlayerInteractEvent event) {
			return TBLTGem.RED_GEM.take(event.getPlayer(), event.getClickedBlock());
		}
	},


	;


	private Material material;


	private InteractiveBlock(Material material) {
		this.material = material;
	}


	@Override
	public boolean isSame(PlayerInteractEvent event) {
		return Optional.of(event)
			.map(e -> e.getClickedBlock())
			.filter(b -> b != null)
			.map(b -> b.getType() == this.material)
			.orElse(false);
	}


	@Override
	public KotobaPriorityValue getPriority() {
		return KotobaPriorityValue.HIGH;
	}


}

