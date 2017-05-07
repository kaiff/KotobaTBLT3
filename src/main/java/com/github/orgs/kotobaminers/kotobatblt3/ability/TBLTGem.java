package com.github.orgs.kotobaminers.kotobatblt3.ability;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockData;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public enum TBLTGem {


	GREEN_GEM(TBLTItemStackIcon.GREEN_GEM, Material.WOOL, (short) 5),
	BLUE_GEM(TBLTItemStackIcon.BLUE_GEM, Material.WOOL, (short) 11),
	RED_GEM(TBLTItemStackIcon.RED_GEM, Material.WOOL, (short) 14),
	;


	private KotobaItemStackIcon icon;
	private Material wireMaterial;
	private short wireData;


	private TBLTGem(KotobaItemStackIcon icon, Material switchMaterial, short wireData) {
		this.icon = icon;
		this.wireMaterial = switchMaterial;
		this.wireData = wireData;
	}


	public boolean place(Player player, Location location) {
			return new TBLTArenaMap().findPlayingMap(player)
				.filter(a -> location.getBlock().getType() == Material.AIR)
					.map(a -> {
					new KotobaBlockData(location, icon.getMaterial(), icon.getData()).placeBlock();
					KotobaEffect.MAGIC_MIDIUM.playEffect(location);
					KotobaEffect.MAGIC_MIDIUM.playSound(location);

					a.getArenaMeta().addOwningGem(player, location.getBlock());
					return true;
				}).orElse(false);
	}


	public boolean take(Player player, Block block) {
		return new TBLTArenaMap().findPlayingMap(player)
			.filter(a -> a.getArenaMeta().canOperateGem(player, block))
			.map(a -> {
				new KotobaBlockData(block.getLocation(), Material.AIR, 0).placeBlock();
				player.getInventory().addItem(getIcon().create(1));
				KotobaEffect.MAGIC_MIDIUM.playEffect(block.getLocation());
				KotobaEffect.MAGIC_MIDIUM.playSound(block.getLocation());
				a.getArenaMeta().removeOwningGem(player, block);
				return true;
			}).orElse(false);
	}


	@SuppressWarnings("deprecation")
	public boolean isWire(Block block) {
		if(block.getData() == wireData && block.getType() == wireMaterial) {
			return true;
		}
		return false;
	}


	public Material getSwitchMaterial() {
		return wireMaterial;
	}


	public short getSwitchData() {
		return wireData;
	};


	public KotobaItemStackIcon getIcon() {
		return icon;
	}


}

