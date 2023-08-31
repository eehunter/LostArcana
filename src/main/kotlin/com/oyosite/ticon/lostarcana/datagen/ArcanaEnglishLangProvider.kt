package com.oyosite.ticon.lostarcana.datagen

import com.oyosite.ticon.lostarcana.aspect.AspectRegistry
import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.block.BlockRegistry.GROWING_VIS_CRYSTAL
import com.oyosite.ticon.lostarcana.config.ThaumometerUIConfig
import com.oyosite.ticon.lostarcana.item.ItemRegistry
import com.oyosite.ticon.lostarcana.research.ResearchCategoryRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemGroup
import net.minecraft.registry.RegistryKey
import net.minecraft.util.DyeColor
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

class ArcanaEnglishLangProvider(generator: FabricDataOutput): FabricLanguageProvider(generator, "en_us") {

    val EXCLUDED_OBJECTS = mutableListOf<Any>(GROWING_VIS_CRYSTAL)

    override fun generateTranslations(tb: TranslationBuilder) {
        tb+=BlockRegistry
        tb+=ItemRegistry

        /*tb.add(BlockRegistry.ARCANE_WORKBENCH, "Arcane Workbench")
        tb.add(BlockRegistry.CRUCIBLE, "Crucible")
        tb.add(BlockRegistry.NITOR, "Nitor")
        tb.add(ItemRegistry.SALIS_MUNDIS, "Salis Mundis")
        tb.add(ItemRegistry.VIS_CRYSTAL, "Vis Crystal")*/
        tb.add(GROWING_VIS_CRYSTAL, "Vis Crystal Seed")
        /*tb.add(ItemRegistry.THAUMOMETER, "Thaumometer")
        tb.add(ItemRegistry.THAUMONOMICON, "Thaumonomicon")
        tb.add(BlockRegistry.ARCANE_STONE, "Arcane Stone")
        tb.add(BlockRegistry.ARCANE_STONE_TILES, "Arcane Stone Tiles")
        tb.add(BlockRegistry.ARCANE_STONE_STAIRS, "Arcane Stone Stairs")
        tb.add(BlockRegistry.ARCANE_STONE_TILE_STAIRS, "Arcane Stone Tiles Stairs")

        tb.add(BlockRegistry::ARCANE_PEDESTAL)
        tb.add(BlockRegistry::RESEARCH_TABLE)
        tb.add(BlockRegistry::WOODEN_TABLE)

        tb.add(ItemRegistry::SCRIBING_TOOLS)*/

        ResearchCategoryRegistry.CATEGORIES.keys.forEach { tb.add("research_category.${it.namespace}.${it.path}.name", it.path.split("_").joinToString(" ", transform = String::capitalize)) }

        AspectRegistry.ASPECTS.values.forEach(tb::add)
        DyeColor.values().forEach { tb.add("nitor.color.${it.name.lowercase()}", it.name.lowercase().capitalize()) }
        //tb.add("itemGroup.lostarcana.items", "Lost Arcana Items")
        tb.add("tooltip.lostarcana.growing_vis_crystal.creative_only", "Not available in survival.")

        tb.add("text.autoconfig.lostarcana.option.clientConfig.thaumometer", "Thaumometer Options")


        ThaumometerUIConfig::class.declaredMemberProperties.forEach{
            val name = it.name.split(Regex("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])")).map(String::capitalize).joinToString(" ")
            tb.add("text.autoconfig.lostarcana.option.clientConfig.thaumometer.${it.name}", name)
        }


    }

    operator fun TranslationBuilder.plusAssign(obj: Any) = obj.javaClass.kotlin.declaredMemberProperties.forEach { field ->
        val value = field.get(obj)
        if(EXCLUDED_OBJECTS.contains(value))return@forEach
        val name = field.name.split("_").joinToString(" ") { it.lowercase().capitalize() }

        when(value){
            is Block -> add(value, name) //Having multiple "add" calls written out may seem redundant, but these are overloaded functions, thereby making it necessary.
            is Item -> add(value, name)
            is ItemGroup -> add("itemGroup.lostarcana.${field.name.split("_").last().lowercase()}", name)
        }
    }

    @JvmName("addItemByProp")
    fun <T: Item> TranslationBuilder.add(field: KProperty0<T>) = add(field.get(), field.name.split("_").map { it.lowercase().capitalize() }.joinToString(" "))
    @JvmName("addBlockByProp")
    fun <T: Block> TranslationBuilder.add(field: KProperty0<T>) = add(field.get(), field.name.split("_").map { it.lowercase().capitalize() }.joinToString(" "))
    @JvmName("addItemGroupByProp")
    fun TranslationBuilder.add(field: KProperty0<RegistryKey<ItemGroup>>) = add(field.get(), field.name.split("_").joinToString(" ") { it.lowercase().capitalize() })

    @JvmName("addMultipleByProp")
    fun TranslationBuilder.addAll(vararg fields: KProperty0<Any>) = fields.forEach{field ->
        val value = field.get()
        val name = field.name.split("_").joinToString(" ") { it.lowercase().capitalize() }

        when(value){
            is Block -> add(value, name) //Having multiple "add" calls written out may seem redundant, but these are overloaded functions, thereby making it necessary.
            is Item -> add(value, name)
        }
    }
}