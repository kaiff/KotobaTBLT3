package com.github.orgs.kotobaminers.kotobatblt3.utility;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaCooldown;

public enum TBLTCooldown implements KotobaCooldown {
	INTERACTIVE(1)
	;


	private Map<UUID, Date> map = new HashMap<>();
	private int duration;


	private TBLTCooldown(int duration) {
		this.duration = duration;
	}



	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public Map<UUID, Date> getMap() {
		return map;
	}

}
