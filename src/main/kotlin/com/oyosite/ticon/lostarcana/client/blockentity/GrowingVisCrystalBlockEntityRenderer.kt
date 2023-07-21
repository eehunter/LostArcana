package com.oyosite.ticon.lostarcana.client.blockentity

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.block.GrowingVisCrystalBlock
import com.oyosite.ticon.lostarcana.block.entity.GrowingVisCrystalBlockEntity
import net.fabricmc.fabric.api.renderer.v1.RendererAccess
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Direction.*

class GrowingVisCrystalBlockEntityRenderer(ctx: BlockEntityRendererFactory.Context) : BlockEntityRenderer<GrowingVisCrystalBlockEntity> {
    override fun render(
        be: GrowingVisCrystalBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        //val world = be.world?:return
        val renderer = RendererAccess.INSTANCE.renderer?:return
        val crystals = be.crystals//world.getBlockState(be.pos)[GrowingVisCrystalBlock.CRYSTALS]
        val vc = vertexConsumers.getBuffer(RenderLayer.getSolid())
        val topSprite = SpriteFinder.get(crystalSpriteAtlasTexture).find(0f, 0f)
        val sprite = SpriteFinder.get(crystalSpriteAtlasTexture).find(0f,1/3f)
        
        val color = be.color
        val r = (color shr 16 and 255) / 256f
        val g = (color shr 8 and 255) / 256f
        val b = (color and 255) / 256f

        matrices.push()
        for (i in 1..crystals){
            val offset = be.getOffset(i)
            val emitter = renderer.meshBuilder().emitter

            for(dir in Direction.values()){
                when(dir){
                    DOWN -> continue
                    UP -> emitter.square(dir, 0.5f+offset.x.toFloat(), 0.5f+offset.z.toFloat(), 0.5f+offset.x.toFloat()+1/8f, 0.5f+offset.z.toFloat()+1/8f, 1-1/8f) // Done
                    NORTH -> emitter.square(dir, 0.5f-offset.x.toFloat()-1/8f, 0f, 0.5f-offset.x.toFloat(), 1/8f, 0.5f-offset.z.toFloat()-1/8f)
                    EAST -> emitter.square(dir, 0.5f+offset.z.toFloat(), 0f, 0.5f+offset.z.toFloat()+1/8f, 1/8f, 0.5f-offset.x.toFloat()-1/8f) // Done
                    SOUTH -> emitter.square(dir, 0.5f+offset.x.toFloat(), 0f, 0.5f+offset.x.toFloat()+1/8f, 1/8f, 0.5f+offset.z.toFloat())
                    WEST -> emitter.square(dir, 0.5f-offset.z.toFloat()-1/8f, 0f, 0.5f-offset.z.toFloat(), 1/8f, 0.5f+offset.x.toFloat()) // Done
                    else -> continue
                }
                /*
                    UP -> emitter.square(dir, 0.5f+offset.x.toFloat()-1/16f, 0.5f+offset.z.toFloat()-1/16f, 0.5f+offset.x.toFloat()+1/16f, 0.5f+offset.z.toFloat()+1/16f, 1-1/8f) // Done
                    NORTH -> emitter.square(dir, 0.5f-offset.x.toFloat()-1/16f, 0f, 0.5f-offset.x.toFloat()-1/16f, 1/8f, 0.5f-offset.z.toFloat()-1/16f)
                    EAST -> emitter.square(dir, 0.5f+offset.z.toFloat()+1/16f, 0f, 0.5f+offset.z.toFloat()+1/16f, 1/8f, 0.5f-offset.x.toFloat()-1/16f) // Done
                    SOUTH -> emitter.square(dir, 0.5f+offset.x.toFloat()+1/16f, 0f, 0.5f+offset.x.toFloat()+1/16f, 1/8f, 0.5f+offset.z.toFloat()+1/16f)
                    WEST -> emitter.square(dir, 0.5f-offset.z.toFloat()-1/16f, 0f, 0.5f-offset.z.toFloat()-1/16f, 1/8f, 0.5f+offset.x.toFloat()+1/16f) // Done*/

                //emitter.color(be.color, be.color, be.color, be.color)
                emitter.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV)
                vc.quad(matrices.peek(), emitter.toBakedQuad(if(dir == UP)topSprite else sprite), r/256f, g/256f, b/256f, light, overlay)
            }



        }
        matrices.pop()

    }

    companion object{
        val crystalSpriteAtlasTexture = SpriteAtlasTexture(LostArcana.id("textures/block/growing_vis_crystal.png"))
    }
}