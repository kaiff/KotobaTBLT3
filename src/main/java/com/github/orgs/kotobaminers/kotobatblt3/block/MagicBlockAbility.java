package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobatblt3.ability.ItemAbilityInterface;

@Deprecated
public enum MagicBlockAbility implements ItemAbilityInterface {
	BLAST_SMALL(Material.TNT, (short) 0, "Magic Bomb", null, 1) {
		@Override
		public void performChain(Location target, Location from, Location to) {
			KotobaEffect.EXPLODE_SMALL.playSound(target);
			KotobaEffect.EXPLODE_SMALL.playEffect(target);
		}
		@Override
		public void playEffect(Location location) {
		}
		@Override
		public void playSound(Location location) {
		}
		@Override
		public void performChest(Location target) {
		}
	},
	;

	private Material material;
	private short data;
	private String name;
	private List<String> lore;
	private int consume;

	private MagicBlockAbility(Material material, short data, String name, List<String> lore, int consume) {
		this.material = material;
		this.data = data;
		this.name = name;
		this.lore = lore;
		this.consume = consume;
	}

	public abstract void performChain(Location target, Location from, Location to);
	public abstract void performChest(Location target);

	public static Optional<MagicBlockAbility> find(ItemStack itemStack) {
		ItemMeta meta = itemStack.getItemMeta();
		return Stream.of(MagicBlockAbility.values())
			.filter(ability ->
				ability.material == itemStack.getType() &&
				ability.getData() == itemStack.getDurability() &&
				ability.name.equalsIgnoreCase(meta.getDisplayName())
			)
			.findFirst();
	}
	public static Optional<MagicBlockAbility> find(Location location) {
		Block block = location.getBlock();
		if(block.getType() == Material.CHEST) {
			Inventory inventory = ((Chest) block.getState()).getInventory();
			return Stream.of(inventory.getContents())
				.findFirst()
				.flatMap(itemStack -> MagicBlockAbility.find(itemStack));
		}
		return Optional.empty();
	}

	public abstract void playEffect(Location location);
	public abstract void playSound(Location location);

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
	public int getConsumption() {
		return consume;
	}
}
