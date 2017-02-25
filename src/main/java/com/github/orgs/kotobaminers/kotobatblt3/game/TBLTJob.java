package com.github.orgs.kotobaminers.kotobatblt3.game;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.orgs.kotobaminers.kotobaapi.ability.ClickBlockAbilityInterface;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;
import com.github.orgs.kotobaminers.kotobatblt3.ability.ClickBlockAbility;
import com.github.orgs.kotobaminers.kotobatblt3.ability.ClickBlockChestAbility;


	public enum TBLTJob {

		MAGICIAN(
			new LinkedHashMap<ClickBlockAbilityInterface, Integer>() {{
				put(ClickBlockAbility.REWIND_TIME, 1);
				put(ClickBlockAbility.CLAIRVOYANCE, 1);
				put(ClickBlockChestAbility.SEE_MEMORY, 1);
			}},
			true,
			KotobaItemStack.create(Material.EMERALD, (short) 0, 1 ,"Magician", null)
		),

		ENGINEER(
			new LinkedHashMap<ClickBlockAbilityInterface, Integer>() {{
				put(ClickBlockChestAbility.INVESTIGATE, 1);
				put(ClickBlockAbility.LOCK_PICKING, 1);
			}},
			true,
			KotobaItemStack.create(Material.GOLD_PICKAXE, (short) 0, 1, "Engineer", null)
		),
		RETURN_TO_DEFAULT(
			new LinkedHashMap<ClickBlockAbilityInterface, Integer>() {{
			}},
			false,
			KotobaItemStack.create(Material.BARRIER, (short) 0, 1, "Return to default", null)
		),
		;

		private Map<ClickBlockAbilityInterface, Integer> abilities;
		private boolean actual;
		private ItemStack icon;

		private TBLTJob(Map<ClickBlockAbilityInterface, Integer> abilities, boolean actual, ItemStack icon) {
			this.abilities = abilities;
			this.actual = actual;
			this.icon = icon;
		}

		public static Optional<TBLTJob> find(String name) {
			return Stream.of(TBLTJob.values())
				.filter(job -> job.name().equalsIgnoreCase(name))
				.findAny();
		}

		private Map<ClickBlockAbilityInterface, Integer> getAbilities() {
			return abilities;
		}

		public void setInventory(Player player) {
			player.getInventory().clear();
			abilities.entrySet().stream().forEach(entry ->player.getInventory().addItem(entry.getKey().getIcon().create(entry.getValue())));
		}

		public ItemStack getIcon() {
			return icon;
		}

		public void become(Player player) {
			player.setGameMode(GameMode.ADVENTURE);
			setInventory(player);
		}

		public static List<TBLTJob> getActualJobs() {
			return Stream.of(TBLTJob.values())
				.filter(j -> j.actual)
				.collect(Collectors.toList());
		}
		public static List<TBLTJob> getNoneJobs() {
			return Stream.of(TBLTJob.values())
				.filter(j -> !j.actual)
				.collect(Collectors.toList());
		}

		public static List<TBLTJob> getAllJobs() {
			List<TBLTJob> jobs = new ArrayList<>();
			jobs.addAll(TBLTJob.getActualJobs());
			jobs.addAll(TBLTJob.getNoneJobs());
			return jobs;
		}

		public static Optional<TBLTJob> find(Player player) {
			List<ItemStack> items = Stream.of(player.getInventory().getContents())
				.filter(item -> item != null)
				.filter(item -> item.getType() != Material.AIR)
				.collect(Collectors.toList());
			return Stream.of(TBLTJob.values())
				.filter(job ->
					job.getAbilities().keySet().stream()
						.map(ability ->
							ability.getIcon().create(1)).anyMatch(ability -> items.stream().anyMatch(item -> item.isSimilar(ability)))
				).findFirst();
		}

		public static void initializeInventory(Player player) {
			Optional<TBLTJob> find = find(player);
			player.getInventory().clear();
			find.ifPresent(job -> job.become(player));
		}

		public boolean isSameIcon(ItemStack itemStack) {
			ItemStack icon = getIcon();
			ItemMeta iconMeta = icon.getItemMeta();
			ItemMeta itemMeta = itemStack.getItemMeta();
			if(
				icon.getType().equals(itemStack.getType()) &&
				icon.getDurability() == itemStack.getDurability() &&
				iconMeta.getDisplayName().equalsIgnoreCase(itemMeta.getDisplayName())
			) {
				return true;
			}
			return false;
		}


	}

