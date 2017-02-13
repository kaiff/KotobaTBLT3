package com.github.orgs.kotobaminers.kotobatblt3.ability;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTUtility;

public class GeneratableBlockListener implements Listener {
	@EventHandler
	void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getAction() == Action.PHYSICAL) {
			Block clickedBlock = event.getClickedBlock();
			if(clickedBlock.getType() == Material.GOLD_PLATE) {
				Player player = event.getPlayer();
				if(!TBLTUtility.isTBLTPlayer(player)) return;
				GeneratableBlock.find(clickedBlock.getLocation())
					.ifPresent(b -> b.perform(event));
			}
		}
	}
}
