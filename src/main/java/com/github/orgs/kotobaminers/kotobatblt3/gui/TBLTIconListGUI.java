package com.github.orgs.kotobaminers.kotobatblt3.gui;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.userinterface.IconListGUI;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaAPIItemStack;
import com.github.orgs.kotobaminers.kotobatblt3.game.BlockReplacer;

public enum TBLTIconListGUI implements IconListGUI {
	BLOCK_REPLACER("Block Replacers ") {
		@Override
		public List<ItemStack> getIcons() {
			List<BlockReplacer> replacers = BlockReplacer.getReplacers().stream().collect(Collectors.toList());
			return Stream.iterate(0, i -> i + 1)
				.limit(BlockReplacer.getReplacers().size())
				.map(i -> KotobaAPIItemStack.create(Material.ANVIL, i + 1, (short) 0, replacers.get(i).getName(), null))
				.collect(Collectors.toList());
		}

		@Override
		public void onIconLeftClickEvent(InventoryClickEvent event) {
			onIconClickEvent(event);
		}
		@Override
		public void onIconRightClickEvent(InventoryClickEvent event) {
			onIconClickEvent(event);
		}
		public void onIconClickEvent(InventoryClickEvent event) {
			String name = event.getCurrentItem().getItemMeta().getDisplayName();
			System.out.println(name);
			BlockReplacer.getReplacers().stream()
				.filter(r -> r.getName().equalsIgnoreCase(name))
				.findAny()
				.flatMap(r -> TBLTGUI.BLOCK_REPLACER.create(r.getGUIIcons()))
				.ifPresent(i -> event.getWhoClicked().openInventory(i));
		}

	},
	SOLID_BLOCK("Blocks ") {
		@Override
		public List<ItemStack> getIcons() {
			return Stream.of(Material.values())
				.filter(m -> m.isBlock() && m.isSolid())
				.map(m -> KotobaAPIItemStack.create(m, 1, (short) 0, m.name(), null))
				.collect(Collectors.toList());
		}

		@Override
		public void onIconLeftClickEvent(InventoryClickEvent event) {
		}
		@Override
		public void onIconRightClickEvent(InventoryClickEvent event) {
		}
	},
	EFFECT("Effect ") {
		@Override
		public List<ItemStack> getIcons() {
			return Stream.of(Effect.values())
				.map(e -> KotobaAPIItemStack.create(Material.NETHER_STAR, 1, (short) 0, e.name(), null))
				.collect(Collectors.toList());
		}

		@Override
		public void onIconLeftClickEvent(InventoryClickEvent event) {
			onIconClickEvent(event);
		}
		@Override
		public void onIconRightClickEvent(InventoryClickEvent event) {
			onIconClickEvent(event);
		}
		private void onIconClickEvent(InventoryClickEvent event) {
			HumanEntity clicker = event.getWhoClicked();
			String name = event.getCurrentItem().getItemMeta().getDisplayName();
			Stream.of(Effect.values())
				.filter(e -> e.name().equalsIgnoreCase(name))
				.findAny()
				.ifPresent(e -> {
					clicker.getWorld().playEffect(clicker.getLocation(), e, 1);
				});
			clicker.closeInventory();
		}
	},
	SOUND("Sound ") {
		@Override
		public List<ItemStack> getIcons() {
			return Stream.of(Sound.values())
				.map(s -> KotobaAPIItemStack.create(Material.NOTE_BLOCK, 1, (short) 0, s.name(), null))
				.collect(Collectors.toList());
		}

		@Override
		public void onIconLeftClickEvent(InventoryClickEvent event) {
			onIconClickEvent(event);
		}
		@Override
		public void onIconRightClickEvent(InventoryClickEvent event) {
			onIconClickEvent(event);
		}
		private void onIconClickEvent(InventoryClickEvent event) {
			HumanEntity clicker = event.getWhoClicked();
			String name = event.getCurrentItem().getItemMeta().getDisplayName();
			Stream.of(Sound.values())
				.filter(s -> s.name().equalsIgnoreCase(name))
				.findAny()
				.ifPresent(s -> {
					clicker.getWorld().playSound(clicker.getLocation(), s, 1, 1);
				});
		}
	},
	;

	private String title;
	private TBLTIconListGUI(String title) {
		this.title = title;
	}

	public static Optional<TBLTIconListGUI> find(Inventory inventory) {
		return Stream.of(TBLTIconListGUI.values())
			.filter(gui -> gui.isIconListGUI(inventory))
			.findAny();
	}

	@Override
	public String getTitle() {
		return title;
	}
}
