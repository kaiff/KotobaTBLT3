package com.github.orgs.kotobaminers.kotobaapi.block;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.util.Vector;


public interface KotobaStructure {


	Map<Vector, Material> getStructure();
	boolean hasRotations();

}

