package com.oyosite.ticon.lostarcana.datagen

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.item.ItemRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Blocks
import net.minecraft.data.client.*
import net.minecraft.item.ItemConvertible
import net.minecraft.util.Identifier
import java.util.*

class ArcanaModelGenerator(generator: FabricDataOutput): FabricModelProvider(generator) {
    override fun generateBlockStateModels(bmg: BlockStateModelGenerator) {
        bmg.registerSingleton(BlockRegistry.ARCANE_WORKBENCH, arcaneWorkbenchTextureMap, Models.CUBE)
        bmg.registerSingleton(BlockRegistry.CRUCIBLE, crucibleTextureMap, block("cauldron", TextureKey.INSIDE, TextureKey.PARTICLE, TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE) )
        bmg.registerSimpleCubeAll(BlockRegistry.ARCANE_STONE)
        bmg.registerSimpleCubeAll(BlockRegistry.ARCANE_STONE_TILES)
        bmg.registerSingleton(BlockRegistry.WOODEN_TABLE, TextureMap().put(TEX_ZERO, LostArcana.id("wooden_table")), Model(Optional.of(LostArcana.id("block/table")), Optional.empty(), TEX_ZERO))
        bmg.registerSingleton(BlockRegistry.RESEARCH_TABLE, TextureMap().put(TEX_ZERO, LostArcana.id("research_table")), Model(Optional.of(LostArcana.id("block/table")), Optional.empty(), TEX_ZERO))

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


    private fun block(parent: String, vararg requiredTextureKeys: TextureKey): Model = Model(Optional.of(Identifier(parent).withPrefixedPath("block/")), Optional.empty(), *requiredTextureKeys)

    override fun generateItemModels(img: ItemModelGenerator) {
        img.register(ItemRegistry.SALIS_MUNDIS, Models.GENERATED)
        img.register(ItemRegistry.VIS_CRYSTAL, Models.GENERATED)
        img.register(BlockRegistry.GROWING_VIS_CRYSTAL, Models.GENERATED)
        img.register(ItemRegistry.THAUMOMETER, Models.GENERATED_TWO_LAYERS, "_frame", "_lens")
        img.register(BlockRegistry.NITOR, Models.GENERATED_TWO_LAYERS, "_flame", "_dot")
        img.register(ItemRegistry.THAUMONOMICON, Models.GENERATED)
        img.register(ItemRegistry.SCRIBING_TOOLS, Models.GENERATED)
        //img.register(BlockRegistry.WOODEN_TABLE, Model(Optional.of(LostArcana.id("table"))))
    }

    companion object{
        val TEXTURE_LAYERS = listOf(TextureKey.LAYER0, TextureKey.LAYER1, TextureKey.LAYER2)
        val TEX_ZERO = TextureKey.of("0")
    }

    fun ItemModelGenerator.register(item: ItemConvertible, model: Model, geckoTextureId: Identifier) = model.upload(ModelIds.getItemModelId(item.asItem()), TextureMap().put(TextureKey.of("0"), geckoTextureId), this.writer)

    fun ItemModelGenerator.register(item: ItemConvertible, model: Model, vararg suffixes: String = arrayOf("")){
        if(suffixes.size !in 1..3) throw IllegalArgumentException("Item model needs between 1 and 3 layers, inclusively.")
        val textureMap = TextureMap().apply{
            suffixes.forEachIndexed { index, s -> put(TEXTURE_LAYERS[index], TextureMap.getSubId(item.asItem(), s))
                //println(TEXTURE_LAYERS[index])
                //println(TextureMap.getSubId(item.asItem(), s))
            }
        }
            /*when(suffixes.size){
            1 -> TextureMap.layer0(LostArcana.id(suffixes[0]))
            2 -> TextureMap.layered()
        }*/
        model.upload(ModelIds.getItemModelId(item.asItem()), textureMap, writer)
    }

}