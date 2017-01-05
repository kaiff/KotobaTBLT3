package com.github.orgs.kotobaminers.kotobaapi.utility;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobatblt3.block.EditableBlock;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Setting;
import com.github.orgs.kotobaminers.kotobatblt3.utility.Utility;

public enum KotobaEffect {
	MAGIC_MIDIUM() {
		@Override
		public void playSound(Location location) {
			for(int i = 0; i < 10; i++) {
				KotobaSound.playRandomPitch(location, Sound.ORB_PICKUP, 0.5f, 0.5f, 2.0f, 10);
			}
		}
		@Override
		public void playEffect(Location location) {
			Effect effect = Effect.valueOf("WITCH_MAGIC");
			for(int i = 0; i < 10; i++) {
				location.getWorld().playEffect(Utility.getRandomLocation(location, 2), effect, 0);
			}
		}
	},

	MAGIC_SMALL() {
		@Override
		public void playSound(Location location) {
			KotobaSound.playRandomPitch(location, Sound.ORB_PICKUP, 1, 0.5f, 2.0f, 10);
		}
		@Override
		public void playEffect(Location location) {
			Effect effect = Effect.valueOf("WITCH_MAGIC");
			location.getWorld().playEffect(location, effect, 0);
			location.getWorld().playEffect(location, effect, 1);
			location.getWorld().playEffect(location, effect, 2);
			location.getWorld().playEffect(location, effect, 3);
		}
	},

	TWINCLE_MIDIUM() {
		@Override
		public void playSound(Location location) {
			for(int i = 0; i < 10; i++) {
				KotobaSound.playRandomPitch(location, Sound.ORB_PICKUP, 0.5f, 0.5f, 2.0f, 10);
			}
		}
		@Override
		public void playEffect(Location location) {
			Effect effect = Effect.valueOf("HAPPY_VILLAGER");
			for(int i = 0; i < 10; i++) {
				location.getWorld().playEffect(Utility.getRandomLocation(location, 1), effect, i);
			}
		}
	},

	CRIT_MIDIUM() {
		@Override
		public void playSound(Location location) {
			for(int i = 0; i < 10; i++) {
				KotobaSound.playRandomPitch(location, Sound.HURT_FLESH, 0.5f, 0.5f, 2.0f, 10);
			}
		}
		@Override
		public void playEffect(Location location) {
			Effect effect = Effect.valueOf("CRIT");
			for(int i = 0; i < 20; i++) {
				location.getWorld().playEffect(location, effect, i);
			}
		}
	},

	BREAK_BLOCK_MIDIUM() {
		@Override
		public void playSound(Location location) {
			for(int i = 0; i < 10; i++) {
				KotobaSound.playRandomPitch(location, Sound.DIG_STONE, 0.5f, 0.5f, 2.0f, 10);
			}
		}
		@Override
		public void playEffect(Location location) {
			Effect effect = Effect.valueOf("TILE_BREAK");
			for(int i = 0; i < 20; i++) {
				location.getWorld().playEffect(location, effect, 1);
			}
		}
	},

	EXPLODE_SMALL() {
		@Override
		public void playSound(Location location) {
			KotobaSound.playRandomPitch(location, Sound.EXPLODE, 1, 0.5f, 2.0f, 10);
		}
		@Override
		public void playEffect(Location location) {
			location.getWorld().playEffect(location, Effect.valueOf("EXPLOSION_LARGE"), 0);
			Utility.getSpherePositions(location, 3)
				.stream()
				.map(l -> l.getWorld().getBlockAt(l))
				.filter(b -> EditableBlock.find(b.getType()).filter(e -> e.getResistance() <= 1).isPresent())
				.filter(b -> new TBLTArenaMap().isInAny(b.getLocation()))
				.forEach(b -> b.breakNaturally());
			}
	},

	ENDER_SIGNAL() {
		@Override
		public void playSound(Location location) {
			KotobaSound.playRandomPitch(location, Sound.ENDERMAN_TELEPORT, 1, 0.5f, 2.0f, 10);
		}
		@Override
		public void playEffect(Location location) {
			location.getWorld().playEffect(location, Effect.ENDER_SIGNAL, 0);
		}
	},

	PORTAL() {
		@Override
		public void playSound(Location location) {
			KotobaSound.playRandomPitch(location, Sound.PORTAL_TRAVEL, 1, 0.5f, 2.0f, 10);
		}
		@Override
		public void playEffect(Location location) {
			location.getWorld().playEffect(location, Effect.ENDER_SIGNAL, 0);
		}
	},

	;

	private KotobaEffect() {
	}

	public abstract void playEffect(Location location);
	public abstract void playSound(Location location);

	public static void dropItemEffect(ItemStack itemStack, Location location) {
		Item item = location.getWorld().dropItemNaturally(location, itemStack);
		item.setPickupDelay(200);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Setting.getPlugin(), new Runnable() {
			@Override
			public void run() {
				item.remove();
				KotobaEffect.MAGIC_SMALL.playEffect(item.getLocation());
				KotobaEffect.MAGIC_SMALL.playSound(item.getLocation());
			}
		}, 20);
	}
}
