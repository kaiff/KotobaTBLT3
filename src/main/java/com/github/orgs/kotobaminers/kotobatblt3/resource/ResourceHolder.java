package com.github.orgs.kotobaminers.kotobatblt3.resource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaItemStack;

public interface ResourceHolder {
	Map<TBLTResource, Integer> getResources();


	default Inventory getResourceInventory() {
		Inventory inventory = Bukkit.createInventory(null, 54, "Resources");
		Stream.of(TBLTResource.values())
			.forEach(r -> inventory.addItem(r.create(getResources().getOrDefault(r, 0))));
		return inventory;
	}

	default Inventory getResourceConsumptionInventory(Map<TBLTResource, Integer> consumption) {
		Inventory inventory = getResourceInventory();
		int offset = ((getResources().size() / 9) + 1) * 9;
		List<TBLTResource> all = Stream.of(TBLTResource.values()).collect(Collectors.toList());
		Stream.iterate(0, i -> i + 1)
			.limit(all.size())
			.forEach(i -> {
				if(consumption.containsKey(all.get(i))) {
					if(consumption.get(all.get(i)) < 1) return;
					ItemStack icon = KotobaItemStack.create(Material.WOOL, (short) 15, consumption.get(all.get(i)), "You need", Arrays.asList(all.get(i).getColoredName()));
					inventory.setItem(i + offset, icon);
				}
			});
		return inventory;
	}

	default void consumeResources(Map<TBLTResource, Integer> consumption) {
		consumption.entrySet().stream().forEach(c -> getResources().computeIfPresent(c.getKey(), (k, v) -> v - c.getValue()));
	}

	default boolean hasResources(Map<TBLTResource, Integer> consumption) {
		return consumption.entrySet().stream()
			.allMatch(c -> c.getValue() <= getResources().getOrDefault(c.getKey(), 0));
	}

	default public void setResources(TBLTResource resource, int amount) {
		getResources().put(resource, amount);
	}

	default public void addResources(TBLTResource resource, int amount) {
		getResources().computeIfPresent(resource, (k, v) -> v + amount);
		getResources().putIfAbsent(resource, amount);
	}

	default void addResources(TBLTResourceBlock resourceBlock) {
		addResources(resourceBlock.getResource(), resourceBlock.getAmount());
	}

	default void initializeResources() {
		Stream.of(TBLTResource.values())
			.forEach(r -> getResources().put(r, 0));
	}

}
