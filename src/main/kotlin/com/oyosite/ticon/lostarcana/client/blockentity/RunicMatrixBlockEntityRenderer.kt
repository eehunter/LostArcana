package com.oyosite.ticon.lostarcana.client.blockentity

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.LostArcanaClient
import com.oyosite.ticon.lostarcana.block.entity.RunicMatrixBlockEntity
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

class RunicMatrixBlockEntityRenderer(ctx: BlockEntityRendererFactory.Context): BlockEntityRenderer<RunicMatrixBlockEntity> {
    var rotationFactor = 1.0

    val modelBase = ctx.getLayerModelPart(LostArcanaClient.runicMatrixEntityModelLayer)

    override fun render(
        entity: RunicMatrixBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        val vc = textureId.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout)
        matrices.push()
        matrices.translate(0.5, 0.5, 0.5)
        if(entity.active){
            matrices.multiply(Quaternionf(AxisAngle4f((rotationFactor* PI/4).toFloat(), 1f, 0f, 1f)))

        }
        var i = 0
        for(x in arrayOf(-1, 1)) for(y in arrayOf(-1, 1)) for(z in arrayOf(-1, 1)){
            matrices.push()
            matrices.translate(3.75/16*x, 3.75/16*y, 3.75/16*z)
            modelBase.getChild("cuboid${i++}").render(matrices, vc, light, overlay)
            matrices.pop()
        }


        matrices.pop()
    }


    companion object{
        val textureId = SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, LostArcana.id("block/runic_matrix"))

        val modelData = ModelData()
        val texturedModelData = TexturedModelData.of(modelData, 56, 56)
        init{
            var i = 0
            for(x in arrayOf(-1, 1)) for(y in arrayOf(-1, 1)) for(z in arrayOf(-1, 1)){
                modelData.root.addChild("cuboid$i", ModelPartBuilder().cuboid(-3.5f, -3.5f, -3.5f, 7f, 7f, 7f).uv(28*(i/4), 14*(i%4)), ModelTransform.NONE)
                i++
            }
        }
    }
}