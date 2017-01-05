package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArena;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.gui.TBLTGUI;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;
import com.github.orgs.kotobaminers.kotobatblt3.resource.ResourceHolder;
import com.github.orgs.kotobaminers.kotobatblt3.resource.TBLTResource;
import com.github.orgs.kotobaminers.kotobatblt3.resource.TBLTResourceBlock;
import com.github.orgs.kotobaminers.kotobatblt3.utility.Utility;

public enum ClickBlockAbility implements ClickBlockAbilityInterface {
	CRUSH_CRYSTAL(
		Material.DIAMOND_PICKAXE,
		(short) 0,
		"Crush crystal",
		null,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK),
		0,
		new HashMap<TBLTResource, Integer>() {{
		}}
	) {
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Location location = event.getClickedBlock().getLocation();
			boolean success = Utility.getSpherePositions(location, 3).stream()
				.filter(loc -> loc.getBlock().getType() == Material.EMERALD_BLOCK)
				.map(loc -> {
					KotobaEffect.MAGIC_MIDIUM.playEffect(loc);
					KotobaEffect.BREAK_BLOCK_MIDIUM.playEffect(loc);
					loc.getBlock().setType(Material.AIR);
					KotobaItemStack.consume(event.getPlayer().getInventory(), event.getPlayer().getInventory().getItemInHand(), 64);
					event.getPlayer().getInventory().addItem(TBLTItem.WARP_CRYSTAL.createItem(1));
					return true;
				}).findAny().orElse(false);
			if(success) {
				KotobaEffect.MAGIC_MIDIUM.playSound(location);
			}
			return success;
		}
	},

	RESOURCES(
		Material.ENDER_CHEST,
		(short) 0,
		"Resources",
		null,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR),
		0,
		new HashMap<TBLTResource, Integer>() {{
		}}
	) {
		@Override
		public boolean perform(PlayerInteractEvent event) {
			new TBLTArenaMap().findUnique(event.getPlayer().getLocation())
				.ifPresent(a -> event.getPlayer().openInventory(((TBLTArena) a).getResourceInventory()));
			return true;
		}
	},

	SYPHON_MANA(
		Material.EMERALD,
		(short) 0,
		"Syphon Mana",
		null,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK),
		1,
		new HashMap<TBLTResource, Integer>() {{
		}}
	) {
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Block block = event.getClickedBlock();
			return new TBLTArenaMap().findUnique(event.getPlayer().getLocation())
				.map(a ->
					TBLTResourceBlock.find(block.getType())
						.map(r -> {
							((ResourceHolder) a).addResources(r);
							r.siphon(block);
							KotobaEffect.dropItemEffect(r.createResource(), block.getLocation().add(0, 1, 0));
							return true;
						}).orElse(false)
				).orElse(false);
		}
	},

	EXTRACT_MANA(
		Material.GOLD_PICKAXE,
		(short) 0,
		"Extract Mana",
		null,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK),
		1,
		new HashMap<TBLTResource, Integer>() {{
		}}
			) {
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Block block = event.getClickedBlock();
			return new TBLTArenaMap().findUnique(event.getPlayer().getLocation())
				.map(a ->
					TBLTResourceBlock.find(block.getType())
						.map(r -> {
							((ResourceHolder) a).addResources(r);
							r.extract(block);
							KotobaEffect.dropItemEffect(r.createResource(), block.getLocation().add(0, 1, 0));
							return true;
						}).orElse(false)
				).orElse(false);
		}
	},

	LOCK_PICKING(
		Material.IRON_HOE,
		(short) 0,
		"Lock picking",
		Arrays.asList("Open a chest once"),
		Arrays.asList(Action.RIGHT_CLICK_BLOCK),
		0,
		new HashMap<TBLTResource, Integer>() {{
			put(TBLTResource.METAL_MANA, 1);
		}}
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
		"Rewind time",
		null,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR),
		0,
		new HashMap<TBLTResource, Integer>() {{
		}}
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
						a.initializeResources();
						players.forEach(p -> a.startCurrent((Player) p));
					}
				});
			return true;
		}
	},

	CLAIRVOYANCE(
		Material.GLASS,
		(short) 0,
		"Clairvoyance",
		null,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR),
		0,
		new HashMap<TBLTResource, Integer>() {{
		}}
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
				event.getPlayer().openInventory(TBLTGUI.CLAIRVOYANCE.create(icons).get());
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
		0,
		new HashMap<TBLTResource, Integer>() {{
		}}
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
		0,
		new HashMap<TBLTResource, Integer>() {{
		}}
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
		1,
		new HashMap<TBLTResource, Integer>() {{
			put(TBLTResource.MAGIC_MANA, 1);
		}}
	) {
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Player user = event.getPlayer();
			return user.getNearbyEntities(5, 5, 5).stream()
				.filter(e -> e instanceof Player)
				.map(e -> (Player) e)
				.filter(e -> new TBLTArenaMap().isInAny(e.getLocation()))
//				.filter(e -> e.isSneaking())TODO
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
		1,
		new HashMap<TBLTResource, Integer>() {{
		}}
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

	TALK(
		Material.SKULL_ITEM,
		(short) 3,
		"Talk with people",
		null,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR),
		0,
		new HashMap<TBLTResource, Integer>() {{
		}}
	) {
		@Override
		public boolean perform(PlayerInteractEvent event) {
			return true;
		}
	},

//	@Deprecated
//	MAGIC_BLOCK_RANGE(
//		Material.ENCHANTED_BOOK,
//		(short) 0,
//		"Magic block range",
//		null,
//		Arrays.asList(Action.RIGHT_CLICK_BLOCK),
//		0,
//		new HashMap<TBLTResource, Integer>() {{
//		}}
//			) {
//		@Override
//		public boolean perform(PlayerInteractEvent event) {
//			Block block = event.getClickedBlock();
//			MagicBlock.find(block.getType())
//			.ifPresent(b -> b.showRange(block.getLocation()));
//			return true;
//		}
//	},
//
//	@Deprecated
//	MAGIC_BLOCK_START(
//			Material.DIAMOND,
//			(short) 0,
//			"Magic",
//			null,
//			Arrays.asList(Action.RIGHT_CLICK_BLOCK),
//			0,
//			new HashMap<TBLTResource, Integer>() {{
//			}}
//			) {
//		@Override
//		public boolean perform(PlayerInteractEvent event) {
//			Block block = event.getClickedBlock();
//			MagicBlock.find(block.getType())
//			.filter(b -> b.equals(MagicBlock.START))
//			.ifPresent(b -> b.perform(block.getLocation()));
//			return true;
//		}
//	},
//
//	@Deprecated
//	MAGIC_BLOCK_CHAIN(
//			Material.ENCHANTED_BOOK,
//			(short) 0,
//			"Replace a chain block",
//			null,
//			Arrays.asList(Action.RIGHT_CLICK_BLOCK),
//			1,
//			new HashMap<TBLTResource, Integer>() {{
//			}}
//			) {
//		@Override
//		public boolean perform(PlayerInteractEvent event) {
//			Block block = event.getClickedBlock();
//			if(block.getType() == Material.CHEST) {
//				KotobaEffect.MAGIC_MIDIUM.playEffect(block.getLocation());
//				KotobaEffect.MAGIC_MIDIUM.playSound(block.getLocation());
//				block.setType(Material.LAPIS_BLOCK);
//			}
//			return true;
//		}
//	},
//
//	@Deprecated
//	MAGIC_BLOCK_GREEN(
//			Material.EMERALD_BLOCK,
//			(short) 0,
//			"Place a magic block",
//			null,
//			Arrays.asList(Action.RIGHT_CLICK_BLOCK),
//			0,
//			new HashMap<TBLTResource, Integer>() {{
//			}}
//			) {
//		@Override
//		public boolean perform(PlayerInteractEvent event) {
//			BlockFace face = event.getBlockFace();
//			Location location = event.getClickedBlock().getLocation();
//			Location newLocation = location.clone().add(face.getModX(), face.getModY(), face.getModZ());
//			Block block = location.getWorld().getBlockAt(newLocation);
//			block.setType(MagicBlock.CHEST.getMaterial());
//			KotobaEffect.MAGIC_MIDIUM.playEffect(newLocation);
//			KotobaEffect.MAGIC_MIDIUM.playSound(newLocation);
//			return true;
//		}
//	},
	;

	private Material material;
	private short data;
	private String name;
	private List<String> lore;
	private List<Action> triggers;
	private int consume;
	private Map<TBLTResource, Integer> resourceConsumption;

	private ClickBlockAbility(Material material, short data, String name, List<String> lore, List<Action> triggers, int consume, Map<TBLTResource, Integer> resourceConsumption) {
		this.material = material;
		this.data = data;
		this.name = name;
		this.lore = lore;
		this.triggers = triggers;
		this.consume = consume;
		this.resourceConsumption = resourceConsumption;
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
		List<String> all = new ArrayList<String>();
		if(lore != null) {
			all.addAll(lore);
		}
		all.addAll(getResourceLore(null));
		return all;
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
	public Map<TBLTResource, Integer> getResourceConsumption(Block block) {
		return resourceConsumption;
	}
}
