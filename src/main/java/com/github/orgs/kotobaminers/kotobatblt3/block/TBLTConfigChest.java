package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.Arrays;
import java.util.List;

import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaConfigChestInterface;

public enum TBLTConfigChest implements KotobaConfigChestInterface {


	VERTICAL(
		Arrays.asList(new Vector(0, 2, 0)),
		InteractType.BLOCK
	),

	THREE_BY_THREE(
			Arrays.asList(
					new Vector(1, 2, 1),
					new Vector(1, 2, 0),
					new Vector(1, 2, -1),
					new Vector(0, 2, 1),
					new Vector(0, 2, 0),
					new Vector(0, 2, -1),
					new Vector(-1, 2, 1),
					new Vector(-1, 2, 0),
					new Vector(-1, 2, -1)
			),
			InteractType.AIR
		),

	TWO_BY_SEVEN(
			Arrays.asList(
					new Vector(0,2,0),
					new Vector(1,2,0),
					new Vector(0,3,0),
					new Vector(1,3,0),
					new Vector(0,4,0),
					new Vector(1,4,0),
					new Vector(0,5,0),
					new Vector(1,5,0),
					new Vector(0,6,0),
					new Vector(1,6,0),
					new Vector(0,7,0),
					new Vector(1,7,0),
					new Vector(0,8,0),
					new Vector(1,8,0)
			),
			InteractType.BLOCK
		),

	;


	private List<Vector> positions;
	private InteractType type;


	private TBLTConfigChest(List<Vector> positions, InteractType type) {
		this.positions = positions;
		this.type = type;
	}


	@Override
	public List<Vector> getPositions() {
		return positions;
	}

	@Override
	public InteractType getTargetType() {
		return type;
	}


}

