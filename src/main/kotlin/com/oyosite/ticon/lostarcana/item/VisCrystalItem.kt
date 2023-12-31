package com.oyosite.ticon.lostarcana.item

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.aspect.Aspect
import com.oyosite.ticon.lostarcana.aspect.AspectRegistry
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.world.World

//Suppress deprecation because I'm too lazy to implement the correct interface manually.
@Suppress("Deprecation")
class VisCrystalItem: Item(FabricItemSettings()), EssentiaItem {
    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        stack.getSubNbt("vis")?.getString("aspect")?.also{
            AspectRegistry.ASPECTS[LostArcana.id(it)]?.name?.let(tooltip::add)
        }
        super.appendTooltip(stack, world, tooltip, context)

    }

    fun getAspect(stack: ItemStack) = AspectRegistry[LostArcana.id(stack.getSubNbt("vis")?.getString("aspect")?:"null")]

    override fun getAspects(stack: ItemStack): List<Pair<Aspect, Int>>? = (stack.item as VisCrystalItem).getAspect(stack)?.let{ listOf(it to 810) }


}