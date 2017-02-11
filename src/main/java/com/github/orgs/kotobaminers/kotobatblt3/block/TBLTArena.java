package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.github.orgs.kotobaminers.kotobaapi.ability.ItemStackAbilityManager;
import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockData;
import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockStorage;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Holograms;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaTitle;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaTitle.TitleOption;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.ability.ClickBlockChestAbility;
import com.github.orgs.kotobaminers.kotobatblt3.citizens.UniqueNPC;
import com.github.orgs.kotobaminers.kotobatblt3.game.TBLTData;
import com.github.orgs.kotobaminers.kotobatblt3.game.TBLTJob;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;
import com.github.orgs.kotobaminers.kotobatblt3.utility.RepeatingEffect;

import net.md_5.bungee.api.ChatColor;

public class TBLTArena extends KotobaBlockStorage {


	private static final File DIRECTORY = new File(Setting.getPlugin().getDataFolder().getAbsolutePath() + "/Arena/");
	private static final String CHECK_POINT = "CheckPoint";
	private static final String PREDICTION = "Prediction";


	private List<Location> checkPoints = new ArrayList<>();
	private Location currentPoint = null;
	private Set<RepeatingEffect> repeatingEffects = new HashSet<>();
	private ItemStack predictionItem = KotobaItemStack.create(Material.BOOK_AND_QUILL, (short) 0, 1, "Prediction", new ArrayList<String>());


	protected TBLTArena(String name) {
		super(name);
	}


	public static TBLTArena create(String name, Player player) {
		return (TBLTArena) (new TBLTArena(name)).setData(name, player);
	}


	private void start(Player player, Location location) {
		TBLTJob.initializeInventory(player);
		TBLTData.getOrDefault(player.getUniqueId()).initialize();
		player.teleport(location);
		KotobaEffect.ENDER_SIGNAL.playEffect(location);
		KotobaEffect.ENDER_SIGNAL.playSound(location);
		player.playSound(location, Sound.AMBIENCE_CAVE, 10, 0.5F);
		KotobaTitle.displayTitle(player, getName(), ChatColor.RED, Arrays.asList(TitleOption.BOLD, TitleOption.ITALIC));
	}


	public void startSpawn(Player player) {
		currentPoint = null;//The current Check point is not initialized by restarting
		initialize();
		start(player, getSpawn());
	}

	public void continueFromCurrent(Player player) {
		start(player, getCurrentPoint());
	}

	public void addCheckPoint(Location location) {
		checkPoints.add(location);
	}

	public Optional<Location> findNearestCheckPoint(Location location) {
		return checkPoints.stream()
			.sorted((loc1, loc2) -> (int) (loc1.distance(location) - loc2.distance(location)))
			.findFirst();
	}

	public void removeNearestCheckPoint(Location location) {
		Optional<Location> nearest = findNearestCheckPoint(location);
		List<Location> points = nearest.map(loc -> checkPoints.stream()
			.filter(point -> 0 != point.distance(loc)).collect(Collectors.toList()))
			.orElse(checkPoints);
		this.checkPoints = points;
	}


	@Override
	protected void saveOptions(YamlConfiguration config) {
		if(checkPoints != null) {
			Stream.iterate(0, i -> i + 1)
				.limit(checkPoints.size())
				.forEach(i -> config.set(CHECK_POINT + "." + i, checkPoints.get(i)));
		}
		config.set(PREDICTION, predictionItem);
	}

	@Override
	protected void setOptions(YamlConfiguration config) {
		if(config.isConfigurationSection(CHECK_POINT)) {
			ConfigurationSection section = config.getConfigurationSection(CHECK_POINT);
			List<Location> points = section.getKeys(false).stream()
				.map(key -> (Location) config.get(CHECK_POINT + "." + key))
				.collect(Collectors.toList());
			this.checkPoints = points;
		}

		if(config.isItemStack(PREDICTION)) {
			setPredictionItem((BookMeta) ((ItemStack) config.get(PREDICTION)).getItemMeta());
		}

	}

	@Override
	protected void initialize() {
		new BlockReplacerMap().getReplacers(this).forEach(r -> r.setBefore());
		Holograms.removeHolograms(Setting.getPlugin(), this);
		initializeUniqueNPCs();
	}
	@Override
	protected void loadFromWorld() {
		initializeRepeatingEffects();
	}

	private void initializeUniqueNPCs() {
		Stream.of(UniqueNPC.values())
			.forEach(u ->
				u.getNPCs().stream()
					.filter(n -> isIn(n.getStoredLocation()))
					.forEach(n -> u.despawn(n.getId()))
		);
	}

	private void initializeRepeatingEffects() {
		this.repeatingEffects.stream().forEach(e -> e.setRepeat(false));
		this.repeatingEffects = new HashSet<>();

		List<RepeatingEffect> effects = getBlocks().stream()
			.filter(b -> b.getType() == ClickBlockChestAbility.CHEST_MATERIAL)
			.filter(b -> b.getState() instanceof Chest)
			.map(b -> (Chest) b.getState())
			.flatMap(b -> Stream.of(b.getInventory().getContents())
				.filter(i -> i != null)
				.flatMap(i -> ItemStackAbilityManager.find(i).stream())
				.filter(a -> a instanceof ClickBlockChestAbility)
				.map(a -> (ClickBlockChestAbility) a)
				.map(a -> a.createPeriodicEffect(b.getLocation().add(ClickBlockChestAbility.POSITION_TO_BLOCK)))
			).filter(e -> e instanceof RepeatingEffect)
			.map(e -> (RepeatingEffect) e)
			.collect(Collectors.toList());
		this.repeatingEffects.addAll(effects);
		this.repeatingEffects.forEach(RepeatingEffect::startRepeating);
	}

	@Override
	public KotobaBlockStorage create(String name) {
		return new TBLTArena(name);
	}

	@Override
	public File getDirectory() {
		return DIRECTORY;
	}


	public List<Location> getCheckPoints() {
		return checkPoints;
	}

	public Location getCurrentPoint() {
		if(currentPoint == null) {
			return getSpawn();
		}
		return currentPoint;
	}

	public void setCurrentPoint(Location location) {
		findNearestCheckPoint(location)
			.ifPresent(a -> this.currentPoint = a);
	}

	public List<RepeatingEffect> findRepeatingEffects(Location blockLocation) {
		return this.repeatingEffects.stream()
			.filter(e -> e.getBlockLocation().clone().distance(blockLocation.clone()) == 0)
			.collect(Collectors.toList());
	}

	public ItemStack getPredictionWrittenBook() {
		return predictionItem;
	}

	public void setPredictionItem(BookMeta bookMeta) {
		ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
		item.setItemMeta(bookMeta);
		this.predictionItem = item;
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

