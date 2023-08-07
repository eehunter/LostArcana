package com.oyosite.ticon.lostarcana

import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.block.NitorBlock.Companion.COLOR
import com.oyosite.ticon.lostarcana.block.entity.GrowingVisCrystalBlockEntity
import com.oyosite.ticon.lostarcana.client.ArcaneWorkbenchScreen
import com.oyosite.ticon.lostarcana.client.blockentity.CrucibleBlockEntityRenderer
import com.oyosite.ticon.lostarcana.client.blockentity.GrowingVisCrystalBlockEntityRenderer
import com.oyosite.ticon.lostarcana.client.blockentity.RunicMatrixBlockEntityRenderer
import com.oyosite.ticon.lostarcana.client.onHudRender
import com.oyosite.ticon.lostarcana.item.ItemRegistry
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.block.BlockState
import net.minecraft.block.MapColor.*
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.item.ItemStack
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView

@Environment(EnvType.CLIENT)
object LostArcanaClient: ClientModInitializer {
    private var cber: CrucibleBlockEntityRenderer? = null

    val visCrystalEntityModelLayer = EntityModelLayer(LostArcana.id("vis_crystal"), "main")
    val runicMatrixEntityModelLayer = EntityModelLayer(LostArcana.id("runic_matrix"), "main")

    override fun onInitializeClient() {
        ColorProviderRegistry.ITEM.register(::getVisColorTint, ItemRegistry.VIS_CRYSTAL, BlockRegistry.GROWING_VIS_CRYSTAL)
        ColorProviderRegistry.ITEM.register(::getThaumometerTint, ItemRegistry.THAUMOMETER)
        ColorProviderRegistry.ITEM.register(::getNitorColor, BlockRegistry.NITOR)
        HandledScreens.register(LostArcana.ARCANE_WORKBENCH_SCREEN_HANDLER, ::ArcaneWorkbenchScreen)
        BlockEntityRendererFactories.register(LostArcana.CRUCIBLE_BLOCK_ENTITY){cber = CrucibleBlockEntityRenderer(it); cber}
        BlockEntityRendererFactories.register(LostArcana.VIS_CRYSTAL_BLOCK_ENTITY, ::GrowingVisCrystalBlockEntityRenderer)
        BlockEntityRendererFactories.register(LostArcana.RUNIC_MATRIX_BLOCK_ENTITY, ::RunicMatrixBlockEntityRenderer)
        //BlockEntityrenderer
        //BlockEntityRendererRegistry.register(LostArcana.CRUCIBLE_BLOCK_ENTITY, ::CrucibleBlockEntityRenderer)



        HudRenderCallback.EVENT.register(::onHudRender)
        EntityModelLayerRegistry.registerModelLayer(visCrystalEntityModelLayer) { GrowingVisCrystalBlockEntityRenderer.texturedModelData }
        EntityModelLayerRegistry.registerModelLayer(runicMatrixEntityModelLayer) { RunicMatrixBlockEntityRenderer.texturedModelData }
    }

    private fun getVisColorTint(stack: ItemStack, tintIndex: Int): Int{
        //if(stack.getSubNbt("vis")?.getList("tintIndicies",3)?.none { (it as NbtInt).intValue() == tintIndex } == true) return 0xFFFFFF
        return stack.getSubNbt("vis")?.getInt("color")?:0xFFFFFF
    }
    //val colors = listOf(WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY, LIGHT_GRAY, CYAN, PURPLE, BLUE, BROWN, )
    private fun getNitorColor(stack: ItemStack, tintIndex: Int): Int = if(tintIndex==0)stack.getSubNbt("nitor")?.getString("color").let{DyeColor.byName(it, null)}?.mapColor?.color?:0xffffff else 0xffffff


    private fun getThaumometerTint(stack: ItemStack, tintIndex: Int) = if(tintIndex==1) 0xdb00ff else 0xffffff
}