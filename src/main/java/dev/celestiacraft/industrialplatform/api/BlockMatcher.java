package dev.celestiacraft.industrialplatform.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class BlockMatcher {
	public static boolean matches(BlockState state, ForgeConfigSpec.ConfigValue<List<? extends String>> configValue) {
		List<? extends String> entries = configValue.get();

		for (String entry : entries) {
			if (entry.startsWith("#")) {
				String tagId = entry.substring(1);
				ResourceLocation loc = ResourceLocation.tryParse(tagId);

				if (loc != null) {
					TagKey<Block> tag = BlockTags.create(loc);
					if (state.is(tag)) {
						return true;
					}
				}
			} else {
				ResourceLocation location = ResourceLocation.tryParse(entry);
				Block block = location == null ? null : ForgeRegistries.BLOCKS.getValue(location);
				if (block != null && state.is(block)) {
					return true;
				}
			}
		}
		return false;
	}
}
