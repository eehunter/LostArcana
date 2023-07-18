package com.oyosite.ticon.lostarcana.fluid

import com.oyosite.ticon.lostarcana.LostArcana
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.block.BlockState
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.WorldView

@Suppress("UnstableApiUsage")
class EssentiaFluid : Fluid() {

    override fun getBucketItem(): Item? = null

    override fun canBeReplacedWith(state: FluidState?, world: BlockView?, pos: BlockPos?, fluid: Fluid?, direction: Direction?): Boolean = true

    override fun getVelocity(world: BlockView?, pos: BlockPos?, state: FluidState?): Vec3d = Vec3d.ZERO

    override fun getTickRate(world: WorldView?): Int = 0

    override fun getBlastResistance(): Float = 0f

    override fun getHeight(state: FluidState?, world: BlockView?, pos: BlockPos?): Float = 1f

    override fun getHeight(state: FluidState?): Float = 1f

    override fun toBlockState(state: FluidState): BlockState = state.blockState

    override fun isStill(state: FluidState?): Boolean = true

    override fun getLevel(state: FluidState?): Int = 1

    override fun getShape(state: FluidState?, world: BlockView?, pos: BlockPos?): VoxelShape = VoxelShapes.cuboid(0.0,0.0,0.0,1.0,1.0,1.0)

    companion object{
        val VARIANTS = mutableMapOf<String,FluidVariant>()

        operator fun get(id: String) = VARIANTS[LostArcana.id(id).toString()]

    }
}