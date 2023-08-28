package com.oyosite.ticon.lostarcana.block

import com.oyosite.ticon.lostarcana.block.entity.ArcanePedestalBlockEntity
import com.oyosite.ticon.lostarcana.block.entity.ArcaneWorkbenchBlockEntity
import com.oyosite.ticon.lostarcana.block.entity.ResearchTableBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World

class ArcanePedestalBlock: BlockWithEntity(FabricBlockSettings.create().nonOpaque()) {


    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = ArcanePedestalBlockEntity(pos, state)

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        val be = world.getBlockEntity(pos).let{if(it is ArcanePedestalBlockEntity) it else return super.onUse(state, world, pos, player, hand, hit)}
        if(!player.getStackInHand(hand).isEmpty){
            if(be.stack.isEmpty){
                be.stack = player.getStackInHand(hand).copy().apply{count = 1}
                if(!player.isCreative)player.getStackInHand(hand).decrement(1)
            } else {
                player.giveItemStack(be.stack)
                be.stack = ItemStack.EMPTY
            }
        } else {
            if(!be.stack.isEmpty){
                player.setStackInHand(hand, be.stack)
                be.stack = ItemStack.EMPTY
            }
        }
        return ActionResult.SUCCESS
    }

    override fun isTransparent(state: BlockState?, world: BlockView?, pos: BlockPos?): Boolean = true

    override fun getRenderType(state: BlockState?): BlockRenderType = BlockRenderType.ENTITYBLOCK_ANIMATED

    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean
    ) {
        if (state.block !== newState.block) {
            val blockEntity = world.getBlockEntity(pos)
            if (blockEntity is ArcaneWorkbenchBlockEntity) {
                ItemScatterer.spawn(world, pos, blockEntity)
                world.updateComparators(pos, this)
            }
            super.onStateReplaced(state, world, pos, newState, moved)
        }
    }
}