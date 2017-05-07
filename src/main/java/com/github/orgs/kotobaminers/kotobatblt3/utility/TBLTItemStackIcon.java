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


	//DUMMY
	TEST(
			Material.APPLE,
			(short) 0,
			"Test",
			null,
			IconFindType.SIMILAR
	),

	//Arena Meta
	ARENA_META(
			Material.BOOK,
			(short) 0,
			"Arena Meata",
			Arrays.asList("Settings"),
			IconFindType.SIMILAR
	),
	ARENA_ID(
			Material.BOOK,
			(short) 0,
			"Arena Meta: ID",
			new ArrayList<>(),
			IconFindType.SIMILAR
	),
	ARENA_NEXT(
			Material.BOOK,
			(short) 0,
			"Arena Meta: Next Arena",
			new ArrayList<>(),
			IconFindType.SIMILAR
	),
	ARENA_SPAWN(
			Material.BOOK,
			(short) 0,
			"Arena Setting: Spawn",
			null,
			IconFindType.SIMILAR
	),
	DIRECTION(
			Material.COMPASS,
			(short) 0,
			"Direction",
			Arrays.asList("/tblt util direction"),
			IconFindType.EXCEPT_LORE
	),
	PLAYER_NUMBER_MULTIPLE(
			Material.BOOK,
			(short) 0,
			"Arena Setting: Multiple Players",
			Arrays.asList("Set the arena for multi players.(Default is for single.)"),
			IconFindType.EXCEPT_LORE
	),
	QUEST_PROGRESS(
			Material.WRITTEN_BOOK,
			(short) 0,
			"Quest Progress",
			Arrays.asList("Right click to read this"),
			IconFindType.EXCEPT_LORE
	),
	JOB_ITEMS(
			Material.BOOK,
			(short) 0,
			"Arena Setting: Job Items",
			Arrays.asList("Starter items for the specific job"),
			IconFindType.EXCEPT_LORE
	),


	//Quest
	CLICK_QUEST_CREATOR(
			Material.IRON_SWORD,
			(short) 0,
			"Quest Creator",
			Arrays.asList("Click Quest"),
			IconFindType.SIMILAR
	),
	WALK_ON_QUEST_CREATOR(
			Material.IRON_SWORD,
			(short) 0,
			"Quest Creator",
			Arrays.asList("Walk On Quest"),
			IconFindType.SIMILAR
	),


	//Ability
	ARENA_MENU(
			Material.CHEST,
			(short) 0,
			"Open Menu",
			Arrays.asList("Open an arena menu"),
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
	TRIGGER_ITEM_AT_LEAST(
			Material.FLINT_AND_STEEL,
			(short) 0,
			"Trigger Item Setting",
			Arrays.asList("You need all the items to activate this chest in the row"),
			IconFindType.SIMILAR
	),


	TRIGGER_ITEM_SAME_AMOUNT(
			Material.FLINT_AND_STEEL,
			(short) 0,
			"Trigger Item Setting",
			Arrays.asList("Same Amount Required"),
			IconFindType.SIMILAR
	),


	INTERACT_EFFECT(
			Material.FLINT_AND_STEEL,
			(short) 0,
			"Chest Effect",
			new ArrayList<>(),
			IconFindType.SIMILAR
	),


	STAND_KEY(
			Material.GLASS,
			(short) 0,
			"Structure Key",
			Arrays.asList("Stand"),
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


	UPDATE_BOOK_KEY(
			Material.ENCHANTMENT_TABLE,
			(short) 0,
			"Structure Key",
			Arrays.asList("Update Book"),
			IconFindType.SIMILAR
	),


	URL_BOOK_OPTION(
			Material.BOOK,
			(short) 0,
			"Book Option",
			Arrays.asList("URL Book"),
			IconFindType.SIMILAR
	),


	SENTENCE_BOOK_OPTION(
			Material.BOOK,
			(short) 0,
			"Book Option",
			Arrays.asList("Sentence Book"),
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

	PORTAL_NEXT_CRYSTAL(
			Material.NETHER_STAR,
			(short) 0,
			"Portal Crystal",
			Arrays.asList("This special item is used to open the time travel portal to your next location."),
			IconFindType.EXCEPT_LORE
	),

	PORTAL_ELEVATOR_CRYSTAL(
			Material.NETHER_STAR,
			(short) 0,
			"Portal Elevator Crystal",
			Arrays.asList("Elevator Crystal"),
			IconFindType.EXCEPT_LORE
	),

	SINGLE_PORTAL(
			Material.NETHER_STAR,
			(short) 0,
			"Single portal key",
			null,
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
