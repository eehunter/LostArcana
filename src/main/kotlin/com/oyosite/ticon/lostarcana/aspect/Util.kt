package com.oyosite.ticon.lostarcana.aspect

import net.minecraft.item.ItemStack


@Suppress("CAST_NEVER_SUCCEEDS")
val ItemStack.essentia get() = (this as EssentiaProvider).essentia