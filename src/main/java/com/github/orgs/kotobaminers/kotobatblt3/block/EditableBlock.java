package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.Material;

public enum EditableBlock {
	GLASS(Material.GLASS, 1),
	THIN_GLASS(Material.THIN_GLASS, 1),
	GRASS(Material.GRASS, 1),
	DIRT(Material.DIRT, 1),
	WOOL(Material.WOOL, 2),
	STONE(Material.STONE, 3),
	BRICK(Material.BRICK, 3),
	SMOOTH_BRICK(Material.SMOOTH_BRICK, 3),
	;

	Material material;
	int resistance;

	private EditableBlock(Material material, int blastResistance) {
		this.material = material;
		this.resistance = blastResistance;
	}

	public static Optional<EditableBlock> find(Material material) {
		return Stream.of(EditableBlock.values())
			.filter(b -> b.material == material)
			.findAny();
	}

	public int getResistance() {
		return resistance;
	}
	public Material getMaterial() {
		return material;
	}

}
