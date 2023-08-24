package com.oyosite.ticon.lostarcana.block.entity

import com.oyosite.ticon.lostarcana.aspect.AspectRegistry
import com.oyosite.ticon.lostarcana.block.InfusionDataProvider
import com.oyosite.ticon.lostarcana.item.VisCrystalItem
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

fun Int.testCrystalInSlot(crystal: ItemStack):Boolean =
    crystal.item is VisCrystalItem && AspectRegistry.ASPECTS.keys.toList()[this].toString() == crystal.getSubNbt("vis")?.getString("aspect")

fun Slot(inventory: Inventory, index: Int, x: Int, y: Int, markDirtyCallback: ()->Unit) = object : Slot(inventory, index, x, y){
    override fun markDirty() {
        super.markDirty()
        markDirtyCallback()
    }
}

fun World.getInfusionDataProviders(pos: BlockPos): List<InfusionDataProvider>?{
    val providers = mutableListOf<InfusionDataProvider>()
    getBlockState(pos).block.let { if (it is InfusionDataProvider) providers.add(it) }
    getBlockEntity(pos)?.let{ if(it is InfusionDataProvider) providers.add(it) }

    return if(providers.isEmpty())null else providers
}