package com.oyosite.ticon.lostarcana.block

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.minecraft.item.ItemStack

@Suppress("UnstableApiUsage")
interface InfusionDataProvider {
    val itemStacks: List<ItemStack>? get() = null

    val essentiaStorage: Storage<FluidVariant>? get() = null

    fun stability(other: InfusionDataProvider?): Double{
        if(other==null)return -1.0
        return if(other==this) 1.0 else 0.0
    }
}