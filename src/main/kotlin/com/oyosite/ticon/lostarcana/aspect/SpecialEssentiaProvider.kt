package com.oyosite.ticon.lostarcana.aspect

import net.minecraft.item.ItemStack

interface SpecialEssentiaProvider<T> {
    fun getEssentia(context: T): Map<Aspect, Long>


}