package com.github.orgs.kotobaminers.kotobatblt3.gui;

import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.userinterface.ChestGUI;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaSound;
import com.github.orgs.kotobaminers.kotobatblt3.game.TBLTData;
import com.github.orgs.kotobaminers.kotobatblt3.game.TBLTJob.TBLTJobEnum;

public enum TBLTGUI implements ChestGUI {
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

	BLOCK_REPLACER("Block Replacer", ChestSize.MINIMUM) {
		@Override
		public void onInventoryClick(InventoryClickEvent event) {
			Optional<TBLTIcon> icon = Stream.of(TBLTIcon.values())
				.filter(i -> i.isIcon(event.getCurrentItem()))
				.findAny();
			if(icon.isPresent()) {
				icon.get().onClickEvent(event);
			}
		}
	},
	ARENA("Arena", ChestSize.MINIMUM) {
		@Override
		public void onInventoryClick(InventoryClickEvent event) {
			Optional<TBLTIcon> icon = Stream.of(TBLTIcon.values())
				.filter(i -> i.isIcon(event.getCurrentItem()))
				.findAny();
			if(icon.isPresent()) {
				icon.get().onClickEvent(event);
			}
		}
	},

	SELECT_JOB("Select Job", ChestSize.MINIMUM) {
		@Override
		public void onInventoryClick(InventoryClickEvent event) {
			event.setCancelled(true);
			if(event.getWhoClicked() instanceof Player) {
				Player player = (Player) event.getWhoClicked();
				player.closeInventory();
				Stream.of(TBLTJobEnum.values())
					.filter(job -> job.isSameIcon(event.getCurrentItem()))
					.findFirst()
					.ifPresent(job -> {
						job.become(player);
						KotobaSound.GOOD.play(player.getLocation());
					});
			}
		}

	},;

	private String title;
	private ChestSize chestSize;

	private TBLTGUI(String title, ChestSize chestSize) {
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

	public static Optional<TBLTGUI> find(Inventory inventory) {
		return Stream.of(TBLTGUI.values())
			.filter(e -> e.getTitle().equalsIgnoreCase(inventory.getTitle()))
			.findFirst();
	}
}
