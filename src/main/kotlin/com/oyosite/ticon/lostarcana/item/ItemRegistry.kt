package com.oyosite.ticon.lostarcana.item

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.aspect.Aspect
import com.oyosite.ticon.lostarcana.aspect.AspectRegistry
import com.oyosite.ticon.lostarcana.registry.AutoRegistry
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text

/**
 * All Item vals in this class will be automatically registered
 *
 * */
@Suppress("unused")
object ItemRegistry : AutoRegistry<Item>(Item::class.java, Registries.ITEM){

    val LOST_ARCANA_ITEMS: ItemGroup = Registry.register(Registries.ITEM_GROUP, LostArcana.id("lostarcana_items"), FabricItemGroup.builder().icon { ItemStack(SALIS_MUNDIS) }
        .displayName(Text.translatable("itemGroup.lostarcana.items")).entries { displayContext, entries ->
            entries.addAll(AspectRegistry.ASPECTS.values.map(Aspect::crystal))
        }.build())

    val SALIS_MUNDIS = SalisMundisItem()
    val VIS_CRYSTAL = VisCrystalItem()






    fun Item(settings: FabricItemSettings.()->Unit): Item = Item(FabricItemSettings().apply(settings))
}