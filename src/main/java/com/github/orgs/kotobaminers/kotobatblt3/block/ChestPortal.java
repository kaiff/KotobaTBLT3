package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockData;
import com.github.orgs.kotobaminers.kotobaapi.block.KotobaBlockStorage;
import com.github.orgs.kotobaminers.kotobaapi.block.KotobaPortal;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.utility.ChestReader;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTUtility;

public enum ChestPortal implements KotobaPortal {
	GEM_PORTAL(
		TBLTItemStackIcon.GEM_PORTAL_KEY_3x3,
		Material.ENDER_PORTAL,
		TBLTInteractiveChestFinder.THREE_BY_THREE
	) {

		@Override
		public boolean goThroughPortal(PlayerPortalEvent event) {
			Location from = event.getFrom();
			new TBLTArenaMap().findUnique(from)
				.ifPresent(a -> {
					List<Chest> chests = findChests(from);
					List<Player> inArena = Bukkit.getOnlinePlayers().stream()
							.filter(p -> TBLTUtility.isTBLTPlayer(p))
							.collect(Collectors.toList());
					List<Player> inSamePortal = inArena.stream()
						.filter(p ->
							chests
								.stream()
								.flatMap(c ->
									chest.getPositions()
										.stream()
										.map(pos -> c.getLocation().clone().add(pos)))
								.anyMatch(l -> p.getLocation().getBlock().getLocation().distance(l) == 0)
						)
						.collect(Collectors.toList());

					if(isSinglePortal(from) || (1 < inSamePortal.size()) && inSamePortal.size() == inArena.size()) {
						if(inSamePortal.stream().allMatch(p -> from.distance(p.getLocation()) < 4)) {
							chests.forEach(c -> {
								fillPortal(c.getLocation(), Material.OBSIDIAN);
								tryWarp(inSamePortal, c, a);
							});
						}
					}
				});
			return true;
		}


		private boolean tryWarp(List<Player> inPortal, Chest chest, KotobaBlockStorage arena) {
			List<ItemStack> contents = Stream.of(chest.getInventory().getContents())
				.filter(i -> i != null)
				.collect(Collectors.toList());

			List<Boolean> next = contents.stream()
				.filter(i -> TBLTItemStackIcon.PORTAL_NEXT_CRYSTAL.isIconItemStack(i))
				.filter(i -> i.getItemMeta().getLore() != null)
				.filter(i -> 0 < i.getItemMeta().getLore().size())
				.map(i -> i.getItemMeta().getLore().get(0))
				.map(name -> new TBLTArenaMap().findUnique(name))
				.map(to -> {
					to.ifPresent(to2 -> ((TBLTArena) to2).join(inPortal));
					arena.load();
					return true;
				})
				.collect(Collectors.toList());

			if(next.contains(true)) {
				return true;
			} else {
				contents.stream()
					.filter(i -> TBLTItemStackIcon.PORTAL_ELEVATOR_CRYSTAL.isIconItemStack(i))
					.findAny()
					.map(i -> {
						Stream.iterate(-ELEVATOR_RANGE, j -> j + 1)
							.limit(2 * ELEVATOR_RANGE - 1)
							.filter(j -> j != 0)
							.flatMap(j -> Stream.of(
								chest.getLocation().clone().add(j, 0, 0),
								chest.getLocation().clone().add(0, j, 0),
								chest.getLocation().clone().add(0, 0, j))
							)
							.filter(l -> arena.isIn(l))
							.map(l -> l.getBlock().getState())
							.filter(s -> s instanceof Chest)
							.map(s -> (Chest) s)
							.filter(c -> Stream.of(c.getInventory().getContents())
								.filter(content -> content != null)
								.anyMatch(content -> TBLTItemStackIcon.PORTAL_ELEVATOR_CRYSTAL.isIconItemStack(content))
							)
							.findAny()
							.map(c -> KotobaUtility.getBlockCenter(c.getBlock()).add(0, 3, 0))
							.ifPresent(l -> {
								inPortal.forEach(player -> {
									player.teleport(l);
									KotobaEffect.ENDER_SIGNAL.playEffect(l);
									l.getWorld().playSound(l, Sound.PORTAL_TRIGGER, 1, 1);
								});
								}
							);
						return true;
					})
					;
			}

			//Failed(Probally settings are missing in the chest)
			return false;
		}

		private static final int ELEVATOR_RANGE = 16;


		@Override
		public boolean canOpen(Location center) {
			if(center.getBlock().getState() instanceof Chest) {
				return ChestReader.checkPattern3By3((Chest) center.getBlock().getState(), new Vector(0, 2, 0));
			}
			return false;
		}

		@Override
		public void failOpen(Location center) {
			List<Location> locations = getPositions().stream()
				.map(vec -> center.clone().add(vec))
				.collect(Collectors.toList());

			if(center.getBlock().getState() instanceof Chest) {
				Chest chest = (Chest) center.getBlock().getState();
				ChestReader.findPattern3By3(chest).ifPresent(map -> {
					long items = map.values().stream().filter(m -> m != Material.AIR).count();
					long gems = getPositions().stream()
						.filter(v -> center.clone().add(v).getBlock().getType() != Material.AIR)
						.count();
					if(items <= gems) {
						KotobaEffect.EXPLODE_SMALL.playSound(center);

						new TBLTArenaMap().findUnique(center)
							.map(s -> (TBLTArena) s)
							.ifPresent(a -> {
								locations.forEach(l -> {
									KotobaEffect.EXPLODE_SMALL.playEffect(l);
									new KotobaBlockData(l, Material.AIR, 0).placeBlock();
								});
								a.restart();
							});
					}
				});
			}
		}
	},
	;


	private KotobaItemStackIcon icon;
	private Material portal;
	protected TBLTInteractiveChestFinder chest;


	private ChestPortal(KotobaItemStackIcon icon, Material portal, TBLTInteractiveChestFinder chest) {
		this.icon = icon;
		this.portal = portal;
		this.chest = chest;
	}


	@Override
	public void successOpen(Location center) {
		chest.getPositions().forEach(vec -> {
			Location location = center.clone().add(vec);
			location.getBlock().setType(portal);
			location.getWorld().playEffect(location.clone().add(0,1,0), Effect.ENDER_SIGNAL, 0);
		});
		KotobaEffect.PORTAL.playSound(center);
	}


	protected boolean isSinglePortal(Location location) {
		long singleKeyNumber = findChests(location).stream()
			.flatMap(c -> Stream.of(c.getInventory().getContents()).filter(i -> i != null).filter(i -> TBLTItemStackIcon.SINGLE_PORTAL.isIconItemStack(i)))
			.count();
		if(0 < singleKeyNumber) {
			return true;
		} else {
			return false;
		}
	}


	public List<Chest> findChests(Location location) {
		return chest.findChests(location).stream()
			.filter(c -> Stream.of(c.getInventory().getContents()).filter(i -> i != null).anyMatch(i -> icon.isIconItemStack(i)))
			.collect(Collectors.toList());
	}


	@Override
	public List<Vector> getPositions() {
		return chest.getPositions();
	}


	@Override
	public Material getPortal() {
		return portal;
	}

	@Override
	public List<Location> findCenters(Location location) {
		return findChests(location).stream().map(c -> c.getLocation()).collect(Collectors.toList());
	}


}

