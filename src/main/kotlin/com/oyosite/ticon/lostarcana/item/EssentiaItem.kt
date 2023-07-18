package com.oyosite.ticon.lostarcana.item

import com.oyosite.ticon.lostarcana.aspect.Aspect
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

/**
 * Used for [Item]s whose essentia values are determined by NBT data, such as [VisCrystalItem]s.
 * */
interface EssentiaItem {
    fun getAspects(stack: ItemStack): List<Pair<Aspect,Int>>?
}