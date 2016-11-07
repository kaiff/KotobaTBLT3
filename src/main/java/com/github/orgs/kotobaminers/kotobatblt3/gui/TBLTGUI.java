package com.github.orgs.kotobaminers.kotobatblt3.gui;

import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.github.orgs.kotobaminers.kotobaapi.userinterface.MinimumGUI;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaAPISound;
import com.github.orgs.kotobaminers.kotobatblt3.player.TBLTJob.TBLTJobEnum;
import com.github.orgs.kotobaminers.kotobatblt3.player.TBLTJob.TBLTNoneJobEnum;

public enum TBLTGUI implements MinimumGUI {
	BLOCK_REPLACER("Block Replacer") {
		@Override
		public void onInventoryClick(InventoryClickEvent event) {
		}
	},

	SELECT_JOB("Select Job") {
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
						KotobaAPISound.GOOD.play(player);
					});
				Stream.of(TBLTNoneJobEnum.values())
					.filter(job -> job.isSameIcon(event.getCurrentItem()))
					.findFirst()
					.ifPresent(job -> {
						job.become(player);
						KotobaAPISound.SHEAR.play(player);
					});
			}
		}

	},;

	private String title;
	private int size;

	private TBLTGUI(String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}
	@Override
	public int getSize() {
		return size;
	}
	@Override
	public void setSize(int size) {
		this.size = size;
	}

	public static Optional<TBLTGUI> find(Inventory inventory) {
		return Stream.of(TBLTGUI.values())
			.filter(e -> e.getTitle().equalsIgnoreCase(inventory.getTitle()))
			.findFirst();
	}
}
