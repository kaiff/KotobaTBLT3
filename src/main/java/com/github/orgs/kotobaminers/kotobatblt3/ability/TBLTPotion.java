package com.github.orgs.kotobaminers.kotobatblt3.ability;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.entity.PotionSplashEvent;

import com.github.orgs.kotobaminers.kotobaapi.kotobaapi.PotionEnumInterface;

public enum TBLTPotion implements PotionEnumInterface {
	FIRE_EXTINGUISHER() {
		@Override
		public void eventSplash(PotionSplashEvent event) {
			World world = event.getEntity().getWorld();
			Block center = event.getEntity().getLocation().getBlock();
			int radius = 1;
			for(int rX = -radius; rX <= radius; rX++) {
				for(int rY = -radius; rY <= radius; rY++) {
					for(int rZ = -radius; rZ <= radius; rZ++) {
						Block block = world.getBlockAt(center.getX() + rX, center.getY() + rY, center.getZ() + rZ);
						if(block.getType() == Material.FIRE) {
							block.setType(Material.AIR);
						}
					}
				}
			}
		}
	}
	;
	public static TBLTPotion find(PotionSplashEvent event) {
		return TBLTPotion.FIRE_EXTINGUISHER;
	}

}
