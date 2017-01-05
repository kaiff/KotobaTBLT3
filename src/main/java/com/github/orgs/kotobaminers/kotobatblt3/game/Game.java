package com.github.orgs.kotobaminers.kotobatblt3.game;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bukkit.entity.Player;


public class Game {
	
	interface GameModeEnumInterface {
		void start(List<Player> players);
		public Integer getPeriod();
	}
	
	public enum TBLTGameMode implements GameModeEnumInterface {
		RESEARCH(
			180,
			Arrays.asList("Research", "Talk with people and find who to be rescued.", "Go around and find the way to rescue and extinguish.")
		) {
			@Override
			public void start(List<Player> players) {
				players.forEach(p -> printStartMessage(p));
			}
		},
		DISCUSS(
			180,	
			Arrays.asList("Discuss", "Make a strategy and pick stuff up.")
		) {
			@Override
			public void start(List<Player> players) {
			}
		},
		RESCUE(
			180,
			Arrays.asList("Rescue", "Rescue all people.")
		) {
			@Override
			public void start(List<Player> players) {
			}
		},
		EXTINGUISH_A_FIRE(
			180,
			Arrays.asList("Extinguish a fire", "Bomb the portal!")
		) {
			@Override
			public void start(List<Player> players) {
			}
		},
		;
		
		private Integer period;
		private List<String> startMessage;
		public static List<TBLTGameMode> order = Arrays.asList(RESEARCH, DISCUSS, RESCUE, EXTINGUISH_A_FIRE);
		
		private TBLTGameMode(Integer period, List<String> startMessage) {
			this.period = period;
			this.startMessage = startMessage;
		}
		
		public static Optional<TBLTGameMode> findNext(TBLTGameMode current) {
			for(TBLTGameMode c : order) {
				if(c.equals(current)) {
					if(c.equals(order.get(order.size()-1))) {
						return Optional.empty();
					} else {
						return Optional.of(c);
					}
				}
			}
			return Optional.empty();
		}
		
		@Override
		public Integer getPeriod() {
			return period;
		}
		
		public void printStartMessage(Player player) {
			startMessage.stream().forEach(msg -> player.sendMessage(msg));
		}
	}
	
	Player player1;
	Player player2;
}
