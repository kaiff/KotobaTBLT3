package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockData;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public enum TBLTGem {


	GREEN_GEM(TBLTItemStackIcon.GREEN_GEM, Material.WOOL, (short) 13),
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
				toggleConnectedGems(block);
				return true;
			}).orElse(false);
	}


	private static final Vector CONNECTION_HEIGHT = new Vector(0, 2, 0);
	private static final int CONNECTION_RANGE = 16;


	@SuppressWarnings("deprecation")
	private boolean isConnectionWire(Block block) {
		return block.getType() == Material.WOOL && block.getData() == 4;
	}



	private void toggleConnectedGems(Block origin) {
		Block start = origin.getLocation().subtract(CONNECTION_HEIGHT).getBlock();
		List<Block> powered = new ArrayList<>();
		List<Block> heads = new ArrayList<>();
		heads.add(start);

		List<Block> targets = KotobaUtility.getBlocks(start.getLocation(), CONNECTION_RANGE).stream()
				.filter(b -> isConnectionWire(b))
				.collect(Collectors.toList());

		Stream.iterate(0, i -> i)
			.limit(CONNECTION_RANGE)
			.forEach(i -> {
				List<Block> newHeads = targets.stream()
					.filter(t -> heads.stream().anyMatch(h -> h.getLocation().distance(t.getLocation()) == 1))
					.filter(t -> powered.stream().noneMatch(p -> p.getLocation().distance(t.getLocation()) == 0))
					.collect(Collectors.toList());
				heads.clear();
				heads.addAll(newHeads);
				powered.addAll(heads);
			});
		powered.stream()
			.map(b -> b.getLocation().clone().add(CONNECTION_HEIGHT).getBlock())
			.forEach(b -> {
				toggleGem(b);
			});
	}


	@SuppressWarnings("deprecation")
	private void toggleGem(Block block) {
		Optional<TBLTGem> gem = Stream.of(TBLTGem.values())
			.filter(g -> block.getType() == g.getIcon().getMaterial())
			.findFirst();
		if(gem.isPresent()) {
			gem.ifPresent(g -> {
				KotobaEffect.BREAK_BLOCK_MIDIUM.playEffect(block.getLocation());
				KotobaEffect.BREAK_BLOCK_MIDIUM.playEffect(block.getLocation());
				new KotobaBlockData(block.getLocation(), g.getSwitchMaterial(), g.getSwitchData()).placeBlock();
			});
		} else {
			Stream.of(TBLTGem.values())
				.filter(g -> g.getSwitchMaterial() == block.getType() && g.getSwitchData() == block.getData())
				.findFirst()
				.ifPresent(g -> {
					KotobaEffect.BREAK_BLOCK_MIDIUM.playEffect(block.getLocation());
					KotobaEffect.BREAK_BLOCK_MIDIUM.playEffect(block.getLocation());
					new KotobaBlockData(block.getLocation(), g.getIcon().getMaterial(), 0).placeBlock();
				});
		}
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

