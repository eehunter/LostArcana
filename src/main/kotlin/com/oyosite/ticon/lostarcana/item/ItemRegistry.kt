package com.oyosite.ticon.lostarcana.item

import com.oyosite.ticon.lostarcana.registry.AutoRegistry
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.registry.Registries

/**
 * All Item vals in this class will be automatically registered
 *
 * */
@Suppress("unused")
object ItemRegistry : AutoRegistry<Item>(Item::class.java, Registries.ITEM){

    val SALIS_MUNDIS = SalisMundisItem()
    val VIS_CRYSTAL = Item{}






    fun Item(settings: FabricItemSettings.()->Unit): Item = Item(FabricItemSettings().apply(settings))
}