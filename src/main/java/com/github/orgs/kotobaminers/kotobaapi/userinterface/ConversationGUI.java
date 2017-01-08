package com.github.orgs.kotobaminers.kotobaapi.userinterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence.Expression;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobatblt3.utility.NPCManager;

import net.citizensnpcs.api.npc.NPC;

public class ConversationGUI implements ChestGUI {


	private static final String TITLE = "Conversation";


	public Inventory create(List<Sentence> sentences, boolean def) {
		Inventory inventory = create(new ArrayList<ItemStack>()).get();
		toIcons(sentences).entrySet().stream()
			.forEach(e -> inventory.setItem(e.getKey(), e.getValue()));
		return inventory;
	}


	private Map<Integer, ItemStack> toIcons(List<Sentence> sentences) {
		Map<Integer, ItemStack> map = new HashMap<Integer, ItemStack>();
		List<List<ItemStack>> icons = sentences.stream().map(s ->toIcons(s)).collect(Collectors.toList());
		Stream.iterate(0, i -> i + 1)
			.limit(icons.size())
			.forEach(i -> Stream.iterate(0, j -> j + 1)
				.limit(icons.get(i).size())
				.forEach(j -> map.put(i + j * 9, icons.get(i).get(j)))
			);
		return map;
	}

	private List<ItemStack> toIcons(Sentence sentence) {
		String name = NPCManager.findNPC(sentence.getNPC()).map(NPC::getName).orElse("N/A");
		ConversationGUIIcon head = ConversationGUIIcon.HEAD;
		ConversationGUIIcon english = ConversationGUIIcon.ENGLISH;
		return Arrays.asList(
			KotobaItemStack.create(head.getMaterial(), head.getData(), head.getAmount(), head.getDisplayName(), Arrays.asList(name, "ID: " + Integer.toString(sentence.getNPC()))),
			KotobaItemStack.create(english.getMaterial(), english.getData(), english.getAmount(), english.getDisplayName(), Arrays.asList(sentence.getLines(Arrays.asList(Expression.ENGLISH)).get(0)))
		);
	}

	@Override
	public String getTitle() {
		return TITLE;
	}

	@Override
	public ChestSize getChestSize() {
		return ChestSize.LARGE;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent event) {

	}


}

