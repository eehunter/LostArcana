package com.oyosite.ticon.lostarcana.item

import com.oyosite.ticon.lostarcana.aspect.Aspect
import com.oyosite.ticon.lostarcana.aspect.ItemStackEssentiaProvider
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

/**
 * Used for [Item]s whose essentia values are determined by NBT data, such as [VisCrystalItem]s.
 * */
@Deprecated("Obsolete", replaceWith = ReplaceWith("ItemStackEssentiaProvider"), DeprecationLevel.WARNING)
interface EssentiaItem : ItemStackEssentiaProvider{
    fun getAspects(stack: ItemStack): List<Pair<Aspect,Int>>?

    override fun getEssentia(context: ItemStack): Map<Aspect, Long> = getAspects(context)?.run{ associate { it.first to it.second.toLong() } }?: mapOf()
}