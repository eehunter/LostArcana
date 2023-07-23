package com.oyosite.ticon.lostarcana.datagen

import com.oyosite.ticon.lostarcana.LostArcana
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import java.util.concurrent.CompletableFuture

class ArcanaBlockTagProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : BlockTagProvider(output, registriesFuture) {
    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        getOrCreateTagBuilder(LostArcana.VALID_CRYSTAL_GROWTH_BASES).addOptionalTag(BlockTags.BASE_STONE_OVERWORLD).addOptionalTag(BlockTags.STONE_BRICKS).addOptionalTag(BlockTags.DEEPSLATE_ORE_REPLACEABLES)

    }
}