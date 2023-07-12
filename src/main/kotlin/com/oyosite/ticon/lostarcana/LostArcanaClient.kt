package com.oyosite.ticon.lostarcana

import com.oyosite.ticon.lostarcana.client.ArcaneWorkbenchScreen
import com.oyosite.ticon.lostarcana.item.ItemRegistry
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtInt
import net.minecraft.nbt.NbtTypes

object LostArcanaClient: ClientModInitializer {
    override fun onInitializeClient() {
        ColorProviderRegistry.ITEM.register(::getVisColorTint, ItemRegistry.VIS_CRYSTAL)
        HandledScreens.register(LostArcana.ARCANE_WORKBENCH_SCREEN_HANDLER, ::ArcaneWorkbenchScreen)
    }

    private fun getVisColorTint(stack: ItemStack, tintIndex: Int): Int{
        //if(stack.getSubNbt("vis")?.getList("tintIndicies",3)?.none { (it as NbtInt).intValue() == tintIndex } == true) return 0xFFFFFF
        return stack.getSubNbt("vis")?.getInt("color")?:0xFFFFFF
    }
}