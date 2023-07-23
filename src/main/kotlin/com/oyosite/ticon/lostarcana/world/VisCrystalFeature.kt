package com.oyosite.ticon.lostarcana.world

import com.mojang.serialization.Codec
import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.block.GrowingVisCrystalBlock
import net.minecraft.fluid.Fluids
import net.minecraft.util.math.BlockPos
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.FeatureContext
import java.util.Properties

class VisCrystalFeature() : Feature<DefaultFeatureConfig>(DefaultFeatureConfig.CODEC) {
    override fun generate(context: FeatureContext<DefaultFeatureConfig>): Boolean {
        val world = context.world
        val origin = context.origin
        val random = context.random
        val config = context.config

        val positions = mutableListOf<BlockPos>()

        for(y in world.bottomY until world.height){
            val pos = BlockPos(origin.x, y, origin.z)
            if(world.getBlockState(pos).isReplaceable && world.getBlockState(pos.down()).isIn(LostArcana.VALID_CRYSTAL_GROWTH_BASES)) positions.add(pos)
        }

        if(positions.isNotEmpty()) {
            val pos = positions[random.nextBetweenExclusive(0, positions.size)]
            world.setBlockState(pos, BlockRegistry.GROWING_VIS_CRYSTAL.defaultState.with(GrowingVisCrystalBlock.CRYSTALS, random.nextBetween(1,6)).with(GrowingVisCrystalBlock.WATERLOGGED, world.getFluidState(pos).fluid == Fluids.WATER), 0x10)
            return true
        }

        return false
    }
}