package com.github.orgs.kotobaminers.kotobatblt3.ability;

//public enum BlockBreakAbility {} implements ItemAbilityInterface {
//	SMOOTH(Material.SMOOTH_BRICK, (short) 2, Material.GOLD_PICKAXE, "Golden Pickaxe", Arrays.asList("lore")),
//	;
//	
//	private Material blockMaterial;
//	private short data;
//	private Material breakerMaterial;
//	private String name;
//	private List<String> lore;
//	private static List<Action> triggers = Arrays.asList(Action.RIGHT_CLICK_BLOCK);
//	
//	private BlockBreakAbility(Material material, short data, Material breaker, String name, List<String> lore) {
//		this.blockMaterial = material;
//		this.data = data;
//		this.breakerMaterial = breaker;
//		this.name = name;
//		this.lore = lore;
//	}
//	
//	@Override
//	public List<Action> getTriggers() {
//		return triggers;
//	}
//	
//	public Optional<BlockBreakAbility> find(PlayerInteractEvent event) {
//		Stream.of(BlockBreakAbility.values())
//			.filter(ability ->
//				ability.blockMaterial.equals(event.getClickedBlock()) &&
//				ability.breakerMaterial.equals(event.getPlayer().getItemInHand())
//				);
//		return Optional.empty();
//	}
//	
//	private ItemStack createBlockIcon() {
//		return new ItemStack(blockMaterial);
//	}
//	private ItemStack createBreakerIcon() {
//		return new ItemStack(breakerMaterial);
//	}
//
//	@Override
//	public boolean canPerform(Action action) {
//		return getTriggers().contains(action);
//	}
//
//	@Override
//	public void perform(PlayerInteractEvent event) {
//		event.getClickedBlock().setType(Material.AIR);
//	}
//	@Override
//	public short getData() {
//		return data;
//	}
//	@Override
//	public Material getMaterial() {
//		return blockMaterial;
//	}
//	@Override
//	public String getDisplayName() {
//		return name;
//	}
//	@Override
//	public List<String> getLore() {
//		return lore;
//	}
//}
