package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobatblt3.ability.TBLTItem;
import com.github.orgs.kotobaminers.kotobatblt3.utility.Utility;

public enum TBLTPortal {
	ARENA_PORTAL(
		KotobaItemStack.create(Material.EYE_OF_ENDER, (short) 0, 1, "Arena portal key", null),
		new HashMap<Vector, Material>(){{
			put(new Vector(1,0,1), Material.ENDER_PORTAL);
			put(new Vector(1,0,0), Material.ENDER_PORTAL);
			put(new Vector(1,0,-1), Material.ENDER_PORTAL);
			put(new Vector(0,0,1), Material.ENDER_PORTAL);
			put(new Vector(0,0,0), Material.ENDER_PORTAL);
			put(new Vector(0,0,-1), Material.ENDER_PORTAL);
			put(new Vector(-1,0,1), Material.ENDER_PORTAL);
			put(new Vector(-1,0,0), Material.ENDER_PORTAL);
			put(new Vector(-1,0,-1), Material.ENDER_PORTAL);
		}},
		new Vector(0, -2, 0),
		2
	) {

		@Override
		public boolean enterPortal(PlayerPortalEvent event) {
			Location from = event.getFrom();
			new TBLTArenaMap().findUnique(from)
				.ifPresent(a -> {
					List<Player> players = Bukkit.getOnlinePlayers().stream()
							.filter(p -> Utility.isTBLTPlayer(p))
						.collect(Collectors.toList());
					if(1 < players.size()) {
						if(players.stream().allMatch(p ->
							p.getWorld().getBlockAt(p.getLocation()).getType() == Material.ENDER_PORTAL)) {
							ItemStack warp = TBLTItem.WARP_CRYSTAL.createItem(1);
							players.stream()
								.filter(p ->
									Stream.of(p.getInventory().getContents())
										.filter(i -> i != null)
										.anyMatch(i -> warp.isSimilar(i)))
								.findFirst()
								.ifPresent(p -> {
									Utility.getSpherePositions(p.getLocation(), 3).stream()
										.map(loc -> loc.getBlock())
										.filter(block -> block.getState() instanceof Chest)
										.flatMap(block -> Stream.of(((Chest) block.getState()).getInventory().getContents())
											.filter(i -> i != null)
											.filter(i -> i.getItemMeta().getDisplayName().equalsIgnoreCase(warp.getItemMeta().getDisplayName()))//ItemStack.isSimilar cannot be used for lore
											.filter(i -> i.getType() == warp.getType())//ItemStack.isSimilar cannot be used for lore
										)
										.filter(i -> i.getItemMeta().getLore() != null)
										.map(i -> i.getItemMeta().getLore())
										.filter(i -> 0 < i.size())
										.map(i -> i.get(0))
										.map(name -> new TBLTArenaMap().findUnique(name))
										.forEach(to -> to.ifPresent(to2 -> players.stream().forEach(p2 -> ((TBLTArena) to2).startSpawn(p2))));
										//TODO initialize arena and players
								});
							}
						}
					}
				);
			return true;
		}
	},

	CHECK_POINT_PORTAL(
		KotobaItemStack.create(Material.EYE_OF_ENDER, (short) 0, 1, "Check point key", null),
		new HashMap<Vector, Material>(){{
			put(new Vector(0,0,0), Material.ENDER_PORTAL);
		}},
		new Vector(0, -2, 0),
		1
	) {
		@Override
		public boolean enterPortal(PlayerPortalEvent event) {
			Location from = event.getFrom();
			new TBLTArenaMap().findUnique(from)
				.map(a -> (TBLTArena) a)
				.ifPresent(a -> {
					List<Player> players = Bukkit.getOnlinePlayers().stream()
						.filter(p -> Utility.isTBLTPlayer(p))
						.collect(Collectors.toList());
					if(1 < players.size()) {
						if(players.stream().allMatch(p ->
							p.getWorld().getBlockAt(p.getLocation()).getType() == Material.ENDER_PORTAL)) {
							a.findNearestCheckPoint(from)
								.ifPresent(check -> {
									if(0 < a.getCurrentPoint().distance(check)) {
										a.setCurrentPoint(check);
										KotobaEffect.MAGIC_MIDIUM.playEffect(from);
										KotobaEffect.MAGIC_MIDIUM.playSound(from);
									}
								});
						}
					}
				});
			return true;
		}
	},
	;


	private ItemStack key;
	private Map<Vector, Material> pattern;
	private Vector chestPosition;
	private int chestRange;


	private TBLTPortal(ItemStack key, Map<Vector, Material> pattern, Vector chestPosition, int chestRange) {
		this.key = key;
		this.pattern = pattern;
		this.chestPosition = chestPosition;
		this.chestRange = chestRange;
	}

	public static List<TBLTPortal> find(PlayerPortalEvent event) {
		return Stream.of(TBLTPortal.values())
			.flatMap(portal -> portal.findChestsInRange(event.getFrom()).stream()
				.map(c -> find(c))
				.filter(c -> c.isPresent())
				.map(c -> c.get())
			).collect(Collectors.toList());
	}

	public static Optional<TBLTPortal> find(List<ItemStack> items) {
		return Stream.of(TBLTPortal.values())
			.filter(portal -> {
				ItemStack key = portal.createKey();
				return items.stream().anyMatch(item -> item.getType() == key.getType() && item.getItemMeta().getDisplayName().equalsIgnoreCase(key.getItemMeta().getDisplayName()));
			})
			.findFirst();
	}

	public static Optional<TBLTPortal> find(Chest chest) {
		List<ItemStack> items = Stream.of(chest.getInventory().getContents())
			.filter(item -> item != null)
			.collect(Collectors.toList());
		return find(items);
	}

	private List<Chest> findChestsInRange(Location location) {
		return Utility.getSpherePositions(location.clone().add(chestPosition), chestRange).stream()
			.map(loc -> loc.getBlock())
			.filter(block -> block.getState() instanceof Chest)
			.map(block -> (Chest) block.getState())
			.collect(Collectors.toList());
	}


	public abstract boolean enterPortal(PlayerPortalEvent event);


	public boolean openPortal(PlayerInteractEvent event) {
		Location origin = event.getClickedBlock().getLocation();
		pattern.entrySet().forEach(entry -> {
			Location location = origin.clone().add(entry.getKey());
			location.getBlock().setType(entry.getValue());
			location.getWorld().playEffect(location.clone().add(0,1,0), Effect.ENDER_SIGNAL, 0);
		});
		KotobaEffect.PORTAL.playSound(origin);
		return true;
	}


	public ItemStack createKey() {
		return key;
	}

	public ItemStack createKey(String point) {
		ItemStack result = key.clone();
		ItemMeta itemMeta = result.getItemMeta();
		itemMeta.setLore(Arrays.asList(point));
		result.setItemMeta(itemMeta);
		return result;
	}

	public Map<Vector, Material> getPattern() {
		return pattern;
	}


}

