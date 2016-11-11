package com.github.orgs.kotobaminers.kotobatblt3.game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaAPIItemStack;
import com.github.orgs.kotobaminers.kotobaapi.worldeditor.BlockStorage;
import com.github.orgs.kotobaminers.kotobatblt3.ability.ClickAbility;
import com.github.orgs.kotobaminers.kotobatblt3.gui.TBLTIcon;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;

public class BlockReplacer extends BlockStorage {

	interface BlockReplaceEventInterface {
		void perform(BlockReplacer replacer);
	}

	private static final File DIRECTORY = new File(Setting.getPlugin().getDataFolder().getAbsolutePath() + "/BlockReplacer/");
	private static final String BLOCK_PATH = "Block";
	private static final String TRIGGER_PATH = "Trigger";

	private static Set<BlockReplacer> replacers = new HashSet<>();
	private Material blockTrigger;
	private Material itemTrigger;

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

	public Material getBlockTrigger() {
		return blockTrigger;
	}
	public Material getItemTrigger() {
		return itemTrigger;
	}
	public void setBlock(Material block) {
		this.blockTrigger = block;
	}
	public void setTrigger(Material trigger) {
		this.itemTrigger = trigger;
	}
	public static boolean isBlock(Material material) {
		return BLOCKS.contains(material);
	}
	public static boolean isTrigger(Material material) {
		return TRIGGERS.contains(material);
	}

	public void saveOptions() {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(getFile());
		if(blockTrigger != null) config.set(BLOCK_PATH, blockTrigger.name());
		if(itemTrigger != null) config.set(TRIGGER_PATH, itemTrigger.name());
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
		if(clicked.getType().equals(blockTrigger) && player.getItemInHand().getType().equals(itemTrigger)) return true;
		return false;
	}
	public boolean isInvestigateBlock(Player player, Block clicked) {
		if(player.getItemInHand().getType().equals(ClickAbility.INVESTIGATE.getMaterial()) && clicked.getType().equals(blockTrigger)) return false;
		return false;
	}

	public List<ItemStack> getGUIIcons() {
		List<ItemStack> icons = new ArrayList<ItemStack>();
		KotobaAPIItemStack information = TBLTIcon.INFORMATION.createItemStack();
		information.setLore(Arrays.asList(getName()));

		KotobaAPIItemStack blockItem = Optional.ofNullable(getBlockTrigger())
			.map(m -> KotobaAPIItemStack.create(m, 1, (short) 0, "Block Trigger", null))
			.orElse(KotobaAPIItemStack.create(Material.GLASS, 1, (short) 0, "Block Not Set", null));
		icons.add(blockItem);

		KotobaAPIItemStack triggerItem = Optional.ofNullable(getItemTrigger())
				.map(m -> KotobaAPIItemStack.create(m, 1, (short) 0, "Item Trigger", null))
			.orElse(KotobaAPIItemStack.create(Material.STICK, 1, (short) 0, "Trigger Not Set", null));
		icons.add(triggerItem);

		KotobaAPIItemStack teleport = TBLTIcon.TELEPORT.createItemStack();
		teleport.setLore(Arrays.asList(getWorld().getName(), String.valueOf(getCenter().getBlockX()) + "," + String.valueOf(getCenter().getBlockY()) + "," + String.valueOf(getCenter().getBlockZ())));
		return Arrays.asList(information, blockItem, triggerItem, teleport);
	}

	public List<ItemStack> getInvestigateIcons() {
		List<ItemStack> icons = new ArrayList<ItemStack>();

		KotobaAPIItemStack blockItem = Optional.ofNullable(getBlockTrigger())
			.map(m -> KotobaAPIItemStack.create(m, 1, (short) 0, "Block Trigger", null))
			.orElse(KotobaAPIItemStack.create(Material.GLASS, 1, (short) 0, "Block Not Set", null));
			icons.add(blockItem);

		KotobaAPIItemStack triggerItem = Optional.ofNullable(getItemTrigger())
			.map(m -> KotobaAPIItemStack.create(m, 1, (short) 0, "Item Trigger", null))
			.orElse(KotobaAPIItemStack.create(Material.STICK, 1, (short) 0, "Trigger Not Set", null));
			icons.add(triggerItem);

		return icons;
	}

}
