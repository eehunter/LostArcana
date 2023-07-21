package com.oyosite.ticon.lostarcana.block.entity

import com.oyosite.ticon.lostarcana.aspect.AspectRegistry
import com.oyosite.ticon.lostarcana.item.VisCrystalItem
import net.minecraft.item.ItemStack

fun Int.testCrystalInSlot(crystal: ItemStack):Boolean =
    crystal.item is VisCrystalItem && AspectRegistry.ASPECTS.keys.toList()[this].toString() == crystal.getSubNbt("vis")?.getString("aspect")

