package com.github.orgs.kotobaminers.kotobatblt3.resource;

import java.util.Map;

import org.bukkit.block.Block;

/**
 * @author fukus
 *
 */
public interface ResourceConsumer {
	/**
	 * @param clicked nullable
	 * @return
	 */
	Map<TBLTResource, Integer> getResourceConsumption(Block block);

}

