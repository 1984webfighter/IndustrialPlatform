package dev.celestiacraft.industrialplatform.datagen.recipe.type;

import dev.celestiacraft.industrialplatform.IndustrialPlatform;
import dev.celestiacraft.industrialplatform.api.IPTags;
import dev.celestiacraft.industrialplatform.block.BlockRegister;
import dev.celestiacraft.industrialplatform.datagen.recipe.IPRecipeProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

import java.util.function.Consumer;

public class IPShapedRecipe extends IPRecipeProvider {
	public IPShapedRecipe(PackOutput output) {
		super(output);
	}

	public static void register(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BlockRegister.INDUSTRIAL_PLATFORM.get())
				.pattern("ACB")
				.pattern("DDD")
				.pattern("DDD")
				.define('A', Tags.Items.DYES_YELLOW)
				.define('B', Tags.Items.DYES_BLACK)
				.define('C', IPTags.Items.DEEPSLATE)
				.define('D', Tags.Items.STONE)
				.unlockedBy("stone", has(Tags.Items.STONE))
				.save(consumer, IndustrialPlatform.loadResource("platform"));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BlockRegister.INDUSTRIAL_PLATFORM.get())
				.pattern("ACB")
				.pattern("DDD")
				.pattern("DDD")
				.define('A', Tags.Items.DYES_BLACK)
				.define('B', Tags.Items.DYES_YELLOW)
				.define('C', IPTags.Items.DEEPSLATE)
				.define('D', Tags.Items.STONE)
				.unlockedBy("stone", has(Tags.Items.STONE))
				.save(consumer, IndustrialPlatform.loadResource("platform2"));

		ConditionalRecipe.builder()
				.addCondition(new ModLoadedCondition("create"))
				.addRecipe((recipe) -> {
					ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BlockRegister.FLUID_POOL.get())
							.pattern("A B")
							.pattern("D D")
							.pattern("DCD")
							.define('A', Tags.Items.DYES_BLACK)
							.define('B', Tags.Items.DYES_YELLOW)
							.define('C', IPTags.Items.DEEPSLATE)
							.define('D', Tags.Items.STONE)
							.unlockedBy("stone", has(Tags.Items.STONE))
							.save(recipe, IndustrialPlatform.loadResource("pool"));
				})
				.build(consumer, IndustrialPlatform.loadResource("pool"));

		ConditionalRecipe.builder()
				.addCondition(new ModLoadedCondition("create"))
				.addRecipe((recipe) -> {
					ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BlockRegister.FLUID_POOL.get())
							.pattern("A B")
							.pattern("D D")
							.pattern("DCD")
							.define('A', Tags.Items.DYES_BLACK)
							.define('B', Tags.Items.DYES_YELLOW)
							.define('C', IPTags.Items.DEEPSLATE)
							.define('D', Tags.Items.STONE)
							.unlockedBy("stone", has(Tags.Items.STONE))
							.save(recipe, IndustrialPlatform.loadResource("pool2"));
				})
				.build(consumer, IndustrialPlatform.loadResource("pool2"));
	}
}