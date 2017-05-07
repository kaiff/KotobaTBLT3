package com.github.orgs.kotobaminers.kotobaapi.utility;

import java.util.List;
import java.util.stream.Collectors;

public interface KotobaPriority {


	public KotobaPriorityValue getPriority();


	default List<KotobaPriority> findHighetPriorities(List<KotobaPriority> priorities) {
		int highest = priorities.stream()
			.mapToInt(p -> p.getPriority().getValue())
			.max()
			.orElse(0);
		return priorities.stream()
			.filter(p -> p.getPriority().getValue() == highest)
			.collect(Collectors.toList());
	}


}

