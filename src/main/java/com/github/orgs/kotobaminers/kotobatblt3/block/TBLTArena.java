package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaTitle;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaTitle.TitleOption;
import com.github.orgs.kotobaminers.kotobaapi.worldeditor.BlockStorage;
import com.github.orgs.kotobaminers.kotobatblt3.ability.ClickBlockChestAbility;
import com.github.orgs.kotobaminers.kotobatblt3.game.TBLTJob.TBLTJobEnum;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;
import com.github.orgs.kotobaminers.kotobatblt3.resource.ResourceHolder;
import com.github.orgs.kotobaminers.kotobatblt3.resource.TBLTResource;
import com.github.orgs.kotobaminers.kotobatblt3.utility.RepeatingEffect;

import net.md_5.bungee.api.ChatColor;

public class TBLTArena extends BlockStorage implements ResourceHolder {


	private static final File DIRECTORY = new File(Setting.getPlugin().getDataFolder().getAbsolutePath() + "/Arena/");
	private static final String CHECK_POINT = "CheckPoint";

	private Map<TBLTResource, Integer> resources = new TreeMap<>();
	private List<Location> checkPoints = new ArrayList<>();
	private Location currentPoint = null;
	private Set<RepeatingEffect> repeatingEffects = new HashSet<>();


	protected TBLTArena(String name) {
		super(name);
	}


	public static TBLTArena create(String name, Player player) {
		return (TBLTArena) (new TBLTArena(name)).setData(name, player);
	}


	private void start(Player player, Location location) {
		TBLTJobEnum.initializeInventory(player);
		player.teleport(location);
		KotobaEffect.ENDER_SIGNAL.playEffect(location);
		KotobaEffect.ENDER_SIGNAL.playSound(location);
		player.playSound(location, Sound.AMBIENCE_CAVE, 10, 0.5F);
		KotobaTitle.displayTitle(player, getName(), ChatColor.RED, Arrays.asList(TitleOption.BOLD, TitleOption.ITALIC));
	}

	public void startSpawn(Player player) {
		start(player, getSpawn());
	}

	public void startCurrent(Player player) {
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
		List<Location> points = nearest.map(loc -> checkPoints.stream().filter(point -> 0 != point.distance(loc)).collect(Collectors.toList()))
			.orElse(checkPoints);
		this.checkPoints = points;
	}


	@Override
	protected void saveOptions(YamlConfiguration config) {
		Stream.iterate(0, i -> i + 1)
			.limit(checkPoints.size())
			.forEach(i -> config.set(CHECK_POINT + "." + i, checkPoints.get(i)));
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
	}

	@Override
	protected void loadFromWorld() {
		initializeRepeatingEffects();
	}

	private void initializeRepeatingEffects() {
		this.repeatingEffects.stream().forEach(e -> e.setRepeat(false));
		this.repeatingEffects = new HashSet<>();

		List<ItemStack> icons = Stream.of(ClickBlockChestAbility.values())
				.map(ability -> ability.createItem(1))
				.collect(Collectors.toList());

		List<RepeatingEffect> effects = getBlocks().stream()
			.filter(b -> b.getType() == ClickBlockChestAbility.CHEST_MATERIAL)
			.filter(b -> b.getState() instanceof Chest)
			.map(b -> (Chest) b.getState())
			.flatMap(b -> Stream.of(b.getInventory().getContents())
				.filter(i -> i != null)
				.filter(i -> icons.stream().anyMatch(icon -> icon.isSimilar(i)))
				.flatMap(icon -> ClickBlockChestAbility.find(icon).stream().map(a -> a.createPeriodicEffect(b.getLocation().add(ClickBlockChestAbility.POSITION_TO_BLOCK))))
			).filter(e -> e instanceof RepeatingEffect)
			.map(e -> (RepeatingEffect) e)
			.collect(Collectors.toList());
		this.repeatingEffects.addAll(effects);
		this.repeatingEffects.forEach(RepeatingEffect::startRepeating);
	}

	@Override
	public BlockStorage create(String name) {
		return new TBLTArena(name);
	}

	@Override
	public File getDirectory() {
		return DIRECTORY;
	}

	@Override
	public Map<TBLTResource, Integer> getResources() {
		return resources;
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
			.filter(e -> e.getBlockLocation().distance(blockLocation) == 0)
			.collect(Collectors.toList());
	}


}

