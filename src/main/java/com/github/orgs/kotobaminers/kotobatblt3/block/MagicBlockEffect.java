package com.github.orgs.kotobaminers.kotobatblt3.block;

public enum MagicBlockEffect {
//	MAGIC_MIDIUM(Effect.valueOf("WITCH_MAGIC")) {
//		@Override
//		public void playSound(Location location) {
//			for(int i = 0; i < 10; i++) {
//				KotobaAPISound.playRandomPitch(location, Sound.ORB_PICKUP, 0.5f, 0.5f, 2.0f, 10);
//			}
//		}
//		@Override
//		public void playEffect(Location location) {
//			for(int i = 0; i < 10; i++) {
//				location.getWorld().playEffect(Utility.getRandomLocation(location, 2), Effect.valueOf("WITCH_MAGIC"), 0);
//			}
//		}
//	},
//	MAGIC_SMALL(Effect.valueOf("WITCH_MAGIC")) {
//		@Override
//		public void playSound(Location location) {
//			KotobaAPISound.playRandomPitch(location, Sound.ORB_PICKUP, 1, 0.5f, 2.0f, 10);
//		}
//		@Override
//		public void playEffect(Location location) {
//			location.getWorld().playEffect(location, getEffect(), 0);
//			location.getWorld().playEffect(location, getEffect(), 1);
//			location.getWorld().playEffect(location, getEffect(), 2);
//			location.getWorld().playEffect(location, getEffect(), 3);
//		}
//	},
//	EXPLODE_SMALL(Effect.valueOf("EXPLOSION_LARGE")) {
//		@Override
//		public void playSound(Location location) {
//			KotobaAPISound.playRandomPitch(location, Sound.EXPLODE, 1, 0.5f, 2.0f, 10);
//		}
//		@Override
//		public void playEffect(Location location) {
//			location.getWorld().playEffect(location, getEffect(), 0);
//			Utility.getSpherePositions(location, 3)
//				.stream()
//				.map(l -> l.getWorld().getBlockAt(l))
//				.filter(b -> EditableBlock.find(b.getType()).filter(e -> e.getResistance() <= 1).isPresent())
//				.filter(b -> TBLTArena.isInArena(b.getLocation()))
//				.forEach(b -> b.breakNaturally());
//			}
//	},
//	;
//
//	private Effect effect;
//
//	private MagicBlockEffect(Effect effect) {
//		this.effect = effect;
//	}
//
//	public abstract void playEffect(Location location);
//	public abstract void playSound(Location location);
//
//	public void play(Location location) {
//		playEffect(location);
//		playSound(location);
//	}
//
//	Effect getEffect() {
//		return effect;
//	}
//
}
