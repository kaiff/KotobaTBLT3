package com.github.orgs.kotobaminers.kotobaapi.utility;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class KotobaBook {


	private BookMeta meta = (BookMeta) new ItemStack(MATERIAL).getItemMeta();
	public static Material MATERIAL = Material.WRITTEN_BOOK;

	public void setAuthor(String author) {
		meta.setAuthor(author);
	}

	public void setTitle(String title) {
		meta.setTitle(title);
	}

	public void giveOpenURLBook(Player player, String text, String url) {
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "give " + player.getName() + " minecraft:" + MATERIAL.name() + " 1 0 {author:" + meta.getAuthor() + ",title:" + meta.getTitle() + ",pages:[\"{text:" + text + ",bold:true,underlined:true,color:dark_aqua,clickEvent:{action:open_url,value:\\\""+ url +"\\\"}}\"]}");
	}

}
