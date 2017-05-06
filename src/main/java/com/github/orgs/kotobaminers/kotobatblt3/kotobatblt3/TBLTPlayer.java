package com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.orgs.kotobaminers.kotobaapi.ability.ClickBlockAbilityInterface;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.ability.ClickBlockAbility;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTUtility;


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

		private static int initialId = -1;
		private static int effectId = initialId;
		private static final int effectInterval = 20 * 60;
		private static final List<PotionEffect> effects = Arrays.asList(
				new PotionEffect(PotionEffectType.JUMP, effectInterval + 20, -5, false, false)
			);


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
			abilities.entrySet().stream().forEach(entry ->player.getInventory().addItem(entry.getKey().getIcon().create(entry.getValue())));
		}


		public void become(Player player) {
			player.setGameMode(mode);
			effects.forEach(e -> player.addPotionEffect(e));
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


		public static void scheduleRepeatingEffects() {
			if(effectId != initialId) {
				Bukkit.getScheduler().cancelTask(effectId);
			}
			int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Setting.getPlugin(), new Runnable() {
				@Override
				public void run() {
					Bukkit.getOnlinePlayers().stream()
						.filter(p -> TBLTUtility.isTBLTPlayer(p))
						.forEach(p -> updatePotionEffects(p));
				}
			}, 5, effectInterval);
			effectId = id;
		}


		public static void updatePotionEffects(Player player) {
			effects.forEach(e -> {
				player.removePotionEffect(e.getType());
				player.addPotionEffect(e);
			});
		}


	}

