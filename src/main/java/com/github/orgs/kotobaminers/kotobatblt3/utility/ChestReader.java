package com.github.orgs.kotobaminers.kotobatblt3.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.userinterface.Holograms;
import com.github.orgs.kotobaminers.kotobaapi.userinterface.RepeatingEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArena;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.TBLTPlayer;
import com.github.orgs.kotobaminers.kotobatblt3.quest.QuestManager;
import com.github.orgs.kotobaminers.kotobatblt3.userinterface.RepeatingEffectHolderManager;

import net.md_5.bungee.api.ChatColor;

public class ChestReader {


	public static List<ItemStack> extractItems(Chest chest) {
		return Stream.of(chest.getInventory().getContents())
				.filter(i -> i != null)
				.collect(Collectors.toList());
	}


	public static void updateArena(Chest chest) {
		new TBLTArenaMap().findUnique(chest.getLocation())
			.map(a -> (TBLTArena) a)
			.ifPresent(a -> {
				List<ItemStack> items = extractItems(chest);
				updateArenaSpawn(items, chest.getLocation(), a);
				updateNextArena(items, a);
				updateQuests(chest, a);
				updateJobItems(items, a);
			});
	}

	private static void updateJobItems(List<ItemStack> items, TBLTArena arena) {
		TBLTItemStackIcon target = TBLTItemStackIcon.JOB_ITEMS;
		if(!items.stream().anyMatch(i -> target.isIconItemStack(i))) return;

		List<ItemStack> options = items.stream()
			.filter(i -> !target.isIconItemStack(i))
			.collect(Collectors.toList());
		if(1 < options.size()) {
			ItemStack o1 = options.get(0);
			Stream.of(TBLTPlayer.values())
				.filter(p -> p.getAbilityIcons().stream().anyMatch(icon -> icon.isIconItemStack(o1)))
				.findFirst()
				.ifPresent(p -> {
					List<ItemStack> jobItems = Stream.iterate(1, i -> i + 1).limit(options.size() - 1).map(i -> options.get(i)).collect(Collectors.toList());
					arena.getArenaMeta().getJobItems().put(p, jobItems);
				});
		}
	}


	private static void updateNextArena(List<ItemStack> items, TBLTArena arena) {
		TBLTItemStackIcon target = TBLTItemStackIcon.ARENA_NEXT;
		if(!items.stream().anyMatch(i -> target.isIconItemStack(i))) return;

		List<ItemStack> options = items.stream()
			.filter(i -> !target.isIconItemStack(i))
			.collect(Collectors.toList());
		if(0 < options.size()) {
			ItemStack o1 = options.get(0);
			List<String> lore = o1.getItemMeta().getLore();
			if(0 < lore.size()) {
				try {
					int next = Integer.parseInt(ChatColor.stripColor(lore.get(0)));
					System.out.println(next);
					arena.getArenaMeta().setNext(next);
					return;
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void updateQuests(Chest chest, TBLTArena arena) {
		List<ItemStack> items = extractItems(chest);
		QuestManager.QUESTS.stream()
			.filter(q -> items.stream().anyMatch(item -> q.getIcon().isIconItemStack(item)))
			.findFirst()
			.ifPresent(q -> {
				q.tryCreate(chest).ifPresent(quest -> arena.getArenaMeta().getQuests().add(quest));
			});
	}


	private static void updateArenaSpawn(List<ItemStack> items, Location chestLocation, TBLTArena arena) {
		TBLTItemStackIcon target = TBLTItemStackIcon.ARENA_SPAWN;
		if(!items.stream().anyMatch(i -> target.isIconItemStack(i))) return;
		List<ItemStack> options = items.stream()
			.filter(i -> !target.isIconItemStack(i))
			.collect(Collectors.toList());
		if(0 < options.size()) {
			ItemStack o1 = options.get(0);
			if(TBLTItemStackIcon.DIRECTION.isIconItemStack(o1)) {
				Location spawn = chestLocation.clone().add(0.5, 2, 0.5);
				List<String> lore = o1.getItemMeta().getLore();
				if(2 < lore.size()) {
					lore = lore.stream().map(l -> ChatColor.stripColor(l)).collect(Collectors.toList());
					try {
						double x = Double.parseDouble(lore.get(0));
						double y = Double.parseDouble(lore.get(1));
						double z = Double.parseDouble(lore.get(2));
						Vector direction = new Vector(x, y, z);
						spawn.setDirection(direction);

						if(1 < options.size()) {
							ItemStack o2 = options.get(1);
							Optional<TBLTPlayer> job = Stream.of(TBLTPlayer.values())
									.filter(p -> p.getAbilityIcons().stream().anyMatch(i -> i.isIconItemStack(o2)))
									.findFirst();
							if(job.isPresent()) {
								arena.getArenaMeta().getJobSpawn().put(job.get(), spawn);
								return;
							}
						}
						return;

					} catch(NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}


	public static List<ItemStack> findItemsInRow(Chest chest, int row) {
		ItemStack[] contents = chest.getInventory().getContents();
		if((row + 1) * 9 - 1 <= contents.length) {
			return Stream.iterate(0, i -> i + 1)
				.limit(9)
				.map(i -> contents[row * 9 + i])
				.filter(i -> i != null)
				.collect(Collectors.toList());
		}
		return new ArrayList<>();
	}


	public static List<ItemStack> findOptions(Chest chest, KotobaItemStackIcon target, List<KotobaItemStackIcon> exceptions) {
		return Stream.iterate(0 , i -> i + 1)
			.limit(3)
			.map(i -> findItemsInRow(chest, i))
			.filter(list -> list.stream().anyMatch(i -> target.isIconItemStack(i)))
			.flatMap(list -> list.stream())
			.filter(i -> !target.isIconItemStack(i))
			.filter(i -> !exceptions.stream().anyMatch(e -> e.isIconItemStack(i)))
			.collect(Collectors.toList());
	}


	public static boolean hasTriggersItem(Chest chest, Player player) {
		List<ItemStack> triggers = findTriggersItem(chest);
		if(triggers.size() == 0) {
			return true;
		} else {
			List<ItemStack> items = Stream.of(player.getInventory().getContents())
				.filter(i -> i != null)
				.collect(Collectors.toList());
			return triggers.stream().allMatch(t -> items.stream().anyMatch(i -> i.isSimilar(t)));
		}
	}


	public static List<ItemStack> findTriggersItem(Chest chest) {
		return Stream.iterate(0, i -> i + 1)
			.limit(3)
			.map(i -> ChestReader.findItemsInRow(chest, i))
			.filter(list ->
				list.stream()
					.anyMatch(i -> TBLTItemStackIcon.TRIGGER_ITEM_AT_LEAST.isIconItemStack(i))
			)
			.flatMap(list -> list.stream())
			.filter(i -> !TBLTItemStackIcon.TRIGGER_ITEM_AT_LEAST.isIconItemStack(i))
			.collect(Collectors.toList());
	}


	public static void displayHolograms(Chest chest) {
		Inventory inventory = chest.getInventory();
		if(!Stream.of(inventory.getContents()).filter(i -> i != null).anyMatch(i -> TBLTItemStackIcon.CHEST_HOLOGRAMS.isIconItemStack(i))) {
			return;
		}

		Stream.iterate(0, i -> i + 1)
			.limit(inventory.getSize())
			.forEach(i -> {
				Optional.ofNullable(inventory.getItem(i))
					.map(item -> item.getItemMeta())
					.filter(m -> m instanceof BookMeta)
					.map(m -> (BookMeta) m)
					.ifPresent(m -> new Holograms().display(KotobaUtility.toStringListFromBookMeta(m), chest.getLocation().clone().add(0.5, 0.5 + i * 0.5, 0.5)));
			})
			;
	}


	@Deprecated
	public static List<RepeatingEffect> findRepeatingEffects(Chest chest) {
		return Stream.of(chest.getInventory().getContents())
			.filter(i -> i != null)
			.flatMap(i -> TBLTItemStackIcon.find(i).stream())
			.flatMap(icon -> RepeatingEffectHolderManager.findHolders(icon).stream())
			.flatMap(holder -> holder.createPeriodicEffects(chest.getLocation()).stream())
			.collect(Collectors.toList());
	}


	public static boolean checkPattern3By3(Chest chest, Vector offset) {
		Location origin = chest.getLocation().clone().add(offset);
		return findPattern3By3(chest)
			.map(pattern -> pattern.entrySet().stream().map(e -> origin.clone().add(e.getKey()).getBlock().getType() == e.getValue()).allMatch(b -> b == true))
			.orElse(false);
	}


	public static Optional<Map<Vector, Material>> findPattern3By3(Chest chest) {
		boolean is3x3 = Stream.of(chest.getInventory().getContents())
			.filter(i -> i != null)
			.anyMatch(i -> TBLTItemStackIcon.GEM_PORTAL_KEY_3x3.isIconItemStack(i));

		if(!is3x3) return Optional.empty();

		Map<Vector, Material> pattern = new HashMap<Vector, Material>();
		Map<Vector, Integer> index = new HashMap<Vector, Integer>() {{
			put(new Vector(-1,0,-1), 3);
			put(new Vector(0,0,-1), 4);
			put(new Vector(1,0,-1), 5);
			put(new Vector(-1,0,0), 12);
			put(new Vector(0,0,0), 13);
			put(new Vector(1,0,0), 14);
			put(new Vector(-1,0,1), 21);
			put(new Vector(0,0,1), 22);
			put(new Vector(1,0,1), 23);
		}};

		Inventory inventory = chest.getInventory();
		index.entrySet().stream()
			.forEach(e -> {
				ItemStack item = inventory.getItem(e.getValue());
				if(item == null) {
					item = new ItemStack(Material.AIR);
				}
				pattern.put(e.getKey(), item.getType());
			});

		return Optional.of(pattern);

	}


}

