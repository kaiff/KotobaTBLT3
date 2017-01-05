package com.github.orgs.kotobaminers.kotobatblt3.block;

@Deprecated
public enum MagicBlock {
//	START(Material.DIAMOND_BLOCK, 10) {
//		@Override
//		public void perform(Location center) {
//			if(0 < searchNext(center).size()) {
//				chainBlocks(center);
//			}
//		}
//	},
//	CHAIN(Material.LAPIS_BLOCK, 10) {
//		@Override
//		public void perform(Location center) {
//			if(0 < searchNext(center).size()) {
//				chainBlocks(center);
//			}
//		}
//	},
//	CHEST(Material.CHEST, 10) {
//		@Override
//		public void perform(Location center) {
//			if(0 < searchNext(center).size()) {
//				turnOff(center);
//				chainBlocks(center);
//				MagicBlockAbility.find(center).ifPresent(a -> a.performChest(center));
//			}
//		}
//	},
//	;
//
//	private Material material;
//	private int range;
//	static Material turnOff = Material.OBSIDIAN;
//
//	static void turnOff(Location location) {
//		location.getWorld().getBlockAt(location).setType(turnOff);
//	}
//
//	private MagicBlock(Material material, int range) {
//		this.material = material;
//		this.range = range;
//	}
//
//	public void chainBlocks(Location location) {
//		int period = 5;
//		int delay = 10;
//		KotobaEffect.MAGIC_MIDIUM.playEffect(location);
//		KotobaEffect.MAGIC_MIDIUM.playSound(location);
//
//		Optional<MagicBlockAbility> ability = MagicBlockAbility.find(location);
//
//		searchNext(location).stream()
//			.map(v -> getPathLocations(location, v))
//			.forEach(locs -> Stream.iterate(0, i -> i + 1)
//				.limit(locs.size())
//				.forEach(i ->
//					Bukkit.getScheduler().scheduleSyncDelayedTask(Setting.getPlugin(), new Runnable(){
//						@Override
//						public void run() {
//							Location loc = locs.get(i);
//							if(i == locs.size() - 1) {
//								MagicBlock.find(loc.getBlock().getType()).ifPresent(b -> b.perform(loc));
//							} else {
//								if(ability.isPresent()) {
//									ability.get().performChain(loc, location, locs.get(locs.size() - 1));
//								} else {
//									KotobaEffect.MAGIC_SMALL.playEffect(loc);
//									KotobaEffect.MAGIC_SMALL.playSound(loc);
//								}
//							}
//						}
//					}, i * period +  delay)
//				)
//			);
//	}
//
//	public abstract void perform(Location location);
//
//	public static Optional<MagicBlock> find(Material material) {
//		return Stream.of(MagicBlock.values())
//				.filter(b -> b.material == material)
//				.findAny();
//	}
//	List<Location> getPathLocations(Location center, Vector vector) {
//		int length = (int) vector.length();
//		Vector direction = vector.clone().multiply((double) 1 / length);
//		return Stream.iterate(1, i -> i + 1)
//				.limit(length)
//				.map(i -> center.clone().add(direction.clone().multiply(i)))
//				.collect(Collectors.toList());
//	}
//
//	List<Vector> searchNext(Location location) {
//		List<Vector> vectors = Arrays.asList(
//				new Vector(1, 0, 0),
//				new Vector(0, 1, 0),
//				new Vector(0, 0, 1),
//				new Vector(-1, 0, 0),
//				new Vector(0, -1, 0),
//				new Vector(0, 0, -1)
//		);
//		return vectors.stream()
//				.map(
//						v ->
//						Stream.iterate(1, i -> i + 1)
//						.limit(range)
//						.map(i -> location.getWorld().getBlockAt(location.clone().add(v.clone().multiply(i))))
//						.filter(b -> MagicBlock.find(b.getType()).filter(find -> find.material != material && find != MagicBlock.START).isPresent())
//						.findFirst()
//						.map(b -> b.getLocation())
//						.filter(l -> TBLTArena.isInAny(l))
//						.orElse(null)
//						).filter(l -> l != null)
//				.map(l -> l.subtract(location))
//				.map(l -> new Vector(l.getBlockX(), l.getBlockY(), l.getBlockZ()))
//				.collect(Collectors.toList());
//	}
//
//	public Material getMaterial() {
//		return material;
//	}
//
//	public static List<Player> findPlayerAbove(Location center) {
//		return center.getWorld().getNearbyEntities(center.clone().add(0, 1, 0), 1, 1, 1).stream()
//			.filter(e -> e.isOnGround())
//			.filter(e -> e.getLocation().add(0, -1, 0).getBlock().getLocation().distance(center) == 0)
//			.filter(e -> e instanceof Player)
//			.map(e -> (Player) e)
//			.collect(Collectors.toList())
//			;
//	}
//
//	public void showRange(Location center) {
//		List<Vector> vectors = Arrays.asList(
//				new Vector(1, 0, 0),
//				new Vector(0, 1, 0),
//				new Vector(0, 0, 1),
//				new Vector(-1, 0, 0),
//				new Vector(0, -1, 0),
//				new Vector(0, 0, -1)
//		);
//		vectors.stream()
//			.map(v -> v.multiply(range))
//			.map(v -> getPathLocations(center, v))
//			.forEach(locs -> locs.stream().forEach(l -> KotobaEffect.MAGIC_SMALL.playEffect(l)));
//		KotobaEffect.MAGIC_SMALL.playSound(center);
//	}
//
}
