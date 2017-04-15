package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockData;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.ability.TBLTGem;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public class ReplacerSwitchChest implements SwitchableChest {


	private static final String PREFIX_MATERIAL = "Material: ";
	private static final String PREFIX_DATA = "Data: ";
	private static final int MAX_HEIGHT = 7;
	private static final List<BlockFace> FACES = Arrays.asList(BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST);


	@Override
	public void turnOn(Chest chest) {
		replaceBlocks(chest, TBLTItemStackIcon.SWITCH_REPLACER_ON);
	}


	@Override
	public void turnOff(Chest chest) {
		replaceBlocks(chest, TBLTItemStackIcon.SWITCH_REPLACER_OFF);
	}


	@Override
	public KotobaItemStackIcon getIcon() {
		return TBLTItemStackIcon.SWITCH_REPLACER_ON;
	}


	private void replaceBlocks(Chest chest, TBLTItemStackIcon target) {
		FACES.stream()
			.filter(f -> Stream.of(TBLTGem.values()).anyMatch(g -> g.isWire(chest.getBlock().getRelative(f))))
			.forEach(f -> {
				List<KotobaBlockData> data = createData(chest, f, target);
				KotobaBlockData.placeBlockSafe(data);
				data.forEach(d -> KotobaEffect.MAGIC_SMALL.playEffect(d.getLocation()));
			});
	}


	//TODO validate
	private List<KotobaBlockData> createData(Chest chest, BlockFace face, TBLTItemStackIcon target) {
		Vector offset = new Vector(0,0,0);
		switch(face) {
		case NORTH:
			offset.add(new Vector(0,0,-1));
			break;
		case SOUTH:
			offset.add(new Vector(0,0,1));
			break;
		case EAST:
			offset.add(new Vector(1,0,0));
			break;
		case WEST:
			offset.add(new Vector(-1,0,0));
			break;
		case UP:
			offset.add(new Vector(0,1,0));
			break;
		case DOWN:
		default:
			offset.add(new Vector(0,-1,0));
			break;
		}

		Location origin = chest.getLocation();
		List<ItemStack> contents = Arrays.asList(chest.getInventory().getContents());

		final Integer rowSize = 3;
		final Integer targetColumn = 1;
		final Integer columnStart = 2;
		final Integer columnLength = 7;
		List<KotobaBlockData> data = Stream.iterate(0, r -> r + 1)
			.limit(rowSize)
			.filter(r ->
				Stream.iterate(r * 9 + targetColumn, i -> i + 1)
					.limit(rowSize)
					.map(i -> contents.get(i))
					.filter(i -> i != null)
					.anyMatch(i -> target.isIconItemStack(i))
			)
			.findFirst()
			.map(r ->
				Stream.iterate(r * 9 + columnStart, i -> i + 1)
					.limit(columnLength)
					.filter(i -> contents.get(i) != null)
					.map(i -> toBlockData(contents.get(i), origin.clone().add(offset.clone().multiply(i % 9))))
					.filter(o -> o.isPresent())
					.map(Optional::get)
					.collect(Collectors.toList())
			).orElse(new ArrayList<>());
		return data;
	}


	private Optional<KotobaBlockData> toBlockData(ItemStack item, Location location) {
		if(TBLTItemStackIcon.SWITCH_MATERIAL.isIconItemStack(item)) {
			ItemMeta itemMeta = item.getItemMeta();
			List<String> lore = itemMeta.getLore();
			if(lore != null) {
				if(1 < lore.size()) {
					String materialStr = ChatColor.stripColor(lore.get(0));
					String dataStr = ChatColor.stripColor(lore.get(1));
					if(materialStr.startsWith(PREFIX_MATERIAL) && dataStr.startsWith(PREFIX_DATA)) {
						dataStr = dataStr.substring(PREFIX_DATA.length());
						String materialStr2 = materialStr.substring(PREFIX_MATERIAL.length());
						try {
							int data = Integer.parseInt(dataStr);
							return Stream.of(Material.values())
								.filter(m -> m.name().equalsIgnoreCase(materialStr2))
								.findFirst()
								.map(m -> new KotobaBlockData(location, m, data));
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return Optional.empty();
	}

	public void placeChestsOn(Player player, BlockFace face, TBLTGem gem) {
		placeChests(player, face, gem, Arrays.asList(SetType.ON));
	}
	public void placeChestsOnAir(Player player, BlockFace face, TBLTGem gem) {
		placeChests(player, face, gem, Arrays.asList(SetType.ON, SetType.OFF_AIR));
	}
	public void placeChestsOff(Player player, BlockFace face, TBLTGem gem) {
		placeChests(player, face, gem, Arrays.asList(SetType.OFF));
	}
	public void placeChestsOffAir(Player player, BlockFace face, TBLTGem gem) {
		placeChests(player, face, gem, Arrays.asList(SetType.ON_AIR, SetType.OFF));
	}


	private void placeChests(Player player, BlockFace face, TBLTGem gem, List<SetType> setTypes) {
		List<Block> blocks = KotobaUtility.getSelectBlocks(player).stream()
			.collect(Collectors.toList());

		int max = blocks.stream().map(b -> b.getLocation().getBlockY()).max(Comparator.naturalOrder()).orElse(0);
		int min = blocks.stream().map(b -> b.getLocation().getBlockY()).min(Comparator.naturalOrder()).orElse(0);

		if(MAX_HEIGHT < max - min + 1) {
			return;
		}

		List<List<Block>> groups = new TBLTArenaMap().findUnique(player.getLocation())
			.filter(a -> blocks.stream().allMatch(b -> a.isIn(b.getLocation())))
			.map(a -> groupBlocks(blocks, face))
			.map(llb -> llb.stream().filter(lb -> !lb.stream().allMatch(b -> b.getType() == Material.AIR)).collect(Collectors.toList()))
			.orElse(new ArrayList<>());

		groups.forEach(list ->
			list.stream().findFirst()
				.map(first -> first.getLocation())
				.ifPresent(l -> {
					new KotobaBlockData(addDirection(l, face, 1), gem.getSwitchMaterial(), gem.getSwitchData()).placeBlock();
					Location chest = addDirection(l, face, 2);
					new KotobaBlockData(chest, getChestMaterial(l), 0).placeBlock();
					if(chest.getBlock().getState() instanceof Chest) {
						Inventory inventory = ((Chest) chest.getBlock().getState()).getInventory();
						List<ItemStack> items = list.stream().map(b -> toItemStack(b)).collect(Collectors.toList());
						setTypes.forEach(t -> t.setItems(inventory, items));
					}
				})
			);
	}

	private enum SetType {
		ON(TBLTItemStackIcon.SWITCH_REPLACER_ON, 0, false),
		OFF(TBLTItemStackIcon.SWITCH_REPLACER_OFF, 1, false),
		ON_AIR(TBLTItemStackIcon.SWITCH_REPLACER_ON, 0, true),
		OFF_AIR(TBLTItemStackIcon.SWITCH_REPLACER_OFF, 1, true),
		;


		private TBLTItemStackIcon icon;
		private int row;
		private boolean air;


		private SetType(TBLTItemStackIcon icon, int row, boolean air) {
			this.icon = icon;
			this.row = row;
			this.air = air;
		}

		public void setItems(Inventory inventory, List<ItemStack> given) {
			clearRow(inventory);

			List<ItemStack> items = new ArrayList<ItemStack>();
			items.addAll(Arrays.asList(TBLTItemStackIcon.SWITCH_KEY.create(1), icon.create(1)));

			if(air) {
				items.addAll(Stream.iterate(0, i -> i + 1).limit(given.size()).map(i -> toItemStack(Material.AIR, new Integer(0).byteValue())).collect(Collectors.toList()));
			} else {
				items.addAll(given);
			}
			Stream.iterate(0, i -> i + 1)
				.limit(Math.min(items.size(), 7))
				.forEach(i -> inventory.setItem(row * 9 + i, items.get(i)));
		}

		private void clearRow(Inventory inventory) {
			Stream.iterate(row * 9, i -> i + 1)
				.limit(9)
				.forEach(i -> inventory.clear(i));
		}

	}


	private List<List<Block>> groupBlocks(List<Block> blocks, BlockFace face) {
		Function<Location, Integer> groupingX = l -> l.getBlockX();
		Function<Location, Integer> groupingY = l -> l.getBlockY();
		Function<Location, Integer> groupingZ = l -> l.getBlockZ();
		Comparator<Block> compareX = (b1, b2) -> b1.getX() - b2.getX();
		Comparator<Block> compareY = (b1, b2) -> b1.getY() - b2.getY();
		Comparator<Block> compareZ = (b1, b2) -> b1.getZ() - b2.getZ();

		BiFunction<Function<Location, Integer>, Function<Location, Integer>, List<List<Block>>> group = (g1, g2) ->
			blocks.stream()
				.collect(Collectors.groupingBy(b -> g1.apply(b.getLocation())))
				.values().stream()
				.flatMap(list -> list.stream().collect(Collectors.groupingBy(b -> g2.apply(b.getLocation()))).values().stream())
				.collect(Collectors.toList());

		BiFunction<List<List<Block>>, Comparator<Block>, List<List<Block>>> sort = (b, comparator) ->
			b.stream()
				.map(b2 -> {
					b2.sort(comparator);
					return b2;
				})
				.collect(Collectors.toList());


		switch(face) {
		case EAST:
			return sort.apply(group.apply(groupingY, groupingZ), compareX.reversed());
		case WEST:
			return sort.apply(group.apply(groupingY, groupingZ), compareX);
		case SOUTH:
			return sort.apply(group.apply(groupingX, groupingY), compareZ.reversed());
		case NORTH:
			return sort.apply(group.apply(groupingX, groupingY), compareZ);
		case UP:
			return sort.apply(group.apply(groupingX, groupingZ), compareY.reversed());
		case DOWN:
		default:
			return sort.apply(group.apply(groupingX, groupingZ), compareY);
		}
	}


	private Location addDirection(Location location, BlockFace face, int magnitude) {
		Vector vector = new Vector(0,0,0);
		switch(face) {
		case UP:
			vector = new Vector(0,1,0);
			break;
		case NORTH:
			vector = new Vector(0,0,-1);
			break;
		case SOUTH:
			vector = new Vector(0,0,1);
			break;
		case EAST:
			vector = new Vector(1,0,0);
			break;
		case WEST:
			vector = new Vector(-1,0,0);
			break;
		default:
			vector = new Vector(0,-1,0);
			break;
		}
		return location.clone().add(vector.multiply(magnitude));
	}


	private Material getChestMaterial(Location location) {
		if((location.getBlockX() + location.getBlockZ()) % 2 == 0) {
			return Material.CHEST;
		} else {
			return Material.TRAPPED_CHEST;
		}
	}


	@SuppressWarnings("deprecation")
	private static ItemStack toItemStack(Block block) {
		return toItemStack(block.getType(), block.getData());
	}

	private static ItemStack toItemStack(Material material, Byte data) {
		ItemStack itemStack = TBLTItemStackIcon.SWITCH_MATERIAL.create(1);

		ItemMeta meta = itemStack.getItemMeta();

		meta.setLore(Arrays.asList(PREFIX_MATERIAL + material, PREFIX_DATA + data.toString()));
		itemStack.setItemMeta(meta);

		return itemStack;
	}



}

