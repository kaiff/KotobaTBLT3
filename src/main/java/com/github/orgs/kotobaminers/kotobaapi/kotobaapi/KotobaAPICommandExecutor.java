package com.github.orgs.kotobaminers.kotobaapi.kotobaapi;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KotobaAPICommandExecutor implements CommandExecutor {
	public enum PermissionEnum implements PermissionEnumInterface {
		OP() {
			@Override
			public boolean canPerform(Player player) {
				return player.isOp();
			}
		},
		PLAYER() {
			@Override
			public boolean canPerform(Player player) {
				return true;
			}
		},
		;
	}

	enum CommandEnum implements CommandEnumInterface {
		TEST(Arrays.asList(Arrays.asList("test")), "", "Command Test", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				return true;
			}
		},
		;

		public static final String LABEL = "kotobaapi";

		private List<List<String>> tree;
		private String option;
		private String description;
		private PermissionEnum permission;
		private CommandEnum(List<List<String>> tree, String option, String description, PermissionEnum permission) {
			this.tree = tree;
			this.option = option;
			this.description = description;
			this.permission = permission;
		}

		@Override
		public List<List<String>> getTree() {
			return tree;
		}

		@Override
		public String getOption() {
			return option;
		}

		@Override
		public String getDescription() {
			return description;
		}

		@Override
		public String getLabel() {
			return LABEL;
		}

		@Override
		public boolean hasPermission(Player player) {
			return getPermission().canPerform(player);
		}

		private PermissionEnum getPermission() {
			return this.permission;
		}

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			Optional<CommandEnum> commandEnum = Stream.of(CommandEnum.values())
				.filter(e -> e.matchArgs(args))
				.findFirst();

			if(commandEnum.isPresent()) {
				CommandEnum com = commandEnum.get();
				if(!com.hasPermission(player)) {
					player.sendMessage(com.getUsage());
					return false;
				}

				if(com.perform(player, args)) {
					return true;
				} else {
					player.sendMessage(com.getUsage());
					return true;
				}
			} else {
				Stream.of(CommandEnum.values()).forEach(e -> player.sendMessage(e.getUsage()));
			}
		}
		return true;
	}
}
