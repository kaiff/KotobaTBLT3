package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.io.File;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockStorage;
import com.github.orgs.kotobaminers.kotobaapi.block.KotobaYamlConfiguration;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;

public class BlockReplacer extends KotobaBlockStorage {


	private static final File DIRECTORY = new File(Setting.getPlugin().getDataFolder().getAbsolutePath() + "/BlockReplacer/");
	private static final String TRIGGER = "TRIGGER";
	private static final String TARGET = "TARGET";


	private boolean isReplaced = false;
	private Material trigger;
	private Material target;


	protected BlockReplacer(String name) {
		super(name);
	}


	public static BlockReplacer create(String name, Player player) {
		return (BlockReplacer) (new BlockReplacer(name)).setData(name, player);
	}


	public boolean replace(Player player, Block clicked) {
		if(isReplaced) return false;
		boolean success = false;
		if(clicked.getType() == target && player.getItemInHand().getType() == trigger) {
			success = getBlocks().stream()
				.filter(block -> block.getType() == target)
				.map(block -> {
					KotobaEffect.MAGIC_SMALL.playEffect(block.getLocation());
					KotobaEffect.MAGIC_SMALL.playSound(block.getLocation());
					return true;
				}).allMatch(b -> b == true);
			if(success) {
				load();
				done();
			}
		}
		return success;
	}

	private void done() {
		this.isReplaced = true;
	}


	public void setTriggers(Material trigger, Material target) {
		//TODO Material Converter is needed
		this.trigger = trigger;
		this.target = target;
	}


	@Override
	protected void saveOptions(YamlConfiguration config) {
		KotobaYamlConfiguration.setMaterial(config, TRIGGER, trigger);
		KotobaYamlConfiguration.setMaterial(config, TARGET, target);
	}

	@Override
	protected void setOptions(YamlConfiguration config) {
		KotobaYamlConfiguration.loadMaterial(config, TRIGGER)
			.ifPresent(material -> this.trigger = material);
		KotobaYamlConfiguration.loadMaterial(config, TARGET)
			.ifPresent(material -> this.target = material);
	}

	@Override
	protected void loadFromWorld() {
	}

	@Override
	public KotobaBlockStorage create(String name) {
		return new BlockReplacer(name);
	}

	@Override
	public File getDirectory() {
		return DIRECTORY;
	}


	public Material getTrigger() {
		return trigger;
	}

	public Material getTarget() {
		return target;
	}


	@Override
	protected void initialize() {
		this.isReplaced = true;
	}

	protected void setBefore() {
		this.isReplaced = false;
	}


}

