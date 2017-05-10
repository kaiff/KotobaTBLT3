package com.github.orgs.kotobaminers.kotobatblt3.userinterface;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.userinterface.IconListGUI;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArena;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public enum TBLTIconListGUI implements IconListGUI {
	HINT("Hints ") {
		@Override
		public List<ItemStack> getIcons(Player player) {
			List<ItemStack> items = new ArrayList<ItemStack>();
			List<ItemStack> fillings = Stream.iterate(0, i -> i + 1).limit(3).map(i -> new ItemStack(Material.AIR)).collect(Collectors.toList());

			new TBLTArenaMap().findPlayingMap(player)
				.ifPresent(a -> {
					a.getArenaMeta().getHint(player).stream()
						.forEach(list -> {
							Stream.iterate(0, i -> i + 1)
								.limit(3)
								.forEach(i -> items.addAll(fillings));
							Stream.iterate(0, i -> i + 1)
								.limit(list.size())
								.forEach(i -> {
									if(i % 3 == 0) items.addAll(fillings);
									items.add(list.get(i));
									if(i % 3 == 2) items.addAll(fillings);
								});
							Stream.iterate(0, i -> i + 1)
								.limit(3)
								.forEach(i -> items.addAll(fillings));
						});
				});
			return items;
		}

		@Override
		public void onIconLeftClickEvent(InventoryClickEvent event) {
		}

		@Override
		public void onIconRightClickEvent(InventoryClickEvent event) {
		}

	},

	ITEM("Items ") {
		@Override
		public List<ItemStack> getIcons(Player player) {
			return Stream.of(
					Stream.of(TBLTItemStackIcon.values()).map(i -> i.create(1)),
					Stream.of(InteractEffect.values()).map(i -> i.create(1))
				).flatMap(i -> i)
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
			event.getWhoClicked().getInventory().addItem(event.getCurrentItem());
		}

	},

	ARENA("Arenas ") {
		@Override
		public List<ItemStack> getIcons(Player player) {
			List<TBLTArena> arenas = new TBLTArenaMap().getMap().values().stream().map(a -> (TBLTArena) a).sorted(Comparator.comparing(a -> a.getId())).collect(Collectors.toList());

			return Stream.iterate(0, i -> i + 1)
				.limit(arenas.size())
				.map(i -> KotobaItemStack.create(Material.GLASS, (short) 0, i + 1, arenas.get(i).getName(), Arrays.asList(String.valueOf(arenas.get(i).getId()))))
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
				.flatMap(arena -> TBLTPlayerGUI.ARENA.create(IconCreatorUtility.createArenaInfo((TBLTArena) arena)))
				.ifPresent(i -> event.getWhoClicked().openInventory(i));
		}
	},


	SOLID_BLOCK("Blocks ") {
		@Override
		public List<ItemStack> getIcons(Player player) {
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
		public List<ItemStack> getIcons(Player player) {
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
		public List<ItemStack> getIcons(Player player) {
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
