package com.oyosite.ticon.lostarcana

import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.block.NitorBlock.Companion.COLOR
import com.oyosite.ticon.lostarcana.client.ArcaneWorkbenchScreen
import com.oyosite.ticon.lostarcana.client.blockentity.CrucibleBlockEntityRenderer
import com.oyosite.ticon.lostarcana.item.ItemRegistry
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.MapColor.*
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.item.ItemStack
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView

@Environment(EnvType.CLIENT)
object LostArcanaClient: ClientModInitializer {
    private var cber: CrucibleBlockEntityRenderer? = null
    override fun onInitializeClient() {
        ColorProviderRegistry.ITEM.register(::getVisColorTint, ItemRegistry.VIS_CRYSTAL)
        ColorProviderRegistry.BLOCK.register(::getNitorColor, BlockRegistry.NITOR)
        HandledScreens.register(LostArcana.ARCANE_WORKBENCH_SCREEN_HANDLER, ::ArcaneWorkbenchScreen)
        BlockEntityRendererFactories.register(LostArcana.CRUCIBLE_BLOCK_ENTITY){cber = CrucibleBlockEntityRenderer(it); cber}
        //BlockEntityrenderer
        //BlockEntityRendererRegistry.register(LostArcana.CRUCIBLE_BLOCK_ENTITY, ::CrucibleBlockEntityRenderer)
    }

    private fun getVisColorTint(stack: ItemStack, tintIndex: Int): Int{
        //if(stack.getSubNbt("vis")?.getList("tintIndicies",3)?.none { (it as NbtInt).intValue() == tintIndex } == true) return 0xFFFFFF
        return stack.getSubNbt("vis")?.getInt("color")?:0xFFFFFF
    }
    //val colors = listOf(WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY, LIGHT_GRAY, CYAN, PURPLE, BLUE, BROWN, )
    private fun getNitorColor(state: BlockState, rv: BlockRenderView?, pos: BlockPos?, tintIndex: Int): Int = DyeColor.byId(state[COLOR]).mapColor.color
}