package com.github.orgs.kotobaminers.kotobatblt3.userinterface;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.userinterface.ChestGUI;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobatblt3.database.TBLTData;

public enum TBLTPlayerGUI implements ChestGUI {


	YOU_NEED("You need...", ChestSize.MINIMUM) {
		@Override
		public void onInventoryClick(InventoryClickEvent event) {
		}
	},


	RESOURCES("Resources", ChestSize.MINIMUM) {
		@Override
		public void onInventoryClick(InventoryClickEvent event) {
		}
	},


	INVESTIGATE("Investigating...", ChestSize.MINIMUM) {
		@Override
		public void onInventoryClick(InventoryClickEvent event) {
		}
	},


	PREDICTION("Prediction...", ChestSize.MINIMUM) {
		@Override
		public void onInventoryClick(InventoryClickEvent event) {
		}
	},


	CLAIRVOYANCE("Clairvoyance...", ChestSize.MINIMUM) {
		@Override
		public void onInventoryClick(InventoryClickEvent event) {
		}
	},


	/**
	 * @author fukus
	 * TBLTData.target -> AIR
	 */
	ITEM_EXCHANGER("Choose one", ChestSize.MINIMUM) {
		@Override
		public void onInventoryClick(InventoryClickEvent event) {
			Player player = (Player) event.getWhoClicked();
			ItemStack item = event.getCurrentItem();
			if(item != null) {
				if(item.getType() != Material.AIR) {
					TBLTData.getOrDefault(player.getUniqueId())
						.findTarget(player)
						.ifPresent(loc -> {
							KotobaItemStack.consume(player.getInventory(), player.getItemInHand(), 1);
							player.getInventory().addItem(item);
							player.closeInventory();
							loc.getBlock().setType(Material.AIR);
							KotobaEffect.MAGIC_MIDIUM.playEffect(loc);
							KotobaEffect.MAGIC_MIDIUM.playSound(loc);
						});

				}
			}
			event.setCancelled(true);

		}
	},


	QUEST_LIST("Quests", ChestSize.MINIMUM) {
		@Override
		public void onInventoryClick(InventoryClickEvent event) {
			event.setCancelled(true);
		}
	},


	ARENA("Arena", ChestSize.MINIMUM) {
		@Override
		public void onInventoryClick(InventoryClickEvent event) {
			ItemStack clicked = event.getCurrentItem();
			List<TBLTIcon> icons = Stream.of(TBLTIcon.values())
				.filter(i -> i.isIcon(clicked))
				.collect(Collectors.toList());
			if(0 < icons.size()) {
				icons.forEach(i -> i.onClickEvent(event));
			} else {
				event.getWhoClicked().getInventory().addItem(clicked);
			}
		}
	},

	QUESTS("Quests", ChestSize.MINIMUM) {
		@Override
		public void onInventoryClick(InventoryClickEvent event) {
		}
	},


	;

	private String title;
	private ChestSize chestSize;

	private TBLTPlayerGUI(String title, ChestSize chestSize) {
		this.title = title;
		this.chestSize = chestSize;
	}

	@Override
	public String getTitle() {
		return title;
	}
	@Override
	public ChestSize getChestSize() {
		return chestSize;
	}

	public static Optional<TBLTPlayerGUI> find(Inventory inventory) {
		return Stream.of(TBLTPlayerGUI.values())
			.filter(e -> e.getTitle().equalsIgnoreCase(inventory.getTitle()))
			.findFirst();
	}
}
