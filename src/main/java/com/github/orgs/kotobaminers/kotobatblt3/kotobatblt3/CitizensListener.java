package com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence;
import com.github.orgs.kotobaminers.kotobaapi.userinterface.ConversationGUI;
import com.github.orgs.kotobaminers.kotobaapi.userinterface.ConversationGUIIcon;
import com.github.orgs.kotobaminers.kotobatblt3.database.PlayerDatabase;
import com.github.orgs.kotobaminers.kotobatblt3.database.SentenceDatabase;
import com.github.orgs.kotobaminers.kotobatblt3.database.TBLTSentenceHolograms;

import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;

public class CitizensListener implements Listener {


	@EventHandler
	public void onRightClickNPC(NPCRightClickEvent event) {
		Player player = event.getClicker();
		NPC npc = event.getNPC();
		new PlayerDatabase().getOrDefault(event.getClicker().getUniqueId()).npc(npc.getId()).update();

		new TBLTSentenceHolograms(npc.getStoredLocation()).display(npc, player);

	}

	@EventHandler
	public void onLeftClickNPC(NPCLeftClickEvent event) {
		Player player = event.getClicker();
		NPC npc = event.getNPC();
		new PlayerDatabase().getOrDefault(event.getClicker().getUniqueId()).npc(npc.getId()).update();

		Optional<List<Sentence>> optional = new SentenceDatabase().findSentencesByNPCId(npc.getId());
		if(optional.isPresent()) {
			player.openInventory(new ConversationGUI().create(optional.get(), false));
		} else {
			new ConversationGUI().create(Arrays.asList(ConversationGUIIcon.CREATE.createItemStack()))
				.ifPresent(inv -> player.openInventory(inv));
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(!event.getWhoClicked().isOp()) return;
		Inventory inventory = event.getInventory();
		if(new ConversationGUI().isChestGUI(inventory.getTitle())) {
			Stream.of(ConversationGUIIcon.values())
				.filter(e -> e.isIcon(event.getCurrentItem()))
				.forEach(e -> e.performClicked(event));
			event.setCancelled(true);
		}
	}


}

