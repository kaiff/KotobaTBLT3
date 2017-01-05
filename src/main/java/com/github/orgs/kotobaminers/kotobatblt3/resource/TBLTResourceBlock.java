package com.github.orgs.kotobaminers.kotobatblt3.resource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;

public enum TBLTResourceBlock {
	LEAVES(Material.LEAVES, TBLTResource.LIFE_MANA, 1),
	RED_ROSE(Material.RED_ROSE, TBLTResource.LIFE_MANA, 1),
	WATER(Material.WATER, TBLTResource.WATER_MANA, 1),
	STATIONARY_WATER(Material.STATIONARY_WATER, TBLTResource.WATER_MANA, 1),
	IRON_ORE(Material.IRON_ORE, TBLTResource.METAL_MANA, 1),
	TORCH(Material.TORCH, TBLTResource.FLAME_MANA, 1),
	MAGIC_ORE(Material.EMERALD_ORE, TBLTResource.MAGIC_MANA, 1),
	;

	private Material material;
	private TBLTResource resource;
	private int amount;

	private TBLTResourceBlock(Material material, TBLTResource resource, int amount) {
		this.material = material;
		this.resource = resource;
		this.amount = amount;
	}

	public void siphon(Block block) {
		block.setType(Material.OBSIDIAN);
		Location location = block.getLocation().clone().add(0.5, 0.5, 0.5);
		KotobaEffect.MAGIC_MIDIUM.playEffect(location);
		KotobaEffect.MAGIC_MIDIUM.playSound(location);
	}
	public void extract(Block block) {
		block.setType(Material.AIR);
		Location location = block.getLocation().clone().add(0.5, 0.5, 0.5);
		KotobaEffect.BREAK_BLOCK_MIDIUM.playEffect(location);
		KotobaEffect.BREAK_BLOCK_MIDIUM.playSound(location);
	}
	public ItemStack createResource() {
		return resource.create(amount);
	}

	public static Optional<TBLTResourceBlock> find(Material material) {
		List<TBLTResourceBlock> blocks = Stream.of(TBLTResourceBlock.values())
			.filter(r -> r.getMaterial() == material)
			.collect(Collectors.toList());
		if(blocks.size() == 1) {
			return Optional.of(blocks.get(0));
		}
		return Optional.empty();
	}

	private Material getMaterial() {
		return material;
	}
	public TBLTResource getResource() {
		return resource;
	}
	public int getAmount() {
		return amount;
	}
}
