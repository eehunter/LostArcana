package com.oyosite.ticon.lostarcana.item

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.aspect.Aspect
import com.oyosite.ticon.lostarcana.aspect.AspectRegistry
import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.registry.AutoRegistry
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

    val LOST_ARCANA_ITEMS: ItemGroup = Registry.register(Registries.ITEM_GROUP, LostArcana.id("lostarcana_items"), FabricItemGroup.builder().icon { ItemStack(SALIS_MUNDIS) }
        .displayName(Text.translatable("itemGroup.lostarcana.items")).entries { displayContext, entries ->
            entries.addAll(listOf<Any>(
                BlockRegistry.ARCANE_WORKBENCH,
                BlockRegistry.CRUCIBLE,
                SALIS_MUNDIS,
                THAUMOMETER,
            ).map{ if(it is ItemConvertible) ItemStack(it) else if(it is ItemStack) it else throw IllegalArgumentException("Non ItemConvertible or ItemStack cannot be added to ItemGroup.")})
            entries.addAll(DyeColor.values().map { ItemStack(BlockRegistry.NITOR, 1).apply{ setSubNbt("nitor", it.toNbt) } })
            entries.addAll(AspectRegistry.PRIMAL_ASPECTS.values.map(Aspect::growing_crystal))
            entries.addAll(AspectRegistry.ASPECTS.values.map(Aspect::crystal))
        }.build())

    val SALIS_MUNDIS = SalisMundisItem()
    val VIS_CRYSTAL = VisCrystalItem()
    val THAUMOMETER = ThaumometerItem()





    val DyeColor.toNbt: NbtCompound get() = NbtCompound().also{it.putString("color", name.lowercase())}

    fun Item(settings: FabricItemSettings.()->Unit): Item = Item(FabricItemSettings().apply(settings))
}