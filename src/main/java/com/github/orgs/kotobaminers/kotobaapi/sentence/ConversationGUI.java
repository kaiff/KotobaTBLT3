package com.github.orgs.kotobaminers.kotobaapi.sentence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.citizens.CitizensManager;
import com.github.orgs.kotobaminers.kotobaapi.sentence.Sentence.Expression;
import com.github.orgs.kotobaminers.kotobaapi.userinterface.ChestGUI;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;

import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatColor;

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
		ConversationGUIIcon edit = ConversationGUIIcon.EDIT;
		Optional<NPC> npc = CitizensManager.findNPC(sentence.getNPC());
		String name = npc.map(NPC::getName).orElse("N/A");
		int id = npc.map(NPC::getId).orElse(-1);
		String english = sentence.getLines(Arrays.asList(Expression.ENGLISH)).get(0);

		ItemStack icon = KotobaItemStack.create(edit.getMaterial(), edit.getData(), edit.getAmount(), edit.getDisplayName(), null);
		icon = KotobaItemStack.setColoredLore(icon, ChatColor.RESET, Arrays.asList(name + "  |  " + id, english));

		return Arrays.asList(icon);

//		ConversationGUIIcon head = ConversationGUIIcon.NPC;
//		ConversationGUIIcon english = ConversationGUIIcon.ENGLISH;
//		return Arrays.asList(
//			KotobaItemStack.create(head.getMaterial(), head.getData(), head.getAmount(), head.getDisplayName(), Arrays.asList(name, "ID: " + Integer.toString(sentence.getNPC()))),
//			KotobaItemStack.create(english.getMaterial(), english.getData(), english.getAmount(), english.getDisplayName(), Arrays.asList(sentence.getLines(Arrays.asList(Expression.ENGLISH)).get(0))),
//			ConversationGUIIcon.PREPEND.createItemStack(),
//			ConversationGUIIcon.REMOVE.createItemStack()
//		);TODO Showing everything -> Show as one
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

