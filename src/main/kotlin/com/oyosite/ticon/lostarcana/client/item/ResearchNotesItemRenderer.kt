package com.oyosite.ticon.lostarcana.client.item

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.datagen.ResearchNotesEntityModel
import com.oyosite.ticon.lostarcana.datagen.ResearchNotesEntityModel.render
import com.oyosite.ticon.lostarcana.item.ResearchNotesItem.Companion.researchCategory
import com.oyosite.ticon.lostarcana.mixin.MinecraftClientAccessor
import net.fabricmc.fabric.api.client.model.BakedModelManagerHelper
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.BakedQuad
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random

object ResearchNotesItemRenderer: BuiltinItemRendererRegistry.DynamicItemRenderer {
    val base: BakedModel by lazy { MinecraftClient.getInstance().itemRenderer.models.modelManager.let{BakedModelManagerHelper.getModel(it, LostArcana.id("item/research_notes"))!! }
        //.getModel(ModelIdentifier(LostArcana.id("item/research_notes"), "inventory"))
    }
    val blank: BakedModel by lazy { MinecraftClient.getInstance().itemRenderer.models.modelManager.let{BakedModelManagerHelper.getModel(it, LostArcana.id("item/research_notes/blank"))!! }
        //.getModel(ModelIdentifier(LostArcana.id("item/research_notes/blank"), "inventory"))
    }

    val MODEL_MAP = mutableMapOf<Identifier, BakedModel>()

    val ItemStack.core get() = MODEL_MAP.getOrDefault(researchCategory?.id, blank)


    override fun render(
        stack: ItemStack,
        mode: ModelTransformationMode,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        matrices.push()
        val vertices = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE))
        base.render(stack, light, overlay, matrices, vertices)
        matrices.translate(0.25, 0.25, 0.0)
        matrices.scale(0.5f, 0.5f, 1f)
        val vertices2 = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE))

        stack.core.render(stack, light, overlay, matrices, vertices2)

        matrices.pop()
    }

    private val itemColors by lazy { (MinecraftClient.getInstance() as MinecraftClientAccessor).itemColors }

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