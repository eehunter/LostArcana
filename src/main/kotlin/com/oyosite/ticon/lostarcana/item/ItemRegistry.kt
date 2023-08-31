package com.oyosite.ticon.lostarcana.item

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.aspect.Aspect
import com.oyosite.ticon.lostarcana.aspect.AspectRegistry
import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.item.ResearchNotesItem.Companion.researchCategory
import com.oyosite.ticon.lostarcana.item.ResearchNotesItem.Companion.researchComplete
import com.oyosite.ticon.lostarcana.item.ResearchNotesItem.Companion.researchProgress
import com.oyosite.ticon.lostarcana.registry.AutoRegistry
import com.oyosite.ticon.lostarcana.research.ResearchCategoryRegistry
import net.fabricmc.fabric.api.item.v1.CustomDamageHandler
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import java.util.*

/**
 * All Item vals in this class will be automatically registered
 *
 * */
@Suppress("unused")
object ItemRegistry : AutoRegistry<Item>(Item::class.java, Registries.ITEM){

    val LOST_ARCANA_ITEMS: ItemGroup by lazy { Registry.register(Registries.ITEM_GROUP, LostArcana.id("lostarcana_items"), FabricItemGroup.builder().icon { ItemStack(SALIS_MUNDIS) }
        .displayName(Text.translatable("itemGroup.lostarcana.items")).entries { displayContext, entries ->
            entries.addAll(listOf<Any>(
                THAUMONOMICON,
                BlockRegistry.ARCANE_WORKBENCH,
                BlockRegistry.CRUCIBLE,
                SALIS_MUNDIS,
                THAUMOMETER,
                SCRIBING_TOOLS,
                BlockRegistry.ARCANE_STONE,
                BlockRegistry.ARCANE_STONE_TILES,
                BlockRegistry.ARCANE_STONE_STAIRS,
                BlockRegistry.ARCANE_STONE_TILE_STAIRS,
                BlockRegistry.WOODEN_TABLE,
                BlockRegistry.RESEARCH_TABLE,
                *ALL_RESEARCH_NOTES
            ).map{ if(it is ItemConvertible) ItemStack(it) else if(it is ItemStack) it else throw IllegalArgumentException("Non ItemConvertible or ItemStack cannot be added to ItemGroup.")})
            entries.addAll(DyeColor.values().map { ItemStack(BlockRegistry.NITOR, 1).apply{ setSubNbt("nitor", it.toNbt) } })
            entries.addAll(AspectRegistry.PRIMAL_ASPECTS.values.map(Aspect::growing_crystal))
            entries.addAll(AspectRegistry.ASPECTS.values.map(Aspect::crystal))
        }.build())}

    val SALIS_MUNDIS = SalisMundisItem()
    val VIS_CRYSTAL = VisCrystalItem()
    val THAUMOMETER = ThaumometerItem()
    val THAUMONOMICON = ThaumonomiconItem()

    val SCRIBING_TOOLS = ScribingToolsItem()//Item { maxDamage(200).customDamage(::scribingToolsDamageHandler) }

    val THEORY_NOTES = ResearchNotesItem(ResearchNotesType.THEORY)
    val OBSERVATION_NOTES = ResearchNotesItem(ResearchNotesType.OBSERVATION)
    val CELESTIAL_NOTES = ResearchNotesItem(ResearchNotesType.CELESTIAL)



    val ALL_RESEARCH_NOTES: Array<ItemStack> get(){
        val stacks = mutableListOf<ItemStack>()
        ResearchCategoryRegistry.CATEGORIES.forEach{(key, value) ->
            value.researchType.forEach {
                val stack = ItemStack(when(it){
                    ResearchNotesType.THEORY -> THEORY_NOTES
                    ResearchNotesType.OBSERVATION -> OBSERVATION_NOTES
                    ResearchNotesType.CELESTIAL -> CELESTIAL_NOTES
                })
                stack.researchCategory = value
                stack.researchProgress = 100
                stack.researchComplete
                stacks+=stack
            }
        }
        return stacks.toTypedArray()
    }

    val DyeColor.toNbt: NbtCompound get() = NbtCompound().also{it.putString("color", name.lowercase())}

    fun Item(settings: FabricItemSettings.()->Unit): Item = Item(FabricItemSettings().apply(settings))
}