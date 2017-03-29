package com.github.orgs.kotobaminers.kotobatblt3.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;

public enum TBLTItemStackIcon implements KotobaItemStackIcon {


	//Ability
	TEST(
			Material.APPLE,
			(short) 0,
			"Test",
			null,
			IconFindType.SIMILAR
	),


	MAGIC_WAND(
			Material.REDSTONE_TORCH_ON,
			(short) 0,
			"Magic Wand",
			null,
			IconFindType.SIMILAR
	),


	MAGIC_SPADE(
			Material.DIAMOND_SPADE,
			(short) 0,
			"Magic Spade",
			null,
			IconFindType.SIMILAR
	),


	//Chest Option
	TRIGGER_ITEM_ALL_MATCH(
			Material.FLINT_AND_STEEL,
			(short) 0,
			"Trigger Item Setting",
			Arrays.asList("You need all the items to activate this chest in the row"),
			IconFindType.SIMILAR
	),


	INTERACT_EFFECT(
			Material.FLINT_AND_STEEL,
			(short) 0,
			"Chest Effect",
			new ArrayList<>(),
			IconFindType.SIMILAR
	),


	ITEM_GATE_KEY(
			Material.GLASS,
			(short) 0,
			"Structure Key",
			Arrays.asList("Player Gate"),
			IconFindType.SIMILAR
	),


	ONE_TIME_GATE_KEY(
			Material.GLASS,
			(short) 0,
			"Structure Key",
			Arrays.asList("One Time Gate"),
			IconFindType.SIMILAR
	),


	CHOOSE_ONE_KEY(
			Material.ENCHANTMENT_TABLE,
			(short) 0,
			"Structure Key",
			Arrays.asList("Choose One"),
			IconFindType.SIMILAR
	),


	SUMMON_SERVENT_KEY(
			Material.EGG,
			(short) 0,
			"Structure Key",
			Arrays.asList("Summon Servents"),
			IconFindType.SIMILAR
	),


	GEM_PORTAL_KEY_3x3(
			Material.TRAPPED_CHEST,
			(short) 0,
			"Portal Key",
			Arrays.asList("Gem 3x3"),
			IconFindType.SIMILAR
	),


	SWITCH_KEY(
			Material.LEVER,
			(short) 0,
			"Switch Key",
			null,
			IconFindType.SIMILAR
	),


	SWITCH_REVERSE(
			Material.LEVER,
			(short) 0,
			"Switch Reverse",
			null,
			IconFindType.SIMILAR
	),


	SWITCH_REPLACER_ON(
			Material.LEVER,
			(short) 0,
			"Replacer Switch",
			Arrays.asList("On"),
			IconFindType.SIMILAR
	),


	SWITCH_REPLACER_OFF(
			Material.LEVER,
			(short) 0,
			"Replacer Switch",
			Arrays.asList("Off"),
			IconFindType.SIMILAR
	),


	SWITCH_MATERIAL(
			Material.GLASS,
			(short) 0,
			"Replacer Switch",
			new ArrayList<>(),
			IconFindType.EXCEPT_LORE
	),


	SWITCHABLE_GATE_1x3(
			Material.LEVER,
			(short) 0,
			"Switchable Structure",
			Arrays.asList("Gate 1x3"),
			IconFindType.SIMILAR
	),


	CHEST_HOLOGRAMS(
			Material.TRAPPED_CHEST,
			(short) 0,
			"Chest Holograms",
			Arrays.asList("Write Books"),
			IconFindType.SIMILAR
	),


	GREEN_GEM(
			Material.EMERALD_BLOCK,
			(short) 0,
			"Green Gem",
			null,
			IconFindType.SIMILAR
	),

	BLUE_GEM(
			Material.DIAMOND_BLOCK,
			(short) 0,
			"Blue Gem",
			null,
			IconFindType.SIMILAR
	),

	RED_GEM(
			Material.REDSTONE_BLOCK,
			(short) 0,
			"Red Gem",
			null,
			IconFindType.SIMILAR
	),

	DUMMY(
			Material.BARRIER,
			(short) 0,
			"Temporaty Dummy",
			null,
			IconFindType.SIMILAR
	),

	PORTAL_CRYSTAL(
			Material.NETHER_STAR,
			(short) 0,
			"Portal Crystal",
			Arrays.asList("This special item is used to open the time travel portal to your next location.", "Click the portal to open it."),
			IconFindType.EXCEPT_LORE
	),

	SINGLE_PORTAL(
			Material.NETHER_STAR,
			(short) 0,
			"Single portal key",
			null,
			IconFindType.SIMILAR
		),

	QUEST_LIST(
			Material.BOOK_AND_QUILL,
			(short) 0,
			"Quest List",
			Arrays.asList("See the list of your quests."),
			IconFindType.EXCEPT_LORE
	),


	WRITTEN_PREDICTION(
			Material.WRITTEN_BOOK,
			(short) 0,
			"A written prediction",
			null,
			IconFindType.EXCEPT_LORE
	),

	PREDICTION(
			Material.ENCHANTED_BOOK,
			(short) 0,
			"Prediction",
			Arrays.asList("Are you stuck?", "Use prediction to get a useful hint about what to do next."),
			IconFindType.SIMILAR
			),
	LOCK_PICKING(
			Material.IRON_HOE,
			(short) 0,
			"Lock picking",
			Arrays.asList("With this tool you can pick any lock.", "Use the lock pick on locked chest to instantly receive the contents."),
			IconFindType.SIMILAR
			),
	REWIND_TIME(
			Material.WATCH,
			(short) 0,
			"Rewind Time",
			Arrays.asList("You can go back in time to the last checkpoint with this item."),
			IconFindType.SIMILAR
			),

	CLAIRVOYANCE(
			Material.GLASS,
			(short) 0,
			"Clairvoyance",
			Arrays.asList("You can see the contents of locked chests with this skill."),
			IconFindType.SIMILAR
	) ,


	INVESTIGATE(
			Material.TRIPWIRE_HOOK,
			(short) 0,
			"Investigate",
			Arrays.asList("Investigate"),
			IconFindType.SIMILAR
	),

	;


	Material material;
	short data;
	String name;
	List<String> lore;
	IconFindType findType;


	private TBLTItemStackIcon(Material material, short data, String name, List<String> lore, IconFindType findType) {
		this.material = material;
		this.data = data;
		this.name = name;
		this.lore = lore;
		this.findType = findType;
	}


	public static List<TBLTItemStackIcon> find(ItemStack itemStack) {
		return Stream.of(TBLTItemStackIcon.values())
			.filter(icon -> icon.isIconItemStack(itemStack))
			.collect(Collectors.toList());
	}


	@Override
	public Material getMaterial() {
		return material;
	}
	@Override
	public short getData() {
		return data;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public List<String> getLore() {
		return lore;
	}
	@Override
	public IconFindType getFindType() {
		return findType;
	}


}
