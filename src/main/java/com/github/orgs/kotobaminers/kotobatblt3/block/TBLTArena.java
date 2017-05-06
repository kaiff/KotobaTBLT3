package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockData;
import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockStorage;
import com.github.orgs.kotobaminers.kotobaapi.userinterface.Holograms;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaTitle;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaTitle.TitleOption;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.citizens.UniqueNPC;
import com.github.orgs.kotobaminers.kotobatblt3.database.TBLTData;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.TBLTPlayer;
import com.github.orgs.kotobaminers.kotobatblt3.utility.ChestReader;

import net.md_5.bungee.api.ChatColor;

public class TBLTArena extends KotobaBlockStorage {


	private static final File DIRECTORY = new File(Setting.getPlugin().getDataFolder().getAbsolutePath() + "/Arena/");
	private TBLTArenaMeta meta = new TBLTArenaMeta();


	public TBLTArenaMeta getArenaMeta() {
		return meta;
	}


	protected TBLTArena(String name) {
		super(name);
	}


	public static TBLTArena create(String name, Player player) {
		int id = 1;
		OptionalInt max = new TBLTArenaMap().getMap().values().stream()
			.mapToInt(s -> s.getId())
			.max();

		if(max.isPresent()) {
			id = max.getAsInt() + 1;
		}
		return (TBLTArena) (new TBLTArena(name)).setData(id, name, player);
	}


	private void start(Player player, Location location) {
		TBLTPlayer.resetCurrentJob(player);
		meta.giveJobItems(player);

		TBLTData.getOrDefault(player.getUniqueId()).initialize();

		player.teleport(location);
		KotobaEffect.ENDER_SIGNAL.playEffect(location);
		KotobaEffect.ENDER_SIGNAL.playSound(location);
		player.playSound(location, Sound.AMBIENCE_CAVE, 10, 0.5F);
		KotobaTitle.displayTitle(player, getName(), ChatColor.RED, Arrays.asList(TitleOption.BOLD, TitleOption.ITALIC));
	}


	public void startNext() {
		new TBLTArenaMap().find(getArenaMeta().getNext())
			.map(a -> (TBLTArena) a)
			.ifPresent(a -> a.join(getTBLTPlayers()));
		load();
	}


	public void restart() {
		load();
		getTBLTPlayers().forEach(p -> start(p, findSpawn(p)));
	}


	public void join(List<Player> additional) {
		load();
		Stream.of(getTBLTPlayers(), additional)
			.flatMap(p -> p.stream())
			.forEach(p -> start(p, findSpawn(p)));
	}


	private Location findSpawn(Player player) {
		TBLTPlayer job = TBLTPlayer.findJob(player);
		switch(job) {
		case ARTIFICER:
		case MAGE:
			return getArenaMeta().getJobSpawn().getOrDefault(job, player.getWorld().getSpawnLocation());
		case NONE:
		default:
			return getArenaMeta().getJobSpawn().getOrDefault(TBLTPlayer.ARTIFICER, player.getWorld().getSpawnLocation());
		}
	}



	@Override
	protected void saveOptions(YamlConfiguration config) {
	}

	@Override
	protected void setOptions(YamlConfiguration config) {
	}

	@Override
	protected void initialize() {
		meta = new TBLTArenaMeta();
		Holograms.removeHolograms(Setting.getPlugin(), this);
		initializeUniqueNPCs();
	}

	@Override
	protected void loadFromWorld() {
		List<Chest> chests = getBlocksExceptForAir().stream()
			.filter(b -> b.getState() instanceof Chest)
			.map(b -> (Chest) b.getState())
			.collect(Collectors.toList());


		chests.forEach(c -> {
			ChestReader.displayHolograms(c);
			ChestReader.updateArena(c);
		});

	}

	private void initializeUniqueNPCs() {
		Stream.of(UniqueNPC.values())
			.forEach(u ->
				u.getNPCs().stream()
					.filter(n -> isIn(n.getStoredLocation()))
					.forEach(n -> u.despawn(n.getId()))
		);
	}


	@Override
	public KotobaBlockStorage create(String name) {
		return new TBLTArena(name);
	}

	@Override
	public File getDirectory() {
		return DIRECTORY;
	}


	private void replaceFloor(Material bottom, Material second) {
		KotobaUtility.getBlocks(getWorld(), getXMax(), getYMin(), getZMax(), getXMin(), getYMin(), getZMin())
			.stream()
			.map(block -> new KotobaBlockData(block.getLocation(), bottom, 0))
			.forEach(data -> data.placeBlock());
		KotobaUtility.getBlocks(getWorld(), getXMax(), getYMin() + 1, getZMax(), getXMin(), getYMin() + 1, getZMin())
			.stream()
			.map(block -> new KotobaBlockData(block.getLocation(), second, 0))
			.forEach(data -> data.placeBlock());
		}

	private void replaceCeiling(Material material) {
		KotobaUtility.getBlocks(getWorld(), getXMax(), getYMax(), getZMax(), getXMin(), getYMax(), getZMin())
			.stream()
			.map(block -> new KotobaBlockData(block.getLocation(), material, 0))
			.forEach(data -> data.placeBlock());
	}

	private void replaceWalls(Material material) {
		Arrays.asList(
			KotobaUtility.getBlocks(getWorld(), getXMin(), getYMax(), getZMax(), getXMin(), getYMin(), getZMin()),
			KotobaUtility.getBlocks(getWorld(), getXMax(), getYMax(), getZMax(), getXMax(), getYMin(), getZMin()),
			KotobaUtility.getBlocks(getWorld(), getXMax(), getYMax(), getZMin(), getXMin(), getYMin(), getZMin()),
			KotobaUtility.getBlocks(getWorld(), getXMax(), getYMax(), getZMax(), getXMin(), getYMin(), getZMax())
		).stream()
			.flatMap(blocks -> blocks.stream())
			.map(block -> new KotobaBlockData(block.getLocation(), material, 0))
			.forEach(data -> data.placeBlock());
		}


	private void replaceCorners(Material material) {
		Location min = new Location(getWorld(), getXMin(), getYMin(), getZMin());
		Location max = new Location(getWorld(), getXMax(), getYMax(), getZMax());
		new KotobaBlockData(min, material, 0).placeBlock();
		new KotobaBlockData(max, material, 0).placeBlock();
	}


	public void placeWallsFloorCorner(Material material) {
		replaceFloor(Material.REDSTONE_ORE, Material.WATER);
		replaceCeiling(material);
		replaceWalls(material);
		replaceCorners(Material.SEA_LANTERN);
	}


	public List<Player> getTBLTPlayers() {
		return Bukkit.getServer().getOnlinePlayers().stream()
			.filter(p -> p.getGameMode() == GameMode.ADVENTURE)
			.filter(p -> isIn(p.getLocation()))
			.collect(Collectors.toList());
	}


}

