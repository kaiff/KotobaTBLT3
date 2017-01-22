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

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaPortalInterface;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobatblt3.ability.TBLTItem;
import com.github.orgs.kotobaminers.kotobatblt3.utility.Utility;

public enum ChestPortal implements KotobaPortalInterface {
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
					List<Player> inPortal = Bukkit.getOnlinePlayers().stream()
						.filter(p -> p.getLocation().getBlock().getType() == Material.ENDER_PORTAL)
						.filter(p -> Utility.isTBLTPlayer(p))
						.collect(Collectors.toList());
					List<Player> inArena = Bukkit.getOnlinePlayers().stream()
							.filter(p -> Utility.isTBLTPlayer(p))
							.collect(Collectors.toList());

					if(isSinglePortal(from) || (1 < inPortal.size()) && inPortal.size() == inArena.size()) {
						if(inPortal.stream().allMatch(p -> from.distance(p.getLocation()) < 4)) {
							Optional<Player> crystalOwner = inPortal.stream()
								.filter(p ->
									Stream.of(p.getInventory().getContents())
										.filter(i -> i != null)
										.anyMatch(i -> TBLTItem.PORTAL_CRYSTAL.isTBLTItem(i)))
								.findFirst();
							if(crystalOwner.isPresent() || isSinglePortal(from)) {
								List<Boolean> success = findChestsInRange(from).stream()
									.flatMap(c -> Stream.of(c.getInventory().getContents()).filter(i -> i != null).filter(i -> TBLTItem.PORTAL_CRYSTAL.isTBLTItem(i)))
									.filter(i -> i.getItemMeta().getLore() != null)
									.filter(i -> 0 < i.getItemMeta().getLore().size())
									.map(i -> i.getItemMeta().getLore().get(0))
									.map(name -> new TBLTArenaMap().findUnique(name))
									.map(to -> {
										to.ifPresent(to2 -> inPortal.stream().forEach(p2 -> ((TBLTArena) to2).startSpawn(p2)));
										return true;
									}).collect(Collectors.toList());
								if(success.contains(true)) {
									a.load();
								}
							}
						}
					}
				});
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
					List<Player> inArena = Bukkit.getOnlinePlayers().stream()
						.filter(p -> Utility.isTBLTPlayer(p))
						.filter(p -> a.isIn(p.getLocation()))
						.collect(Collectors.toList());
					List<Player> inPortal = inArena.stream()
						.filter(p -> p.getLocation().getBlock().getType() == Material.ENDER_PORTAL)
						.collect(Collectors.toList());

					if(isSinglePortal(from) || (1 < inPortal.size()) && inPortal.size() == inArena.size()) {
						a.findNearestCheckPoint(from)
							.filter(check -> 0 < a.getCurrentPoint().distance(check))
							.ifPresent(check -> {
								KotobaEffect.MAGIC_MIDIUM.playEffect(from);
								KotobaEffect.MAGIC_MIDIUM.playSound(from);

								a.load();

								a.setCurrentPoint(check);
								inArena.forEach(p -> a.continueFromCurrent(p));

							});
					}
				});
			return true;
		}
	},

	BOTTOM_PORTAL(
		KotobaItemStack.create(Material.EYE_OF_ENDER, (short) 0, 1, "Arena portal key", null),
		new HashMap<Vector, Material>(){{
		}},
		new Vector(0, -2, 0),
		2
	) {
		@Override
		public boolean enterPortal(PlayerPortalEvent event) {
			return false;
		}
	}
	;


	private ItemStack key;
	private Map<Vector, Material> pattern;
	private Vector chestPosition;
	private int chestRange;


	private ChestPortal(ItemStack key, Map<Vector, Material> pattern, Vector chestPosition, int chestRange) {
		this.key = key;
		this.pattern = pattern;
		this.chestPosition = chestPosition;
		this.chestRange = chestRange;
	}

	@Override//TODO check
	public boolean isThis(Location location) {
		return findChestsInRange(location).stream()
			.flatMap(chest -> Stream.of(chest.getInventory().getContents()))
			.filter(i -> i != null)
			.anyMatch(i -> isPortalKey(i));
	}

	private boolean isPortalKey(ItemStack itemStack) {
		if(itemStack.getType() == key.getType() && itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(key.getItemMeta().getDisplayName())) {
			return true;
		}
		return false;
	}

	protected List<Chest> findChestsInRange(Location location) {
		return Utility.getSpherePositions(location.clone().add(chestPosition), chestRange).stream()
			.map(loc -> loc.getBlock())
			.filter(block -> block.getState() instanceof Chest)
			.map(block -> (Chest) block.getState())
			.collect(Collectors.toList());
	}


	public boolean openPortal(PlayerInteractEvent event) {
		Location origin = event.getClickedBlock().getLocation().clone();
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

	protected boolean isSinglePortal(Location location) {
		long singleKeyNumber = findChestsInRange(location).stream()
			.flatMap(c -> Stream.of(c.getInventory().getContents()).filter(i -> i != null).filter(i -> TBLTItem.SINGLE_PORTAL.isTBLTItem(i)))
			.count();
		if(0 < singleKeyNumber) {
			return true;
		} else {
			return false;
		}
	}

}

