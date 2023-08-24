package com.oyosite.ticon.lostarcana.client.blockentity

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.LostArcanaClient
import com.oyosite.ticon.lostarcana.block.entity.RunicMatrixBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.model.ModelData
import net.minecraft.client.model.ModelPartBuilder
import net.minecraft.client.model.ModelTransform
import net.minecraft.client.model.TexturedModelData
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.screen.PlayerScreenHandler
import org.joml.AxisAngle4f
import org.joml.Quaternionf
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class RunicMatrixBlockEntityRenderer(ctx: BlockEntityRendererFactory.Context): BlockEntityRenderer<RunicMatrixBlockEntity> {


    val modelBase = ctx.getLayerModelPart(LostArcanaClient.runicMatrixEntityModelLayer)
    val cuboids = Array(8){modelBase.getChild("cuboid$it")}

    //var debugFlag = false

    val halfSqrt2 = (sqrt(2.0) / 2.0).toFloat()

    override fun render(
        entity: RunicMatrixBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        val vc = textureId.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout)
        val deltaTime = ((MinecraftClient.getInstance().renderTime-entity.lastRenderTime).toFloat() + tickDelta)
        entity.lastRenderTime = MinecraftClient.getInstance().renderTime
        matrices.push()
        matrices.translate(0.5, 0.5, 0.5)
        if(entity.active){
            if(!MinecraftClient.getInstance().isPaused) {
                if (entity.tilt < 1) entity.tilt = min(entity.tilt + tickDelta / 50f, 1f)
                entity.angle += tickDelta * PI.toFloat() / 180f * entity.tilt
            }
            matrices.multiply(Quaternionf(AxisAngle4f(entity.angle, 0f, 1f, 0f)))
            matrices.multiply(Quaternionf(AxisAngle4f((entity.tilt * PI/4).toFloat(), halfSqrt2, 0f, halfSqrt2)))
        } else if(!MinecraftClient.getInstance().isPaused) if(entity.tilt>0) entity.tilt = max(entity.tilt - tickDelta / 50f, 0f)

        var i = 0
        for(x in arrayOf(-1, 1)) for(y in arrayOf(-1, 1)) for(z in arrayOf(-1, 1)){
            matrices.push()
            matrices.translate(3.75/16*x, 3.75/16*y, 3.75/16*z)
            //if(!debugFlag) println("$i: ${cuboids[i]}")

            cuboids[i++].render(matrices, vc, light, overlay)
            matrices.pop()
        }
        //debugFlag = true


        matrices.pop()
    }


    companion object{
        val textureId = SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, LostArcana.id("block/runic_matrix"))

        val modelData = ModelData()
        val texturedModelData = TexturedModelData.of(modelData, 56, 56)
        init{
            var i = 0
            for(x in arrayOf(-1, 1)) for(y in arrayOf(-1, 1)) for(z in arrayOf(-1, 1)){
                //println("$i: ${28 * (i / 4)}, ${14 * (i % 4)}")
                modelData.root.addChild("cuboid$i", ModelPartBuilder().uv(28*(i/4), 14*(i%4)).cuboid(-3.5f, -3.5f, -3.5f, 7f, 7f, 7f), ModelTransform.NONE)
                i++
            }
        }
    }
}