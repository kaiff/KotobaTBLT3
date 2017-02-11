package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.orgs.kotobaminers.kotobaapi.ability.ClickBlockAbilityInterface;
import com.github.orgs.kotobaminers.kotobaapi.block.Interactive;
import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockData;
import com.github.orgs.kotobaminers.kotobaapi.block.Placeble;
import com.github.orgs.kotobaminers.kotobaapi.block.Takable;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.block.ChestPortal;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public enum InteractiveBlockAbility implements ClickBlockAbilityInterface, Placeble, Takable, Interactive {
	GREEN_GEM(TBLTItemStackIcon.GREEN_GEM, 1, Arrays.asList(Action.RIGHT_CLICK_BLOCK)) {

		@Override
		public boolean perform(PlayerInteractEvent event) {
			Block block = event.getClickedBlock().getRelative(event.getBlockFace());
			if(!canPlace(block)) return false;
			place(getBlockData(block.getLocation()));
			ChestPortal.GEM_PORTAL.findChests(block.getLocation()).stream()
				.filter(c -> ChestReader.checkPattern(c, ClickBlockChestAbility.POSITION_TO_BLOCK))
				.map(c -> c.getLocation().add(ClickBlockChestAbility.POSITION_TO_BLOCK))
				.forEach(l -> ChestPortal.GEM_PORTAL.openPortal(l));
			return true;
		}

		@Override
		public boolean interact(PlayerInteractEvent event) {
			if(!canTake(event.getClickedBlock(), event.getPlayer())) return false;
			take(event.getClickedBlock(), event.getPlayer());
			return false;
		}

		@Override
		public boolean canPlace(Block block) {
			if(block.getType() != Material.AIR) return false;
			if(0 < ChestPortal.GEM_PORTAL.findChests(block.getLocation()).size()) {
				return true;
			}
			return false;

		}
		@Override
		public boolean canTake(Block block, Player player) {
			if(block.getType() != getIcon().getMaterial()) return false;
			return true;
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
		return triggers;
	}

	@Override
	public boolean place(KotobaBlockData data) {
		data.placeBlock();
		Location location = data.getLocation().add(0.5, 0.5, 0.5);
		KotobaEffect.MAGIC_MIDIUM.playEffect(location);
		KotobaEffect.MAGIC_MIDIUM.playSound(location);
		return true;
	}

	@Override
	public boolean take(Block block, Player player) {
		KotobaBlockData data = new KotobaBlockData(block.getLocation(), Material.AIR, 0);
		data.placeBlock();
		player.getInventory().addItem(getIcon().create(1));
		Location location = block.getLocation().add(0.5, 0.5, 0.5);
		KotobaEffect.BREAK_BLOCK_MIDIUM.playEffect(location);
		KotobaEffect.BREAK_BLOCK_MIDIUM.playSound(location);
		return true;
	}


	protected KotobaBlockData getBlockData(Location location) {
		return new KotobaBlockData(location, getIcon().getMaterial(), getIcon().getData());
	}


}

