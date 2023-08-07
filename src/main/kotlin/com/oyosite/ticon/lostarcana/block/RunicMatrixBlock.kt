package com.oyosite.ticon.lostarcana.block

import com.oyosite.ticon.lostarcana.block.entity.RunicMatrixBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

class RunicMatrixBlock: BlockWithEntity(FabricBlockSettings.create().luminance(5).allowsSpawning { state, world, pos, type -> false }) {
    val voxelShape = createCuboidShape(1.0, 1.0, 1.0, 15.0, 15.0, 15.0)

    override fun getRaycastShape(state: BlockState?, world: BlockView?, pos: BlockPos?): VoxelShape = voxelShape
    override fun getOutlineShape(state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?): VoxelShape = voxelShape


    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = RunicMatrixBlockEntity(pos, state)


}