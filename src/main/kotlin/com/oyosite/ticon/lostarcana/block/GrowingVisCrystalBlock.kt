package com.oyosite.ticon.lostarcana.block

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.aspect.AspectRegistry
import com.oyosite.ticon.lostarcana.block.entity.GrowingVisCrystalBlockEntity
import com.oyosite.ticon.lostarcana.component.LostArcanaComponentEntrypoint
import com.oyosite.ticon.lostarcana.item.ItemRegistry
import com.oyosite.ticon.lostarcana.math.plus
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.AbstractBlock.Offsetter
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.ShapeContext
import net.minecraft.block.Waterloggable
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.IntProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ItemScatterer
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

class GrowingVisCrystalBlock: BlockWithEntity(FabricBlockSettings.create().collidable(false).nonOpaque().offset(visCrystalOffsetter)), Waterloggable {

    val shape = createCuboidShape(2.0, 0.0, 2.0, 14.0, 6.0, 14.0)

    override fun getRaycastShape(state: BlockState?, world: BlockView?, pos: BlockPos?): VoxelShape = shape

    override fun getOutlineShape(state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?): VoxelShape = shape

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = GrowingVisCrystalBlockEntity(pos, state)

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) { builder.add(CRYSTALS).add(WATERLOGGED) }

    override fun isTransparent(state: BlockState?, world: BlockView?, pos: BlockPos?): Boolean = true

    override fun isShapeFullCube(state: BlockState?, world: BlockView?, pos: BlockPos?): Boolean = false

    override fun getMaxHorizontalModelOffset(): Float = 7/8f

    override fun canPlaceAt(state: BlockState?, world: WorldView, pos: BlockPos): Boolean = world.getBlockState(pos.down()).isIn(LostArcana.VALID_CRYSTAL_GROWTH_BASES)

    override fun getFluidState(state: BlockState): FluidState = if(state[WATERLOGGED]) Fluids.WATER.getStill(false) else super.getFluidState(state)

    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean
    ) {
        if(moved) return
        //if(newState.block is GrowingVisCrystalBlock) return
        val count = state[CRYSTALS] - newState.getOrEmpty(CRYSTALS).getOrElse { 0 }
        if(count <= 0)return
        val blockEntity = world.getBlockEntity(pos)
        if(blockEntity is GrowingVisCrystalBlockEntity) {
            val color = blockEntity.color
            val id = blockEntity.aspect.id
            ItemScatterer.spawn(world, pos,
                DefaultedList.ofSize(1, ItemStack(ItemRegistry.VIS_CRYSTAL, count).also {
                        it.setSubNbt(
                            "vis",
                            NbtCompound().also { vis ->
                                vis.putInt("color", color);
                                vis.putString("aspect", id.toString())
                            })
                    })
            )
        }
    }

    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        super.randomTick(state, world, pos, random)
        if(random.nextDouble() > 0.05)return
        val vis = LostArcanaComponentEntrypoint.CHUNK_VIS[world.getChunk(pos)]
        if(!(vis.vis >= vis.visCap || (vis.vis > 0 && random.nextDouble() > 0.5)))return
        if(state[CRYSTALS]<6 && random.nextDouble() < 0.5){
            vis.vis-=1
            world.setBlockState(pos, state.with(CRYSTALS, state[CRYSTALS]+1))
        } else {
            for(dir in Direction.shuffle(random).filter { it.horizontal != -1 }){
                val tPosBase = BlockPos(pos.plus(dir.vector))

                for(tPos in listOf(tPosBase, tPosBase.up(), tPosBase.down())){
                    if (!world.getBlockState(tPos).isReplaceable) continue
                    if (BlockRegistry.GROWING_VIS_CRYSTAL.defaultState.canPlaceAt(world, tPos)) {
                        vis.vis -= 1
                        world.setBlockState(pos, state.with(CRYSTALS, 1).with(WATERLOGGED, world.getFluidState(tPos).fluid == Fluids.WATER))
                        world.getBlockEntity(tPos, LostArcana.VIS_CRYSTAL_BLOCK_ENTITY).getOrNull()?.run{
                            world.getBlockEntity(pos, LostArcana.VIS_CRYSTAL_BLOCK_ENTITY).getOrNull()?.let{aspect = it.aspect}
                        }
                        return
                    }
                }


            }
        }
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        return defaultState.with(CRYSTALS,1).with(WATERLOGGED,ctx.world.getFluidState(ctx.blockPos).fluid == Fluids.WATER)
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

    override fun onPlaced(
        world: World,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack
    ) {
        val be = world.getBlockEntity(pos)
        if(be is GrowingVisCrystalBlockEntity){
            val vis = stack.getSubNbt("vis")?:return
            val aspect = vis.getString("aspect")?:return
            be.aspect = AspectRegistry[LostArcana.id(aspect)]?:return
        }
    }

    companion object{
        val CRYSTALS = IntProperty.of("crystals", 1, 6)
        val WATERLOGGED = Properties.WATERLOGGED

        val visCrystalOffsetter: Offsetter = Offsetter{ state, world, pos ->
            val l = MathHelper.hashCode(pos) xor MathHelper.hashCode(0, state[CRYSTALS], 0)
            val f: Float = state.block.maxHorizontalModelOffset
            val x = MathHelper.clamp(
                (((l and 0xFL).toFloat() / 15.0f).toDouble() - 0.5) * 0.5,
                (-f).toDouble(),
                f.toDouble()
            )
            val z = MathHelper.clamp(
                (((l shr 8 and 0xFL).toFloat() / 15.0f).toDouble() - 0.5) * 0.5,
                (-f).toDouble(),
                f.toDouble()
            )
            val y = ((l shr 16 and 0xFL).toFloat() / 15f).toDouble() * -.08
            Vec3d(x, y, z)
        }

    }
}