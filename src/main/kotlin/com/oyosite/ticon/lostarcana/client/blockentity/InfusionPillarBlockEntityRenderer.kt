package com.oyosite.ticon.lostarcana.client.blockentity

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.block.InfusionPillar
import com.oyosite.ticon.lostarcana.block.entity.InfusionPillarBlockEntity
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Direction
import software.bernie.geckolib.model.DefaultedBlockGeoModel
import software.bernie.geckolib.renderer.GeoBlockRenderer

class InfusionPillarBlockEntityRenderer(context: BlockEntityRendererFactory.Context): GeoBlockRenderer<InfusionPillarBlockEntity>(MODEL) {

    init {
        //println("InfusionPillar Texture Path: "+MODEL.buildFormattedTexturePath(LostArcana.id("infusion_pillar")))
    }

    override fun getFacing(block: InfusionPillarBlockEntity): Direction = block.cachedState[InfusionPillar.DIRECTION]

    override fun rotateBlock(facing: Direction, poseStack: MatrixStack) {
        super.rotateBlock(facing.opposite, poseStack)
        poseStack.translate(0f, -0.01f, 0f)
    }

    companion object{
        val MODEL = DefaultedBlockGeoModel<InfusionPillarBlockEntity>(LostArcana.id("infusion_pillar"))
    }
}