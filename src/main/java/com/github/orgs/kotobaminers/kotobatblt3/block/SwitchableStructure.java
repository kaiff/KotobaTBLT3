package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockData;
import com.github.orgs.kotobaminers.kotobaapi.block.KotobaStructure;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public enum SwitchableStructure implements KotobaStructure, SwitchableChest {
	GATE_1x3(
		TBLTItemStackIcon.SWITCHABLE_GATE_1x3,
		new HashMap<Vector, Material>() {{
			put(new Vector(0,2,0), Material.IRON_FENCE);
			put(new Vector(0,3,0), Material.IRON_FENCE);
			put(new Vector(0,4,0), Material.IRON_FENCE);
		}},
		false
	),
	;


	private TBLTItemStackIcon icon;
	private Map<Vector, Material> structure;
	private boolean hasRotations;


	private SwitchableStructure(TBLTItemStackIcon icon, Map<Vector, Material> structure, boolean hasRotations) {
		this.icon = icon;
		this.structure = structure;
		this.hasRotations = hasRotations;
	}


	private void playEffect(Location location) {
		KotobaEffect.MAGIC_MIDIUM.playEffect(location);
	}
	private void playSound(Location location) {
		KotobaEffect.MAGIC_MIDIUM.playSound(location);
	}


	private void perform(Chest chest, boolean turnOn) {
		playSound(chest.getLocation());
		if(hasReverse(chest)) {
			turnOn = !turnOn;
		}

		if(turnOn) {
			placeBlock(chest.getLocation());
		} else {
			placeAir(chest.getLocation());
		}
	}

	private void placeBlock(Location origin) {
		createBlockData(origin).forEach(d -> {
			d.placeBlock();
			playEffect(d.getLocation());
		});
	}

	private void placeAir(Location origin) {
		createBlockData(origin).forEach(d -> {
			d.getLocation().getBlock().setType(Material.AIR);
			playEffect(d.getLocation());
		});
	}


	private List<KotobaBlockData> createBlockData(Location origin) {
		return structure.entrySet().stream()
			.map(e -> new KotobaBlockData(origin.clone().add(e.getKey()), e.getValue(), 0))
			.collect(Collectors.toList());
	}



	@Override
	public void turnOn(Chest chest) {
		perform(chest, true);
	}

	@Override
	public void turnOff(Chest chest) {
		perform(chest, false);
	}


	public TBLTItemStackIcon getIcon() {
		return icon;
	}

	@Override
	public Map<Vector, Material> getStructure() {
		return structure;
	}

	@Override
	public boolean hasRotations() {
		return hasRotations;
	}


}

