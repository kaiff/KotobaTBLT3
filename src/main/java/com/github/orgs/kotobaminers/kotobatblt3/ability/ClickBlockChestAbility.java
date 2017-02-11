package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.kotobaapi.ability.ClickBlockAbilityInterface;
import com.github.orgs.kotobaminers.kotobaapi.block.ConfigChest;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaBook;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArena;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.citizens.UniqueNPC;
import com.github.orgs.kotobaminers.kotobatblt3.gui.TBLTPlayerGUI;
import com.github.orgs.kotobaminers.kotobatblt3.utility.RepeatingEffect;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.utility.Utility;

public enum ClickBlockChestAbility implements ClickBlockAbilityInterface, ConfigChest {


	SEE_MEMORY(
		TBLTItemStackIcon.DUMMY,
		0,
		ChestFindType.UNDER_CHEST,
		KotobaEffect.TWINCLE_MIDIUM,
		KotobaEffect.TWINCLE_MIDIUM
	) {
		@Override
		public boolean performAbility(PlayerInteractEvent event) {
			Block block = event.getClickedBlock();
			List<BookMeta> metas = findOptions(block.getLocation()).stream()
				.filter(i -> i.getType() == Material.BOOK_AND_QUILL)
				.map(i -> (BookMeta) i.getItemMeta())
				.filter(meta -> 0 < meta.getPageCount())
				.collect(Collectors.toList());
			if(0 < metas.size()) {
				event.setCancelled(true);

				Player player = event.getPlayer();
				new TBLTArenaMap().findUnique(player.getLocation())
					.ifPresent(arena -> {
						List<Player> others = Bukkit.getOnlinePlayers().stream()
							.filter(p -> !p.getUniqueId().equals(player.getUniqueId()))
							.filter(p -> arena.isIn(player.getLocation()))
							.filter(p -> Utility.isTBLTPlayer(p))
							.collect(Collectors.toList());

						KotobaEffect.ENDER_SIGNAL.playEffect(block.getLocation());

						removeMemoryBook(player.getInventory());
						others.forEach(p -> removeMemoryBook(p.getInventory()));
						giveOpenURLBook(player, metas.get(0));

						if(1 < metas.size()) {
							others.forEach(p -> giveOpenURLBook(p, metas.get(1)));
						}
					});
			}
			return true;
		}

		private static final String MEMORY = "Memory";
		private static final String TEXT = "Memory";

		private boolean isMemoryBook(ItemStack item) {
			if(item.getType() == KotobaBook.MATERIAL) {
				BookMeta meta = (BookMeta) item.getItemMeta();
				String title = meta.getTitle();
				if(title.equalsIgnoreCase(MEMORY)) {
					return true;
				}
			}
			return false;
		}

		private void removeMemoryBook(Inventory inventory) {
			Stream.of(inventory.getContents())
				.filter(i -> i != null)
				.filter(i -> isMemoryBook(i))
				.forEach(i -> inventory.remove(i));
		}

		private void giveOpenURLBook(Player player, BookMeta meta) {
			String url = ChatColor.stripColor(meta.getPage(1));
			KotobaBook book = new KotobaBook();
			book.setAuthor(player.getName());
			book.setTitle(MEMORY);
			book.giveOpenURLBook(player, TEXT, url);
			player.getWorld().playSound(player.getLocation(), Sound.SPIDER_WALK, 1f, 0.5f);
		}
	},


	INVESTIGATE(
		TBLTItemStackIcon.DUMMY,
		0,
		ChestFindType.UNDER_CHEST,
		KotobaEffect.TWINCLE_MIDIUM,
		KotobaEffect.TWINCLE_MIDIUM
	) {
		@Override
		public boolean performAbility(PlayerInteractEvent event) {
			Block block = event.getClickedBlock();
			List<ItemStack> options = findOptions(block.getLocation()).stream()
				.filter(i -> i.getType() != Material.BOOK_AND_QUILL)
				.collect(Collectors.toList());

			List<ItemStack> informations = findOptions(block.getLocation()).stream()
				.filter(i -> i.getType() == Material.BOOK_AND_QUILL)
				.map(i -> (BookMeta) i.getItemMeta())
				.map(meta -> KotobaUtility.toStringListFromBookMeta(meta))
				.map(lore -> {
					lore = 	lore.stream().flatMap(sentence -> KotobaUtility.splitSentence(sentence, KotobaItemStackIcon.LORE_LENGTH).stream()).collect(Collectors.toList());
					ItemStack item = KotobaItemStack.setColoredLore(getIcon().create(1), ChatColor.RESET, lore);
					ItemMeta itemMeta = item.getItemMeta();
					item.setItemMeta(itemMeta);
					return item;
				})
				.collect(Collectors.toList());

			informations.addAll(options);
			if(0 < informations.size()) {
				TBLTPlayerGUI.INVESTIGATE.create(informations)
					.ifPresent(inventory -> {
						event.getPlayer().openInventory(inventory);
					});
				return true;
			}
			return false;
		}
	},


	SUMMON_SERVANT_1(
		TBLTItemStackIcon.DUMMY,
		0,
		ChestFindType.UNDER_CHEST,
		KotobaEffect.TWINCLE_MIDIUM,
		KotobaEffect.TWINCLE_MIDIUM
	) {
		@Override
		public boolean performAbility(PlayerInteractEvent event) {
			Block block = event.getClickedBlock();
			return new TBLTArenaMap().findUnique(block.getLocation())
				.map(arena -> {
					UniqueNPC unique = UniqueNPC.CAT;
					boolean success = findOptions(block.getLocation()).stream()
						.map(item -> unique.findNPCByKey(item))
						.filter(npc -> npc.isPresent())
						.map(Optional::get)
						.map(npc -> {
							unique.getNPCs().stream()
								.filter(n -> arena.isIn(n.getStoredLocation()))
								.forEach(n -> unique.despawn(n.getId()));
							unique.spawn(npc.getId(), block.getLocation().clone().add(0.5,1.5,0.5));
							return true;
						}).collect(Collectors.toList()).contains(true);
					return success;
				}).orElse(false);
		}
	},


	SUMMON_SERVANT_2(
		TBLTItemStackIcon.DUMMY,
		0,
		ChestFindType.UNDER_CHEST,
		KotobaEffect.TWINCLE_MIDIUM,
		KotobaEffect.TWINCLE_MIDIUM
	) {
		@Override
		public boolean performAbility(PlayerInteractEvent event) {
			Block block = event.getClickedBlock();
			return new TBLTArenaMap().findUnique(block.getLocation())
				.map(arena -> {
					UniqueNPC unique = UniqueNPC.SLIME;
					boolean success = findOptions(block.getLocation()).stream()
							.map(item -> unique.findNPCByKey(item))
							.filter(npc -> npc.isPresent())
							.map(Optional::get)
							.map(npc -> {
								unique.getNPCs().stream()
									.filter(n -> arena.isIn(n.getStoredLocation()))
									.forEach(n -> unique.despawn(n.getId()));
								unique.spawn(npc.getId(), block.getLocation().clone().add(0.5,1.5,0.5));
								return true;
							}).collect(Collectors.toList()).contains(true);
					return success;
				}).orElse(false);
		}
	},

	;


	public static final Material CHEST_MATERIAL = Material.TRAPPED_CHEST;


	private KotobaItemStackIcon icon;
	private int consume;
	private ChestFindType findType;
	private int period = 5;
	private KotobaEffect effect;
	private KotobaEffect sound;


	public static final Vector POSITION_TO_BLOCK = new Vector(0, 2, 0);


	private ClickBlockChestAbility(
		KotobaItemStackIcon icon,
		int consume,
		ChestFindType findType,
		KotobaEffect effect,
		KotobaEffect sound
	) {
		this.icon = icon;
		this.consume = consume;
		this.findType = findType;
		this.effect = effect;
		this.sound = sound;
	}


	public RepeatingEffect createPeriodicEffect(Location location) {
		return RepeatingEffect.create(period, effect, sound, true, location);
	}


	protected void clearContents(Location location) {
		findChests(location.clone())
			.forEach(chest -> chest.getInventory().clear());
	}


	@Override
	public boolean perform(PlayerInteractEvent event) {
		findEffects(event.getClickedBlock().getLocation().clone()).forEach(e -> e.setRepeat(false));
		return performAbility(event);
	}

	public abstract boolean performAbility(PlayerInteractEvent event);

	private List<RepeatingEffect> findEffects(Location blockLocation) {
		return new TBLTArenaMap().findUnique(blockLocation.clone())
			.map(storage -> (TBLTArena) storage)
			.map(arena -> arena.findRepeatingEffects(blockLocation.clone()))
			.orElse(new ArrayList<>());
	}


	@Override
	public List<Chest> findChests(Location location) {
		Block block = location.clone().subtract(POSITION_TO_BLOCK.clone()).getBlock();
		List<Chest> chests = new ArrayList<Chest>();
		if(block.getState() instanceof Chest) {
			chests.add((Chest) block.getState());
		}
		return chests;
	}

	@Override
	public KotobaItemStackIcon getKey() {
		return icon;
	}
	@Override
	public KotobaItemStackIcon getIcon() {
		return icon;
	}
	@Override
	public List<Action> getTriggers() {
		return Arrays.asList(Action.RIGHT_CLICK_BLOCK);
	}
	@Override
	public int getConsumption() {
		return consume;
	}


	//TODO
	protected enum ChestFindType {
		UNDER_CHEST {
			@Override
			protected boolean canFind(PlayerInteractEvent event, ClickBlockChestAbility ability) {
				if(event.getClickedBlock() == null) return false;
				if(!event.getPlayer().getItemInHand().isSimilar(ability.getIcon().create(1))) return false;
				return 0 < ability.findOptions(event.getClickedBlock().getLocation().clone()).size();
			}
		},

		ANYWHERE {
			@Override
			protected boolean canFind(PlayerInteractEvent event, ClickBlockChestAbility ability) {
				return true;
			}
		},
		;

		protected abstract boolean canFind(PlayerInteractEvent event, ClickBlockChestAbility ability);


	}



}
