package com.oyosite.ticon.lostarcana.client.blockentity

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.block.entity.ArcanePedestalBlockEntity
import com.oyosite.ticon.lostarcana.block.entity.InfusionPillarBlockEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.RotationAxis
import software.bernie.geckolib.model.DefaultedBlockGeoModel
import software.bernie.geckolib.renderer.GeoBlockRenderer
import kotlin.math.sin

class ArcanePedestalBlockEntityRenderer(ctx: BlockEntityRendererFactory.Context): GeoBlockRenderer<ArcanePedestalBlockEntity>(MODEL) {
    override fun render(
        blockEntity: BlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        super.render(blockEntity, tickDelta, matrices, vertexConsumers, light, overlay)
        val entity = blockEntity as ArcanePedestalBlockEntity
        if(entity.isEmpty)return
        if(entity.world==null)return
        matrices.push()
        val offset = sin((entity.world!!.time + tickDelta) / 8.0) / 4.0
        matrices.translate(0.5, 1.25 + offset, 0.5)
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.world!!.time + tickDelta) * 4))
        val itemLight = WorldRenderer.getLightmapCoordinates(entity.world, entity.pos.up())
        MinecraftClient.getInstance().itemRenderer.renderItem(entity.stack, ModelTransformationMode.GROUND, itemLight, overlay, matrices, vertexConsumers, entity.world, 0)
        matrices.pop()


    }



    companion object{
        val MODEL = DefaultedBlockGeoModel<ArcanePedestalBlockEntity>(LostArcana.id("arcane_pedestal"))
    }
}