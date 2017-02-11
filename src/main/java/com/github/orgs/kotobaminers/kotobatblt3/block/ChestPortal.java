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
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.block.ConfigChest;
import com.github.orgs.kotobaminers.kotobaapi.block.KotobaPortalInterface;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.ability.ChestKey;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.utility.Utility;

public enum ChestPortal implements KotobaPortalInterface, ConfigChest {
	GEM_PORTAL(
		TBLTItemStackIcon.GEM_PORTAL_KEY_3X3,
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
		new Vector(0, -2, 0)
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
										.anyMatch(i -> ChestKey.PORTAL_CRYSTAL.getIcon().isIconItemStack(i)))
								.findFirst();
							if(crystalOwner.isPresent() || isSinglePortal(from)) {
								List<Boolean> success = findChests(from).stream()
									.flatMap(c -> Stream.of(c.getInventory().getContents()).filter(i -> i != null).filter(i -> ChestKey.PORTAL_CRYSTAL.getIcon().isIconItemStack(i)))
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

		@Override
		public boolean canOpen(PlayerInteractEvent event) {
			Block block = event.getClickedBlock().getRelative(event.getBlockFace());
			findChests(block.getLocation()).stream()
				;
			return false;
		}


	},

	CHECK_POINT_PORTAL(
		TBLTItemStackIcon.DUMMY,
		new HashMap<Vector, Material>(){{
			put(new Vector(0,0,0), Material.ENDER_PORTAL);
		}},
		new Vector(0, -2, 0)
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

		@Override
		public boolean canOpen(PlayerInteractEvent event) {
			return true;
		}
	},
	;


	private KotobaItemStackIcon icon;
	private Map<Vector, Material> pattern;
	private Vector chestPosition;


	private ChestPortal(KotobaItemStackIcon icon, Map<Vector, Material> pattern, Vector chestPosition) {
		this.icon = icon;
		this.pattern = pattern;
		this.chestPosition = chestPosition;
	}


	@Override
	public List<Chest> findChests(Location location) {
		return pattern.keySet().stream()
			.map(v -> location.clone().subtract(v).add(chestPosition))
			.filter(l -> l.getBlock().getState() instanceof Chest)
			.map(l -> (Chest) l.getBlock().getState())
			.filter(c -> Stream.of(c.getInventory().getContents()).filter(i -> i != null).anyMatch(i -> getKey().isIconItemStack(i)))
			.collect(Collectors.toList());
	}


	@Override
	public void openPortal(Location center) {
		Location origin = center.clone();
		pattern.entrySet().forEach(entry -> {
			Location location = origin.clone().add(entry.getKey());
			location.getBlock().setType(entry.getValue());
			location.getWorld().playEffect(location.clone().add(0,1,0), Effect.ENDER_SIGNAL, 0);
		});
		KotobaEffect.PORTAL.playSound(origin);
	}


	public ItemStack createKey(String point) {
		ItemStack result = icon.create(1);
		ItemMeta itemMeta = result.getItemMeta();
		itemMeta.setLore(Arrays.asList(point));
		result.setItemMeta(itemMeta);
		return result;
	}

	protected boolean isSinglePortal(Location location) {
		long singleKeyNumber = findChests(location).stream()
			.flatMap(c -> Stream.of(c.getInventory().getContents()).filter(i -> i != null).filter(i -> ChestKey.SINGLE_PORTAL.getIcon().isIconItemStack(i)))
			.count();
		if(0 < singleKeyNumber) {
			return true;
		} else {
			return false;
		}
	}


	@Override
	public KotobaItemStackIcon getKey() {
		return icon;
	}



}

