package com.github.orgs.kotobaminers.kotobatblt3.userinterface;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.orgs.kotobaminers.kotobaapi.userinterface.GUIIcon;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;

public enum TBLTGUIIcon implements GUIIcon {
	RESTART(Material.WATCH, 1, (short) 0, "Restart the game", null) {
		@Override
		public void onClickEvent(InventoryClickEvent event) {
			if(event.getWhoClicked() instanceof Player) {
				Player player = (Player) event.getWhoClicked();
				new TBLTArenaMap().findPlayingMap(player)
					.ifPresent(a -> a.restart());
			}
		}
	},

	SEE_HINTS(Material.BOOK_AND_QUILL, 1, (short) 0, "See hints", null) {
		@Override
		public void onClickEvent(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				new TBLTArenaMap().findPlayingMap(player)
					.ifPresent(a -> player.openInventory(TBLTIconListGUI.HINT.createInventory(player, 1)));
		}
	},

	INFORMATION(Material.BOOK, 1, (short) 0, "Information", null) {
		@Override
		public void onClickEvent(InventoryClickEvent event) {
		}
	},

	TELEPORT(Material.ENDER_PEARL, 1, (short) 0, "Teleport", null) {
		@Override
		public void onClickEvent(InventoryClickEvent event) {
			List<String> itemLore = event.getCurrentItem().getItemMeta().getLore();
			if(itemLore != null) {
				if(1 < itemLore.size()) {
					String world = itemLore.get(0);
					String[] coordinate = itemLore.get(1).split(",");
					if(2 < coordinate.length) {
						try {
							int x = Integer.parseInt(coordinate[0]);
							int y = Integer.parseInt(coordinate[1]);
							int z = Integer.parseInt(coordinate[2]);
							Bukkit.getWorlds().stream()
								.filter(w -> w.getName().equalsIgnoreCase(world))
								.findAny()
								.ifPresent(w -> event.getWhoClicked().teleport(new Location(w, x, y, z)));
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	},

	CHECK_POINT(Material.ENDER_PEARL, 1, (short) 0, "Check Point", null) {
		@Override
		public void onClickEvent(InventoryClickEvent event) {
			List<String> itemLore = event.getCurrentItem().getItemMeta().getLore();
			if(itemLore != null) {
				if(1 < itemLore.size()) {
					String world = itemLore.get(0);
					String[] coordinate = itemLore.get(1).split(",");
					if(2 < coordinate.length) {
						try {
							int x = Integer.parseInt(coordinate[0]);
							int y = Integer.parseInt(coordinate[1]);
							int z = Integer.parseInt(coordinate[2]);
							Bukkit.getWorlds().stream()
								.filter(w -> w.getName().equalsIgnoreCase(world))
								.findAny()
								.ifPresent(w -> event.getWhoClicked().teleport(new Location(w, x, y, z)));
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	},

	;

	private Material material;
	private int amount = 1;
	private short data = 0;
	private String displayName;
	private List<String> lore;

	private TBLTGUIIcon(Material material, int amount, short data, String displayName, List<String> lore) {
		setMaterial(material);
		setAmount(amount);
		setData(data);
		setDisplayName(displayName);
		setLore(lore);
	}

	@Override
	public Material getMaterial() {
		return material;
	}
	@Override
	public int getAmount() {
		return amount;
	}
	@Override
	public short getData() {
		return data;
	}
	@Override
	public String getDisplayName() {
		return displayName;
	}
	@Override
	public List<String> getLore() {
		return lore;
	}
	@Override
	public void setMaterial(Material material) {
		this.material = material;
	}
	@Override
	public void setAmount(int amount) {
		this.amount = amount;
	}
	@Override
	public void setData(short data) {
		this.data = data;
	}
	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	@Override
	public void setLore(List<String> lore) {
		this.lore = lore;
	}
}
