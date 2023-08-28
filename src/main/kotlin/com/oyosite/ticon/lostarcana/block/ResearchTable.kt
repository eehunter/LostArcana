package com.oyosite.ticon.lostarcana.block

import com.oyosite.ticon.lostarcana.block.entity.ResearchTableBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World

class ResearchTable: BlockWithEntity(FabricBlockSettings.create().nonOpaque()) {

    override fun isTransparent(state: BlockState?, world: BlockView?, pos: BlockPos?): Boolean = true

    override fun getRenderType(state: BlockState?): BlockRenderType = BlockRenderType.MODEL
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = ResearchTableBlockEntity(pos, state)

    override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult): ActionResult {
        val screenHandlerFactory = state.createScreenHandlerFactory(world, pos)
        screenHandlerFactory?.let(player::openHandledScreen)

        return ActionResult.SUCCESS
    }

    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean
    ) {
        if (state.block !== newState.block) {
            val blockEntity = world.getBlockEntity(pos)
            if (blockEntity is ResearchTableBlockEntity) {
                ItemScatterer.spawn(world, pos, blockEntity.inv)
                world.updateComparators(pos, this)
            }
            super.onStateReplaced(state, world, pos, newState, moved)
        }
    }


    override fun hasComparatorOutput(state: BlockState): Boolean = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int = ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos))
}