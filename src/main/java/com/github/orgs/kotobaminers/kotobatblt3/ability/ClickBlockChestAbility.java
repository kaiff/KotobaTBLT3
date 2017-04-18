package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

import com.github.orgs.kotobaminers.kotobaapi.ability.ClickBlockAbilityInterface;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaBook;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaEffect;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.block.ChestPortal;
import com.github.orgs.kotobaminers.kotobatblt3.block.SwitchableChestManager;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTInteractiveChestFinder;
import com.github.orgs.kotobaminers.kotobatblt3.userinterface.InteractEffect;
import com.github.orgs.kotobaminers.kotobatblt3.userinterface.TBLTPlayerGUI;
import com.github.orgs.kotobaminers.kotobatblt3.utility.ChestChecker;
import com.github.orgs.kotobaminers.kotobatblt3.utility.ChestReader;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTUtility;

public enum ClickBlockChestAbility implements ClickBlockAbilityInterface, InteractiveChestFinderHolder {


	GREEN_GEM_PORTAL(
			TBLTItemStackIcon.GREEN_GEM,
			1,
			TBLTInteractiveChestFinder.THREE_BY_THREE,
			KotobaEffect.TWINCLE_SMALL,
			KotobaEffect.MUTE
			) {
		@Override
		public boolean interactWithChests(PlayerInteractEvent event) {
			getChestFinder().findTargetBlock(event)
			.ifPresent(b -> {
				TBLTGem.GREEN_GEM.place(b.getLocation());
				ChestPortal.GEM_PORTAL.findCenters(b.getLocation()).forEach(c -> ChestPortal.GEM_PORTAL.tryOpen(c));
			});
			return true;
		}

	},


	BLUE_GEM_PORTAL(
			TBLTItemStackIcon.BLUE_GEM,
			1,
			TBLTInteractiveChestFinder.THREE_BY_THREE,
			KotobaEffect.TWINCLE_SMALL,
			KotobaEffect.MUTE
		) {
		@Override
		public boolean interactWithChests(PlayerInteractEvent event) {
			Optional<Block> findTargetBlock = getChestFinder().findTargetBlock(event);
			findTargetBlock
				.ifPresent(b -> {
					TBLTGem.BLUE_GEM.place(b.getLocation());
					ChestPortal.GEM_PORTAL.findCenters(b.getLocation()).forEach(c -> ChestPortal.GEM_PORTAL.tryOpen(c));
				});
			return true;
		}
	},


	RED_GEM_PORTAL(
			TBLTItemStackIcon.RED_GEM,
			1,
			TBLTInteractiveChestFinder.THREE_BY_THREE,
			KotobaEffect.TWINCLE_SMALL,
			KotobaEffect.MUTE
		) {
		@Override
		public boolean interactWithChests(PlayerInteractEvent event) {
			getChestFinder().findTargetBlock(event)
				.ifPresent(b -> {
					TBLTGem.RED_GEM.place(b.getLocation());
					ChestPortal.GEM_PORTAL.findCenters(b.getLocation()).forEach(c -> ChestPortal.GEM_PORTAL.tryOpen(c));
				});
			return true;
		}
	},


	GREEN_GEM_BASE(
		TBLTItemStackIcon.GREEN_GEM,
		1,
		TBLTInteractiveChestFinder.BASE,
		KotobaEffect.TWINCLE_SMALL,
		KotobaEffect.MUTE
	) {
		private final TBLTGem gem = TBLTGem.GREEN_GEM;

		@Override
		public boolean interactWithChests(PlayerInteractEvent event) {
			getChestFinder().findTargetBlock(event)
			.ifPresent(b -> {
				gem.place(b.getLocation());
				getChestFinder().findChests(b.getLocation()).stream()
					.flatMap(c -> TBLTSwitch.findPoweredChests(c, gem).stream())
					.forEach(c -> SwitchableChestManager.find(c).stream().forEach(s -> s.turnOn(c)));
			});
			return true;
		}

	},


	RED_GEM_BASE(
			TBLTItemStackIcon.RED_GEM,
			1,
			TBLTInteractiveChestFinder.BASE,
			KotobaEffect.TWINCLE_SMALL,
			KotobaEffect.MUTE
		) {
			private final TBLTGem gem = TBLTGem.RED_GEM;

			@Override
			public boolean interactWithChests(PlayerInteractEvent event) {
				getChestFinder().findTargetBlock(event)
				.ifPresent(b -> {
					gem.place(b.getLocation());
					getChestFinder().findChests(b.getLocation()).stream()
					.flatMap(c -> TBLTSwitch.findPoweredChests(c, gem).stream())
					.forEach(c -> SwitchableChestManager.find(c).stream().forEach(s -> s.turnOn(c)));
				});
				return true;
			}

		},


	BLUE_GEM_BASE(
			TBLTItemStackIcon.BLUE_GEM,
			1,
			TBLTInteractiveChestFinder.BASE,
			KotobaEffect.TWINCLE_SMALL,
			KotobaEffect.MUTE
		) {
			private final TBLTGem gem = TBLTGem.BLUE_GEM;

			@Override
			public boolean interactWithChests(PlayerInteractEvent event) {
				getChestFinder().findTargetBlock(event)
				.ifPresent(b -> {
					gem.place(b.getLocation());
					getChestFinder().findChests(b.getLocation()).stream()
					.flatMap(c -> TBLTSwitch.findPoweredChests(c, gem).stream())
					.forEach(c -> SwitchableChestManager.find(c).stream().forEach(s -> s.turnOn(c)));
				});
				return true;
			}

		},


	UPDATE_BOOK(
		TBLTItemStackIcon.DUMMY,
		0,
		TBLTInteractiveChestFinder.VERTICAL,
		KotobaEffect.TWINCLE_MIDIUM,
		KotobaEffect.MUTE
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
						List<Player> others = Bukkit.getOnlinePlayers().stream()
							.filter(p -> !p.getUniqueId().equals(player.getUniqueId()))
							.filter(p -> arena.isIn(player.getLocation()))
							.filter(p -> TBLTUtility.isTBLTPlayer(p))
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
		TBLTItemStackIcon.INVESTIGATE,
		0,
		TBLTInteractiveChestFinder.VERTICAL,
		KotobaEffect.TWINCLE_MIDIUM,
		KotobaEffect.MUTE
	) {
		@Override
		public boolean interactWithChests(PlayerInteractEvent event) {
			List<ItemStack> options = getChestFinder().findChests(event, getIcon()).stream()
					.flatMap(c -> ChestReader.findOptions(c, getIcon(), new ArrayList<>()).stream())
					.collect(Collectors.toList());

			List<ItemStack> informations = options.stream()
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


	;


	private KotobaItemStackIcon icon;
	private int consume;
	private TBLTInteractiveChestFinder chestType;


	private ClickBlockChestAbility(
		KotobaItemStackIcon icon,
		int consume,
		TBLTInteractiveChestFinder chest,
		KotobaEffect effect,
		KotobaEffect sound
	) {
		this.icon = icon;
		this.consume = consume;
		this.chestType = chest;
	}


	@Override
	public boolean perform(PlayerInteractEvent event) {
		List<Chest> chests = getChestFinder().findChests(event, getIcon());
		chests = chests.stream()
			.filter(c -> ChestChecker.checkTriggers(c, event.getPlayer()))
			.collect(Collectors.toList());

		chests.forEach(c -> InteractEffect.playEffect(c, event));
		if(0 < chests.size()) {
			return interactWithChests(event);
		}

		return false;
	}


	@Override
	public TBLTInteractiveChestFinder getChestFinder() {
		return chestType;
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


}

