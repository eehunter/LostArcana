package com.oyosite.ticon.lostarcana.item

import com.oyosite.ticon.lostarcana.aspect.Aspect
import net.minecraft.item.ItemStack

interface EssentiaItem {
    fun getAspects(stack: ItemStack): List<Pair<Aspect,Int>>?
}