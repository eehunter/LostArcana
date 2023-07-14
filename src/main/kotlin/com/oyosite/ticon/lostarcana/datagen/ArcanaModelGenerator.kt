package com.oyosite.ticon.lostarcana.datagen

import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.item.ItemRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Blocks
import net.minecraft.data.client.*
import net.minecraft.util.Identifier
import java.util.*

class ArcanaModelGenerator(generator: FabricDataOutput): FabricModelProvider(generator) {
    override fun generateBlockStateModels(bmg: BlockStateModelGenerator) {
        bmg.registerSingleton(BlockRegistry.ARCANE_WORKBENCH, arcaneWorkbenchTextureMap, Models.CUBE)
        bmg.registerSingleton(BlockRegistry.CRUCIBLE, crucibleTextureMap, block("cauldron", TextureKey.INSIDE, TextureKey.PARTICLE, TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE) )
    }

    val arcaneWorkbenchTextureMap = TextureMap().put(TextureKey.UP, TextureMap.getSubId(BlockRegistry.ARCANE_WORKBENCH, "_top"))
        .put(TextureKey.SIDE, TextureMap.getSubId(BlockRegistry.ARCANE_WORKBENCH, "_side"))
        .put(TextureKey.DOWN, TextureMap.getId(Blocks.OAK_PLANKS))
        .put(TextureKey.PARTICLE, TextureMap.getSubId(BlockRegistry.ARCANE_WORKBENCH, "_top"))

    val crucibleTextureMap = TextureMap()
        .put(TextureKey.PARTICLE, TextureMap.getSubId(BlockRegistry.CRUCIBLE, "_side"))
        .put(TextureKey.SIDE, TextureMap.getSubId(BlockRegistry.CRUCIBLE, "_side"))
        .put(TextureKey.TOP, TextureMap.getSubId(BlockRegistry.CRUCIBLE, "_top"))
        .put(TextureKey.BOTTOM, TextureMap.getSubId(BlockRegistry.CRUCIBLE, "_bottom"))
        .put(TextureKey.INSIDE, TextureMap.getSubId(BlockRegistry.CRUCIBLE, "_inner"))


    private fun block(parent: String, vararg requiredTextureKeys: TextureKey): Model = Model(Optional.of(Identifier("minecraft", "block/$parent")), Optional.empty(), *requiredTextureKeys)

    override fun generateItemModels(img: ItemModelGenerator) {
        img.register(ItemRegistry.SALIS_MUNDIS, Models.GENERATED)
        img.register(ItemRegistry.VIS_CRYSTAL, Models.GENERATED)
    }

}