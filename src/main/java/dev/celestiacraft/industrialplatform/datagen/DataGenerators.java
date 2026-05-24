package dev.celestiacraft.industrialplatform.datagen;

import dev.celestiacraft.industrialplatform.IndustrialPlatform;
import dev.celestiacraft.industrialplatform.datagen.language.LanguageGenerate;
import dev.celestiacraft.industrialplatform.datagen.language.locale.Chinese;
import dev.celestiacraft.industrialplatform.datagen.language.locale.English;
import dev.celestiacraft.industrialplatform.datagen.loot.IPLootTableProvider;
import dev.celestiacraft.industrialplatform.datagen.recipe.IPRecipeProvider;
import dev.celestiacraft.industrialplatform.datagen.tags.IPBlockTagsProvider;
import dev.celestiacraft.industrialplatform.datagen.tags.IPItemTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = IndustrialPlatform.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	@SubscribeEvent
	public static void onDatagen(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

		// Client
		LanguageGenerate.register();
		generator.addProvider(event.includeClient(), new English(output));
		generator.addProvider(event.includeClient(), new Chinese(output));

		// Server
		IPBlockTagsProvider blockTags = new IPBlockTagsProvider(output, provider, helper);
		IPItemTagsProvider itemTags = new IPItemTagsProvider(output, provider, blockTags, helper);

		generator.addProvider(event.includeServer(), blockTags);
		generator.addProvider(event.includeServer(), itemTags);
		generator.addProvider(event.includeServer(), new IPRecipeProvider(output));
		generator.addProvider(event.includeServer(), new IPLootTableProvider(output, provider));
	}
}