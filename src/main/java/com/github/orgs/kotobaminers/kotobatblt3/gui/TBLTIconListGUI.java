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
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobaapi.worldeditor.BlockStorage;
import com.github.orgs.kotobaminers.kotobatblt3.block.BlockReplacer;
import com.github.orgs.kotobaminers.kotobatblt3.block.BlockReplacerMap;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArena;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;

public enum TBLTIconListGUI implements IconListGUI {
	ARENA("Arenas ") {
		@Override
		public List<ItemStack> getIcons() {
			List<BlockStorage> arenas = new TBLTArenaMap().getStorages();
			return Stream.iterate(0, i -> i + 1)
				.limit(arenas.size())
				.map(i -> KotobaItemStack.create(Material.GLASS, (short) 0, i + 1, arenas.get(i).getName(), null))
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
			String name = event.getCurrentItem().getItemMeta().getDisplayName();
			new TBLTArenaMap().findUnique(name)
				.flatMap(arena -> TBLTGUI.ARENA.create(IconCreatorUtility.getIcons((TBLTArena) arena)))
				.ifPresent(i -> event.getWhoClicked().openInventory(i));
		}
	},

	BLOCK_REPLACER("Block Replacers ") {
		@Override
		public List<ItemStack> getIcons() {
			List<BlockStorage> replacers = new BlockReplacerMap().getStorages();
			return Stream.iterate(0, i -> i + 1)
				.limit(replacers.size())
				.map(i -> KotobaItemStack.create(Material.ANVIL, (short) 0, i + 1, replacers.get(i).getName(), null))
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
			new BlockReplacerMap().findUnique(name)
				.flatMap(arena -> TBLTGUI.BLOCK_REPLACER.create(IconCreatorUtility.getIcons((BlockReplacer) arena)))
				.ifPresent(i -> event.getWhoClicked().openInventory(i));
		}
	},
	SOLID_BLOCK("Blocks ") {
		@Override
		public List<ItemStack> getIcons() {
			return Stream.of(Material.values())
				.filter(m -> m.isBlock() && m.isSolid())
				.map(m -> KotobaItemStack.create(m, (short) 0, 1, m.name(), null))
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
				.map(e -> KotobaItemStack.create(Material.NETHER_STAR, (short) 0, 1, e.name(), null))
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
				.map(s -> KotobaItemStack.create(Material.NOTE_BLOCK, (short) 0, 1, s.name(), null))
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
