package com.github.orgs.kotobaminers.kotobatblt3.gui;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.orgs.kotobaminers.kotobaapi.userinterface.GUIIcon;
import com.github.orgs.kotobaminers.kotobaapi.userinterface.Toggler;


public enum TogglerIcon implements GUIIcon, Toggler {
	EXPLOSION(Material.TNT, 1, (short) 0, "Explosion") {
		@Override
		public void onClickEvent(InventoryClickEvent event) {
		}
	},
	;

	private boolean flag;
	private Material material;
	private int amount = 1;
	private short data = 0;
	private String displayName;
	private List<String> lore;
	private static final String ON = "" + ChatColor.BOLD + ChatColor.GREEN + "ON";
	private static final String OFF = "" + ChatColor.BOLD + ChatColor.RED + "OFF";

	private TogglerIcon(Material material, int amount, short data, String displayName) {
		setMaterial(material);
		setAmount(amount);
		setData(data);
		setDisplayName(displayName);
		setLore(Arrays.asList(getFlagSymbol()));
	}

	private String getFlagSymbol() {
		if(getFlag()) {
			return ON;
		}
		return OFF;
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
	@Override
	public boolean getFlag() {
		return flag;
	}
	@Override
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
