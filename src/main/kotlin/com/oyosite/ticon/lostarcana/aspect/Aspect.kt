package com.oyosite.ticon.lostarcana.aspect

import com.oyosite.ticon.lostarcana.item.ItemRegistry
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

data class Aspect(val id: Identifier, val color: Int, val components: Pair<Aspect, Aspect>? = null, val texture: Identifier = Identifier("${id.namespace}:textures/aspect/${id.path}.png"), val name: Text = Text.translatable("aspect.${id.namespace}.${id.path}").styled { it.withColor(color) }){


    val crystal: ItemStack get() {
        val otpt = ItemStack(ItemRegistry.VIS_CRYSTAL)
        val vis = otpt.getOrCreateSubNbt("vis")
        vis.putInt("color", color)
        vis.putString("aspect", id.toString())
        return otpt
    }
}
