package com.github.orgs.kotobaminers.kotobaapi.block;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerPortalEvent;

public interface KotobaPortalInterface {


	boolean enterPortal(PlayerPortalEvent event);


	boolean isThis(Location location);



}

