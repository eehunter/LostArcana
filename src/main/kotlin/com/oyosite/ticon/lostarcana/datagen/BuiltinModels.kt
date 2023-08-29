package com.oyosite.ticon.lostarcana.datagen

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.mixin.MinecraftClientAccessor
import net.minecraft.client.MinecraftClient
import net.minecraft.client.model.Model
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.BakedQuad
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random

//val RESEARCH_NOTES_ENTITY_MODEL_LAYER = EntityModelLayerRegistry.registerModelLayer(EntityModelLayer(LostArcana.id("research_notes"), "main"))
object ResearchNotesEntityModel {

    val base: BakedModel by lazy { MinecraftClient.getInstance().itemRenderer.models.modelManager.getModel(ModelIdentifier(LostArcana.id("item/research_notes"), "inventory")) }




    private val itemColors = (MinecraftClient.getInstance() as MinecraftClientAccessor).itemColors


    fun render(
        stack: ItemStack,
        matrices: MatrixStack,
        vertices: VertexConsumer,
        light: Int,
        overlay: Int
    ) {
        matrices.push()
        base.render(stack, light, overlay, matrices, vertices)
        matrices.translate(0.25, 0.25, 0.0)
        matrices.scale(0.5f, 0.5f, 0.5f)

        //val entry = matrices.peek()

        //vertices.quad(entry, )
        matrices.pop()
    }

    private fun BakedModel.render(stack: ItemStack, light: Int, overlay: Int, matrices: MatrixStack, vertices: VertexConsumer){
        val random = Random.create()
        for (dir in Direction.values()){
            random.setSeed(42L)
            renderBakedQuads(matrices, vertices, getQuads(null, dir, random), stack, light, overlay)
        }
        random.setSeed(42L)
        renderBakedQuads(matrices, vertices, getQuads(null, null, random), stack, light, overlay)
    }

    private fun renderBakedQuads(matrices: MatrixStack, vertices: VertexConsumer, quads: List<BakedQuad>, stack: ItemStack, light: Int, overlay: Int){
        val notEmpty = !stack.isEmpty
        val entry = matrices.peek()
        for(quad in quads){
            var i = -1
            if(notEmpty && quad.hasColor()) i = itemColors.getColor(stack, quad.colorIndex)
            val r = (i shr 16 and 0xFF).toFloat() / 255.0f
            val g = (i shr 8 and 0xFF).toFloat() / 255.0f
            val b = (i and 0xFF).toFloat() / 255.0f
            vertices.quad(entry, quad, r, g, b, light, overlay)
        }

    }

}