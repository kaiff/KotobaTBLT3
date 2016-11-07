package com.github.orgs.kotobaminers.kotobatblt3.ability;

//enum InvestigationBlock {} {
//	SMOOTH_BRICKS(Material.SMOOTH_BRICK, (short) 2, "Cracked stone bricks", Arrays.asList("It's old.")),
//	;
//	private Material material;
//	private short data;
//	private String name;
//	private List<String> lore;
//	
//	private InvestigationBlock(Material material, short data, String name, List<String> lore) {
//		this.material = material;
//		this.data = data;
//		this.name= name;
//		this.lore = lore;
//	}
//	
//	public Inventory getInventory() {
//		Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST, "Investigation!");
//
//		ItemStack block = new ItemStack(this.material, 1, this.data);
//		ItemMeta blockMeta = block.getItemMeta();
//		blockMeta.setDisplayName(name);
//		blockMeta.setLore(lore);
//		block.setItemMeta(blockMeta);			
//		
//		inventory.addItem(block);
//		return inventory;
//	}
//	
//	public static Optional<InvestigationBlock> findBlock(Material material, short data) {
//		return Stream.of(InvestigationBlock.values())
//			.filter(block -> block.material.equals(material) && block.data == data)
//			.findFirst();
//	}
//	
//	public static void breakBlock(PlayerInteractEvent event) {
//		event.getClickedBlock().setType(Material.AIR);
//	}
//}	
