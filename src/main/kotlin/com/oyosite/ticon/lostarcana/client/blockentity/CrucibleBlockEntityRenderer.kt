package com.oyosite.ticon.lostarcana.client.blockentity

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.block.entity.CrucibleBlockEntity
import com.oyosite.ticon.lostarcana.client.SodiumCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.renderer.v1.RendererAccess
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

@Suppress("UnstableApiUsage")
@Environment(EnvType.CLIENT)
class CrucibleBlockEntityRenderer(ctx: BlockEntityRendererFactory.Context) : BlockEntityRenderer<CrucibleBlockEntity>{

    init{
        println("Initializing CrucibleBlockEntityRenderer.")
    }

    override fun rendersOutsideBoundingBox(blockEntity: CrucibleBlockEntity?): Boolean = true

    @Suppress("OPT_IN_USAGE")// I don't care if it's unstable. This is just for debugging purposes
    private fun printlnR(message: Any) = GlobalScope.launch(Dispatchers.Default) { println(message) }

    override fun render(
        entity: CrucibleBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {


        //throw Exception("If logging doesn't work, what about crashing?")//nope
        //if(entity.world?.time?.rem(20) == 0L)LostArcana.LOGGER.info("Rendering fluid in crucible.")
        //printlnR("Rendering fluid in crucible.")
        if(entity.fluidContent.isResourceBlank)return

        val fluid = entity.fluidContent.resource
        val fill = entity.fluidContent.amount
        val vc = vertexConsumers.getBuffer(RenderLayer.getTranslucent())
        val sprite = FluidVariantRendering.getSprite(fluid)
        val color = FluidVariantRendering.getColor(fluid, entity.world, entity.pos)
        val r = (color shr 16 and 255) / 256f
        val g = (color shr 8 and 255) / 256f
        val b = (color and 255) / 256f


        //SodiumCompat.markSpriteActive(sprite)

        val fillRatio = (fill.coerceIn(0L..entity.fluidContent.capacity)/entity.fluidContent.capacity.toFloat())
        val topHeight = 0.2f + 0.6f*fillRatio
        val renderer = RendererAccess.INSTANCE.renderer?:return
        val emitter = renderer.meshBuilder().emitter
        emitter.square(Direction.UP, 2/16f, 2/16f, 1 - 2/16f, 1 - 2/16f, 1-topHeight)
        emitter.spriteBake( sprite, MutableQuadView.BAKE_LOCK_UV)
        vc.quad(matrices.peek(), emitter.toBakedQuad(sprite), r, g, b, FULL_LIGHT, OverlayTexture.DEFAULT_UV)
    }


    companion object{
        const val FULL_LIGHT = 0x00F000F0
    }

}