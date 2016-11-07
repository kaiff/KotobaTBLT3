package com.github.orgs.kotobaminers.kotobaapi.worldeditor;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

public abstract class BlockStorage {
	
	private static final String NAME = "Name";
	private static final String WORLD = "World";
	private static final String SPAWN_X = "Spawn.X";
	private static final String SPAWN_Y = "Spawn.Y";
	private static final String SPAWN_Z = "Spawn.Z";
	private static final String EXTENSION = ".yml";

	private String name;
	private File directory;
	private World world;
	private Location spawn;
	private Integer XMax;
	private Integer YMax;
	private Integer ZMax;
	private Integer XMin;
	private Integer YMin;
	private Integer ZMin;
	
	public BlockStorage setData(Player player, String name) {
		WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if (worldEdit == null) {
			player.sendMessage("WorldEdit Not Load");
		}
		Selection sel = worldEdit.getSelection(player);
		return setData(
			name,
			sel.getWorld(),
			sel.getMaximumPoint().getBlockX(),
			sel.getMaximumPoint().getBlockY(),
			sel.getMaximumPoint().getBlockZ(),
			sel.getMinimumPoint().getBlockX(),
			sel.getMinimumPoint().getBlockY(),
			sel.getMinimumPoint().getBlockZ()
		);
	}
	
	public BlockStorage setData(String name, World world, Integer XMax, Integer YMax, Integer ZMax, Integer XMin, Integer YMin, Integer ZMin) {
		this.name = name;
		this.world = world;
		this.XMax = XMax;
		this.YMax = YMax;
		this.ZMax = ZMax;
		this.XMin = XMin;
		this.YMin = YMin;
		this.ZMin = ZMin;
		return this;
	}
	
	public boolean isLocationIn(Location location) {
		int pX = location.getBlockX();
		int pY = location.getBlockY();
		int pZ = location.getBlockZ();
		if (pX <= getXMax() && pX >= getXMin() && pY <= getYMax() && pY >= getYMin() && pZ <= getZMax() && pZ >= getZMin()) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public void save() {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(getFile());
		config.set(NAME, getName());
		config.set(WORLD, getWorld().getName());
		if(getSpawn() != null) {
			Location s = getSpawn();
			config.set(SPAWN_X, s.getBlockX());
			config.set(SPAWN_Y, s.getBlockY());
			config.set(SPAWN_Z, s.getBlockZ());
		}
		for (int x = getXMin(); x <= getXMax(); x++) {
			for (int y = getYMin(); y <= getYMax(); y++) {
				for (int z = getZMin(); z <= getZMax(); z++) {
					Block block = getWorld().getBlockAt(x, y, z);
					config.set("Blocks." + x + "," + y + "," + z, block.getType().toString() + ":" + block.getData());
				}
			}
		}
		try {
			config.save(getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void load(List<World> worlds) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(getFile());
		worlds.stream()
			.filter(w -> w.getName().equalsIgnoreCase(config.getString(WORLD)))
			.findFirst()
			.ifPresent(w -> {
				ConfigurationSection privateSection = config.getConfigurationSection("Blocks");
				for (String location : privateSection.getKeys(false)) {
					String[] cords = location.split(",");
					int X = Integer.valueOf(cords[0]);
					int Y = Integer.valueOf(cords[1]);
					int Z = Integer.valueOf(cords[2]);
					String[] Blockdata = config.getString("Blocks." + location).split(":");
					String material = Blockdata[0];
					byte data = Byte.valueOf(Blockdata[1]);
					w.getBlockAt(X, Y, Z).setType(Material.getMaterial(material));
					w.getBlockAt(X, Y, Z).setData(data);
				}
			}
		);
	}
	
	public void setDataFromConfig(List<World> worlds) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(getFile());
		Optional<World> worldOpt = worlds.stream()
			.filter(w -> w.getName().equalsIgnoreCase(config.getString(WORLD)))
			.findFirst();
		if(worldOpt.isPresent()) {
			String name = config.getString(NAME);
			World world = worldOpt.get();
			List<Vector> vectors = config.getConfigurationSection("Blocks").getKeys(false)
				.stream()
				.map(str -> str.split(","))
				.map(cords -> new Vector(Integer.valueOf(cords[0]), Integer.valueOf(cords[1]), Integer.valueOf(cords[2])))
				.collect(Collectors.toList());

			Optional<Integer> xMax = vectors.stream().map(loc -> loc.getX()).max(Comparator.naturalOrder()).map(d -> d.intValue());
			Optional<Integer> xMin = vectors.stream().map(loc -> loc.getX()).min(Comparator.naturalOrder()).map(d -> d.intValue());
			Optional<Integer> yMax = vectors.stream().map(loc -> loc.getY()).max(Comparator.naturalOrder()).map(d -> d.intValue());
			Optional<Integer> yMin = vectors.stream().map(loc -> loc.getY()).min(Comparator.naturalOrder()).map(d -> d.intValue());
			Optional<Integer> zMax = vectors.stream().map(loc -> loc.getZ()).max(Comparator.naturalOrder()).map(d -> d.intValue());
			Optional<Integer> zMin = vectors.stream().map(loc -> loc.getZ()).min(Comparator.naturalOrder()).map(d -> d.intValue());
			
			if(xMax.isPresent() && xMin.isPresent() && yMax.isPresent() && yMin.isPresent() && zMax.isPresent() && zMin.isPresent()) {
				this.setData(name, world, xMax.get(), yMax.get(), zMax.get(), xMin.get(), yMin.get(), zMin.get());
				if(config.isInt(SPAWN_X) && config.isInt(SPAWN_Y) && config.isInt(SPAWN_Z)) {
					int spawnX = config.getInt(SPAWN_X);
					int spawnY= config.getInt(SPAWN_Y);
					int spawnZ = config.getInt(SPAWN_Z);
					setSpawn(new Location(world, spawnX, spawnY, spawnZ));
				}
			}
		}
	}
	
	public Location getCenter() {
		int xCenter = (getXMax() + getXMin()) / 2;
		int yCenter = (getYMax() + getYMin()) / 2;
		int zCenter = (getZMax() + getZMin()) / 2;
		return new Location(getWorld(), xCenter, yCenter, zCenter);
	}

	public File getFile() {
		File file = new File(getDirectory().getAbsolutePath() + "/" + getName() + EXTENSION);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public String getName() {
		return name;
	}
	public File getDirectory() {
		return directory;
	}
	public World getWorld() {
		return world;
	}
	public Location getSpawn() {
		return spawn;
	}
	public Integer getXMax() {
		return XMax;
	}
	public Integer getYMax() {
		return YMax;
	}
	public Integer getZMax() {
		return ZMax;
	}
	public Integer getXMin() {
		return XMin;
	}
	public Integer getYMin() {
		return YMin;
	}
	public Integer getZMin() {
		return ZMin;
	}

	protected void setName(String name) {
		this.name= name;
	}
	public void setSpawn(Location location) {
		this.spawn = location;
	}
	
}
