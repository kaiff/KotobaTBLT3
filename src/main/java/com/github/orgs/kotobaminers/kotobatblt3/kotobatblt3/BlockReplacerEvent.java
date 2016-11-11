package com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import com.github.orgs.kotobaminers.kotobatblt3.game.BlockReplacer;

public class BlockReplacerEvent {
	interface BlockReplacerEventEnumInterface {
		void perform(BlockReplacer replacer);
	}

	public enum BlockReplacerEventEnum implements BlockReplacerEventEnumInterface {
		REPLACE {
			@Override
			public void perform(BlockReplacer replacer) {
				replacer.load(Bukkit.getWorlds());
			}
		},
		EXPLODE_EFFECT {
			@Override
			public void perform(BlockReplacer replacer) {
				replacer.getBlocks().stream()
					.filter(b -> b.getType().equals(replacer.getBlockTrigger()))
					.map(Block::getLocation)
					.forEach(l -> replacer.getWorld().createExplosion(l.getBlockX(), l.getBlockY(), l.getBlockZ(), 2, false, false));
			}
		},
		BREAK_EFFECT {
			@Override
			public void perform(BlockReplacer replacer) {
			}
		},
		;

	}
}
