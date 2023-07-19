package com.oyosite.ticon.lostarcana.block

import com.mojang.brigadier.StringReader
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.particle.DustParticleEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.IntProperty
import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import org.joml.Vector3f

class NitorBlock: Block(FabricBlockSettings.create().luminance(15).collidable(false).allowsSpawning { state, world, pos, type -> false }) {

    val SHAPE = createCuboidShape(5.0, 5.0, 5.0, 11.0, 11.0, 11.0)


    override fun getRaycastShape(state: BlockState?, world: BlockView?, pos: BlockPos?): VoxelShape = SHAPE
    override fun getOutlineShape(state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?): VoxelShape = SHAPE

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) { builder.add(COLOR) }

    override fun getRenderType(state: BlockState?): BlockRenderType = BlockRenderType.INVISIBLE

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random?) {


        //world.spawnParticles(DustParticleEffect(Vector3f(r, g, b), 1.0f),//DustParticleEffect.PARAMETERS_FACTORY.read(ParticleTypes.DUST, StringReader(" ${(color shr 16 and 255)/256f} ${(color shr 8 and 255)/256f} ${(color and 255)/256f} 1.0")),
        //    pos.x+.5, pos.y+.5, pos.z+.5, 1, 0.0, 0.0, 0.0, 0.1)
    }



    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        val color = DyeColor.byId(state[COLOR]).mapColor.color
        val r = (color shr 16 and 255)/256f
        val g = (color shr 8 and 255)/256f
        val b = (color and 255)/256f
        for(i in 0..random.nextBetween(0,6))world.addImportantParticle(DustParticleEffect(Vector3f(r, g, b), 1.0f), pos.x+.5, pos.y+.5, pos.z+.5, 0.0, 0.0, 0.0)
    }


    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val color = ctx.stack.getSubNbt("nitor")?.getString("color")?:return defaultState
        return defaultState.with(COLOR, DyeColor.byName(color, DyeColor.WHITE)?.id?: 0)
    }

    override fun appendTooltip(stack: ItemStack, world: BlockView?, tooltip: MutableList<Text>, options: TooltipContext) {
        val color = stack.getSubNbt("nitor")?.getString("color")?.lowercase()?:return
        val dyeColor = color.let{DyeColor.byName(color, null)}?:return
        tooltip.add(Text.translatable("nitor.color.$color").styled { it.withColor(dyeColor.mapColor.color) })
    }







    companion object{
        @JvmStatic
        val COLOR = IntProperty.of("color", 0, 16)
    }
}