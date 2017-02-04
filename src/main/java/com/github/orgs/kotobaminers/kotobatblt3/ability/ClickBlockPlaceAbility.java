package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.orgs.kotobaminers.kotobaapi.block.Placeble;
import com.github.orgs.kotobaminers.kotobaapi.block.Takable;

public enum ClickBlockPlaceAbility implements ClickBlockAbilityInterface, Placeble, Takable {

	GREEN_GEM() {

	}

	;

	@Override
	public Material getMaterial() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public short getData() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public String getName() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public List<String> getLore() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public int getConsumption() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public boolean canTake() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean take() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean canPlace() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean place() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public List<Action> getTriggers() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public boolean perform(PlayerInteractEvent event) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

}
