package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.ArrayList;
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
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaSound;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.block.EditableBlock;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;
import com.github.orgs.kotobaminers.kotobatblt3.resource.TBLTResource;
import com.github.orgs.kotobaminers.kotobatblt3.utility.Utility;

public enum ProjectileAbility implements ProjectileAbilityInterface {

	BLAST_ARROW(
		EntityType.ARROW,
		Material.ARROW,
		(short) 0,
		"Blast Arrow",
		null,
		Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK),
		0,
		2d,
		new HashMap<TBLTResource, Integer>() {{
		}}
	) {
		@Override
		public void onHit(ProjectileHitEvent event) {
			Projectile projectile = event.getEntity();
			Location location = projectile.getLocation();
			KotobaEffect.MAGIC_MIDIUM.playEffect(location);
			KotobaEffect.MAGIC_MIDIUM.playSound(location);
			Location center = projectile.getWorld().getBlockAt(location).getLocation();
			List<Material> materials = Stream.of(EditableBlock.values())
					.filter(e -> e.getResistance() <= 1)
					.map(b -> b.getMaterial())
					.collect(Collectors.toList());
			Utility.getSpherePositions(center, 3)
			.stream()
			.map(l -> l.getWorld().getBlockAt(l))
			.filter(b -> materials.contains(b.getType()))
			.filter(b -> new TBLTArenaMap().isInAny(b.getLocation()))
			.forEach(b -> b.setType(Material.AIR));
			projectile.remove();
		}
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Projectile projectile = launchProjectile(event.getPlayer());
			projectile.setVelocity(projectile.getVelocity().clone().add(new Vector(0,0.3,0)));
			playPathEffect(projectile, Effect.valueOf("WITCH_MAGIC"), 5);
			projectile.getWorld().playSound(projectile.getLocation(), Sound.WITHER_SHOOT, 1, 1);
			return true;
		}
	},

	MAGNETIC_ARROW(
		EntityType.ARROW,
		Material.ARROW,
		(short) 0,
		"Magnetic Arrow",
		null,
		Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK),
		0,
		2d,
		new HashMap<TBLTResource, Integer>() {{
		}}
	) {
		@Override
		public void onHit(ProjectileHitEvent event) {
			Projectile projectile = event.getEntity();
			Location location = projectile.getLocation();
			KotobaEffect.MAGIC_MIDIUM.playEffect(location);
			KotobaEffect.MAGIC_MIDIUM.playSound(location);
			if(projectile.getShooter() instanceof Player) {
				Player shooter = (Player) projectile.getShooter();
				Bukkit.getOnlinePlayers().stream()
					.filter(p -> p.getUniqueId() != shooter.getUniqueId())
					.filter(p -> ((Entity) p).isOnGround())
					.filter(p -> p.getLocation().distance(projectile.getLocation()) < 3)
					.forEach(p -> {
						p.setVelocity(shooter.getLocation().subtract(p.getLocation()).toVector().normalize().multiply(new Vector(2,0,2)).add(new Vector(0,0.5,0)));
						Utility.playJumpEffect(p);
					});
				Bukkit.getOnlinePlayers().stream()
					.forEach(p -> System.out.println(p.getName() + p.getLocation().distance(projectile.getLocation())));
			}
			projectile.remove();
		}
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Projectile projectile = launchProjectile(event.getPlayer());
			projectile.setVelocity(projectile.getVelocity().clone().add(new Vector(0,0.3,0)));
			playPathEffect(projectile, Effect.valueOf("SLIME"), 5);
			projectile.getWorld().playSound(projectile.getLocation(), Sound.WITHER_SHOOT, 1, 1);
			return true;
		}
	},

	FROST_ARROW(
		EntityType.ARROW,
		Material.ARROW,
		(short) 0,
		"Frost Arrow",
		null,
		Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK),
		0,
		2d,
		new HashMap<TBLTResource, Integer>() {{
		}}
	) {
		@Override
		public void onHit(ProjectileHitEvent event) {
			Projectile projectile = event.getEntity();
			Location location = projectile.getLocation();
			KotobaEffect.MAGIC_MIDIUM.playEffect(location);
			KotobaEffect.MAGIC_MIDIUM.playSound(location);
			Location center = projectile.getWorld().getBlockAt(location).getLocation();
			List<Material> materials = Arrays.asList(Material.WATER, Material.STATIONARY_WATER);
			Utility.getSpherePositions(center, 3)
			.stream()
			.map(l -> l.getWorld().getBlockAt(l))
			.filter(b -> materials.contains(b.getType()))
			.filter(b -> new TBLTArenaMap().isInAny(b.getLocation()))
			.forEach(b -> b.setType(Material.ICE));
			projectile.remove();
		}
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Projectile projectile = launchProjectile(event.getPlayer());
			projectile.setVelocity(projectile.getVelocity().clone().add(new Vector(0,0.3,0)));
			playPathEffect(projectile, Effect.valueOf("WATERDRIP"), 5);
			projectile.getWorld().playSound(projectile.getLocation(), Sound.WITHER_SHOOT, 1, 1);
			return true;
		}
	},

	SMALL_BOMB(
		EntityType.SNOWBALL,
		Material.TNT,
		(short) 0,
		"Small bomb",
		null,
		Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK),
		0,
		0.7d,
		new HashMap<TBLTResource, Integer>() {{
			put(TBLTResource.FLAME_MANA, 1);
		}}
	) {
		@Override
		public void onHit(ProjectileHitEvent event) {
			Projectile projectile = event.getEntity();
			Location location = projectile.getLocation();
			projectile.getWorld().playEffect(location, Effect.valueOf("EXPLOSION_LARGE"), 0);
			projectile.getWorld().playSound(location, Sound.EXPLODE, 1, 1);
			Location center = projectile.getWorld().getBlockAt(location).getLocation();
			List<Material> materials = Stream.of(EditableBlock.values())
				.filter(e -> e.getResistance() <= 1)
				.map(b -> b.getMaterial())
				.collect(Collectors.toList());
			Utility.getSpherePositions(center, 3)
				.stream()
				.map(l -> l.getWorld().getBlockAt(l))
				.filter(b -> materials.contains(b.getType()))
				.filter(b -> new TBLTArenaMap().isInAny(b.getLocation()))
				.forEach(b -> b.setType(Material.AIR));
		}
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Projectile projectile = launchProjectile(event.getPlayer());
			playPathEffect(projectile, Effect.valueOf("LAVA_POP"), 5);
			projectile.getWorld().playSound(projectile.getLocation(), Sound.FUSE, 1, 1);
			return true;
		}
	},

	TREE_GROWTH(
		EntityType.SPLASH_POTION,
		Material.POTION,
		(short) 0,
		"Grow trees",
		null,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR),
		1,
		0.7d,
		new HashMap<TBLTResource, Integer>() {{
			put(TBLTResource.LIFE_MANA, 1);
		}}
	){
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Player player = event.getPlayer();
			launchProjectile(player);
			return true;
		}
		@Override
		public void onHit(ProjectileHitEvent event) {
			Utility.getSpherePositions(event.getEntity().getLocation().getBlock().getLocation(), 2)
				.stream()
				.map(l -> l.getWorld().getBlockAt(l))
				.filter(b -> b.getType() == Material.DEAD_BUSH)
				.filter(b -> new TBLTArenaMap().isInAny(b.getLocation()))
				.map(b -> b.getLocation())
				.forEach(l -> {
					l.getWorld().getBlockAt(l).setType(Material.AIR);
					l.getWorld().generateTree(l, TreeType.TREE);
				});
		}
	},

	TRANSPARENT_BLOCK(
		EntityType.SPLASH_POTION,
		Material.POTION,
		(short) 0,
		"Transparent block",
		null,
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR),
		1,
		0.7d,
		new HashMap<TBLTResource, Integer>() {{
			put(TBLTResource.LIFE_MANA, 1);
		}}
	){
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Player player = event.getPlayer();
			launchProjectile(player);
			return true;
		}
		@Override
		public void onHit(ProjectileHitEvent event) {
			Utility.getSpherePositions(event.getEntity().getLocation().getBlock().getLocation(), 2)
			.stream()
			.map(l -> l.getWorld().getBlockAt(l))
			.filter(b -> EditableBlock.find(b.getType()).filter(e -> e.getResistance() <= 2).isPresent())
			.filter(b -> new TBLTArenaMap().isInAny(b.getLocation()))
			.forEach(b -> b.setType(Material.GLASS));
			;
		}
	},

	GENERATE_BLOCK(
		EntityType.SNOWBALL,
		Material.SNOW_BALL,
		(short) 0,
		"Generate block",
		null,
		Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK),
		1,
		0.7d,
		new HashMap<TBLTResource, Integer>() {{
			put(TBLTResource.METAL_MANA, 1);
		}}
	) {

		@Override
		public boolean perform(PlayerInteractEvent event) {
			Player player = event.getPlayer();
			Entity entity = (Entity) player;
			if(!entity.isOnGround()) return false;
			launchProjectile(player);
			return true;
		}

		@Override
		public void onHit(ProjectileHitEvent event) {
			if(event.getEntity().getShooter() instanceof Player) {
				Location location = event.getEntity().getLocation();
				Player player = (Player) event.getEntity().getShooter();
				Entity entity = (Entity) player;
				if(!entity.isOnGround()) return;

				int duration = 5;
				if(GeneratableBlock.BLOCK.canGenerate(location)) {
					GeneratableBlock.BLOCK.generate(location);
					GeneratableBlock.BLOCK.delete(location, duration);
					Utility.scheduleBlockEffect(player, duration, location);
					Utility.stopPlayer(player, duration);
					KotobaUtility.playCountDown(Setting.getPlugin(), Arrays.asList(player), duration);
				} else {
					KotobaSound.FAILED.play(player.getLocation());
				}
			}
		}
	},

	MAGIC_WEB(
		EntityType.SNOWBALL,
		Material.WEB,
		(short) 0,
		"Generate magic web",
		null,
		Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK),
		0,
		0.7d,
		new HashMap<TBLTResource, Integer>() {{
			put(TBLTResource.LIFE_MANA, 1);
		}}
	) {
		@Override
		public boolean perform(PlayerInteractEvent event) {
			Player player = event.getPlayer();
			Entity entity = (Entity) player;
			if(!entity.isOnGround()) return false;
			Projectile projectile = launchProjectile(player);
			playPathEffect(projectile, Effect.SMOKE, 5);
			projectile.getWorld().playSound(projectile.getLocation(), Sound.SHOOT_ARROW, 1, 1);			return true;
		}
		@Override
		public void onHit(ProjectileHitEvent event) {
			if(event.getEntity().getShooter() instanceof Player) {
				Location location = event.getEntity().getLocation();
				Player player = (Player) event.getEntity().getShooter();
				Entity entity = (Entity) player;
				if(!entity.isOnGround()) return;

				int duration = 5;
				if(GeneratableBlock.WEB.canGenerate(location)) {
					GeneratableBlock.WEB.generate(location);
					GeneratableBlock.WEB.delete(location, duration);
					Utility.scheduleBlockEffect(player, duration, location);
					Utility.stopPlayer(player, duration);
					KotobaUtility.playCountDown(Setting.getPlugin(), Arrays.asList(player), duration);
				} else {
					KotobaSound.FAILED.play(player.getLocation());
				}
			}
		}
	},


	MAGIC_TRAMPOLINE(
		EntityType.SNOWBALL,
		Material.SNOW_BALL,
		(short) 0,
		"Generate jumber",
		null,
		Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK),
		1,
		0.7d,
		new HashMap<TBLTResource, Integer>() {{
			put(TBLTResource.METAL_MANA, 1);
		}}
	) {
		@Override
		public void onHit(ProjectileHitEvent event) {
			ProjectileSource shooter = event.getEntity().getShooter();
			if(shooter instanceof Player) {
				Location location = event.getEntity().getLocation();
				Player player = (Player) shooter;
				Entity entity = (Entity) player;
				if(!entity.isOnGround()) return;

				int duration = 5;
				if(GeneratableBlock.JUMPER.canGenerate(location)) {
					GeneratableBlock.JUMPER.generate(location);
					GeneratableBlock.JUMPER.delete(location, duration);
					Utility.scheduleBlockEffect(player, duration, location);
					Utility.stopPlayer(player, duration);
					KotobaUtility.playCountDown(Setting.getPlugin(), Arrays.asList(player), duration);
				} else {
					KotobaSound.FAILED.play(player.getLocation());
				}
			}
		}

		@Override
		public boolean perform(PlayerInteractEvent event) {
			Player player = event.getPlayer();
			Entity entity = (Entity) player;
			if(!entity.isOnGround()) return false;
			Projectile projectile = launchProjectile(event.getPlayer());
			playPathEffect(projectile, Effect.ENDER_SIGNAL, 5);
			projectile.getWorld().playSound(projectile.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
			return true;
		}
	},
	;

	private EntityType type;
	private Material material;
	private short data;
	private String name;
	private List<String> lore;
	private List<Action> triggers;
	private int consume;
	private double speed;
	private Map<TBLTResource, Integer> resourceConsumption;

	private ProjectileAbility(EntityType type, Material material, short data, String name, List<String> lore, List<Action> triggers, int consume, double speed, Map<TBLTResource, Integer> resourceConsumption) {
		this.type = type;
		this.material = material;
		this.data = data;
		this.name = name;
		this.lore = lore;
		this.triggers = triggers;
		this.consume = consume;
		this.speed = speed;
		this.resourceConsumption = resourceConsumption;
	}

	public static List<ProjectileAbility> find(ItemStack itemStack) {
		return Stream.of(ProjectileAbility.values())
			.filter(ability ->
				ability.getMaterial() == itemStack.getType() &&
				ability.getName().equalsIgnoreCase(itemStack.getItemMeta().getDisplayName())
			)
			.collect(Collectors.toList());
	}
	public static Optional<ProjectileAbility> find(Projectile projectile) {
		return Stream.of(ProjectileAbility.values())
			.filter(ability ->
				ability.type == projectile.getType() &&
				ability.getName().equalsIgnoreCase(projectile.getCustomName())
			)
			.findFirst();
	}

	private static void playPathEffect(Projectile projectile, Effect effect, int second) {
		for(int i = 1; i < second * 20; i = i + 2) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Setting.getPlugin(), new Runnable() {
				@Override
				public void run() {
					if(projectile.isValid()) {
						projectile.getWorld().playEffect(projectile.getLocation(), effect, 0);
					}
				}
			}, i);
		}
	}


	@Override
	public EntityType getType() {
		return type;
	}
	@Override
	public List<Action> getTriggers() {
		return triggers;
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
	public double getSpeed() {
		return speed;
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
