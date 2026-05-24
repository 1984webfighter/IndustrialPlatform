package dev.celestiacraft.industrialplatform.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class CommonConfig {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> TRIGGER_BLOCK;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ADJUSTER;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> NO_DROP_BLOCKS;
	public static final ForgeConfigSpec.IntValue TOP_FILLING_DISTANCE;
	public static final ForgeConfigSpec.IntValue BOTTOM_FILLING_DISTANCE;

	static {
		BUILDER.comment("All settings below will only take effect after restarting the server or client.")
				.push("general");

		TRIGGER_BLOCK = BUILDER
				.comment("Items that can trigger platform placement (right-click).")
				.comment("Use \"#namespace:path\" for tags, \"namespace:path\" for item IDs.")
				.comment("Examples: \"#forge:stone\", \"minecraft:cobblestone\"")
				.defineListAllowEmpty(
						"trigger_block",
						List.of("#forge:stone","minecraft:cobblestone"),
						CommonConfig::validateString
				);

		ADJUSTER = BUILDER
				.comment("Items that can adjust platform mode (right-click) and show boundary preview (hold).")
				.comment("Use \"#namespace:path\" for tags, \"namespace:path\" for item IDs.")
				.comment("Examples: \"#forge:tools/wrench\", \"minecraft:stick\"")
				.defineListAllowEmpty(
						"adjuster",
						List.of("#forge:tools/wrench", "minecraft:stick"),
						CommonConfig::validateString
				);

		NO_DROP_BLOCKS = BUILDER
				.comment("Blocks that should be destroyed without drops during platform deployment.")
				.comment("Use \"#namespace:path\" for block tags, \"namespace:path\" for block IDs.")
				.comment("Defaults are driven by the industrial_platform:no_drop_blocks block tag.")
				.comment("Examples: \"#industrial_platform:no_drop_blocks\", \"#forge:stone\", \"minecraft:dirt\"")
				.defineListAllowEmpty(
						"no_drop_blocks",
						List.of("#industrial_platform:no_drop_blocks"),
						CommonConfig::validateString
				);

		TOP_FILLING_DISTANCE = BUILDER
				.comment("The distance that the platform fills on its top")
				.comment("type: int")
				.comment("default: 5")
				.defineInRange("top_filling_distance", 5, 0, 64);

		BOTTOM_FILLING_DISTANCE = BUILDER
				.comment("The distance that the platform fills on its bottom")
				.comment("type: int")
				.comment("default: 5")
				.defineInRange("bottom_filling_distance", 5, 0, 64);

		BUILDER.pop();
	}

	private static boolean validateString(Object object) {
		return object instanceof String;
	}

	public static final ForgeConfigSpec SPEC = BUILDER.build();
}