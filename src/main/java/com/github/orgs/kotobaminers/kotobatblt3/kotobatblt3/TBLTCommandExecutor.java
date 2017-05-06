package com.github.orgs.kotobaminers.kotobatblt3.kotobatblt3;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.github.orgs.kotobaminers.develop.TBLTTest;
import com.github.orgs.kotobaminers.kotobaapi.block.KotobaStructure;
import com.github.orgs.kotobaminers.kotobaapi.citizens.KotobaCitizensManager;
import com.github.orgs.kotobaminers.kotobaapi.kotobaapi.CommandEnumInterface;
import com.github.orgs.kotobaminers.kotobaapi.kotobaapi.PermissionEnumInterface;
import com.github.orgs.kotobaminers.kotobaapi.userinterface.Holograms;
import com.github.orgs.kotobaminers.kotobatblt3.ability.TBLTGem;
import com.github.orgs.kotobaminers.kotobatblt3.block.InteractiveStructure;
import com.github.orgs.kotobaminers.kotobatblt3.block.ReplacerSwitchChest;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArena;
import com.github.orgs.kotobaminers.kotobatblt3.block.TBLTArenaMap;
import com.github.orgs.kotobaminers.kotobatblt3.citizens.UniqueNPC;
import com.github.orgs.kotobaminers.kotobatblt3.database.PlayerData;
import com.github.orgs.kotobaminers.kotobatblt3.database.PlayerDatabase;
import com.github.orgs.kotobaminers.kotobatblt3.database.SentenceDatabase;
import com.github.orgs.kotobaminers.kotobatblt3.database.TBLTConversationEditorMap;
import com.github.orgs.kotobaminers.kotobatblt3.userinterface.IconCreatorUtility;
import com.github.orgs.kotobaminers.kotobatblt3.userinterface.TBLTIconListGUI;
import com.github.orgs.kotobaminers.kotobatblt3.userinterface.TBLTPlayerGUI;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTItemStackIcon;
import com.github.orgs.kotobaminers.kotobatblt3.utility.TBLTUtility;

public class TBLTCommandExecutor implements CommandExecutor {


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


	public enum PlayerCommand implements CommandEnumInterface {
		TEST(Arrays.asList(Arrays.asList("test")), "", "Command Test", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				System.out.println(new TBLTArenaMap().getStorages().size());
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
				player.openInventory(TBLTIconListGUI.ITEM.createInventory(1));
				return true;
			}
		},


		CHEST(Arrays.asList(Arrays.asList("chest")), "", "Open the nearest chest", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				Location location = player.getLocation();
				TBLTUtility.getSpherePositions(player.getLocation(), 5).stream()
					.map(l -> l.getBlock())
					.filter(b -> b.getState() instanceof Chest)
					.map(b -> b.getLocation())
					.sorted((l1, l2) -> (int) (l1.distance(location) - l2.distance(location)))
					.findFirst()
					.ifPresent(l -> player.openInventory(((Chest) l.getBlock().getState()).getInventory()));
				return true;
			}
		},


		HOLOGRAM_REMOVE(Arrays.asList(Arrays.asList("hologram"), Arrays.asList("remove")), "", "Remove All Holograms", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				Holograms.removeAllHolograms(Setting.getPlugin());
				return true;
			}
		},


		UNIQUENPC_SPAWN(Arrays.asList(Arrays.asList("uniquenpc", "un"), Arrays.asList("spawn", "s")), "", "Spawn All Unique NPCs", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				Stream.of(UniqueNPC.values())
					.forEach(u -> u.getNPCs().stream().forEach(npc -> {
						npc.spawn(npc.getStoredLocation());
						u.become(npc);
					}));
				return true;
			}
		},


		UNIQUENPC_DESPAWN(Arrays.asList(Arrays.asList("uniquenpc", "un"), Arrays.asList("despawn", "d")), "", "Despawn All Unique NPCs", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				Stream.of(UniqueNPC.values())
					.forEach(u -> u.getNPCs().stream().forEach(npc -> npc.despawn()));
				return true;
			}
		},


		EDIT_ENGLISH(Arrays.asList(Arrays.asList("edit", "e"), Arrays.asList("english", "e")), "", "Edit English", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				PlayerData data = new PlayerDatabase().getOrDefault(player.getUniqueId());
				List<String> options = takeOptions(args);
				if(0 < options.size()) {
					String line = String.join(" ", options);
					new SentenceDatabase().findConversation(data.getNPC())
						.ifPresent(conversation -> new TBLTConversationEditorMap().registerConversationEditorOrDefault(conversation, player.getUniqueId()).editEnglish(line));
					return true;
				}
				return false;
			}
		},


		EDIT_NPC(Arrays.asList(Arrays.asList("edit", "e"), Arrays.asList("npc", "n")), "", "Change NPC by ID", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				PlayerData data = new PlayerDatabase().getOrDefault(player.getUniqueId());
				List<String> options = takeOptions(args);
				if(0 < options.size()) {
					try {
						int npc = Integer.parseInt(options.get(0));
						KotobaCitizensManager.findNPC(npc)
							.ifPresent(n -> new SentenceDatabase().findConversation(data.getNPC())
								.ifPresent(conversation -> new TBLTConversationEditorMap().registerConversationEditorOrDefault(conversation, player.getUniqueId()).editNPC(n.getId())));
					} catch(NumberFormatException e) {
						e.printStackTrace();
					}
					return true;
				}
				return false;
			}
		},


		EDIT_REMOVE(Arrays.asList(Arrays.asList("edit", "e"), Arrays.asList("remove", "r")), "", "Remove sentence", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				PlayerData data = new PlayerDatabase().getOrDefault(player.getUniqueId());
				new SentenceDatabase().findConversation(data.getNPC())
					.ifPresent(conversation -> new TBLTConversationEditorMap().registerConversationEditorOrDefault(conversation, player.getUniqueId()).removeSentence());
				return true;
			}
		},

		EDIT_PREPEND(Arrays.asList(Arrays.asList("edit", "e"), Arrays.asList("prepend", "p")), "", "Prepend sentence", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				PlayerData data = new PlayerDatabase().getOrDefault(player.getUniqueId());
				new SentenceDatabase().findConversation(data.getNPC())
					.ifPresent(conversation -> new TBLTConversationEditorMap().registerConversationEditorOrDefault(conversation, player.getUniqueId()).prependEmpty());
				return true;
			}
		},

		EDIT_APPEND(Arrays.asList(Arrays.asList("edit", "e"), Arrays.asList("append", "a")), "", "Append sentence", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				PlayerData data = new PlayerDatabase().getOrDefault(player.getUniqueId());
				new SentenceDatabase().findConversation(data.getNPC())
					.ifPresent(conversation -> new TBLTConversationEditorMap().registerConversationEditorOrDefault(conversation, player.getUniqueId()).appendEmpty());
				return true;
			}
		},

		EDIT_SERVANT(Arrays.asList(Arrays.asList("edit", "e"), Arrays.asList("servant", "s")), "<" + String.join(", ", Stream.of(UniqueNPC.values()).map(UniqueNPC::name).collect(Collectors.toList())) +">", "Change as servant", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				PlayerData data = new PlayerDatabase().getOrDefault(player.getUniqueId());
				List<String> options = takeOptions(args);
				if(0 < options.size()) {
					Stream.of(UniqueNPC.values())
						.filter(u -> u.name().equalsIgnoreCase(options.get(0)))
						.findAny()
						.ifPresent(u ->
							KotobaCitizensManager.findNPC(data.getNPC())
								.ifPresent(npc -> {
									u.become(npc);
									player.getInventory().addItem(u.createKey(npc.getId()));
								})
						);

					;
				}
				return true;
			}
		},


		JOB_SELECT(Arrays.asList(Arrays.asList("job", "j"), Arrays.asList("select", "s")), "<NAME>", "Select Job", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player, String[] args) {
				List<String> options = takeOptions(args);
				if(0 < options.size()) {
					Optional<TBLTPlayer> job = TBLTPlayer.find(options.get(0));
					if(job.isPresent()) {
						job.ifPresent(j -> j.become(player));
						return true;
					}
				}
				player.sendMessage("Jobs: " + String.join(", ", Stream.of(TBLTPlayer.values()).map(p -> p.name()).collect(Collectors.toList())));
				return false;
			}
		},


		SWITCH_REPLACER(Arrays.asList(Arrays.asList("switch", "s"), Arrays.asList("replacer", "r")), "<DIRECTION> <GEM> <ON|OFF> (AIR)", "Switch Replacer", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player, String[] args) {
				List<String> options = takeOptions(args);
				if(1 < options.size()) {
					return DIRECTIONS.stream()
						.filter(d -> d.name().equalsIgnoreCase(options.get(0)))
						.findAny()
						.map(d ->
							GEMS.stream().filter(g -> g.name().equalsIgnoreCase(options.get(1)))
								.findAny()
								.map(g -> {
									if(2 < options.size()) {
										if(options.get(2).equalsIgnoreCase("on")) {
											if(3 < options.size()) {
												if(options.get(3).equalsIgnoreCase("air")) {
													new ReplacerSwitchChest().placeChestsOnAir(player, d, g);
													return true;
												}
											}
										} else if(options.get(2).equalsIgnoreCase("off")) {
											if(3 < options.size()) {
												if(options.get(3).equalsIgnoreCase("air")) {
													new ReplacerSwitchChest().placeChestsOffAir(player, d, g);
													return true;
												}
											} else {
												new ReplacerSwitchChest().placeChestsOff(player, d, g);
												return true;
											}
										}
									}
									new ReplacerSwitchChest().placeChestsOn(player, d, g);
									return true;
								}).orElse(false)
						)
						.orElse(false);
				}
				return false;
			}

			private final List<BlockFace> DIRECTIONS = Arrays.asList(BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST);
			private final List<TBLTGem> GEMS = Arrays.asList(TBLTGem.GREEN_GEM, TBLTGem.RED_GEM, TBLTGem.BLUE_GEM);

		},


		ARENA_CREATE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("create", "c")), "<Arena>", "Create Arena", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player, String[] args) {
				List<String> options = takeOptions(args);
				if(0 < options.size()) {
					TBLTArena arena = TBLTArena.create(options.get(0), player);
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

		ARENA_UPDATE_RELOAD_HERE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("updatereloadhere", "urh")), "", "Update and reload an arena where you are", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player, String[] args) {
				return new TBLTArenaMap().findUnique(player.getLocation())
					.map(arena -> {
						arena.save();
						arena.load();
						return true;
					}).orElse(false);
			}
		},

		ARENA_RENAME_HERE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("renamehere")), "", "Rename an arena where you are", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player, String[] args) {
				List<String> options = takeOptions(args);
				if(0 < options.size()) {
					return new TBLTArenaMap().findUnique(player.getLocation())
						.map(arena -> {
							if(arena.rename(options.get(0))) {
								arena.save();
								arena.load();
								return true;
							}
							return false;
						}).orElse(false);
					}
				return false;
				}
		},

		ARENA_REMOVE_HERE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("removehere")), "", "Remove arena where you are", PermissionEnum.OP) {
		@Override
		public boolean perform(Player player, String[] args) {
			TBLTArenaMap arenas = new TBLTArenaMap();
			List<Boolean> success = new TBLTArenaMap().find(player.getLocation()).stream()
				.map(a -> {
					arenas.remove(a);
					a.delete();
					return true;
				})
				.collect(Collectors.toList());
			return success.contains(true);
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

		ARENA_SET_WALLS(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("setwallshere", "swh")), "<Material>", "Set Walls", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				List<String> options = takeOptions(args);
				if(0 < options.size()) {
					return new TBLTArenaMap().findUnique(player.getLocation())
						.map(arena -> (TBLTArena) arena)
						.flatMap(arena -> {
							String arg = options.get(0);
							return Stream.of(Material.values())
								.filter(m -> m.name().equalsIgnoreCase(arg))
								.findFirst()
								.map(m -> {
									arena.placeWallsFloorCorner(m);
									return true;
								});
						}).orElse(false);
				}
				return false;
			}
		},


		ARENA_JOIN_HERE(Arrays.asList(Arrays.asList("arena", "a"), Arrays.asList("joinhere", "jh")), "", "Join arena where you are", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				return new TBLTArenaMap().findUnique(player.getLocation())
					.map(arena -> (TBLTArena) arena)
					.map(arena -> {
						arena.join(Arrays.asList(player));
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
						TBLTPlayerGUI.ARENA.create(IconCreatorUtility.getIcons((TBLTArena) arena)).ifPresent(player::openInventory);;
						return true;
					}).orElse(false);
			}
		},


		STRUCTURE_GENERATE(Arrays.asList(Arrays.asList("structure"), Arrays.asList("generate", "g")), "<STRUCTURE>", "Generate a structure", PermissionEnum.OP) {

			private final List<KotobaStructure> STRUCTURES = Stream.of(Stream.of(InteractiveStructure.values())).flatMap(s -> s).collect(Collectors.toList());

			@Override
			public boolean perform(Player player , String[] args) {
				List<String> options = takeOptions(args);
				if(0 < options.size()) {
					Boolean success = STRUCTURES.stream()
						.filter(s -> s.getName().equalsIgnoreCase(options.get(0)))
						.findFirst()
						.map(s -> {
							s.generate(player);
							return true;
						}).orElse(false);
					if(!success) {
						String message = "Structures: " + String.join(", ", STRUCTURES.stream().map(s -> s.getName()).collect(Collectors.toList()));
						player.sendMessage(message);
					}
					return success;
				}
				return false;
			}
		},


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


		//Util
		DIRECTION(Arrays.asList(Arrays.asList("util", "utility", "u"), Arrays.asList("direction", "d")), "", "Getting Direction Item", PermissionEnum.OP) {
			@Override
			public boolean perform(Player player , String[] args) {
				Vector d = player.getLocation().getDirection();
				List<String> lore = Stream.of(d.getX(), d.getY(), d.getZ())
					.map(value -> String.valueOf(value))
					.collect(Collectors.toList());

				ItemStack icon = TBLTItemStackIcon.DIRECTION.create(64);
				ItemMeta meta = icon.getItemMeta();
				meta.setLore(lore);
				icon.setItemMeta(meta);

				player.getInventory().addItem(icon);
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
