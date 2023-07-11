package com.oyosite.ticon.lostarcana.datagen

import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.item.ItemRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Blocks
import net.minecraft.data.client.*

class ArcanaModelGenerator(generator: FabricDataOutput): FabricModelProvider(generator) {
    override fun generateBlockStateModels(bmg: BlockStateModelGenerator) {
        bmg.registerSingleton(BlockRegistry.ARCANE_WORKBENCH, arcaneWorkbenchTextureMap, Models.CUBE)
    }

    val arcaneWorkbenchTextureMap = TextureMap().put(TextureKey.UP, TextureMap.getSubId(BlockRegistry.ARCANE_WORKBENCH, "_top"))
        .put(TextureKey.SIDE, TextureMap.getSubId(BlockRegistry.ARCANE_WORKBENCH, "_side"))
        .put(TextureKey.DOWN, TextureMap.getId(Blocks.OAK_PLANKS))
        .put(TextureKey.PARTICLE, TextureMap.getSubId(BlockRegistry.ARCANE_WORKBENCH, "_top"))

    override fun generateItemModels(img: ItemModelGenerator) {
        img.register(ItemRegistry.SALIS_MUNDIS, Models.GENERATED)
    }

}