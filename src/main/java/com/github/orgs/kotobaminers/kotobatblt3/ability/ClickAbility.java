package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaAPISound;
import com.github.orgs.kotobaminers.kotobatblt3.game.BlockReplacer;
import com.github.orgs.kotobaminers.kotobatblt3.gui.TBLTGUI;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.Utility;

public enum ClickAbility implements ItemAbilityInterface {
	WATER(
		Material.POTION,
		(short) 0,
		"Water",
		Arrays.asList("lore"),
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR)) {
		@Override
		public void perform(PlayerInteractEvent event) {
			event.getPlayer().launchProjectile(ThrownPotion.class);
		}
	},
	JUMP_1(
		Material.FEATHER,
		(short) 0,
		"Jump+1",
		Arrays.asList("lore"),
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR)) {
		@Override
		public void perform(PlayerInteractEvent event) {
			Entity entity = event.getPlayer();
			if(entity.isOnGround()) {
				event.getPlayer().setVelocity(new Vector(0, 0.7, 0));
				Utility.playJumpEffect(event.getPlayer());
			}
		}
	},
	JUMP_2(
		Material.FEATHER,
		(short) 0,
		"Jump+2",
		Arrays.asList("lore"),
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR)) {
		@Override
		public void perform(PlayerInteractEvent event) {
			Entity entity2 = event.getPlayer();
			if(entity2.isOnGround()) {
				event.getPlayer().setVelocity(new Vector(0, 0.8, 0));
				Utility.playJumpEffect(event.getPlayer());
			}
		}
	},
	DUSH_JUMP_1(
		Material.FEATHER,
		(short) 0,
		"Dush Jump+1",
		Arrays.asList("lore"),
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR)) {
		@Override
		public void perform(PlayerInteractEvent event) {
			Entity entity0 = event.getPlayer();
			if(entity0.isOnGround()) {
				double x = event.getPlayer().getLocation().getDirection().getX();
				double z = event.getPlayer().getLocation().getDirection().getZ();
				double rate = 1 / Math.sqrt(x*x + z*z);
				x = x * rate * 3;
				z = z * rate * 3;
				event.getPlayer().setVelocity(new Vector(x, 0.4, z));
				Utility.playJumpEffect(event.getPlayer());
			}
		}
	},
	DUSH_JUMP_2(
		Material.FEATHER,
		(short) 0,
		"Dush Jump+2",
		Arrays.asList("lore"),
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR)) {
		@Override
		public void perform(PlayerInteractEvent event) {
			Entity entity3 = event.getPlayer();
			if(entity3.isOnGround()) {
				double x = event.getPlayer().getLocation().getDirection().getX();
				double z = event.getPlayer().getLocation().getDirection().getZ();
				double rate = 1 / Math.sqrt(x*x + z*z);
				x = x * rate * 3;
				z = z * rate * 3;
				event.getPlayer().setVelocity(new Vector(x, 0.7, z));
				Utility.playJumpEffect(event.getPlayer());
			}
		}
	},
	DRONE(
		Material.BREWING_STAND_ITEM,
		(short) 0,
		"Drone",
		Arrays.asList("lore"),
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR)) {
		@Override
		public void perform(PlayerInteractEvent event) {
			Entity entity4 = event.getPlayer();
			if(entity4.isOnGround()) {
				Drone.performDrone(event.getPlayer(), 10);
			}
		}
	},
	INVESTIGATE(
		Material.TRIPWIRE_HOOK,
		(short) 0,
		"Investigate",
		Arrays.asList("lore"),
		Arrays.asList(Action.RIGHT_CLICK_BLOCK)
	) {
		@Override
		public void perform(PlayerInteractEvent event) {
			Block block = event.getClickedBlock();
			if(block == null) return;
			if(block .getType().equals(Material.AIR)) return;

			Optional<BlockReplacer> replacer = BlockReplacer.getReplacers().stream()
				.filter(r -> r.isLocationIn(block.getLocation()))
				.filter(r -> r.getBlock().equals(block.getType()))
				.findAny();
			if(replacer.isPresent()) {
				KotobaAPISound.CLICK.play(event.getPlayer());
				TBLTGUI.BLOCK_REPLACER.create(replacer.get().getGUIIcons()).ifPresent(i -> event.getPlayer().openInventory(i));
				return;
			}
			KotobaAPISound.SHEAR.play(event.getPlayer());
		}
	},
	TALK(
		Material.SKULL,
		(short) 0,
		"Talk with people",
		Arrays.asList("lore"),
		Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR)) {
		@Override
		public void perform(PlayerInteractEvent event) {
		}
	},;

	private Material material;
	private short data;
	private String name;
	private List<String> lore;
	private List<Action> triggers;

	private ClickAbility(Material material, short data, String name, List<String> lore, List<Action> triggers) {
		this.material = material;
		this.data = data;
		this.name = name;
		this.lore = lore;
		this.triggers = triggers;
	}

	@Override
	public boolean canPerform(Player player, Action action) {
		return getTriggers().contains(action) && Utility.isTBLTPlaying(player);
	}
	@Override
	public List<Action> getTriggers() {
		return triggers;
	}

	public static Optional<ClickAbility> find(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		return Stream.of(ClickAbility.values())
			.filter(ability ->
				ability.material.equals(item.getType()) &&
				ability.getData() == item.getDurability() &&
				ability.name.equalsIgnoreCase(meta.getDisplayName())
				)
			.findFirst();
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
	public String getDisplayName() {
		return name;
	}
	@Override
	public List<String> getLore() {
		return lore;
	}
}
