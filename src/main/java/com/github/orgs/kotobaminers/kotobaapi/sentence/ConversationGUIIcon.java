package com.github.orgs.kotobaminers.kotobaapi.sentence;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.orgs.kotobaminers.kotobaapi.userinterface.GUIIcon;
import com.github.orgs.kotobaminers.kotobatblt3.database.PlayerDatabase;
import com.github.orgs.kotobaminers.kotobatblt3.database.SentenceDatabase;
import com.github.orgs.kotobaminers.kotobatblt3.database.TBLTConversationEditorMap;
import com.github.orgs.kotobaminers.kotobatblt3.database.TBLTSentence;
import com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3.TBLTCommandExecutor.PlayerCommand;

public enum ConversationGUIIcon implements GUIIcon {

	EDIT(Material.SKULL_ITEM, 1, (short) 3, "Edit Sentence", null) {
		@Override
		public void onClickEvent(InventoryClickEvent event) {
			int select = event.getRawSlot() % 9;
			int npc = new
					PlayerDatabase().getOrDefault(event.getWhoClicked().getUniqueId()).getNPC();
			new SentenceDatabase().findConversation(npc)
				.ifPresent(conversation -> new TBLTConversationEditorMap().registerConversationEditorOrDefault(conversation, event.getWhoClicked().getUniqueId(), select));
			event.getWhoClicked().closeInventory();
			event.getCurrentItem().getItemMeta().getLore()
				.forEach(line -> event.getWhoClicked().sendMessage(line));
			Arrays.asList(
				PlayerCommand.EDIT_NPC,
				PlayerCommand.EDIT_ENGLISH,
				PlayerCommand.EDIT_PREPEND,
				PlayerCommand.EDIT_APPEND,
				PlayerCommand.EDIT_REMOVE,
				PlayerCommand.EDIT_SERVANT
			).stream()
				.map(c -> c.getUsage())
				.forEach(usage -> event.getWhoClicked().sendMessage(usage));
		}
	},

	CREATE(Material.SKULL_ITEM, 1, (short) 3, "Create Conversation", null) {
		@Override
		public void onClickEvent(InventoryClickEvent event) {
			int id = new PlayerDatabase().getOrDefault(event.getWhoClicked().getUniqueId()).getNPC();
			new TBLTConversationEditorMap().createConversation(id, event.getWhoClicked().getUniqueId());
			event.getWhoClicked().closeInventory();
		}
	},

	NPC(Material.SKULL_ITEM, 1, (short) 3, "Change NPC", null) {
		@Override
		public void onClickEvent(InventoryClickEvent event) {
		}
	},

	ENGLISH(Material.WOOL, 1, (short) 3, "English", null) {
		@Override
		public void onClickEvent(InventoryClickEvent event) {
			int select = event.getRawSlot() % 9;
			int npc = new PlayerDatabase().getOrDefault(event.getWhoClicked().getUniqueId()).getNPC();
			new SentenceDatabase().findConversation(npc)
				.ifPresent(conversation -> new TBLTConversationEditorMap().registerConversationEditorOrDefault(conversation, event.getWhoClicked().getUniqueId(), select));
			event.getWhoClicked().closeInventory();
		}
	},

	KANJI(Material.WOOL, 1, (short) 14, "Kanji", null) {
		@Override
		public void onClickEvent(InventoryClickEvent event) {
		}
	},

	PREPEND(Material.APPLE, 1, (short) 0, "Prepend Sentence", null) {
		@Override
		public void onClickEvent(InventoryClickEvent event) {
			int select = event.getRawSlot() % 9;
			int npc = new PlayerDatabase().getOrDefault(event.getWhoClicked().getUniqueId()).getNPC();
			new SentenceDatabase().findConversation(npc)
				.ifPresent(conversation -> new TBLTConversationEditorMap().registerConversationEditorOrDefault(conversation, event.getWhoClicked().getUniqueId(), select).prependSentence(new TBLTSentence().empty(conversation, npc)));
			event.getWhoClicked().closeInventory();
		}
	},

	REMOVE(Material.GLASS_BOTTLE, 1, (short) 0, "Remove Sentence", null) {
		@Override
		public void onClickEvent(InventoryClickEvent event) {
			int select = event.getRawSlot() % 9;
			int npc = new PlayerDatabase().getOrDefault(event.getWhoClicked().getUniqueId()).getNPC();
			new SentenceDatabase().findConversation(npc)
				.ifPresent(conversation -> new TBLTConversationEditorMap().registerConversationEditorOrDefault(conversation, event.getWhoClicked().getUniqueId(), select).removeSentence());
			event.getWhoClicked().closeInventory();
		}
	},

	ADD(Material.GOLDEN_APPLE, 1, (short) 0, "Add Sentence", null) {
		@Override
		public void onClickEvent(InventoryClickEvent event) {
			int npc = new PlayerDatabase().getOrDefault(event.getWhoClicked().getUniqueId()).getNPC();
			new SentenceDatabase().findConversation(npc)
				.ifPresent(conversation -> new TBLTConversationEditorMap().registerConversationEditorOrDefault(conversation, event.getWhoClicked().getUniqueId()).appendSentence(new TBLTSentence().empty(conversation, npc)));
			event.getWhoClicked().closeInventory();
		}
	},

	;


	private Material material;
	private int amount = 1;
	private short data = 0;
	private String displayName;
	private List<String> lore;


	private ConversationGUIIcon(Material material, int amount, short data, String displayName, List<String> lore) {
		setMaterial(material);
		setAmount(amount);
		setData(data);
		setDisplayName(displayName);
		setLore(lore);
	}


	public void performClicked(InventoryClickEvent event) {
		playClickedSound(event.getWhoClicked().getLocation());
		onClickEvent(event);
	}
	public void playClickedSound(Location location) {
		location.getWorld().playSound(location, Sound.CLICK, 1, 1);
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
