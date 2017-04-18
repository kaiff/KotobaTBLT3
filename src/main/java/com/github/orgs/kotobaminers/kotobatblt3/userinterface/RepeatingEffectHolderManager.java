package com.github.orgs.kotobaminers.kotobatblt3.userinterface;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.orgs.kotobaminers.kotobaapi.userinterface.RepeatingEffectHolder;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;

public class RepeatingEffectHolderManager {


	private static final List<RepeatingEffectHolder> HOLDERS = Arrays.asList();


	private static List<RepeatingEffectHolder> getHolders() {
		return HOLDERS.stream()
			.flatMap(holders -> Stream.of(holders))
			.collect(Collectors.toList());
	}


	public static List<RepeatingEffectHolder> findHolders(TBLTItemStackIcon icon) {
		return getHolders().stream()
			.filter(h -> h.getIcon() == icon)
			.collect(Collectors.toList());
	}


}

