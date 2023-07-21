package com.oyosite.ticon.lostarcana.block

import com.oyosite.ticon.lostarcana.block.entity.GrowingVisCrystalBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.AbstractBlock.Offsetter
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.state.StateManager
import net.minecraft.state.property.IntProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.BlockView

class GrowingVisCrystalBlock: BlockWithEntity(FabricBlockSettings.create().collidable(false).nonOpaque().offset(visCrystalOffsetter)) {


    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = GrowingVisCrystalBlockEntity(pos, state)

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) { builder.add(CRYSTALS) }

    override fun isTransparent(state: BlockState?, world: BlockView?, pos: BlockPos?): Boolean = true

    override fun isShapeFullCube(state: BlockState?, world: BlockView?, pos: BlockPos?): Boolean = false

    override fun getMaxHorizontalModelOffset(): Float = 7/8f

    companion object{
        val CRYSTALS = IntProperty.of("crystals", 1, 6)

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
            Vec3d(x, 0.0, z)
        }

    }
}