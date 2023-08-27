package com.oyosite.ticon.lostarcana.datagen

import com.oyosite.ticon.lostarcana.aspect.AspectRegistry
import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.config.ThaumometerUIConfig
import com.oyosite.ticon.lostarcana.item.ItemRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.util.DyeColor
import kotlin.reflect.full.declaredMemberProperties

class ArcanaEnglishLangProvider(generator: FabricDataOutput): FabricLanguageProvider(generator, "en_us") {
    override fun generateTranslations(tb: TranslationBuilder) {
        tb.add(BlockRegistry.ARCANE_WORKBENCH, "Arcane Workbench")
        tb.add(BlockRegistry.CRUCIBLE, "Crucible")
        tb.add(BlockRegistry.NITOR, "Nitor")
        tb.add(ItemRegistry.SALIS_MUNDIS, "Salis Mundis")
        tb.add(ItemRegistry.VIS_CRYSTAL, "Vis Crystal")
        tb.add(BlockRegistry.GROWING_VIS_CRYSTAL, "Vis Crystal Seed")
        tb.add(ItemRegistry.THAUMOMETER, "Thaumometer")
        tb.add(ItemRegistry.THAUMONOMICON, "Thaumonomicon")
        tb.add(BlockRegistry.ARCANE_STONE, "Arcane Stone")
        tb.add(BlockRegistry.ARCANE_STONE_TILES, "Arcane Stone Tiles")
        tb.add(BlockRegistry.ARCANE_STONE_STAIRS, "Arcane Stone Stairs")
        tb.add(BlockRegistry.ARCANE_STONE_TILE_STAIRS, "Arcane Stone Tiles Stairs")

        AspectRegistry.ASPECTS.values.forEach(tb::add)
        DyeColor.values().forEach { tb.add("nitor.color.${it.name.lowercase()}", it.name.lowercase().capitalize()) }
        tb.add("itemGroup.lostarcana.items", "Lost Arcana Items")
        tb.add("tooltip.lostarcana.growing_vis_crystal.creative_only", "Not available in survival.")

        tb.add("text.autoconfig.lostarcana.option.clientConfig.thaumometer", "Thaumometer Options")
        

        ThaumometerUIConfig::class.declaredMemberProperties.forEach{
            val name = it.name.split(Regex("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])")).map(String::capitalize).joinToString(" ")
            tb.add("text.autoconfig.lostarcana.option.clientConfig.thaumometer.${it.name}", name)
        }
    }
}