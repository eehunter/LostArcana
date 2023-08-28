package com.oyosite.ticon.lostarcana.block

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.item.ItemRegistry
import com.oyosite.ticon.lostarcana.item.ScribingToolsItem
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class TableBlock(settings: FabricBlockSettings.()->Unit) : Block(FabricBlockSettings.create().apply(settings).nonOpaque()) {

    override fun isTransparent(state: BlockState?, world: BlockView?, pos: BlockPos?): Boolean = true


    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        if(state.block == BlockRegistry.WOODEN_TABLE){
            val stack = player.getStackInHand(hand)
            if(stack.item !is ScribingToolsItem)return ActionResult.PASS
            if(!player.canModifyBlocks() || !player.canModifyAt(world, pos))return ActionResult.PASS
            world.setBlockState(pos, BlockRegistry.RESEARCH_TABLE.defaultState)
            world.getBlockEntity(pos, LostArcana.RESEARCH_TABLE_BLOCK_ENTITY).getOrNull()?.run{
                inv.setStack(0, stack)
                player.setStackInHand(hand, ItemStack.EMPTY)
            }
        }
        return super.onUse(state, world, pos, player, hand, hit)
    }
}