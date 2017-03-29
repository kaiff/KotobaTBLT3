package com.github.orgs.kotobaminers.kotobatblt3.userinterface;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.ChatColor;
import org.bukkit.block.Chest;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaSound;
import com.github.orgs.kotobaminers.kotobatblt3.ability.TBLTGem;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public enum InteractEffect {
	CLEAR_INVENTORY("Clear Inventory") {
		@Override
		public void playEffect(PlayerInteractEvent event) {
			event.getPlayer().getInventory().clear();
			KotobaSound.FIZZ.play(event.getPlayer().getLocation());
		}
	},


	CLEAR_INVENTORY_EXCEPT_FOR_TOOLS("Clear Inventory except for Magic Tools") {
		@Override
		public void playEffect(PlayerInteractEvent event) {
			PlayerInventory inventory = event.getPlayer().getInventory();
			Stream.of(inventory.getContents())
				.filter(i -> i != null)
				.filter(i -> !exceptions.stream().anyMatch(e -> e.isIconItemStack(i)))
				.forEach(i -> inventory.remove(i));
			KotobaSound.FIZZ.play(event.getPlayer().getLocation());
		}

		private List<TBLTItemStackIcon> exceptions = Arrays.asList(TBLTItemStackIcon.MAGIC_WAND, TBLTItemStackIcon.MAGIC_SPADE);

	},


	CLEAR_GEMS("Remove Gems from Player's Inventory") {
		@Override
		public void playEffect(PlayerInteractEvent event) {
			PlayerInventory inventory = event.getPlayer().getInventory();
			Stream.of(inventory.getContents())
				.filter(i -> i != null)
				.filter(i -> gems.stream().anyMatch(e -> e.getIcon().isIconItemStack(i)))
				.forEach(i -> inventory.remove(i));
			KotobaSound.FIZZ.play(event.getPlayer().getLocation());
		}

		private List<TBLTGem> gems = Stream.of(TBLTGem.values()).collect(Collectors.toList());

	},


	;


	private TBLTItemStackIcon icon = TBLTItemStackIcon.INTERACT_EFFECT;
	private String type = "";


	private InteractEffect(String type) {
		this.type = type;
	}


	public abstract void playEffect(PlayerInteractEvent event);


	public ItemStack create(int amount) {
		ItemStack itemStack = icon.create(amount);
		itemStack = KotobaItemStack.setColoredLore(itemStack, ChatColor.RESET, Arrays.asList(type));
		return itemStack;
	}


	private static List<InteractEffect> find(ItemStack itemStack) {
		return Stream.of(InteractEffect.values())
			.filter(i -> i.create(1).isSimilar(itemStack))
			.collect(Collectors.toList());
	}


	public static void playEffect(Chest chest, PlayerInteractEvent event) {
		Stream.of(chest.getInventory().getContents())
			.filter(i -> i != null)
			.forEach(i -> find(i).forEach(e -> e.playEffect(event)));
	}


}

