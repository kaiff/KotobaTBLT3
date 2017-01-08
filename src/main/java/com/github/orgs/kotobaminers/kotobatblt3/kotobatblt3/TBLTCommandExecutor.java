package com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.orgs.kotobaminers.develop.TBLTTest;
import com.github.orgs.kotobaminers.kotobaapi.kotobaapi.CommandEnumInterface;
import com.github.orgs.kotobaminers.kotobaapi.kotobaapi.PermissionEnumInterface;
import com.github.orgs.kotobaminers.kotobaapi.utility.KotobaUtility;
import com.github.orgs.kotobaminers.kotobatblt3.ability.ClickBlockAbility;
import com.github.orgs.kotobaminers.kotobatblt3.ability.ClickBlockChestAbility;
import com.github.orgs.kotobaminers.kotobatblt3.ability.ClickEntityAbility;
import com.github.orgs.kotobaminers.kotobatblt3.ability.ProjectileAbility;
import com.github.orgs.kotobaminers.kotobatblt3.ability.TBLTItem;
import com.github.orgs.kotobaminers.kotobatblt3.block.BlockReplacer;
import com.github.orgs.kotobaminers.kotobatblt3.block.BlockReplacerMap;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArena;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTPortal;
import com.github.orgs.kotobaminers.kotobatblt3.game.Game.TBLTGameMode;
import com.github.orgs.kotobaminers.kotobatblt3.game.TBLTJob;
import com.github.orgs.kotobaminers.kotobatblt3.gui.IconCreatorUtility;
import com.github.orgs.kotobaminers.kotobatblt3.gui.TBLTGUI;
import com.github.orgs.kotobaminers.kotobatblt3.gui.TBLTIconListGUI;
import com.github.orgs.kotobaminers.kotobatblt3.resource.TBLTResource;

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

	protected enum PlayerCommand implements CommandEnumInterface {
		TEST(Arrays.asList(Arrays.asList("test")), "", "Command Test", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				return true;
			}
		},

		TEST2(Arrays.asList(Arrays.asList("test2")), "", "Command Test", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
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

		ITEM(Arrays.asList(Arrays.asList("item")), "", "Get Items", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				Inventory inventory = player.getInventory();
				Stream.of(TBLTItem.values())
					.map(item -> item.createItem(64))
					.forEach(item -> inventory.addItem(item));
				Stream.of(TBLTPortal.values())
					.map(item -> item.createKey())
					.forEach(item -> inventory.addItem(item));
				return true;
			}
		},

		ABILITY(Arrays.asList(Arrays.asList("ability")), "", "Get Abilities", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				Inventory inventory = player.getInventory();
				Stream.of(ClickBlockAbility.values())
					.forEach(a -> inventory.addItem(a.createItem(64)));
				Stream.of(ClickEntityAbility.values())
					.forEach(a -> inventory.addItem(a.createItem(64)));
				Stream.of(ProjectileAbility.values())
					.forEach(a -> inventory.addItem(a.createItem(64)));
				Stream.of(ClickBlockChestAbility.values())
					.forEach(a -> inventory.addItem(a.createItem(64)));
				return true;
			}
		},

		RESOURCE(Arrays.asList(Arrays.asList("resource")), "", "Get Resources", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				Inventory inventory = player.getInventory();
				Stream.of(TBLTResource.values())
				.forEach(r -> inventory.addItem(r.create(64)));
				return true;
			}
		},

		RESOURCE_HERE(Arrays.asList(Arrays.asList("resourcehere")), "", "Get Resources for arena", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				List<String> options = takeOptions(args);
				Optional<TBLTArena> arena = new TBLTArenaMap().findUnique(player.getLocation())
						.map(storage -> (TBLTArena) storage);
				if(0 < options.size()) {
					arena.ifPresent(a -> Stream.of(TBLTResource.values()).forEach(resource -> a.setResources(resource, Integer.parseInt(options.get(0)))));
				}
				arena.ifPresent(a -> player.openInventory(a.getResourceInventory()));
				return true;
			}
		},

//		REPLACER_CREATE(Arrays.asList(Arrays.asList("replacer"), Arrays.asList("create", "c")), "<Replacer>", "Create Replacer", PermissionEnum.OP) {
//			@Override
//			public boolean perform(Player player , String[] args) {
//				List<String> options = takeOptions(args);
//				if(0 < options.size()) {
//					BlockReplacer replacer = BlockReplacer.create(options.get(0));
//					replacer.create(player, replacer.getName());
//					replacer.setSpawn(player.getLocation());
//					replacer.save();
//					BlockReplacer.addReplacer(replacer);
//					return true;
//				}
//				return false;
//			}
//		},
//
//		REPLACER_SETTRIGGER(Arrays.asList(Arrays.asList("replacer"), Arrays.asList("settrigger", "st")), "<Replacer>", "", PermissionEnum.OP) {
//			@Override
//			public boolean perform(Player player , String[] args) {
//				Block block = player.getTargetBlock((Set<Material>) null, 10);
//				if(block.getType().equals(Material.AIR)) {
//					player.sendMessage("No block");
//					return true;
//				}
//				Optional<BlockReplacer> replacer = BlockReplacer.getReplacers().stream()
//					.filter(r -> r.isIn(block.getLocation()))
//					.findAny();
//				if(replacer.isPresent()) {
//					Material material = player.getItemInHand().getType();
//					if(BlockReplacer.isTrigger(material)) {
//						replacer.get().setTrigger(material);
//					}
//					if(BlockReplacer.isBlock(block.getType())) {
//						replacer.get().setBlock(block.getType());
//					}
//					TBLTGUI.BLOCK_REPLACER.create(replacer.get().getGUIIcons())
//						.ifPresent(inventory -> player.openInventory(inventory));
//				} else {
//					player.sendMessage("Replacer Not Found");
//				}
//				return true;
//			}
//		},
//
//		REPLACER_SAVE(Arrays.asList(Arrays.asList("replacer"), Arrays.asList("save", "s")), "<Replacer>", "Save Arena", PermissionEnum.OP) {
//			@Override
//			public boolean perform(Player player , String[] args) {
//				List<String> options = takeOptions(args);
//				if(0 < options.size()) {
//					return BlockReplacer.getReplacers().stream()
//						.filter(replacer -> replacer.getName().equalsIgnoreCase(options.get(0)))
//						.findAny()
//						.map(replacer -> {
//							replacer.save();
//							replacer.saveOptions();
//							return true;
//						}).orElse(false);
//				}
//				return false;
//			}
//		},
//
//		REPLACER_RELOAD(Arrays.asList(Arrays.asList("replacer"), Arrays.asList("reload")), "<Replacer>", "Reload Replacer", PermissionEnum.OP) {
//			@Override
//			public boolean perform(Player player , String[] args) {
//				List<String> options = takeOptions(args);
//				if(0 < options.size()) {
//					return BlockReplacer.findUnique(options.get(0))
//						.map(replacer -> {
//							TBLTArena.findUnique(replacer.getCenter())
//								.map(arena -> arena.getBlocks());
//							return true;
//						}).orElse(false);
//				}
//				return false;
//			}
//		},

		GAME_START(Arrays.asList(Arrays.asList("game", "g"), Arrays.asList("start", "s")), "<GameMode>", "Start Game Mode", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player, String[] args) {
				List<String> options = takeOptions(args);
				if(1 < options.size()) {
					return Stream.of(TBLTGameMode.values())
					.filter(mode -> mode.name().equalsIgnoreCase(options.get(0)))
					.findFirst()
					.map(mode -> {
						KotobaUtility.setCoundDownScoreboard(Setting.getPlugin(), Arrays.asList(player), "Time Left", mode.name(), Integer.valueOf(options.get(1)));
						player.getWorld().playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
						mode.start(Bukkit.getOnlinePlayers().stream().collect(Collectors.toList()));
						return true;
					}).orElse(false);
				}
				return false;
			}
		},

		JOB_SELECT(Arrays.asList(Arrays.asList("job", "j"), Arrays.asList("select", "s")), "", "Select Job", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player, String[] args) {
				List<String> options = takeOptions(args);
				if(0 < options.size()) {
					Optional<TBLTJob> job = TBLTJob.find(options.get(0));
						if(job.isPresent()) {
							job.ifPresent(j -> j.become(player));
							return true;
						} else {
							return false;
						}
				} else {
					player.getInventory().clear();
					TBLTGUI.SELECT_JOB.create(TBLTJob.getAllJobs().stream().map(j -> j.getIcon()).collect(Collectors.toList()))
					.ifPresent(gui -> player.openInventory(gui));
					return true;
				}
			}
		},

		ARENA_CREATE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("create", "c")), "<Arena>", "Create Arena", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player, String[] args) {
				List<String> options = takeOptions(args);
				if(0 < options.size()) {
					TBLTArena arena = TBLTArena.create(options.get(0), player);
					arena.setSpawn(player.getLocation());
					new TBLTArenaMap().put(arena);
					arena.save();
					return true;
				}
				return false;
			}
		},

		ARENA_UPDATE_HERE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("updatehere", "uh")), "", "Update arena where you are", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player, String[] args) {
				return new TBLTArenaMap().findUnique(player.getLocation())
					.map(arena -> {
						arena.save();
						return true;
					}).orElse(false);
			}
		},

		ARENA_RELOAD_HERE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("reloadhere", "rh")), "", "Reload arena where you are", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player, String[] args) {
				return new TBLTArenaMap().findUnique(player.getLocation())
					.map(arena -> {
						arena.load();
						return true;
					}).orElse(false);
			}
		},

		ARENA_REMOVE_HERE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("removehere")), "", "Remove arena where you are", PermissionEnum.OP) {
		@Override
		public boolean perform(Player player, String[] args) {
			TBLTArenaMap arenas = new TBLTArenaMap();
			return arenas.findUnique(player.getLocation())
				.map(arena -> {
					arenas.remove(arena);
					arena.delete();
					return true;
				}).orElse(false);
		}
	},

	ARENA_RESIZE_HERE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("resizehere")), "", "Resize arena", PermissionEnum.OP) {
		@Override
		public boolean perform(Player player , String[] args) {
			return new TBLTArenaMap().findUnique(player.getLocation())
					.map(arena -> (TBLTArena) arena)
					.map(arena -> {
						arena.resize(player);
						arena.save();
						return true;
					}).orElse(false);
		}
	},

	ARENA_SETSPAWN_HERE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("setspawnhere", "ssh")), "", "Set arena spawn", PermissionEnum.OP) {
		@Override
		public boolean perform(Player player , String[] args) {
			return new TBLTArenaMap().findUnique(player.getLocation())
					.map(arena -> {
						arena.setSpawn(player.getLocation());
						arena.save();
						return true;
					}).orElse(false);
		}
	},

	ARENA_CHECK_POINT_HERE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("checkpointhere", "cph")), "", "Add check point", PermissionEnum.OP) {
		@Override
		public boolean perform(Player player , String[] args) {
			return new TBLTArenaMap().findUnique(player.getLocation())
				.map(arena -> (TBLTArena) arena)
				.map(arena -> {
					arena.addCheckPoint(player.getLocation());
					arena.save();
					return true;
				}).orElse(false);
		}
	},

	ARENA_CHECK_POINT_REMOVE_HERE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("checkpointremovehere", "cprh")), "", "Remove nearest check point", PermissionEnum.OP) {
		@Override
		public boolean perform(Player player , String[] args) {
			return new TBLTArenaMap().findUnique(player.getLocation())
				.map(arena -> (TBLTArena) arena)
				.map(arena -> {
					arena.removeNearestCheckPoint(player.getLocation());
					arena.save();
					return true;
				}).orElse(false);
		}
	},

	ARENA_SET_PREDICTION_HERE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("setpredictionhere", "sph")), "", "Set prediction with book and quill", PermissionEnum.OP) {
		@Override
		public boolean perform(Player player , String[] args) {
			ItemStack item = player.getItemInHand();
			if(item.getItemMeta() instanceof ItemMeta) {
				return new TBLTArenaMap().findUnique(player.getLocation())
					.map(a -> {
						((TBLTArena) a).setPredictionItem((BookMeta) item.getItemMeta());
						a.save();
						return true;
					}).orElse(false);
			}
			return false;
		}
	},

	ARENA_GET_PREDICTION_HERE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("getpredictionhere", "gph")), "", "Get prediction with book and quill", PermissionEnum.OP) {
		@Override
		public boolean perform(Player player , String[] args) {
			return new TBLTArenaMap().findUnique(player.getLocation())
				.map(a -> ((TBLTArena) a).getPredictionWrittenBook())
				.map(i -> {
					BookMeta meta = (BookMeta) i.getItemMeta();
					ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
					book.setItemMeta(meta);
					player.getInventory().addItem(book);
					return true;
				}).orElse(false);
		}
	},

	ARENA_LIST(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("list", "l")), "", "List arenas", PermissionEnum.OP) {
		@Override
		public boolean perform(Player player , String[] args) {
			player.openInventory(TBLTIconListGUI.ARENA.createInventory(1));
			return true;
		}
	},

	ARENA_HERE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("here", "h")), "", "Arena where you are", PermissionEnum.OP) {
		@Override
		public boolean perform(Player player , String[] args) {
			return new TBLTArenaMap().findUnique(player.getLocation())
				.map(arena -> {
					TBLTGUI.ARENA.create(IconCreatorUtility.getIcons((TBLTArena) arena)).ifPresent(player::openInventory);;
					return true;
				}).orElse(false);
		}
	},


	REPLACER_CREATE(Arrays.asList(Arrays.asList("replacer", "r"), Arrays.asList("create", "c")), "<Arena>", "Create Arena", PermissionEnum.OP) {
		@Override
		public boolean perform(Player player, String[] args) {
			List<String> options = takeOptions(args);
			if(0 < options.size()) {
				BlockReplacer replacer = BlockReplacer.create(options.get(0), player);
				replacer.setSpawn(player.getLocation());
				new BlockReplacerMap().put(replacer);
				replacer.save();
				return true;
			}
			return false;
		}
	},

	REPLACER_UPDATE_HERE(Arrays.asList(Arrays.asList("replacer", "r"), Arrays.asList("updatehere", "uh")), "", "Update where you are", PermissionEnum.OP) {
		@Override
		public boolean perform(Player player, String[] args) {
			return new BlockReplacerMap().findUnique(player.getLocation())
				.map(replacer -> {
					replacer.save();
					return true;
				}).orElse(false);
		}
	},

	REPLACER_AFTER_HERE(Arrays.asList(Arrays.asList("replacer", "r"), Arrays.asList("afterhere", "ah")), "", "Replace as after where you are", PermissionEnum.OP) {
		@Override
		public boolean perform(Player player, String[] args) {
			return new BlockReplacerMap().findUnique(player.getLocation())
				.map(replacer -> {
					replacer.load();
					return true;
				}).orElse(false);
		}
	},

	REPLACER_BEFORE_HERE(Arrays.asList(Arrays.asList("replacer", "r"), Arrays.asList("beforehere", "bh")), "", "Replace as before where you are", PermissionEnum.OP) {
		@Override
		public boolean perform(Player player, String[] args) {
			return false;//TODO not implemented yet
//			return new DevReplacerMap().findUnique(player.getLocation())
//				.map(replacer -> {
//					Location center = replacer.getCenter();
//					new DevArenaMap().findUnique(center)
//						.map(arena -> arena.getBlocks())
//						.ifPresent(blocks -> {
//							blocks.stream()
//								.filter(block -> replacer.isIn(block.getLocation()))
//								.forEach(block -> block. setType(block.getType()));
//						});
//					return true;
//				}).orElse(false);
		}
	},

	REPLACER_REMOVE_HERE(Arrays.asList(Arrays.asList("replacer", "r"), Arrays.asList("removehere")), "", "Remove arena where you are", PermissionEnum.OP) {
		@Override
		public boolean perform(Player player, String[] args) {
			BlockReplacerMap replacers = new BlockReplacerMap();
			return replacers.findUnique(player.getLocation())
					.map(replacer -> {
						replacers.remove(replacer);
						replacer.delete();
						return true;
					}).orElse(false);
		}
	},

	REPLACER_SETTRIGGER(Arrays.asList(Arrays.asList("replacer", "r"), Arrays.asList("settrigger", "st")), "", "Set trigger and target", PermissionEnum.OP) {
		@Override
		public boolean perform(Player player , String[] args) {
			Block block = player.getTargetBlock((Set<Material>) null, 10);
			if(block.getType().equals(Material.AIR)) {
				player.sendMessage("No block");
				return true;
			}

			return new BlockReplacerMap().findUnique(block.getLocation())
				.map(replacer -> {
					BlockReplacer r = (BlockReplacer) replacer;
					Material trigger = player.getItemInHand().getType();
					Material target = block.getType();
					r.setTriggers(trigger, target);
					r.save();
					TBLTGUI.BLOCK_REPLACER.create(IconCreatorUtility.getIcons(r))
						.ifPresent(inventory -> player.openInventory(inventory));
					return true;
				}).orElse(false);
		}
	},

	REPLACER_LIST(Arrays.asList(Arrays.asList("replacer", "r"), Arrays.asList("list", "l")), "", "List replacers", PermissionEnum.OP) {
		@Override
		public boolean perform(Player player , String[] args) {
			player.openInventory(TBLTIconListGUI.BLOCK_REPLACER.createInventory(1));
			return true;
		}
	},

	REPLACER_HERE(Arrays.asList(Arrays.asList("replacer", "r"), Arrays.asList("here", "h")), "", "Replacer where you are", PermissionEnum.OP) {
		@Override
		public boolean perform(Player player , String[] args) {
			return new BlockReplacerMap().findUnique(player.getLocation())
				.map(arena -> {
					TBLTGUI.BLOCK_REPLACER.create(IconCreatorUtility.getIcons((BlockReplacer) arena)).ifPresent(player::openInventory);;
					return true;
				}).orElse(false);
		}
	},
//		ARENA_JOIN(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("join", "j")), "<Arena>", "Join Arena", PermissionEnum.PLAYER) {
//			@Override
//			public boolean perform(Player player , String[] args) {
//				List<String> options = takeOptions(args);
//				if(0 < options.size()) {
//					TBLTArena.getArenas().stream()
//						.filter(arena -> arena.getName().equalsIgnoreCase(options.get(0)) && arena.getSpawn() != null)
//						.findAny()
//						.ifPresent(arena -> arena.join(player));
//					return true;
//				}
//				return false;
//			}
//		},
//		ARENA_LEAVE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("leave", "l")), "<Arena>", "Leave Arena", PermissionEnum.OP) {
//			@Override
//			public boolean perform(Player player , String[] args) {
//				return false;
//			}
//		},
		GUI_EFFECT(Arrays.asList(Arrays.asList("gui"), Arrays.asList("effect", "e")), "", "Command Test", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				player.openInventory(TBLTIconListGUI.EFFECT.createInventory(1));
				return true;
			}
		},
		GUI_SOUND(Arrays.asList(Arrays.asList("gui"), Arrays.asList("sound", "s")), "", "Command Test", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				player.openInventory(TBLTIconListGUI.SOUND.createInventory(1));
				return true;
			}
		},
		;

		public static final String LABEL = "tblt";

		private List<List<String>> tree;
		private String option;
		private String description;
		private PermissionEnum permission;
		private PlayerCommand(List<List<String>> tree, String option, String description, PermissionEnum permission) {
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
			Optional<PlayerCommand> playerCommand = Stream.of(PlayerCommand.values())
				.filter(e -> e.matchArgs(args))
				.findFirst();

			if(playerCommand.isPresent()) {
				PlayerCommand com = playerCommand.get();
				if(!com.hasPermission(player)) {
					player.sendMessage(com.getUsage());
					return false;
				}

				if(com.perform(player, args)) {//Perform command here
					return true;
				} else {
					player.sendMessage(com.getUsage());//Cancelled
					return true;
				}
			} else {
				Stream.of(PlayerCommand.values()).forEach(e -> player.sendMessage(e.getUsage()));
			}
		}

		//Develop command from console
		if(sender instanceof ConsoleCommandSender) {
			TBLTTest.test(sender, args);
			return true;
		}
		return true;
	}

}
