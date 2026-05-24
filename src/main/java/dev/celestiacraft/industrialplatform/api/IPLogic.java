package dev.celestiacraft.industrialplatform.api;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraftforge.registries.ForgeRegistries;
import dev.celestiacraft.industrialplatform.config.CommonConfig;

import java.util.Optional;

public class IPLogic {
	private static final String MODID = "industrial_platform";

	public static void placeStructure(ServerLevel level, int x, int y, int z, String structureId) {
		StructureTemplateManager manager = level.getStructureManager();
		ResourceLocation structureName = loadResource(structureId);
		Optional<StructureTemplate> template = manager.get(structureName);
		template.ifPresent((temp) -> {
			temp.placeInWorld(
					level,
					new BlockPos(x, y, z),
					new BlockPos(x, y, z),
					createSafePlaceSettings(level),
					level.random,
					3
			);
		});
	}

	public static void placeExtendedStructure(ServerLevel level, int x, int y, int z, String structureId) {
		StructureTemplateManager manager = level.getStructureManager();
		ResourceLocation structureName = loadResource(structureId);
		Optional<StructureTemplate> template = manager.get(structureName);
		for (int i = x - 16; i <= x + 16; i = i + 16) {
			for (int j = z - 16; j <= z + 16; j = j + 16) {
				int finalI = i;
				int finalJ = j;
				template.ifPresent((temp) -> {
					temp.placeInWorld(
							level,
							new BlockPos(finalI, y, finalJ),
							new BlockPos(finalI, y, finalJ),
							createSafePlaceSettings(level),
							level.random,
							3
					);
				});
			}
		}
	}

	private static StructurePlaceSettings createSafePlaceSettings(ServerLevel level) {
		return new StructurePlaceSettings()
				.setRotation(Rotation.NONE)
				.setMirror(Mirror.NONE)
				.setIgnoreEntities(false)
				.addProcessor(new DropBeforePlaceProcessor(level));
	}

	public static void fillArea(ServerLevel level, int x0, int y0, int z0, int x1, int y1, int z1) {
		for (int x = x0; x <= x1; x++) {
			for (int y = y0; y <= y1; y++) {
				for (int z = z0; z <= z1; z++) {
					destroyIfBreakable(level, new BlockPos(x, y, z));
				}
			}
		}
	}

	public static void fillAreaConditional(ServerLevel level, int x0, int y0, int z0, int x1, int y1, int z1) {
		for (int x = x0; x <= x1; x++) {
			for (int y = y0; y <= y1; y++) {
				for (int z = z0; z <= z1; z++) {
					BlockPos pos = new BlockPos(x, y, z);
					fillStoneIfOpen(level, pos);
				}
			}
		}
	}

	public static void consumeItem(Player player, ItemStack stack, InteractionHand hand) {
		player.swing(hand);

		if (!player.isCreative()) {
			stack.shrink(1);
		}
	}

	private static boolean isUnbreakable(ServerLevel level, BlockPos pos, BlockState state) {
		return state.getDestroySpeed(level, pos) < 0.0F;
	}

	private static boolean destroyIfBreakable(ServerLevel level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		if (state.isAir() || isUnbreakable(level, pos, state)) {
			return false;
		}

		level.destroyBlock(pos, shouldDrop(state));
		return true;
	}

	private static boolean fillStoneIfOpen(ServerLevel level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		if (!state.isAir() && !(state.getBlock() instanceof LiquidBlock)) {
			return false;
		}

		level.setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState());
		return true;
	}

	private static boolean clearForTemplateAir(ServerLevel level, BlockPos pos, BlockState state) {
		if (state.isAir() || isUnbreakable(level, pos, state)) {
			return false;
		}

		level.destroyBlock(pos, shouldDrop(state));
		return true;
	}

	private static boolean shouldDrop(BlockState state) {
		ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(state.getBlock());
		return !loadResource("industrial_platform").equals(blockId)
				&& !loadResource("fluid_pool").equals(blockId)
				&& !BlockMatcher.matches(state, CommonConfig.NO_DROP_BLOCKS);
	}

	private static ResourceLocation loadResource(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}

	private static class DropBeforePlaceProcessor extends StructureProcessor {
		private final ServerLevel level;

		private DropBeforePlaceProcessor(ServerLevel level) {
			this.level = level;
		}

		@Override
		public StructureTemplate.StructureBlockInfo processBlock(
				LevelReader levelReader,
				BlockPos offset,
				BlockPos pos,
				StructureTemplate.StructureBlockInfo originalBlockInfo,
				StructureTemplate.StructureBlockInfo currentBlockInfo,
				StructurePlaceSettings settings
		) {
			BlockPos targetPos = currentBlockInfo.pos();
			BlockState targetState = level.getBlockState(targetPos);

			if (isUnbreakable(level, targetPos, targetState)) {
				return null;
			}

			if (currentBlockInfo.state().isAir()) {
				clearForTemplateAir(level, targetPos, targetState);
				return currentBlockInfo;
			}

			if (!targetState.isAir()) {
				level.destroyBlock(targetPos, shouldDrop(targetState));
			}

			return currentBlockInfo;
		}

		@Override
		protected StructureProcessorType<?> getType() {
			return StructureProcessorType.NOP;
		}
	}
}
