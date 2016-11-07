package com.github.orgs.kotobaminers.kotobatblt3.player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.orgs.kotobaminers.kotobatblt3.ability.ClickAbility;

public class TBLTJob implements Listener {
	public interface KotobaJobInterface {
		ItemStack getIcon();
		void become(Player player);

		default boolean isSameIcon(ItemStack itemStack) {
			ItemStack icon = getIcon();
			ItemMeta iconMeta = icon.getItemMeta();
			ItemMeta itemMeta = itemStack.getItemMeta();
			if(
				icon.getType().equals(itemStack.getType()) &&
				icon.getDurability() == itemStack.getDurability() &&
				iconMeta.getDisplayName().equalsIgnoreCase(itemMeta.getDisplayName())
			) {
				return true;
			}
			return false;
		}
	}

	public enum TBLTNoneJobEnum implements KotobaJobInterface {
		RETURN_TO_DEFAULT() {
			@Override
			public ItemStack getIcon() {
				ItemStack itemStack = new ItemStack(Material.BARRIER, 1, (short) 0);
				ItemMeta itemMeta = itemStack.getItemMeta();
				itemMeta.setDisplayName("Return to Default");
				itemStack.setItemMeta(itemMeta);
				return itemStack;
			}
			@Override
			public void become(Player player) {
				player.getInventory().clear();
				player.setGameMode(GameMode.SURVIVAL);
			}
		}
		;
	}

	public enum TBLTJobEnum implements KotobaJobInterface {
		RESCUE(Arrays.asList(ClickAbility.TALK)) {
			@Override
			public ItemStack getIcon() {
				ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
				ItemMeta itemMeta = itemStack.getItemMeta();
				itemMeta.setDisplayName("Rescue");
				itemMeta.setLore(Arrays.asList("Talk with people"));
				itemStack.setItemMeta(itemMeta);
				return itemStack;
			}
		},
		ENGINEER(Arrays.asList(ClickAbility.INVESTIGATE)) {
			@Override
			public ItemStack getIcon() {
				ItemStack itemStack = new ItemStack(Material.TRIPWIRE_HOOK, 1, (short) 0);
				ItemMeta itemMeta = itemStack.getItemMeta();
				itemMeta.setDisplayName("Engineer");
				itemMeta.setLore(Arrays.asList("Investigate blocks"));
				itemStack.setItemMeta(itemMeta);
				return itemStack;
			}
		},
		;

		private List<ClickAbility> uniqueAbility;

		private TBLTJobEnum(List<ClickAbility> uniqueAbility) {
			this.uniqueAbility = uniqueAbility;
		}

		public void setInventory(Player player) {
			player.getInventory().clear();
			uniqueAbility.stream().map(a -> a.createItem(1)).forEach(i -> player.getInventory().addItem(i));
		}

		@Override
		public void become(Player player) {
			player.setGameMode(GameMode.ADVENTURE);
			setInventory(player);
		}

		@Override
		public ItemStack getIcon() {
			return null;
		}
	}

	public static List<ItemStack> getSelectJobIcons() {
		List<ItemStack> icons = Stream.of(TBLTJobEnum.values())
				.map(TBLTJobEnum::getIcon)
				.collect(Collectors.toList());
		icons.add(TBLTNoneJobEnum.RETURN_TO_DEFAULT.getIcon());
		return icons;
	}
}
