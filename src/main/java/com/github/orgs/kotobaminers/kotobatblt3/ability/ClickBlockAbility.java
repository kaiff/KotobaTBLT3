package com.github.orgs.kotobaminers.kotobatblt3.ability;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.ability.ClickBlockAbilityInterface;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStackIcon;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaPriorityValue;
import com.github.orgs.kotobaminers.kotobatblt3.block.SwitchableChestManager;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTInteractiveChestFinder;
import com.github.orgs.kotobaminers.kotobatblt3.userinterface.TBLTPlayerGUI;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public enum ClickBlockAbility implements ClickBlockAbilityInterface {


	MAGIC_WAND(
			TBLTItemStackIcon.MAGIC_WAND,
			Arrays.asList(Action.RIGHT_CLICK_BLOCK),
			0
		) {
			private final List<TBLTGem> gems = Arrays.asList(TBLTGem.GREEN_GEM, TBLTGem.RED_GEM);

			@Override
			public boolean perform(PlayerInteractEvent event) {
				Block block = event.getClickedBlock();
				return gems.stream()
					.filter(g -> block.getType() == g.getIcon().getMaterial())
					.findFirst()
					.map(g -> {
						g.take(event.getPlayer(), block);
						TBLTInteractiveChestFinder.BASE.findChests(block.getLocation()).stream()
							.flatMap(c -> TBLTSwitch.findPoweredChests(c, g).stream())
							.forEach(c -> SwitchableChestManager.find(c).stream().forEach(s -> s.turnOff(c)));
						return true;
					}).orElse(false);
			}
		},


	MAGIC_SPADE(
			TBLTItemStackIcon.MAGIC_SPADE,
			Arrays.asList(Action.RIGHT_CLICK_BLOCK),
			0
		) {
			private final List<TBLTGem> gems = Arrays.asList(TBLTGem.GREEN_GEM, TBLTGem.BLUE_GEM);

			@Override
			public boolean perform(PlayerInteractEvent event) {
				Block block = event.getClickedBlock();
				return gems.stream()
					.filter(g -> block.getType() == g.getIcon().getMaterial())
					.findFirst()
					.map(g -> {
						g.take(event.getPlayer(), block);
						TBLTInteractiveChestFinder.BASE.findChests(block.getLocation()).stream()
							.flatMap(c -> TBLTSwitch.findPoweredChests(c, g).stream())
							.forEach(c -> SwitchableChestManager.find(c).stream().forEach(s -> s.turnOff(c)));
						return true;
					}).orElse(false);
			}
		},


	QUEST_LIST(
			TBLTItemStackIcon.QUEST_PROGRESS,
			Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR, Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK),
			0
		) {
			@Override
			public boolean perform(PlayerInteractEvent event) {
				Player player = event.getPlayer();
				new TBLTArenaMap().findPlayingMap(player)
					.ifPresent(a ->
						TBLTPlayerGUI.QUESTS.create(a.getArenaMeta().getQuests().stream().map(q -> q.createProgress()).collect(Collectors.toList()))
							.ifPresent(i -> player.openInventory(i))
					);
				return true;
			}
		},
	;


	private KotobaItemStackIcon icon;
	private List<Action> triggers;
	private int consume;


	private ClickBlockAbility(KotobaItemStackIcon icon, List<Action> triggers, int consume) {
		this.icon = icon;
		this.triggers = triggers;
		this.consume = consume;
	}


	public static List<ClickBlockAbility> find(ItemStack item) {
		return Stream.of(ClickBlockAbility.values())
			.filter(ability -> ability.getIcon().create(1).isSimilar(item))
			.collect(Collectors.toList());
	}


	@Override
	public KotobaItemStackIcon getIcon() {
		return icon;
	}

	@Override
	public List<Action> getTriggers() {
		return triggers;
	}

	@Override
	public int getConsumption() {
		return consume;
	}

	@Override
	public KotobaPriorityValue getPriority() {
		return KotobaPriorityValue.MIDIUM;
	}

}

