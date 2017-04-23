package com.github.orgs.kotobaminers.kotobaapi.utility;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public interface KotobaCooldown {


	int getDuration();
	Map<UUID, Date> getMap();


	default void updateCooldown(UUID uuid) {
		getMap().put(uuid, new Date());
	}


	default boolean isCooldown(UUID uuid) {
		if(getMap().containsKey(uuid)) {
			if(new Date().getTime() - getMap().get(uuid).getTime() < 50 * getDuration()) {
				return true;
			}
		}
		return false;
	}


}

