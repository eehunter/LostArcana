package com.oyosite.ticon.lostarcana.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class ArcanaItemTagProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>): ItemTagProvider(output, registriesFuture) {
    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        getOrCreateTagBuilder(GOLD_INGOTS).add(Items.GOLD_INGOT)
        getOrCreateTagBuilder(GLASS_PANES).addOptionalTag(VANILLA_GLASS_PANES)
        getOrCreateTagBuilder(FEATHERS).add(Items.FEATHER)
    }
}