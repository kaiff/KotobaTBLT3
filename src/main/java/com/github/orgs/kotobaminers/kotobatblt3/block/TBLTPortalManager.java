package com.github.orgs.kotobaminers.kotobatblt3.block;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.orgs.kotobaminers.kotobaapi.block.KotobaPortalInterface;
import com.github.orgs.kotobaminers.kotobaapi.block.KotobaPortalManager;

public class TBLTPortalManager extends KotobaPortalManager {


	@Override
	protected List<KotobaPortalInterface> getAllPortals() {
		return Arrays.asList(
			ChestPortal.values(),
			SimplePortal.values()
		).stream()
		.flatMap(portals -> Stream.of(portals)).collect(Collectors.toList());
	}


}

