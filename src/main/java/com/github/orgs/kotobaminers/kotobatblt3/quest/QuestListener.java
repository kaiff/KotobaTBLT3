package com.github.orgs.kotobaminers.kotobatblt3.quest;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArena;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;

public class QuestListener implements Listener {

	@EventHandler
	private void onPlayerInteract(PlayerInteractEvent event) {
		new TBLTArenaMap().findPlayingMap(event.getPlayer())
			.ifPresent(arena -> {
				if(checkFinished(arena)) return;
				arena.getArenaMeta().getQuests().stream()
					.filter(q -> q.isSameEvent(event))
					.filter(q -> !q.getDone())
					.forEach(q -> q.checkQuest(event));

				if(checkFinished(arena)) {
					arena.startNext();
				}
			});
	}


	@EventHandler
	private void onPlayerMove(PlayerMoveEvent event) {
		new TBLTArenaMap().findPlayingMap(event.getPlayer())
			.ifPresent(arena -> {
				if(checkFinished(arena)) return;
				arena.getArenaMeta().getQuests().stream()
					.filter(q -> q.isSameEvent(event))
					.filter(q -> !q.getDone())
					.forEach(q -> q.checkQuest(event));

				if(checkFinished(arena)) {
					arena.startNext();
				}
			});
	}


	private boolean checkFinished(TBLTArena arena) {
		if(arena.getArenaMeta().getQuests().stream().allMatch(q -> q.getDone())) {
			return true;
		}
		return false;
	}

}

