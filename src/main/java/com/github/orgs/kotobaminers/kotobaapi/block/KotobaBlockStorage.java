package com.github.orgs.kotobaminers.kotobaapi.block;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

public abstract class KotobaBlockStorage extends KotobaYamlConfiguration {


	private enum Path {
		ID("ID"),
		NAME("NAME"),
		WORLD("WORLD"),
		BLOCKS("BLOCKS"),
		CHESTS("CHESTS"),
		TOP_CORNER("TOP_CORNER"),
		BOTTOM_CORNER("BOTTOM_CORNER"),
		;
		private String path;
		private Path(String path) {
			this.path = path;
		}
		public String getPath() {
			return path;
		}
	}


	private int id;
	private String name;
	private World world;
	private Integer xMax;
	private Integer yMax;
	private Integer zMax;
	private Integer xMin;
	private Integer yMin;
	private Integer zMin;


	protected KotobaBlockStorage(String name) {
		this.name = name;
	}


	public abstract KotobaBlockStorage create(String name);
	protected abstract void saveOptions(YamlConfiguration config);
	protected abstract void setOptions(YamlConfiguration config);

	protected KotobaBlockStorage setData(int id, String name, World world, Integer XMax, Integer YMax, Integer ZMax, Integer XMin, Integer YMin, Integer ZMin) {
		this.id = id;
		this.name = name;
		this.world = world;
		this.xMax = XMax;
		this.yMax = YMax;
		this.zMax = ZMax;
		this.xMin = XMin;
		this.yMin = YMin;
		this.zMin = ZMin;
		return this;
	}

	protected KotobaBlockStorage setData(int id, String name, Player player) {
		WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if (worldEdit == null) {
			player.sendMessage("WorldEdit Not Load");
			return this;
		}
		Selection sel = worldEdit.getSelection(player);
		return setData(
			id,
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

	protected KotobaBlockStorage setDataFromConfig() {
		YamlConfiguration config = getConfiguration();
		if(!config.contains(Path.WORLD.getPath())) return this;

		Bukkit.getWorlds().stream()
			.filter(w -> w.getName().equalsIgnoreCase(config.getString(Path.WORLD.getPath())))
			.findFirst()
			.ifPresent(world -> {
				int id = config.getInt(Path.ID.getPath());
				String name = config.getString(Path.NAME.getPath());

				Location topCorner = (Location) config.get(Path.TOP_CORNER.getPath());
				Location bottomCorner = (Location) config.get(Path.BOTTOM_CORNER.getPath());

				int xMax = topCorner.getBlockX();
				int xMin = bottomCorner.getBlockX();
				int yMax = topCorner.getBlockY();
				int yMin = bottomCorner.getBlockY();
				int zMax = topCorner.getBlockZ();
				int zMin = bottomCorner.getBlockZ();

				setData(id, name, world, xMax, yMax, zMax, xMin, yMin, zMin);
				setOptions(config);
			});
		return this;
	}


	public List<KotobaBlockStorage> importAll() {
		final String extension = ".yml";
		return Stream.of(getDirectory().listFiles())
			.filter(f -> f.getName().endsWith(extension))
			.map(f -> f.getName().substring(0, f.getName().length()-".yml".length()))
			.map(name -> create(name))
			.map(storage -> storage.setDataFromConfig())
			.collect(Collectors.toList());
	}

	public KotobaBlockStorage resize(Player player) {
		return setData(this.id, this.name, player);
	}


	@Override
	public void delete() {
		if(getFileEvenCreate().exists()) getFileEvenCreate().delete();
	}

	@Override
	public void save() {
		File file = getFileEvenCreate();
		delete();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		YamlConfiguration config = getConfiguration();

		saveOptions(config);

		config.set(Path.ID.getPath(), getId());
		config.set(Path.NAME.getPath(), getName());
		config.set(Path.WORLD.getPath(), getWorld().getName());
		config.set(Path.BOTTOM_CORNER.getPath(), new Location(world, xMin, yMin, zMin));
		config.set(Path.TOP_CORNER.getPath(), new Location(world, xMax, yMax, zMax));

		List<Block> blocks = getBlocksExceptForAir();
		KotobaYamlConfiguration.setBlocks(blocks, config, Path.BLOCKS.getPath());

		List<Chest> chests = blocks.stream()
			.filter(b -> b.getState() instanceof Chest)
			.map(b -> (Chest) b.getState())
			.collect(Collectors.toList());
		KotobaYamlConfiguration.setChests(chests, config, Path.CHESTS.getPath());


		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void load() {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(getFileEvenCreate());
		initialize();

		List<KotobaBlockData> blocksData = KotobaYamlConfiguration.loadBlocksData(config, Path.BLOCKS.getPath(), Path.WORLD.getPath());
		replaceAir();
		KotobaBlockData.placeBlockSafe(blocksData);

		KotobaYamlConfiguration.loadChests(config, Path.CHESTS.getPath(), Path.WORLD.getPath(), true);

		loadFromWorld();

		setOptions(config);
	}


	public void replaceAir() {
		Location target = new Location(world, xMin, yMin, zMin);
		KotobaBlockData block = new KotobaBlockData(target, Material.AIR, 0);
		Stream.iterate(xMin, i -> i + 1)
			.limit(xMax - xMin + 1)
			.forEach(x -> {
				target.setX(x);
				Stream.iterate(yMin, i -> i + 1)
					.limit(yMax - yMin + 1)
					.forEach(y -> {
						target.setY(y);
						Stream.iterate(zMin, i -> i + 1)
							.limit(zMax - zMin + 1)
							.forEach(z -> {
								target.setZ(z);
								BlockState state = target.getBlock().getState();
								if(state instanceof Chest) {
									((Chest) state).getInventory().clear();
								}
								block.setLocation(target);
								block.placeBlock();
							});
					});
			});
	}


	protected abstract void loadFromWorld();
	protected abstract void initialize();


	public List<Block> getBlocksExceptForAir() {
		return KotobaUtility.getBlocks(getWorld(), getXMax(), getYMax(), getZMax(), getXMin(), getYMin(), getZMin())
			.stream()
			.filter(b -> b.getType() != Material.AIR)
			.collect(Collectors.toList());
	}

	public Location getCenter() {
		int xCenter = (getXMax() + getXMin()) / 2;
		int yCenter = (getYMax() + getYMin()) / 2;
		int zCenter = (getZMax() + getZMin()) / 2;
		return new Location(getWorld(), xCenter, yCenter, zCenter);
	}

	public boolean isOverlap(KotobaBlockStorage storage) {
		Integer xMax1 = this.getXMax();
		Integer xMin1 = this.getXMin();
		Integer xMax2 = storage.getXMax();
		Integer xMin2 = storage.getXMin();
		boolean overlapX = (xMin2 <= xMax1 && xMax1 <= xMax2) || (xMin2 <= xMin1  && xMin1 <= xMax2);

		Integer yMax1 = this.getYMax();
		Integer yMin1 = this.getYMin();
		Integer yMax2 = storage.getYMax();
		Integer yMin2 = storage.getYMin();
		boolean overlapY = (yMin2 <= yMax1 && yMax1 <= yMax2) || (yMin2 <= yMin1  && yMin1 <= yMax2);

		Integer zMax1 = this.getZMax();
		Integer zMin1 = this.getZMin();
		Integer zMin2 = storage.getZMin();
		Integer zMax2 = storage.getZMax();
		boolean overlapZ = (zMin2 <= zMax1 && zMax1 <= zMax2) || (zMin2 <= zMin1  && zMin1 <= zMax2);

		if(overlapX && overlapY && overlapZ) {
			return true;
		}
		return false;

	}

	public boolean isIn(Location location) {
		int pX = location.getBlockX();
		int pY = location.getBlockY();
		int pZ = location.getBlockZ();
		if (pX <= getXMax() && pX >= getXMin() && pY <= getYMax() && pY >= getYMin() && pZ <= getZMax() && pZ >= getZMin()) {
			return true;
		}
		return false;
	}


	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public World getWorld() {
		return world;
	}
	public Integer getXMax() {
		return xMax;
	}
	public Integer getYMax() {
		return yMax;
	}
	public Integer getZMax() {
		return zMax;
	}
	public Integer getXMin() {
		return xMin;
	}
	public Integer getYMin() {
		return yMin;
	}
	public Integer getZMin() {
		return zMin;
	}
	protected void setName(String name) {
		this.name= name;
	}

	@Override
	public String getFileName() {
		return name;
	}
	@Override
	public void setFileName(String name) {
		this.name = name;
	}
	public abstract File getDirectory();
}

