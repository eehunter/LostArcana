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
        AspectRegistry.ASPECTS.values.forEach(tb::add)
        DyeColor.values().forEach { tb.add("nitor.color.${it.name.lowercase()}", it.name.lowercase().capitalize()) }
        tb.add("itemGroup.lostarcana.items", "Lost Arcana Items")

        tb.add("text.autoconfig.lostarcana.option.clientConfig.thaumometer", "Thaumometer Options")

        ThaumometerUIConfig::class.declaredMemberProperties.forEach{
            val name = it.name.split(Regex("(?<=[a-z])(?=[A-Z])")).map(String::capitalize).joinToString(" ")
            println(name)
            tb.add("text.autoconfig.lostarcana.option.clientConfig.thaumometer.${it.name}", name)
        }
    }
}