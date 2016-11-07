package com.github.orgs.kotobaminers.kotobatblt3.game;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaAPIUtility;
import com.github.orgs.kotobaminers.kotobaapi.worldeditor.BlockStorage;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;

public class BlockReplacer extends BlockStorage {

	interface BlockReplaceEventInterface {
		void perform(BlockReplacer replacer);
	}

	public enum BlockReplacerEvent implements BlockReplaceEventInterface {
		REPLACE {
			@Override
			public void perform(BlockReplacer replacer) {
				replacer.load(Bukkit.getWorlds());
			}
		}
	}

	private static final File DIRECTORY = new File(Setting.getPlugin().getDataFolder().getAbsolutePath() + "/BlockReplacer/");
	private static final String BLOCK_PATH = "Block";
	private static final String TRIGGER_PATH = "Trigger";

	private static Set<BlockReplacer> replacers = new HashSet<>();
	private Material block;
	private Material trigger;
	private List<BlockReplacerEvent> events;

	private static final List<Material> BLOCKS = Stream.of(Material.values())
		.filter(m -> m.isBlock() && m.isSolid())
		.collect(Collectors.toList());
	private static final List<Material> TRIGGERS = Stream.of(Material.values())
		.filter(m -> !m.isBlock())
		.collect(Collectors.toList());

	private BlockReplacer(String name) {
		setName(name);
	}

	public static BlockReplacer create(String name) {
		BlockReplacer arena = new BlockReplacer(name);
		return arena;
	}

	@Override
	public File getDirectory() {
		return DIRECTORY;
	}

	public static void importAll() {
		replacers = new HashSet<>();
		Stream.of(DIRECTORY.listFiles())
			.map(f -> f.getName().substring(0, f.getName().length()-".yml".length()))
			.forEach(name -> {
				BlockReplacer replacer = BlockReplacer.create(name);
				replacer.setDataFromConfig(Bukkit.getWorlds());
				replacer.importOptions();
				addReplacer(replacer);
			});
	}

	public static void addReplacer(BlockReplacer replacer) {
		replacers.removeIf(r -> r.getName().equalsIgnoreCase(replacer.getName()));
		replacers.add(replacer);
	}

	public static boolean isInReplacer(Location location) {
		return replacers
			.stream()
			.anyMatch(arena -> arena.isLocationIn(location));
	}

	public static void saveAll() {
		replacers.stream().forEach(BlockStorage::save);
		replacers.stream().forEach(BlockReplacer::saveOptions);
	}

	public static Set<BlockReplacer> getReplacers() {
		return replacers;
	}

	public Material getBlock() {
		return block;
	}
	public Material getTrigger() {
		return trigger;
	}
	public void setBlock(Material block) {
		this.block = block;
	}
	public void setTrigger(Material trigger) {
		this.trigger = trigger;
	}
	public static boolean isBlock(Material material) {
		return BLOCKS.contains(material);
	}
	public static boolean isTrigger(Material material) {
		return TRIGGERS.contains(material);
	}

	public void saveOptions() {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(getFile());
		if(block != null) config.set(BLOCK_PATH, block.name());
		if(trigger != null) config.set(TRIGGER_PATH, trigger.name());
		try {
			config.save(getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void importOptions() {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(getFile());
		if(config.isString(BLOCK_PATH)) {
			Stream.of(Material.values())
				.filter(m -> m.name().equalsIgnoreCase(config.getString(BLOCK_PATH)))
				.filter(m -> BLOCKS.contains(m))
				.findAny()
				.ifPresent(m -> setBlock(m));
		}
		if(config.isString(TRIGGER_PATH)) {
			Stream.of(Material.values())
				.filter(m -> m.name().equalsIgnoreCase(config.getString(TRIGGER_PATH)))
				.filter(m -> TRIGGERS.contains(m))
				.findAny()
				.ifPresent(m -> setTrigger(m));
		}
	}

	public boolean isValidBlockAndTrigger(Player player, Block clicked) {
		if(clicked.getType().equals(block) && player.getItemInHand().getType().equals(trigger)) return true;
		return false;
	}

	public List<ItemStack> getGUIIcons() {
		ItemStack blockItem = Optional.ofNullable(getBlock())
			.map(m -> new ItemStack(getBlock(), 1))
			.orElse(KotobaAPIUtility.createCustomItem(Material.GLASS, 1, (short) 0, "Block Not Set", null).get());
		ItemStack triggerItem = Optional.ofNullable(getTrigger())
			.map(m -> new ItemStack(getTrigger(), 1))
			.orElse(KotobaAPIUtility.createCustomItem(Material.STICK, 1, (short) 0, "Trigger Not Set", null).get());
		ItemStack teleport = KotobaAPIUtility.createCustomItem(Material.COMPASS, 1, (short) 0, "Teleport", null).get();
		return Arrays.asList(blockItem, triggerItem, teleport);
	}


}
