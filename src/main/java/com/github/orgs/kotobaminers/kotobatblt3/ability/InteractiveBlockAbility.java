package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.Arrays;
import java.util.List;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.orgs.kotobaminers.kotobaapi.ability.ClickBlockAbilityInterface;
import com.github.orgs.kotobaminers.kotobaapi.block.Placeble;
import com.github.orgs.kotobaminers.kotobaapi.block.Takable;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public enum InteractiveBlockAbility implements ClickBlockAbilityInterface, Placeble, Takable {
	GREEN_GEM(TBLTItemStackIcon.GREEN_GEM, 1, Arrays.asList(Action.RIGHT_CLICK_BLOCK)) {
		@Override
		public boolean perform(PlayerInteractEvent event) {
			return false;
		}
		@Override
		public boolean canPlace() {
			return false;
		}
		@Override
		public boolean place() {
			return false;
		}
		@Override
		public boolean canTake() {
			return false;
		}
		@Override
		public boolean take() {
			return false;
		}
	}

	;


	KotobaItemStackIcon icon;
	int consumption;
	List<Action> triggers;


	private InteractiveBlockAbility(KotobaItemStackIcon icon, int consumption, List<Action> triggers) {
		this.icon = icon;
		this.consumption = consumption;
		this.triggers = triggers;
	}


	@Override
	public KotobaItemStackIcon getIcon() {
		return icon;
	}

	@Override
	public int getConsumption() {
		return consumption;
	}

	@Override
	public List<Action> getTriggers() {
		return null;
	}


}

