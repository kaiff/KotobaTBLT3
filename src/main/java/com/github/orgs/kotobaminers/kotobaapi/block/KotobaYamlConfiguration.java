package com.github.orgs.kotobaminers.kotobaapi.block;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTUtility;

public abstract class KotobaYamlConfiguration {


	private static final String DELIMETER = ",";
	public static final String EXTENSION = ".yml";
	public abstract String getFileName();
	public abstract void setFileName(String name);
	public abstract File getDirectory();
	public abstract void save();
	public abstract void load();
	public abstract void delete();


	public boolean rename(String newName) {
		Optional<File> oldFile = findFile();
		File newFile = new File(getDirectory() + "/" + newName + EXTENSION);
		boolean success = false;
		if (!newFile.exists() && oldFile.isPresent()) {
			success = oldFile.get().renameTo(newFile);
			if(success) {
				findFile().ifPresent(file -> file.delete());
				setFileName(newName);
			}
		}
		return success;
	}


	private Optional<File> findFile() {
		File file = new File(getDirectory() + "/" + getFileName() + EXTENSION);
		if(file.exists()) {
			return Optional.of(file);
		}
		return Optional.empty();
	}


	public File getFileEvenCreate() {
		File file = new File(getDirectory() + "/" + getFileName() + EXTENSION);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public YamlConfiguration getConfiguration() {
		return YamlConfiguration.loadConfiguration(getFileEvenCreate());
	}


	@SuppressWarnings("deprecation")
	private static String toString(Block block) {
		String x = String.valueOf(block.getX());
		String y = String.valueOf(block.getY());
		String z = String.valueOf(block.getZ());
		String material = block.getType().name();
		String data = String.valueOf(block.getData());
		return String.join(DELIMETER, Arrays.asList(x, y, z, material, data));
	}

	public static void setBlocks(List<Block> blocks, YamlConfiguration config, String path) {
		List<String> list = blocks.stream()
			.map(b -> toString(b))
			.collect(Collectors.toList());
		config.set(path, list);
	}

	private static KotobaBlockData loadBlockData(String line, World world) {
		String[] split = line.split(DELIMETER);
		int x = Integer.valueOf(split[0]);
		int y = Integer.valueOf(split[1]);
		int z = Integer.valueOf(split[2]);
		Material material = Material.valueOf(split[3]);
		int data = Integer.valueOf(split[4]);
		Location location = new Location(world, x, y, z);

		return new KotobaBlockData(location, material, data);
	}

	public static List<KotobaBlockData> loadBlocksData(YamlConfiguration config, String blockPath, String worldPath) {
		return Optional.ofNullable(config.getString(worldPath))
			.flatMap(name -> TBLTUtility.findWorld(name))
			.map(world ->
				config.getStringList(blockPath)
					.stream()
					.map(line -> loadBlockData(line, world))
					.collect(Collectors.toList())
			).orElse(new ArrayList<KotobaBlockData>());
	}

	public static void setChests(List<Chest> chests, YamlConfiguration config, String path) {
		chests.stream()
			.forEach(chest -> {
				Block block = chest.getBlock();
				Map<Integer, ItemStack> contents = new HashMap<Integer, ItemStack>();
				Stream.iterate(0, i -> i + 1)
				.limit(chest.getInventory().getSize())
				.filter(i -> chest.getInventory().getContents()[i] != null)
				.forEach(i -> contents.put(i, chest.getInventory().getContents()[i]));
				config.set(path + "." + toString(block), contents);
			});
	}

	public static List<Chest> loadChests(YamlConfiguration config, String chestPath, String worldPath, boolean update) {
		List<Chest> chests = new ArrayList<>();
		Optional.ofNullable(config.getString(worldPath))
			.flatMap(name -> TBLTUtility.findWorld(name))
			.ifPresent(world -> {
				if(!config.isConfigurationSection(chestPath)) return;
				config.getConfigurationSection(chestPath).getKeys(false).stream()
				.forEach(key -> {
					KotobaBlockData blockData = loadBlockData(key, world);
					Block block = blockData.getCurrentBlock();
					if(block.getState() instanceof Chest) {
						Chest chest = (Chest) block.getState();
						String key2 = chestPath + "." + key;
						config.getConfigurationSection(key2).getKeys(false).stream()
						.forEach(key3 -> {
							chest.getInventory().setItem(Integer.valueOf(key3), config.getItemStack(key2 + "." + key3));
						});
						chests.add(chest);
					}
				});
			});
		return chests;
	}

	public static void setMaterial(YamlConfiguration config, String path, Material material) {
		if(material != null) {
			config.set(path, material.name());
		}
	}

	public static Optional<Material> loadMaterial(YamlConfiguration config, String path) {
		if(config.isString(path)) {
			String str = config.getString(path);
			return Stream.of(Material.values())
				.filter(material -> material.name().equalsIgnoreCase(str))
				.findAny();
		}
		return Optional.empty();
	}


}

