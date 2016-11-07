package com.github.orgs.kotobaminers.kotobaapi.kotobaapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public interface CommandEnumInterface {
	default boolean matchArgs(String[] args) {
		if(getTree().size() <= args.length) {
			for(int i = 0; i < getTree().size(); i++) {
				if(!getTree().get(i).contains(args[i])) return false;
			}
			return true;
		}
		return false;
	}
	
	default String getUsage() {
		return String.join(" ", 
			Arrays.asList(
				"" + ChatColor.DARK_GREEN + ChatColor.BOLD + "/" + getLabel(),
				ChatColor.GREEN + getName(),
				ChatColor.YELLOW + getOption(),
				ChatColor.RESET + " : ",
				getDescription()
			));
	}
	default String getName() {
		return String.join(" ", getTree().stream().map(list -> list.get(0)).collect(Collectors.toList()));
	}
	default List<String> takeOptions(String[] args) {
		List<String> options = new ArrayList<String>();
		for(int i = getTree().size(); i < args.length; i++) {
			options.add(args[i]);
		}
		return options;
	}

	List<List<String>> getTree();
	String getLabel();
	String getOption();
	String getDescription();

	boolean canPerform(Player player);
	
	boolean perform(Player player, String[] args);
	
}
