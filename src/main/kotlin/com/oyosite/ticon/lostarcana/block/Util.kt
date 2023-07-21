package com.oyosite.ticon.lostarcana.block

import com.oyosite.ticon.lostarcana.mixin.BlockSettingsAccessor
import net.minecraft.block.AbstractBlock
import net.minecraft.block.AbstractBlock.Offsetter
import java.util.*


fun AbstractBlock.Settings.offset(offsetter: Offsetter?) = also{(it as BlockSettingsAccessor).setOffsetter(Optional.ofNullable(offsetter))}