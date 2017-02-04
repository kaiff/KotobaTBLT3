package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.ability.ClickBlockAbilityInterface;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArena;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.gui.TBLTPlayerGUI;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.utility.Utility;

public enum ClickBlockAbility implements ClickBlockAbilityInterface {


	PREDICTION(
		TBLTItemStackIcon.PREDICTION,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR),
		0
	) {
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Player player = event.getPlayer();
			ItemStack prediction = new TBLTArenaMap().findUnique(player.getLocation())
				.map(a -> (TBLTArena) a)
				.map(a -> {
					ItemStack base = TBLTItem.PREDICTION.getIcon().create(1);
					BookMeta baseMeta = (BookMeta) base.getItemMeta();
					List<String> pages = ((BookMeta) a.getPredictionWrittenBook().getItemMeta()).getPages();
					baseMeta.setPages(pages);
					base.setItemMeta(baseMeta);
					return base;
				})
				.orElse(null);
			if(prediction == null) return false;
			List<ItemStack> currentPrediction = Stream.of(player.getInventory().getContents())
				.filter(i -> i != null)
				.filter(i -> TBLTItem.PREDICTION.getIcon().isSame(i))
				.collect(Collectors.toList());
			int currentPage = currentPrediction.stream()
					.map(i -> ((BookMeta) i.getItemMeta()).getPageCount())
					.max(Comparator.naturalOrder()).orElse(0);

			BookMeta meta = (BookMeta) prediction.getItemMeta();
			if(currentPage < meta.getPageCount()) {
				currentPrediction.forEach(i -> KotobaItemStack.consume(player.getInventory(), i, i.getAmount()));

				meta.setPages(meta.getPages().subList(0, currentPage + 1));
				meta.setAuthor(player.getName());
				prediction.setItemMeta(meta);
				player.getInventory().addItem(prediction);

				KotobaEffect.MAGIC_MIDIUM.playSound(player.getLocation());

				return true;
			}
			return false;
		}
	},


	LOCK_PICKING(
		TBLTItemStackIcon.LOCK_PICKING,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK),
		0
	) {
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Player player = event.getPlayer();
			Block block = event.getClickedBlock();
			if(block.getType() == Material.CHEST) {
				Chest chest = (Chest) block.getState();
				Stream.of(chest.getInventory().getContents())
					.filter(i -> i != null)
					.forEach(i -> player.getInventory().addItem(i));
				player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
				KotobaEffect.CRIT_MIDIUM.playEffect(block.getLocation().add(0.5, 0.5, 0.5));
				chest.getInventory().clear();
				block.setType(Material.AIR);
				return true;
			}
			return false;
		}
	},


	REWIND_TIME(
		TBLTItemStackIcon.REWIND_TIME,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR),
		0
	) {
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Player player = event.getPlayer();

			new TBLTArenaMap().findUnique(player.getLocation())
				.ifPresent(arena -> {
					List<Player> players = Bukkit.getOnlinePlayers().stream()
						.filter(p -> arena.isIn(p.getLocation()))
						.filter(p -> Utility.isTBLTPlayer(p))
						.collect(Collectors.toList());
					if(players.stream().allMatch(p -> p.isSneaking())) {
						TBLTArena a = (TBLTArena) arena;
						a.load();
						players.forEach(p -> a.continueFromCurrent((Player) p));
					}
				});
			return true;
		}
	},


	CLAIRVOYANCE(
		TBLTItemStackIcon.CLAIRVOYANCE,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR),
		0
	) {
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Block block = event.getClickedBlock();
			if(block == null) return false;
			if(block.getType() == Material.CHEST) {
				Chest chest = (Chest) block.getState();
				List<ItemStack> icons = Stream.of(chest.getInventory().getContents())
					.filter(i -> i != null)
					.collect(Collectors.toList());
				event.getPlayer().openInventory(TBLTPlayerGUI.CLAIRVOYANCE.create(icons).get());
				return true;
			}
			return false;
		}
	},

	PSYCHOKINESIS(
		TBLTItemStackIcon.GREEN_GEM,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK),
		0
	) {
		@Deprecated
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Player player = event.getPlayer();
			Entity entity = (Entity) player;
			if(!entity.isOnGround()) return false;
			Block block = event.getClickedBlock();
			List<Material> cant = Arrays.asList(Material.GOLD_PLATE, Material.IRON_DOOR);//TODO
			if(cant.contains(block.getType())) return false;
			Location above = block.getLocation().clone().add(0, 1, 0);
			if(above.distance(player.getLocation().getBlock().getLocation()) == 0) return false;

			if(block.getWorld().getBlockAt(above).getType() == Material.AIR) {
				FallingBlock falling = block.getWorld().spawnFallingBlock(block.getLocation(), block.getType(), (byte) block.getData());
				falling.setVelocity(new Vector(0, 1.2, 0));
				falling.setDropItem(false);
				block.setType(Material.AIR);
				Utility.playJumpEffect(falling);
				int duration = 3;
				Utility.stopPlayer(player, duration);
				KotobaUtility.playCountDown(Setting.getPlugin(), Arrays.asList(player), duration);
				return true;
			}
			return false;
		}
	},

	TELEPORT(
		TBLTItemStackIcon.GREEN_GEM,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR),
		0
	) {
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Player player = event.getPlayer();
			player.getNearbyEntities(5, 5, 5).stream()
				.filter(e -> e instanceof Player)
				.map(e -> (Player) player)
				.filter(p -> p.isSneaking())
				.filter(p -> Utility.isTBLTPlayer(p))
				.findFirst()
				.ifPresent(e -> player.teleport(e.getLocation()));
			return true;
		}
	},

	TRANSITION(
		TBLTItemStackIcon.GREEN_GEM,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR),
		1
	) {
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Player user = event.getPlayer();
			return user.getNearbyEntities(5, 5, 5).stream()
				.filter(e -> e instanceof Player)
				.map(e -> (Player) e)
				.filter(e -> new TBLTArenaMap().isInAny(e.getLocation()))
				.filter(e -> e.isSneaking())
				.findAny()
				.map(e -> {
					Location userLocation = e.getLocation();
					Location entityLocation = user.getLocation();
					user.teleport(userLocation);
					e.teleport(entityLocation);
					KotobaEffect.ENDER_SIGNAL.playEffect(user.getLocation());
					KotobaEffect.ENDER_SIGNAL.playSound(user.getLocation());
					KotobaEffect.ENDER_SIGNAL.playEffect(e.getLocation());
					KotobaEffect.ENDER_SIGNAL.playSound(e.getLocation());
					return true;
				}).isPresent();
		}
	},

	DISGUISE_BAT(
		TBLTItemStackIcon.GREEN_GEM,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR),
		1
	) {
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Entity entity4 = event.getPlayer();
			if(entity4.isOnGround()) {
				Drone.performDrone(event.getPlayer(), 10);
				return true;
			}
			return false;
		}
	},
	;


	private KotobaItemStackIcon icon;
	private List<Action> triggers;
	private int consume;


	private ClickBlockAbility(KotobaItemStackIcon icon, List<Action> triggers, int consume) {
		this.icon = icon;
		this.triggers = triggers;
		this.consume = consume;
	}

	public static List<ClickBlockAbility> find(ItemStack item) {
		return Stream.of(ClickBlockAbility.values())
			.filter(ability -> ability.getIcon().create(1).isSimilar(item))
			.collect(Collectors.toList());
	}

	@Override
	public KotobaItemStackIcon getIcon() {
		return icon;
	}
	@Override
	public List<Action> getTriggers() {
		return triggers;
	}
	@Override
	public int getConsumption() {
		return consume;
	}


}

