package com.oyosite.ticon.lostarcana.block

import com.oyosite.ticon.lostarcana.block.entity.InfusionPillarBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView

class InfusionPillar: BlockWithEntity(FabricBlockSettings.create().nonOpaque()) {

    init{
        defaultState = defaultState.with(DIRECTION, Direction.NORTH).with(WATERLOGGED, false)
    }

    override fun isTransparent(state: BlockState?, world: BlockView?, pos: BlockPos?): Boolean = true

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val dir = ctx.horizontalPlayerFacing
        val waterlogged = ctx.world.getFluidState(ctx.blockPos).fluid == Fluids.WATER
        return defaultState.with(DIRECTION, dir).with(WATERLOGGED, waterlogged)
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState?,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos?
    ): BlockState {
        if(state[WATERLOGGED])world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        return state[DIRECTION]==Direction.DOWN || world.getBlockState(pos.up()).isReplaceable
    }

    override fun onPlaced(
        world: World,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        itemStack: ItemStack
    ) {
        if(state[DIRECTION]==Direction.DOWN)return
        world.setBlockState(pos.up(), defaultState.with(DIRECTION, Direction.DOWN).with(WATERLOGGED, world.getFluidState(pos.up()).fluid == Fluids.WATER))
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) { builder.add(DIRECTION, WATERLOGGED) }
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? = if(state[DIRECTION]==Direction.DOWN)null else InfusionPillarBlockEntity(pos, state)

    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean
    ) {
        super.onStateReplaced(state, world, pos, newState, moved)
        when(state[DIRECTION]){
            Direction.DOWN -> pos.down()
            else -> pos.up()
        }.let{if(world.getBlockState(it).block == BlockRegistry.INFUSION_PILLAR)world.setBlockState(it, Blocks.AIR.defaultState)}
    }

    override fun getRenderType(state: BlockState?): BlockRenderType = BlockRenderType.ENTITYBLOCK_ANIMATED




    companion object{
        val DIRECTION = DirectionProperty.of("pillar_direction"){it != Direction.UP}
        val WATERLOGGED = Properties.WATERLOGGED

    }
}