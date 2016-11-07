package com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.orgs.kotobaminers.kotobaapi.kotobaapi.CommandEnumInterface;
import com.github.orgs.kotobaminers.kotobaapi.kotobaapi.PermissionEnumInterface;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaAPIUtility;
import com.github.orgs.kotobaminers.kotobatblt3.game.BlockReplacer;
import com.github.orgs.kotobaminers.kotobatblt3.game.Game.GameMode;
import com.github.orgs.kotobaminers.kotobatblt3.game.TBLTArena;
import com.github.orgs.kotobaminers.kotobatblt3.gui.TBLTGUI;
import com.github.orgs.kotobaminers.kotobatblt3.gui.TBLTIconListGUI;
import com.github.orgs.kotobaminers.kotobatblt3.player.TBLTJob;

public class TBLTCommandExecutor implements CommandExecutor {
	private final KotobaTBLT3 plugin;

	public TBLTCommandExecutor (KotobaTBLT3 plugin) {
		this.plugin = plugin;
	}

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
				player.openInventory(TBLTIconListGUI.SOLID_BLOCK.createInventory(1));
				return true;
			}
		},
		RELOAD(Arrays.asList(Arrays.asList("reload")), "", "Reload Plugin", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				Setting.initialize(Setting.getPlugin());
				return true;
			}
		},
		REPLACER_LIST(Arrays.asList(Arrays.asList("replacer"), Arrays.asList("list", "c")), "", "List Replacer", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				player.openInventory(TBLTIconListGUI.BLOCK_REPLACER.createInventory(1));
				return true;
			}
		},
		REPLACER_CREATE(Arrays.asList(Arrays.asList("replacer"), Arrays.asList("create", "c")), "<Replacer>", "Create Replacer", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				List<String> options = takeOptions(args);
				if(0 < options.size()) {
					BlockReplacer replacer = BlockReplacer.create(options.get(0));
					replacer.setData(player, replacer.getName());
					replacer.save();
					BlockReplacer.addReplacer(replacer);
					return true;
				}
				return false;
			}
		},
		REPLACER_BLOCK(Arrays.asList(Arrays.asList("replacer"), Arrays.asList("block", "b")), "<Replacer>", "Set Block", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				List<String> options = takeOptions(args);
				if(0 < options.size()) {
					Optional<BlockReplacer> replacer = BlockReplacer.getReplacers().stream()
						.filter(r -> r.getName().equalsIgnoreCase(options.get(0)))
						.findFirst();
					if(replacer.isPresent()) {
						Material material = player.getItemInHand().getType();
						if(BlockReplacer.isBlock(material)) {
							replacer.get().setBlock(material);
						} else {
							player.sendMessage("Invalid Item Type for Block: " + material.name());
						}
					} else {
						player.sendMessage("Replacer Not Found:" + options.get(0));
					}
					return true;
				}
				return false;
			}
		},
		REPLACER_TRIGGER(Arrays.asList(Arrays.asList("replacer"), Arrays.asList("trigger", "t")), "<Replacer>", "Set Trigger", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				List<String> options = takeOptions(args);
				if(0 < options.size()) {
					Optional<BlockReplacer> replacer = BlockReplacer.getReplacers().stream()
						.filter(r -> r.getName().equalsIgnoreCase(options.get(0)))
						.findFirst();
					if(replacer.isPresent()) {
						Material material = player.getItemInHand().getType();
						if(BlockReplacer.isTrigger(material)) {
							replacer.get().setTrigger(material);
						} else {
							player.sendMessage("Invalid Item Type for Trigger: " + material.name());
						}
					} else {
						player.sendMessage("Replacer Not Found" + options.get(0));
					}
					return true;
				}
				return false;
			}
		},
		REPLACER_SAVE(Arrays.asList(Arrays.asList("replacer"), Arrays.asList("save", "s")), "<Replacer>", "Save Arena", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				List<String> options = takeOptions(args);
				if(0 < options.size()) {
					return BlockReplacer.getReplacers().stream()
						.filter(replacer -> replacer.getName().equalsIgnoreCase(options.get(0)))
						.findAny()
						.map(replacer -> {
							replacer.save();
							replacer.saveOptions();
							return true;
						}).orElse(false);
				}
				return false;
			}
		},
		GAME_START(Arrays.asList(Arrays.asList("game", "g"), Arrays.asList("start", "s")), "<GameMode>", "Start Game Mode", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player, String[] args) {
				List<String> options = takeOptions(args);
				if(1 < options.size()) {
					return Stream.of(GameMode.values())
					.filter(mode -> mode.name().equalsIgnoreCase(options.get(0)))
					.findFirst()
					.map(mode -> {
						KotobaAPIUtility.setCoundDownScoreboard(Setting.getPlugin(), Arrays.asList(player), "Time Left", mode.name(), Integer.valueOf(options.get(1)));
						player.getWorld().playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
						mode.start(Bukkit.getOnlinePlayers().stream().collect(Collectors.toList()));
						return true;
					}).orElse(false);
				}
				return false;
			}
		},
		JOB_SELECT(Arrays.asList(Arrays.asList("job", "j"), Arrays.asList("select", "s")), "", "Select Job", PermissionEnum.PLAYER) {
			@Override
			public boolean perform(Player player, String[] args) {
				player.getInventory().clear();
				TBLTGUI.SELECT_JOB.create(TBLTJob.getSelectJobIcons()).ifPresent(gui -> player.openInventory(gui));
				return true;
			}
		},
		ARENA_CREATE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("create", "c")), "<Arena>", "Create Arena", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player, String[] args) {
				List<String> options = takeOptions(args);
				if(0 < options.size()) {
					TBLTArena arena = TBLTArena.create(options.get(0));
					arena.setData(player, arena.getName());
					arena.save();
					TBLTArena.addArena(arena);
					return true;
				}
				return false;
			}
		},
//		ARENA_RESIZE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("resize")), "<Arena>", "Create Arena", PermissionEnum.OP) {
//			@Override
//			public boolean perform(Player player, String[] args) {
//				List<String> options = takeOptions(args);
//				if(0 < options.size()) {
//					MinigamesApi.Arenas.stream()
//						.filter(arena -> arena.getName().equalsIgnoreCase(options.get(0)))
//						.findFirst()
//						.map(arena -> (TBLTArenaOld) arena)
//						.ifPresent(arena -> arena.resize(player));
//					return true;
//				}
//				return false;
//			}
//		},
//		ARENA_REMOVE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("remove")), "<Arena>", "Remove Arena", PermissionEnum.OP) {
//			@Override
//			public boolean perform(Player player, String[] args) {
//				List<String> options = takeOptions(args);
//				if(0 < options.size()) {
//					return MinigamesApi.Arenas.stream()
//						.filter(a -> a.getName().equalsIgnoreCase(options.get(0)))
//						.findFirst()
//						.map(a -> {
//							MinigamesApi.Arenas.remove(a);
//							new TBLTConfig(a.getName(), Setting.getPlugin().getName(), TBLTArenaOld.arenaDirectory).delete();
//							return true;
//						}).orElse(false);
//				}
//				return false;
//			}
//		},
		ARENA_JOIN(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("join", "j")), "<Arena>", "Join Arena", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				List<String> options = takeOptions(args);
				if(0 < options.size()) {
					String message = TBLTArena.getArenas().stream()
						.filter(arena -> arena.getName().equalsIgnoreCase(options.get(0)) && arena.getSpawn() != null)
						.findFirst()
						.map(TBLTArena::getSpawn)
						.map(loc -> {
							player.teleport(loc);
							return "teleporting...";
						}).orElse("Invalid");
					player.sendMessage(message);
					return true;
				}
				return false;
			}
		},
		ARENA_LEAVE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("leave", "l")), "<Arena>", "Leave Arena", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				return false;
			}
		},
		ARENA_SAVE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("save", "s")), "<Arena>", "Save Arena", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				List<String> options = takeOptions(args);
				if(0 < options.size()) {
					return TBLTArena.getArenas().stream()
						.filter(arena -> arena.getName().equalsIgnoreCase(options.get(0)))
						.findAny()
						.map(arena -> {
							arena.save();
							return true;
						}).orElse(false);
				}
				return false;
			}
		},
		ARENA_RELOAD(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("reload", "r")), "<Arena>", "Reload Arena", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				List<String> options = takeOptions(args);
				if(0 < options.size()) {
					return TBLTArena.getArenas().stream()
						.filter(arena -> arena.getName().equalsIgnoreCase(options.get(0)))
						.findAny()
						.map(arena -> {
							arena.setDataFromConfig(Bukkit.getWorlds());
							return true;
						}).orElse(false);
				}
				return false;
			}
		},
		ARENA_LIST(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("list")), "", "List Arena", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				TBLTArena.getArenas().stream()
					.forEach(arena -> player.sendMessage(arena.getName()));
				return true;
			}
		},
		ARENA_SETSPAWN(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("setspawn", "sp")), "", "Set Arena Spawn", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				List<String> options = takeOptions(args);
				if(0 < options.size()) {
					return TBLTArena.getArenas().stream()
						.filter(arena -> arena.getName().equalsIgnoreCase(options.get(0)))
						.findAny()
						.map(arena -> {
							arena.setSpawn(player.getLocation());
							return true;
						}).orElse(false);
				}
				return false;
			}
		},
		;

		public static final String label = "tblt";

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
			return label;
		}

		@Override
		public boolean canPerform(Player player) {
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
				if(!com.canPerform(player)) {
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
