package com.github.orgs.kotobaminers.kotobatblt3.block;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerPortalEvent;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaPortalInterface;

public enum SimplePortal implements KotobaPortalInterface {
	BOTTOM_PORTAL() {

		@Override
		public boolean enterPortal(PlayerPortalEvent event) {
			new TBLTArenaMap().findUnique(event.getFrom())
				.map(arena -> (TBLTArena) arena)
				.ifPresent(arena -> Bukkit.getOnlinePlayers().stream().filter(p -> arena.isIn(p.getLocation()) && p.getGameMode() == GameMode.ADVENTURE).forEach(p -> arena.continueFromCurrent(p)));
			return true;
		}


		@Override
		public boolean isThis(Location location) {
			return new TBLTArenaMap().findUnique(location)
				.map(arena -> (TBLTArena) arena)
				.filter(arena -> arena.getYMin() == location.getY())
				.isPresent();
		}
	},
	;





}

