package com.github.orgs.kotobaminers.kotobatblt3.gui;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.userinterface.IconListGUI;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaAPIUtility;
import com.github.orgs.kotobaminers.kotobatblt3.game.BlockReplacer;

public enum TBLTIconListGUI implements IconListGUI {
	BLOCK_REPLACER("Block Replacers ") {
		@Override
		public List<ItemStack> getIcons() {
			List<BlockReplacer> replacers = BlockReplacer.getReplacers().stream().collect(Collectors.toList());
			return Stream.iterate(0, i -> i + 1)
				.limit(BlockReplacer.getReplacers().size())
				.map(i -> KotobaAPIUtility.createCustomItem(Material.ANVIL, i + 1, (short) 0, replacers.get(i).getName(), null))
				.filter(Optional::isPresent)
				.map(Optional::get)
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
			BlockReplacer.getReplacers().stream()
				.filter(r -> r.getName().equalsIgnoreCase(name))
				.findAny()
				.ifPresent(r -> r.getGUIIcons().stream().forEach(i -> System.out.println(i.getType())));
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
				.map(m -> KotobaAPIUtility.createCustomItem(m, 1, (short) 0, m.name(), null))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());
		}

		@Override
		public void onIconLeftClickEvent(InventoryClickEvent event) {
		}

		@Override
		public void onIconRightClickEvent(InventoryClickEvent event) {
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
