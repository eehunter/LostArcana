package com.oyosite.ticon.lostarcana.block

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.block.entity.RunicMatrixBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class RunicMatrixBlock: BlockWithEntity(FabricBlockSettings.create().luminance(5).allowsSpawning { state, world, pos, type -> false }) {
    val voxelShape = createCuboidShape(0.75, 0.75, 0.75, 15.25, 15.25, 15.25)

    override fun getRaycastShape(state: BlockState?, world: BlockView?, pos: BlockPos?): VoxelShape = voxelShape
    override fun getOutlineShape(state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?): VoxelShape = voxelShape


    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = RunicMatrixBlockEntity(pos, state)

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult = world.getBlockEntity(pos, LostArcana.RUNIC_MATRIX_BLOCK_ENTITY).getOrNull()?.onUse(state, world, pos, player, hand, hit)?:ActionResult.PASS

}