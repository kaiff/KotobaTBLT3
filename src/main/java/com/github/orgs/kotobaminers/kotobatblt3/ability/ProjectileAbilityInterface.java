package com.github.orgs.kotobaminers.kotobatblt3.ability;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.ability.ClickBlockAbilityInterface;

public interface ProjectileAbilityInterface extends ClickBlockAbilityInterface {
	void onHit(ProjectileHitEvent event);
	EntityType getType();
	String getName();
	double getSpeed();
	default Projectile launchProjectile(Player player) {
		Location location = player.getLocation();
		Vector direction = location.getDirection();
		double x = direction.getX();
		double y = direction.getY();
		double z = direction.getZ();
		double rate = Math.sqrt(x*x + y*y + z*z);
		x = x / rate;
		y = y / rate;
		z = z / rate;
		Vector speed = new Vector(x * getSpeed(), y * getSpeed(), z * getSpeed());
		Entity entity = player.getWorld().spawnEntity(location.clone().add(0, 1, 0), getType());
		entity.setCustomName(getName());
		entity.setVelocity(speed);
		if(entity instanceof Projectile) {
			Projectile projectile = (Projectile) entity;
			projectile.setShooter(player);
			return projectile;
		}
		return null;
	}
}
