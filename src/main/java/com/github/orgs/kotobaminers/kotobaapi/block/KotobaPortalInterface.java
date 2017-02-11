package com.github.orgs.kotobaminers.kotobaapi.block;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.util.Vector;

public interface KotobaPortalInterface {


	List<Vector> getPositions();
	Material getPortal();
	List<Location> findCenters(Location location);


	default boolean tryOpen(Location center) {
		if(canOpen(center)) {
			successOpen(center);
			return true;
		} else {
			failOpen(center);
		}
		return false;
	}


	boolean canOpen(Location center);


	void successOpen(Location center);


	void failOpen(Location center);


	boolean enterPortal(PlayerPortalEvent event);


}

