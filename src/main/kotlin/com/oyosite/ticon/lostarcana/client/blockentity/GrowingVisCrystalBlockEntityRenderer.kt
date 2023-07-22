package com.oyosite.ticon.lostarcana.client.blockentity

import com.mojang.blaze3d.platform.GlStateManager
import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.LostArcanaClient
import com.oyosite.ticon.lostarcana.block.GrowingVisCrystalBlock
import com.oyosite.ticon.lostarcana.block.entity.GrowingVisCrystalBlockEntity
import net.fabricmc.fabric.api.renderer.v1.RendererAccess
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder
import net.minecraft.client.MinecraftClient
import net.minecraft.client.model.ModelData
import net.minecraft.client.model.ModelPartBuilder
import net.minecraft.client.model.ModelTransform
import net.minecraft.client.model.TexturedModelData
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Direction.*

class GrowingVisCrystalBlockEntityRenderer(ctx: BlockEntityRendererFactory.Context) : BlockEntityRenderer<GrowingVisCrystalBlockEntity> {

    val modelBase = ctx.getLayerModelPart(LostArcanaClient.visCrystalEntityModelLayer)
    val crystal2pix = modelBase.getChild("2pix")
    val crystal3pix = modelBase.getChild("3pix")
    val crystal4pix = modelBase.getChild("4pix")


    override fun render(
        be: GrowingVisCrystalBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        //val world = be.world?:return
        //val renderer = RendererAccess.INSTANCE.renderer?:return
        val crystals = be.crystals//world.getBlockState(be.pos)[GrowingVisCrystalBlock.CRYSTALS]

        val vc = textureId.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout)//vertexConsumers.getBuffer(RenderLayer.getSolid())
        //val atlas = MinecraftClient.getInstance().bakedModelManager.getAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)
        //val topSprite = atlas.getSprite(Identifier("block/white_concrete"))//LostArcana.id("block/growing_vis_crystal"))//SpriteFinder.get(at).//find(0f, 0f)
        //val sprite = atlas.getSprite(Identifier("block/white_concrete"))//LostArcana.id("block/growing_vis_crystal"))//SpriteFinder.get(crystalSpriteAtlasTexture).find(0f,1/3f)


        val color = be.color
        val r = (color shr 16 and 255) / 256f
        val g = (color shr 8 and 255) / 256f
        val b = (color and 255) / 256f

        for (i in 1..crystals){

            matrices.push()
            val offset = be.getOffset(i)
            //val emitter = renderer.meshBuilder().emitter

            matrices.translate(0.5+offset.x, offset.y, 0.5+offset.z)

            when(offset.x.toBits()%3){
                0L -> crystal2pix
                1L -> crystal3pix
                else -> crystal4pix
            }.render(matrices, vc, light, overlay, r, g, b, 1f)

            /*for(dir in Direction.values()){
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
                emitter.color(color, color, color, color)
                //emitter.spriteColor(0, -1, -1, -1, -1)
                vc.quad(matrices.peek(), emitter.toBakedQuad(if(dir == UP)topSprite else sprite), r/256f, g/256f, b/256f, light, overlay)
            }*/


            matrices.pop()



        }

    }

    companion object{
        val crystalSpriteAtlasTexture = SpriteAtlasTexture(LostArcana.id("textures/block/growing_vis_crystal.png"))

        val textureId = SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, LostArcana.id("block/growing_vis_crystal"))

        val modelData = ModelData()
        val texturedModelData = TexturedModelData.of(modelData, 16, 16)

        init {
            modelData.root.addChild("2pix", ModelPartBuilder().cuboid(-1f,0f,-1f,2f,2f,2f), ModelTransform.NONE)
            modelData.root.addChild("3pix", ModelPartBuilder().cuboid(-1f,0f,-1f,2f,3f,2f), ModelTransform.NONE)
            modelData.root.addChild("4pix", ModelPartBuilder().cuboid(-1f,0f,-1f,2f,4f,2f), ModelTransform.NONE)

        }
    }
}