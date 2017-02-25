package com.github.orgs.kotobaminers.kotobatblt3.ability;

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
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArena;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTInteractiveChestType;
import com.github.orgs.kotobaminers.kotobatblt3.userinterface.TBLTPlayerGUI;
import com.github.orgs.kotobaminers.kotobatblt3.utility.RepeatingEffect;
import com.github.orgs.kotobaminers.kotobatblt3.utility.RepeatingEffectHolder;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTUtility;

public enum ClickBlockChestAbility implements ClickBlockAbilityInterface, RepeatingEffectHolder {


	GREEN_GEM_PORTAL(
			TBLTItemStackIcon.GREEN_GEM,
			1,
			TBLTInteractiveChestType.THREE_BY_THREE,
			KotobaEffect.TWINCLE_SMALL,
			KotobaEffect.MUTE
			) {
		@Override
		public boolean performActually(PlayerInteractEvent event) {
			getChestType().findTargetBlock(event)
			.ifPresent(b -> {
				Gems.GREEN_GEM.place(b.getLocation());
				ChestPortal.GEM_PORTAL.findCenters(b.getLocation()).forEach(c -> ChestPortal.GEM_PORTAL.tryOpen(c));
			});
			return true;
		}
	},


	GREEN_GEM_BASE(
			TBLTItemStackIcon.GREEN_GEM,
			1,
			TBLTInteractiveChestType.BASE,
			KotobaEffect.TWINCLE_SMALL,
			KotobaEffect.MUTE
			) {
		@Override
		public boolean performActually(PlayerInteractEvent event) {
			getChestType().findTargetBlock(event)
				.ifPresent(b -> {
					Gems.GREEN_GEM.place(b.getLocation());
					getChestType().findChests(b.getLocation()).stream()
						.flatMap(c -> TBLTSwitch.findPoweredChests(c, Gems.GREEN_GEM).stream())
						.forEach(c -> SwitchableChestManager.find(c).stream().forEach(s -> s.turnOn(c)));
			});
			return true;
		}
	},


	BLUE_GEM(
			TBLTItemStackIcon.BLUE_GEM,
			1,
			TBLTInteractiveChestType.THREE_BY_THREE,
			KotobaEffect.TWINCLE_SMALL,
			KotobaEffect.MUTE
		) {
		@Override
		public boolean performActually(PlayerInteractEvent event) {
			Optional<Block> findTargetBlock = getChestType().findTargetBlock(event);
			findTargetBlock
				.ifPresent(b -> {
					Gems.BLUE_GEM.place(b.getLocation());
					ChestPortal.GEM_PORTAL.findCenters(b.getLocation()).forEach(c -> ChestPortal.GEM_PORTAL.tryOpen(c));
				});
			return true;
		}
	},


	RED_GEM(
			TBLTItemStackIcon.RED_GEM,
			1,
			TBLTInteractiveChestType.THREE_BY_THREE,
			KotobaEffect.TWINCLE_SMALL,
			KotobaEffect.MUTE
		) {
		@Override
		public boolean performActually(PlayerInteractEvent event) {
			getChestType().findTargetBlock(event)
				.ifPresent(b -> {
					Gems.RED_GEM.place(b.getLocation());
					ChestPortal.GEM_PORTAL.findCenters(b.getLocation()).forEach(c -> ChestPortal.GEM_PORTAL.tryOpen(c));
				});
			return true;
		}
	},


	SEE_MEMORY(
		TBLTItemStackIcon.DUMMY,
		0,
		TBLTInteractiveChestType.VERTICAL,
		KotobaEffect.TWINCLE_MIDIUM,
		KotobaEffect.MUTE
	) {
		@Override
		public boolean performActually(PlayerInteractEvent event) {
			Block block = event.getClickedBlock();
			List<BookMeta> metas = getChestType().findOptions(event, Arrays.asList(getIcon())).stream()
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
		TBLTInteractiveChestType.VERTICAL,
		KotobaEffect.TWINCLE_MIDIUM,
		KotobaEffect.MUTE
	) {
		@Override
		public boolean performActually(PlayerInteractEvent event) {
			List<ItemStack> options = getChestType().findOptions(event, Arrays.asList(getIcon())).stream()
				.filter(i -> i.getType() != Material.BOOK_AND_QUILL)
				.collect(Collectors.toList());

			List<ItemStack> informations = getChestType().findOptions(event, Arrays.asList(getIcon())).stream()
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
	private TBLTInteractiveChestType chestType;
	private int period = 5;
	private KotobaEffect effect;
	private KotobaEffect sound;


	private ClickBlockChestAbility(
		KotobaItemStackIcon icon,
		int consume,
		TBLTInteractiveChestType chest,
		KotobaEffect effect,
		KotobaEffect sound
	) {
		this.icon = icon;
		this.consume = consume;
		this.chestType = chest;
		this.effect = effect;
		this.sound = sound;
	}


	abstract boolean performActually(PlayerInteractEvent event);


	@Override
	public List<RepeatingEffect> createPeriodicEffects(Location origin) {
		return getChestType().getPositions().stream()
			.map(vec -> RepeatingEffect.create(period, effect, sound, true, origin.clone().add(vec)))
			.collect(Collectors.toList());
	}


	@Override
	public boolean perform(PlayerInteractEvent event) {
		if(0 < getChestType().findChests(event, getIcon()).size()) {
			stopRepeatingEffects(event.getClickedBlock().getLocation().clone());
			return performActually(event);
		}
		return false;
	}


	private void stopRepeatingEffects(Location blockLocation) {
		new TBLTArenaMap().findUnique(blockLocation.clone())
			.map(storage -> (TBLTArena) storage)
			.ifPresent(arena -> arena.stopRepeatingEffects(blockLocation.clone()));
	}


	public TBLTInteractiveChestType getChestType() {
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


	@Override
	public int getPeriod() {
		return period;
	}

	@Override
	public KotobaEffect getEffect() {
		return effect;
	}

	@Override
	public KotobaEffect getSound() {
		return sound;
	}


}

