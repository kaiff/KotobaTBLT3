package com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.ability.ClickBlockAbilityInterface;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.ability.ClickBlockAbility;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;


	public enum TBLTPlayer {

		MAGE(
			new LinkedHashMap<ClickBlockAbilityInterface, Integer>() {{
				put(ClickBlockAbility.MAGIC_WAND, 1);
			}},
			TBLTItemStackIcon.MAGIC_WAND
		),

		ARTIFICER(
			new LinkedHashMap<ClickBlockAbilityInterface, Integer>() {{
				put(ClickBlockAbility.MAGIC_SPADE, 1);
			}},
			TBLTItemStackIcon.MAGIC_SPADE
		),
		NONE(
			new LinkedHashMap<ClickBlockAbilityInterface, Integer>() {{
			}},
			TBLTItemStackIcon.TEST
		),
		;


		private Map<ClickBlockAbilityInterface, Integer> abilities;
		private TBLTItemStackIcon icon;
		private GameMode mode = GameMode.ADVENTURE;


		public List<KotobaItemStackIcon> getAbilityIcons() {
			return abilities.keySet().stream().map(a -> a.getIcon()).collect(Collectors.toList());
		}


		private TBLTPlayer(Map<ClickBlockAbilityInterface, Integer> abilities, TBLTItemStackIcon icon) {
			this.abilities = abilities;
			this.icon = icon;
		}


		public static Optional<TBLTPlayer> find(String name) {
			return Stream.of(TBLTPlayer.values())
				.filter(job -> job.name().equalsIgnoreCase(name))
				.findAny();
		}


		private void setInventory(Player player) {
			player.getInventory().clear();
			List<ItemStack> items = abilities.entrySet().stream()
				.map(entry -> entry.getKey().getIcon().create(entry.getValue()))
				.collect(Collectors.toList());
			int max = player.getInventory().getSize() - 1;
			Stream.iterate(0, i -> i - 1)
				.limit(Math.min(max, items.size()))
				.forEach(i -> player.getInventory().setItem(max - i, items.get(i)));
		}


		public void become(Player player) {
			player.setGameMode(mode);
			setInventory(player);
		}


		public static void resetCurrentJob(Player player) {
			findJob(player).become(player);
		}


		public static TBLTPlayer findJob(Player player) {
			List<ItemStack> items = Stream.of(player.getInventory().getContents())
				.filter(item -> item != null)
				.collect(Collectors.toList());
			return Stream.of(TBLTPlayer.values())
				.filter(job -> items.stream().anyMatch(i -> job.icon.isIconItemStack(i)))
				.findFirst()
				.orElse(NONE);
		}


	}

