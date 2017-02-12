package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.Arrays;
import java.util.List;

import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.block.InteractiveChestFinder;

public enum TBLTInteractiveChestType implements InteractiveChestFinder {


	VERTICAL(
			Arrays.asList(new Vector(0, 2, 0)),
			InteractType.BLOCK,
			false
		),


	BASE(
			Arrays.asList(new Vector(0, 4, 0)),
			InteractType.AIR,
			false
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
			InteractType.AIR,
			false
		),


	TWO_BY_SIX(
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
			InteractType.BLOCK,
			true
		),
	;


	private List<Vector> positions;
	private InteractType type;
	private boolean hasRotations;


	private TBLTInteractiveChestType(List<Vector> positions, InteractType type, boolean hasRotations) {
		this.positions = positions;
		this.type = type;
		this.hasRotations = hasRotations;
	}


	@Override
	public List<Vector> getPositions() {
		return positions;
	}

	@Override
	public InteractType getTargetType() {
		return type;
	}


	@Override
	public boolean hasRotations() {
		return hasRotations;
	}


}

