package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArena;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTPortal;
import com.github.orgs.kotobaminers.kotobatblt3.citizens.UniqueNPC;
import com.github.orgs.kotobaminers.kotobatblt3.game.TBLTData;
import com.github.orgs.kotobaminers.kotobatblt3.gui.TBLTPlayerGUI;
import com.github.orgs.kotobaminers.kotobatblt3.resource.TBLTResource;
import com.github.orgs.kotobaminers.kotobatblt3.utility.RepeatingEffect;

public enum ClickBlockChestAbility implements ClickBlockAbilityInterface {


	OPEN_PORTAL(
		Material.NETHER_STAR,
		(short) 0,
		"Open portal",
		null,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK),
		0,
		FindType.UNDER_CHEST,
		KotobaEffect.TWINCLE_MIDIUM,
		KotobaEffect.TWINCLE_MIDIUM
	) {
		@Override
		public boolean performAbility(PlayerInteractEvent event) {
			Block block = event.getClickedBlock();
			if(block.getType() == Material.ENDER_PORTAL) return false;
			TBLTPortal.find(findOptions(block.getLocation()))
				.ifPresent(portal -> portal.openPortal(event));
			return true;
		}
	},

	INVESTIGATE(
		Material.TRIPWIRE_HOOK,
		(short) 0,
		"Investigate",
		null,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK),
		0,
		FindType.UNDER_CHEST,
		KotobaEffect.TWINCLE_MIDIUM,
		KotobaEffect.TWINCLE_MIDIUM
	) {
		@Override
		public boolean performAbility(PlayerInteractEvent event) {
			Block block = event.getClickedBlock();
			List<ItemStack> options = findOptions(block.getLocation());

			List<ItemStack> informations = options.stream()
				.filter(i -> i.getType() == Material.BOOK_AND_QUILL)
				.map(i -> (BookMeta) i.getItemMeta())
				.map(meta -> KotobaUtility.toStringListFromBookMeta(meta))
				.map(lore -> {
					ItemStack item = createItem(1);
					ItemMeta itemMeta = item.getItemMeta();
					itemMeta.setLore(lore);
					item.setItemMeta(itemMeta);
					return item;
				})
				.collect(Collectors.toList());

			informations.addAll(options);
			Player player = event.getPlayer();
			if(0 < informations.size()) {
				TBLTPlayerGUI.INVESTIGATE.create(informations)
					.ifPresent(inventory -> {
						player.openInventory(inventory);
					});
				return true;
			}
			return false;
		}
	},

	FIND_SPELL(
		Material.ENCHANTED_BOOK,
		(short) 0,
		"Find a new spell",
		null,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK),
		0,
		FindType.UNDER_CHEST,
		KotobaEffect.TWINCLE_MIDIUM,
		KotobaEffect.TWINCLE_MIDIUM
	) {
		@Override
		public boolean performAbility(PlayerInteractEvent event) {
			TBLTPlayerGUI.ITEM_EXCHANGER.create(findOptions(event.getClickedBlock().getLocation()))
				.ifPresent(inventory -> {
					TBLTData.getOrDefault(event.getPlayer().getUniqueId()).target(event.getClickedBlock());
					event.getPlayer().openInventory(inventory);
				});
			return true;
		}
	},

	FIND_TOOL(
		Material.ENCHANTED_BOOK,
		(short) 0,
		"Find a new tool",
		null,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK),
		0,
		FindType.UNDER_CHEST,
		KotobaEffect.TWINCLE_MIDIUM,
		KotobaEffect.TWINCLE_MIDIUM
	) {
		@Override
		public boolean performAbility(PlayerInteractEvent event) {
			TBLTPlayerGUI.ITEM_EXCHANGER.create(findOptions(event.getClickedBlock().getLocation()))
				.ifPresent(inventory -> {
					TBLTData.getOrDefault(event.getPlayer().getUniqueId()).target(event.getClickedBlock());
					event.getPlayer().openInventory(inventory);
			});
			return true;
		}
	},

	SUMMON_SERVANT_1(
		Material.REDSTONE_TORCH_ON,
		(short) 0,
		"Summon a servant",
		null,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK),
		0,
		FindType.UNDER_CHEST,
		KotobaEffect.TWINCLE_MIDIUM,
		KotobaEffect.TWINCLE_MIDIUM
	) {
		@Override
		public boolean performAbility(PlayerInteractEvent event) {
			Block block = event.getClickedBlock();
			UniqueNPC unique = UniqueNPC.CAT;
			boolean success = findOptions(block.getLocation()).stream()
				.map(item -> unique.findNPCByKey(item))
				.filter(npc -> npc.isPresent())
				.map(Optional::get)
				.map(a -> {
					unique.despawnAll();
					unique.spawn(a.getId());
					return true;
				}).collect(Collectors.toList()).contains(true);
			return success;
		}


	},


	SUMMON_SERVANT_2(
		Material.SLIME_BALL,
		(short) 0,
		"Summon a servant",
		null,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK),
		0,
		FindType.UNDER_CHEST,
		KotobaEffect.TWINCLE_MIDIUM,
		KotobaEffect.TWINCLE_MIDIUM
	) {
		@Override
		public boolean performAbility(PlayerInteractEvent event) {
			Block block = event.getClickedBlock();
			UniqueNPC unique = UniqueNPC.SLIME;
			boolean success = findOptions(block.getLocation()).stream()
					.map(item -> unique.findNPCByKey(item))
					.filter(npc -> npc.isPresent())
					.map(Optional::get)
					.map(a -> {
						unique.despawnAll();
						unique.spawn(a.getId());
						return true;
					}).collect(Collectors.toList()).contains(true);
			return success;
		}


	},

	;


	public static final Material CHEST_MATERIAL = Material.TRAPPED_CHEST;

	private Material material;
	private short data;
	private String name;
	private List<String> lore;
	private List<Action> triggers;
	private int consume;
	private FindType findType;
	private int period = 10;
	private KotobaEffect effect;
	private KotobaEffect sound;


	public static final Vector POSITION_TO_BLOCK = new Vector(0, 2, 0);


	private ClickBlockChestAbility(
		Material material,
		short data,
		String name,
		List<String> lore,
		List<Action> triggers,
		int consume,
		FindType findType,
		KotobaEffect effect,
		KotobaEffect sound
	) {
		this.material = material;
		this.data = data;
		this.name = name;
		this.lore = lore;
		this.triggers = triggers;
		this.consume = consume;
		this.findType = findType;
		this.effect = effect;
		this.sound = sound;
	}


	public static List<ClickBlockChestAbility> find(PlayerInteractEvent event) {
		List<ClickBlockChestAbility> collect = Stream.of(ClickBlockChestAbility.values())
			.filter(ability -> ability.findType.canFind(event, ability))
			.collect(Collectors.toList());
		return collect;
	}

	public static List<ClickBlockChestAbility> find(ItemStack item) {
		List<ClickBlockChestAbility> collect = Stream.of(ClickBlockChestAbility.values())
			.filter(ability -> ability.createItem(1).isSimilar(item))
			.collect(Collectors.toList());
		return collect;
	}

	public RepeatingEffect createPeriodicEffect(Location location) {
		return RepeatingEffect.create(period, effect, sound, true, location);
	}


	private static Optional<Chest> findChest(Location location) {
		Block block = location.clone().add(POSITION_TO_BLOCK.multiply(-1)).getBlock();
		if(block.getState() instanceof Chest) {
			return Optional.ofNullable((Chest) block.getState());
		}
		return Optional.empty();
	}


	protected void clearContents(Location location) {
		findChest(location)
			.ifPresent(chest -> chest.getInventory().clear());
	}


	protected List<ItemStack> findOptions(Location location) {
		ItemStack item = createItem(1);
		return findChest(location)
			.map(chest -> {
				Inventory inventory = chest.getInventory();
				return Stream.iterate(0, i -> i + 1)
						.limit(inventory.getSize())
						.filter(i -> inventory.getItem(i) != null)
						.filter(i -> inventory.getItem(i).isSimilar(item))
						.findFirst()
						.map(i -> {
							int line = i / 9;
							return Stream.iterate(line * 9, j -> j + 1)
								.limit(9)
								.filter(j -> j != i)
								.filter(j -> inventory.getItem(j) != null)
								.map(j -> inventory.getItem(j))
								.collect(Collectors.toList());
							}
						).orElse(null);
			})
			.filter(list -> list != null)
			.filter(list -> 0 < list.size())
			.orElse(new ArrayList<ItemStack>());
	}


	@Override
	public boolean perform(PlayerInteractEvent event) {
		findEffects(event.getClickedBlock().getLocation()).forEach(e -> e.setRepeat(false));
		return performAbility(event);
	}

	public abstract boolean performAbility(PlayerInteractEvent event);

	private List<RepeatingEffect> findEffects(Location blockLocation) {
		return new TBLTArenaMap().findUnique(blockLocation)
			.map(storage -> (TBLTArena) storage)
			.map(arena -> arena.findRepeatingEffects(blockLocation))
			.orElse(new ArrayList<>());
	}


	@Override
	public short getData() {
		return data;
	}
	@Override
	public Material getMaterial() {
		return material;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public List<String> getLore() {
		return lore;
	}
	@Override
	public List<Action> getTriggers() {
		return triggers;
	}
	@Override
	public int getConsumption() {
		return consume;
	}
	@Override
	public Map<TBLTResource, Integer> getResourceConsumption(Block clicked) {
		if(clicked == null) return null;
		return TBLTResource.getResources(findOptions(clicked.getLocation()));
	}


	protected enum FindType {
		//Idea: UNDER_CHEST, NEXT_CHEST, NEAR_CHEST
		UNDER_CHEST {
			@Override
			protected boolean canFind(PlayerInteractEvent event, ClickBlockChestAbility ability) {
				if(event.getClickedBlock() == null) return false;
				if(!event.getPlayer().getItemInHand().isSimilar(ability.createItem(1))) return false;
				return 0 < ability.findOptions(event.getClickedBlock().getLocation()).size();
			}
		},

		ANYWHERE {
			@Override
			protected boolean canFind(PlayerInteractEvent event, ClickBlockChestAbility ability) {
				if(event.getClickedBlock() == null) return false;
				return event.getPlayer().getItemInHand().isSimilar(ability.createItem(1));
			}
		},
		;

		protected abstract boolean canFind(PlayerInteractEvent event, ClickBlockChestAbility ability);


	}



}
