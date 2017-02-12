package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.ability.ClickEntityAbilityInterface;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.utility.Utility;

public enum ClickEntityAbility implements ClickEntityAbilityInterface {
	MIRROR_IMAGE(
		TBLTItemStackIcon.DUMMY,
		1
	) {
		@Override
		public boolean interact(PlayerInteractEntityEvent event) {
			if(event.getRightClicked() instanceof Player) {
				Player clicked = (Player) event.getRightClicked();
				if(!Utility.isTBLTPlayer(clicked)) return false;
				Location from = clicked.getLocation();

				List<Block> targets = clicked.getLastTwoTargetBlocks(new HashSet<Material>(Arrays.asList(Material.AIR)), 4);
				if(targets.size() != 2) return false;
				Block lookAt = targets.get(1);
				List<Material> mirrors = Arrays.asList(Material.GLASS, Material.THIN_GLASS, Material.STAINED_GLASS, Material.STAINED_GLASS_PANE, Material.ICE);
				if(!mirrors.contains(lookAt.getType())) return false;

				Block opposite = lookAt.getRelative(lookAt.getFace(targets.get(0)).getOppositeFace());
				if(opposite.getType() != Material.AIR) return false;

				Location down = opposite.getLocation().clone().add(0,-1,0);
				Location up = opposite.getLocation().clone().add(0,1,0);
				Location to = null;
				if(down.getBlock().getType() == Material.AIR) {
					to = down;
				} else if(up.getBlock().getType() == Material.AIR) {
					to = opposite.getLocation();
				} else {
					return false;
				}

				if(new TBLTArenaMap().isInAny(to)) {
					to.add(0.5,0,0.5);

					Vector direction = from.clone().subtract(to).toVector();
					to.setDirection(direction);
					clicked.teleport(to);
					KotobaEffect.ENDER_SIGNAL.playEffect(from);
					KotobaEffect.ENDER_SIGNAL.playEffect(to);
					to.getWorld().playSound(to, Sound.PORTAL_TRIGGER, 1, 1);
					return true;
				}
			}
			return false;
		}

	},

	FIRE_RESISTANCE(
		TBLTItemStackIcon.DUMMY,
		1
	) {
		@Override
		public boolean interact(PlayerInteractEntityEvent event) {
			Entity entity = event.getRightClicked();
			if(entity instanceof Player) {
				Player player = (Player) entity;
				player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60 * 20, 1));
				KotobaEffect.MAGIC_MIDIUM.playEffect(player.getLocation());
				KotobaEffect.MAGIC_MIDIUM.playSound(player.getLocation());
				return true;
			}
			return false;
		}
	},

	GIVE_A_RIDE(
		TBLTItemStackIcon.DUMMY,
		1
	) {
		@Override
		public boolean interact(PlayerInteractEntityEvent event) {
			if(event.getRightClicked() instanceof Player) {
				Player player = (Player) event.getRightClicked();
				if(Utility.isTBLTPlayer(player)) {
					player.leaveVehicle();
					Minecart cart = (Minecart) player.getWorld().spawnEntity(player.getLocation(), EntityType.MINECART);
					cart.setPassenger(player);
					KotobaEffect.MAGIC_MIDIUM.playEffect(player.getLocation());
					KotobaEffect.MAGIC_MIDIUM.playSound(player.getLocation());
					return true;
				}
			}
			return false;
		}
	},

	THROW_ABOVE_1(
		TBLTItemStackIcon.DUMMY,
		1
	) {
		@Override
		public boolean interact(PlayerInteractEntityEvent event) {
			Entity entity = event.getRightClicked();
			if(!entity.isOnGround()) return false;

			entity.setVelocity(new Vector(0, 0.4, 0));
			Utility.playJumpEffect(entity);
			entity.getLocation().getWorld().playEffect(entity.getLocation(), Effect.POTION_BREAK, 0);
			return true;
		}
	},

	THROW_AWAY_1(
		TBLTItemStackIcon.DUMMY,
		1
	) {
		@Override
		public boolean interact(PlayerInteractEntityEvent event) {
			Player player = event.getPlayer();
			Entity clicked = event.getRightClicked();

			if(!clicked.isOnGround()) return false;

			double x = player.getLocation().getDirection().getX();
			double z = player.getLocation().getDirection().getZ();
			double rate = 1 / Math.sqrt(x*x + z*z);
			x = x * rate * 3;
			z = z * rate * 3;

			Vector velocity = new Vector(x, 0.4, z);
			clicked.setVelocity(velocity);

			KotobaEffect.MAGIC_MIDIUM.playEffect(clicked.getLocation());
			Utility.playJumpEffect(clicked);

			return true;
		}
	},
	;


	KotobaItemStackIcon icon;
	int consumption;


	private ClickEntityAbility(KotobaItemStackIcon icon, int consumption) {
		this.icon = icon;
		this.consumption = consumption;
	}


	@Override
	public KotobaItemStackIcon getIcon() {
		return icon;
	}
	@Override
	public int getConsumption() {
		return consumption;
	}


}

