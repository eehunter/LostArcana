package com.oyosite.ticon.lostarcana.block.entity

import com.oyosite.ticon.lostarcana.LostArcana
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class RunicMatrixBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(LostArcana.RUNIC_MATRIX_BLOCK_ENTITY, pos, state) {

    var active = true; private set
    var crafting = false; private set

}