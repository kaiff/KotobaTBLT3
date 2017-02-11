package com.github.orgs.kotobaminers.kotobatblt3.utility;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;

public enum TBLTItemStackIcon implements KotobaItemStackIcon {


	PLAYER_GATE_KEY(
			Material.GLASS,
			(short) 0,
			"Player Gate Key",
			null,
			IconFindType.SIMILAR
	),


	GEM_PORTAL_KEY_3X3(
			Material.TRAPPED_CHEST,
			(short) 0,
			"Gem Portal Key 3x3",
			null,
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
			Material.EMERALD_BLOCK,
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
			IconFindType.EXCEPT_LORE
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
