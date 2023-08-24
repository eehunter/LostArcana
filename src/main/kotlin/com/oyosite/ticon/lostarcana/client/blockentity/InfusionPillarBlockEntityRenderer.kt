package com.oyosite.ticon.lostarcana.client.blockentity

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.block.entity.InfusionPillarBlockEntity
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import software.bernie.geckolib.model.DefaultedBlockGeoModel
import software.bernie.geckolib.renderer.GeoBlockRenderer

class InfusionPillarBlockEntityRenderer(): GeoBlockRenderer<InfusionPillarBlockEntity>() {





    companion object{
        val MODEL = DefaultedBlockGeoModel<InfusionPillarBlockEntity>(LostArcana.id("infusion_pillar"))
    }
}