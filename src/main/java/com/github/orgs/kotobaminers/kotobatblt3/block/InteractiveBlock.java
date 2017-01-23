package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobatblt3.utility.Utility;

public enum InteractiveBlock {
	COIN(Material.GOLD_BLOCK) {

		@Override
		boolean interact(PlayerInteractEvent event) {
			if(Utility.isTBLTPlayer(event.getPlayer())) {
				Block block = event.getClickedBlock();
				block.setType(Material.AIR);
				Location location = block.getLocation();
				KotobaEffect.dropItemEffect(new ItemStack(Material.GOLD_INGOT), location);
				KotobaEffect.BREAK_BLOCK_MIDIUM.playEffect(location);
				KotobaEffect.BREAK_BLOCK_MIDIUM.playSound(location);
				return true;
			}
			return false;
		}

	},

	;

	private Material material;

	InteractiveBlock(Material material) {
		this.material = material;
	}

	abstract boolean interact(PlayerInteractEvent event);


	public static List<InteractiveBlock> find(Block block) {
		if(block == null) return new ArrayList<InteractiveBlock>();
		return Stream.of(InteractiveBlock.values())
			.filter(i -> i.isThis(block))
			.collect(Collectors.toList());
	}


	private boolean isThis(Block block) {
		if(block.getType() == material) {
			return true;
		}
		return false;
	}


}

