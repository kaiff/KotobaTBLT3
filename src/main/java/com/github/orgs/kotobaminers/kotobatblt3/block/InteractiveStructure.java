package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaStructure;
import com.github.orgs.kotobaminers.kotobaapi.block.PlayerBlockInteractive;
import com.github.orgs.kotobaminers.kotobaapi.userinterface.RepeatingEffect;
import com.github.orgs.kotobaminers.kotobaapi.userinterface.RepeatingEffectHolder;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaBook;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaPriorityValue;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaStructureUtility;
import com.github.orgs.kotobaminers.kotobatblt3.ability.InteractiveChestFinderHolder;
import com.github.orgs.kotobaminers.kotobatblt3.citizens.UniqueNPC;
import com.github.orgs.kotobaminers.kotobatblt3.userinterface.InteractEffect;
import com.github.orgs.kotobaminers.kotobatblt3.utility.ChestChecker;
import com.github.orgs.kotobaminers.kotobatblt3.utility.ChestReader;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

import net.citizensnpcs.api.npc.NPC;

public enum InteractiveStructure implements KotobaStructure, PlayerBlockInteractive, RepeatingEffectHolder, InteractiveChestFinderHolder {


	UPDATE_BOOK(
			TBLTItemStackIcon.UPDATE_BOOK_KEY,
			new HashMap<Vector, Material>() {{
				put(new Vector(0,2,0), Material.ENCHANTMENT_TABLE);
			}},
			TBLTInteractiveChestFinder.VERTICAL,
			false
		) {
			@Override
			public boolean interactWithChests(PlayerInteractEvent event) {
				Block block = event.getClickedBlock();
				List<ItemStack> options = getChestFinder().findChests(event, getIcon()).stream()
					.flatMap(c -> ChestReader.findOptions(c, getIcon(), new ArrayList<>()).stream())
					.collect(Collectors.toList());
				List<BookMeta> metas = options.stream()
					.filter(i -> i.getType() == Material.BOOK_AND_QUILL)
					.map(i -> (BookMeta) i.getItemMeta())
					.filter(meta -> 0 < meta.getPageCount())
					.collect(Collectors.toList());

				if(0 < metas.size()) {
					event.setCancelled(true);

					Player player = event.getPlayer();
					new TBLTArenaMap().findUnique(player.getLocation())
						.ifPresent(arena -> {
							removeMagicBook(player.getInventory());
							boolean success = giveBooks(player, metas, options);
							if(success) {
								KotobaEffect.ENDER_SIGNAL.playEffect(block.getLocation());
								player.getWorld().playSound(player.getLocation(), Sound.SPIDER_WALK, 1f, 0.5f);
							}
						});
				}
				return true;
			}

			private static final String TITLE = "Magic Book";
			private static final String TEXT = "PastRecords";

			private boolean isMagicBook(ItemStack item) {
				if(item.getType() == KotobaBook.MATERIAL) {
					BookMeta meta = (BookMeta) item.getItemMeta();
					String title = meta.getTitle();
					if(title.equalsIgnoreCase(TITLE)) {
						return true;
					}
				}
				return false;
			}

			private void removeMagicBook(Inventory inventory) {
				Stream.of(inventory.getContents())
					.filter(i -> i != null)
					.filter(i -> isMagicBook(i))
					.forEach(i -> inventory.remove(i));
			}

			private boolean giveBooks(Player player, List<BookMeta> metas, List<ItemStack> options) {
				List<TBLTItemStackIcon> keys = options.stream()
					.filter(i -> TBLTItemStackIcon.SENTENCE_BOOK_OPTION.isIconItemStack(i) || TBLTItemStackIcon.URL_BOOK_OPTION.isIconItemStack(i))
					.flatMap(i -> Stream.of(TBLTItemStackIcon.values()).filter(icon -> icon.isIconItemStack(i)))
					.collect(Collectors.toList());

				if(0 < keys.size()) {
					List<KotobaBook> books = metas.stream().map(meta -> {
						KotobaBook book = new KotobaBook(meta);
						book.setAuthor(player.getName());
						book.setTitle(TITLE);
						return book;
					}).collect(Collectors.toList());
					keys.stream()
						.filter(o -> o == TBLTItemStackIcon.SENTENCE_BOOK_OPTION)
						.findAny()
						.ifPresent(o -> books.forEach(book -> player.getInventory().addItem(book.createRawBook())));
					keys.stream()
						.filter(o -> o == TBLTItemStackIcon.URL_BOOK_OPTION)
						.findAny()
						.ifPresent(o -> books.stream().forEach(book -> book.giveOpenURLBook(player, TEXT, ChatColor.stripColor(book.getMeta().getPage(1)))));
					return true;
				}
				return false;
			}
		},


	SUMMON_CAT(
			TBLTItemStackIcon.SUMMON_SERVENT_KEY,
			new HashMap<Vector, Material>() {{
				put(new Vector(0,2,0), Material.PUMPKIN);
			}},
			TBLTInteractiveChestFinder.VERTICAL,
			false
		) {

			private List<UniqueNPC> uniqueNPCs = Arrays.asList(UniqueNPC.CAT_1, UniqueNPC.CAT_2);

			private boolean checkJob(Player player) {
				return true;//TODO
			}

			@Override
			public boolean interactWithChests(PlayerInteractEvent event) {
				if(!checkJob(event.getPlayer())) return false;
				Block block = event.getClickedBlock();
				Long summoned = new TBLTArenaMap().findUnique(block.getLocation())
					.map(arena ->
						uniqueNPCs.stream().map(u -> {
							u.getNPCs().stream()
								.filter(n -> arena.isIn(n.getStoredLocation()))
								.forEach(n -> u.despawn(n.getId()));

							List<ItemStack> options = getChestFinder().findChests(event, getIcon()).stream()
									.flatMap(c -> ChestReader.findOptions(c, getIcon(), new ArrayList<>()).stream())
									.collect(Collectors.toList());
							List<NPC> npcs = options.stream()
								.flatMap(item -> uniqueNPCs.stream().map(n -> n.findNPCByKey(item)))
								.filter(npc -> npc.isPresent())
								.map(Optional::get)
								.collect(Collectors.toList());
							Stream.iterate(0, i -> i + 1)
								.limit(npcs.size())
								.forEach(i -> u.spawn(npcs.get(i).getId(), block.getLocation().clone().add(0.5 + i, 1.5, 0.5)));
								return npcs.size();
						}).count()
					).orElse(0L);
				return 0 < summoned;
			}
		},


	SUMMON_SLIME(
			TBLTItemStackIcon.SUMMON_SERVENT_KEY,
			new HashMap<Vector, Material>() {{
				put(new Vector(0,2,0), Material.MELON_BLOCK);
			}},
			TBLTInteractiveChestFinder.VERTICAL,
			false
		) {
			private List<UniqueNPC> uniqueNPCs = Arrays.asList(UniqueNPC.SLIME_1, UniqueNPC.SLIME_2);

			private boolean checkJob(Player player) {
				return true;//TODO
			}


			@Override
			public boolean interactWithChests(PlayerInteractEvent event) {
				if(!checkJob(event.getPlayer())) return false;
				Block block = event.getClickedBlock();
				Long summoned = new TBLTArenaMap().findUnique(block.getLocation())
					.map(arena ->
						uniqueNPCs.stream().map(u -> {
							u.getNPCs().stream()
								.filter(n -> arena.isIn(n.getStoredLocation()))
								.forEach(n -> u.despawn(n.getId()));

							List<ItemStack> options = getChestFinder().findChests(event, getIcon()).stream()
									.flatMap(c -> ChestReader.findOptions(c, getIcon(), new ArrayList<>()).stream())
									.collect(Collectors.toList());
							List<NPC> npcs = options.stream()
								.flatMap(item -> uniqueNPCs.stream().map(n -> n.findNPCByKey(item)))
								.filter(npc -> npc.isPresent())
								.map(Optional::get)
								.collect(Collectors.toList());
							Stream.iterate(0, i -> i + 1)
								.limit(npcs.size())
								.forEach(i -> u.spawn(npcs.get(i).getId(), block.getLocation().clone().add(0.5 + i, 1.5, 0.5)));
								return npcs.size();
						}).count()
					).orElse(0L);
				return 0 < summoned;
			}
		},
	;

	private KotobaItemStackIcon icon;
	private Map<Vector, Material> structure;
	private TBLTInteractiveChestFinder chestFinder;
	private boolean hasRotaions;


	private InteractiveStructure(KotobaItemStackIcon icon, Map<Vector, Material> structure, TBLTInteractiveChestFinder chest, boolean hasRotaions) {
		this.icon = icon;
		this.structure = structure;
		this.chestFinder = chest;
		this.hasRotaions = hasRotaions;
	}


	@Override
	public boolean interact(PlayerInteractEvent event) {
		List<Chest> chests = getChestFinder().findChests(event, getIcon());
		chests = chests.stream()
			.filter(c -> ChestChecker.checkTriggers(c, event.getPlayer()))
			.collect(Collectors.toList());

		if(0 < chests.size()) {
			chests.forEach(c -> InteractEffect.playEffect(c, event));
			return interactWithChests(event);
		}

		return false;
	}

	@Override
	public KotobaItemStackIcon getIcon() {
		return icon;
	}


	@Override
	public Map<Vector, Material> getStructure() {
		return structure;
	}


	@Override
	public TBLTInteractiveChestFinder getChestFinder() {
		return chestFinder;
	}


	@Override
	public boolean isSame(PlayerInteractEvent event) {
		return getChestFinder().getInteractType().getTargetBlock(event)
			.filter(b -> getStructure().values().contains(b.getType()))
			.map(b -> 0 < getChestFinder().findChests(event, getIcon()).size())
			.orElse(false);
	}


	@Override
	public int getPeriod() {
		return 5;
	}
	@Override
	public KotobaEffect getEffect() {
		return KotobaEffect.TWINCLE_SMALL;
	}
	@Override
	public KotobaEffect getSound() {
		return KotobaEffect.MUTE;
	}


	@Override
	public String getName() {
		return name();
	}


	@Override
	public List<RepeatingEffect> createPeriodicEffects(Location origin) {
		return KotobaStructureUtility.findExistings(getStructure(), origin.clone().add(getChestFinder().getPositions().get(0)), hasRotaions).stream()
			.flatMap(list -> list.stream().map(b -> RepeatingEffect.create(getPeriod(), getEffect(), getSound(), true, b.getLocation())))
			.collect(Collectors.toList());
	}


	@Override
	public boolean hasRotations() {
		return hasRotaions;
	}

	@Override
	public KotobaPriorityValue getPriority() {
		return KotobaPriorityValue.MIDIUM;
	}

}

