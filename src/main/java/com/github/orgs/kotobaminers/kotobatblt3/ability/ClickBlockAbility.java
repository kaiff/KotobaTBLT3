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

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArena;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.gui.TBLTPlayerGUI;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;
import com.github.orgs.kotobaminers.kotobatblt3.utility.Utility;

public enum ClickBlockAbility implements ClickBlockAbilityInterface {


	PREDICTION(
		Material.ENCHANTED_BOOK,
		(short) 0,
		"Prediction",
		Arrays.asList("Are you stuck?", "Use prediction to get a useful hint about what to do next."),
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR),
		0
	) {
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Player player = event.getPlayer();
			ItemStack prediction = new TBLTArenaMap().findUnique(player.getLocation())
				.map(a -> (TBLTArena) a)
				.map(a -> {
					ItemStack base = TBLTItem.PREDICTION.createItem(1);
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
				.filter(i -> TBLTItem.PREDICTION.isTBLTItem(i))
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
		Material.IRON_HOE,
		(short) 0,
		"Lock picking",
		Arrays.asList("With this tool you can pick any lock.", "Use the lock pick on locked chest to instantly receive the contents."),
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
		Material.WATCH,
		(short) 0,
		"Rewind Time",
		Arrays.asList("You can go back in time to the last checkpoint with this item."),
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
		Material.GLASS,
		(short) 0,
		"Clairvoyance",
		Arrays.asList("You can see the contents of locked chests with this skill."),
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
		Material.FEATHER,
		(short) 0,
		"Psychokinesis",
		null,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK),
		0
	) {
		@SuppressWarnings("deprecation")
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
		Material.NETHER_STAR,
		(short) 0,
		"Teleport+5",
		null,
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
		Material.ENDER_CHEST,
		(short) 0,
		"Transition+5",
		Arrays.asList("Exchange positions with each other", "Both of two players have to jump"),
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
		Material.BREWING_STAND_ITEM,
		(short) 0,
		"Drone",
		null,
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

	private Material material;
	private short data;
	private String name;
	private List<String> lore;
	private List<Action> triggers;
	private int consume;

	private ClickBlockAbility(Material material, short data, String name, List<String> lore, List<Action> triggers, int consume) {
		this.material = material;
		this.data = data;
		this.name = name;
		this.lore = lore;
		this.triggers = triggers;
		this.consume = consume;
	}

	public static List<ClickBlockAbility> find(ItemStack item) {
		return Stream.of(ClickBlockAbility.values())
			.filter(ability -> ability.createItem(1).isSimilar(item))
			.collect(Collectors.toList());
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
}
