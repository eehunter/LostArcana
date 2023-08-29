package com.oyosite.ticon.lostarcana.client.item

import com.oyosite.ticon.lostarcana.LostArcanaClient
import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.block.InfusionPillar
import com.oyosite.ticon.lostarcana.block.entity.InfusionPillarBlockEntity
import com.oyosite.ticon.lostarcana.client.blockentity.InfusionPillarBlockEntityRenderer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import org.joml.AxisAngle4f
import org.joml.Quaternionf
import kotlin.math.PI
import kotlin.math.sqrt

@Environment(EnvType.CLIENT)
object InfusionPillarItemRenderer: BuiltinItemRendererRegistry.DynamicItemRenderer {

    val dummyBlockEntity = InfusionPillarBlockEntity(BlockPos(0,0,0), BlockRegistry.INFUSION_PILLAR.defaultState.with(InfusionPillar.DIRECTION, Direction.SOUTH))
    val ipber: InfusionPillarBlockEntityRenderer get() = LostArcanaClient.ipber!!

    val halfSqrt2 = (sqrt(2.0) / 2.0).toFloat()

    override fun render(
        stack: ItemStack,
        mode: ModelTransformationMode,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        matrices.scale(0.4f, 0.4f, 0.4f)
        matrices.translate(0.5f, 0.5f, 0.5f)
        matrices.multiply(Quaternionf(AxisAngle4f((PI/4).toFloat(), 0f, 1f, 0f)))
        matrices.multiply(Quaternionf(AxisAngle4f((PI/4).toFloat(), halfSqrt2, 0f, halfSqrt2)))
        ipber.render(dummyBlockEntity, 0f, matrices, vertexConsumers, light, overlay)
    }

}