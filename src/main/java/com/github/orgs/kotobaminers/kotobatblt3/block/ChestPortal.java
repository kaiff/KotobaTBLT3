package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaPortal;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public enum ChestPortal implements KotobaPortal {
	GEM_PORTAL(
		TBLTItemStackIcon.GEM_PORTAL_KEY_3x3,
		Material.ENDER_PORTAL,
		TBLTInteractiveChestFinder.THREE_BY_THREE
	) {

		private final Vector OFFSET = new Vector(0, -3, 0);


		@Override
		public boolean goThroughPortal(PlayerPortalEvent event) {
			Player player = event.getPlayer();
			return new TBLTArenaMap().findPlayingMap(player)
				.map(a -> {
					List<Player> inArena = a.getTBLTPlayers();
					return new TBLTArenaMap().find(a.getArenaMeta().getNext())
						.map(next -> (TBLTArena) next)
						.map(next -> {
							if(a.getArenaMeta().isSingle() || (inArena.stream().allMatch(p -> this.getPortal() == p.getLocation().getBlock().getType() && 1 < inArena.size()))) {
								a.load();
								next.join(inArena);
								return true;
							}
							return false;
						}).orElse(false);
				}).orElse(false);
		}


		private List<Block> getBlocksInPortal(Location center) {
			return TBLTInteractiveChestFinder.THREE_BY_THREE.getPositions().stream()
				.map(v -> center.clone().add(v).getBlock())
				.collect(Collectors.toList());
		}
		private List<Block> getAnswers(Location center) {
			return getBlocksInPortal(center).stream()
				.map(b -> b.getLocation().add(OFFSET).getBlock())
				.collect(Collectors.toList());
		}


		@Override
		public boolean canOpen(Location center) {
			return getBlocksInPortal(center).stream().allMatch(b -> b.getType() == b.getLocation().add(OFFSET).getBlock().getType());
		}

		@Override
		public void failOpen(Location center) {
			if(center.getBlock().getState() instanceof Chest) {
				List<Block> inPortal = getBlocksInPortal(center).stream().filter(b -> b.getType() != Material.AIR).collect(Collectors.toList());
				List<Block> answers = getAnswers(center).stream().filter(b -> b.getType() != Material.AIR).collect(Collectors.toList());

				if(answers.size() <= inPortal.size()) {
					KotobaEffect.EXPLODE_SMALL.playSound(center);
					inPortal.forEach(b -> KotobaEffect.EXPLODE_SMALL.playEffect(b.getLocation()));
					new TBLTArenaMap().findUnique(center)
					.map(s -> (TBLTArena) s)
					.ifPresent(a -> a.restart());
				}
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

