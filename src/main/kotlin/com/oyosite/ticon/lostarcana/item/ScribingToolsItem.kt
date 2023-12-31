package com.oyosite.ticon.lostarcana.item

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.math.MathHelper
import kotlin.math.max
import kotlin.math.round

open class ScribingToolsItem: Item(FabricItemSettings().maxCount(1)) {

    override fun getRecipeRemainder(stack: ItemStack): ItemStack = stack.copyWithCount(1).apply { ink-=1 }

    override fun isItemBarVisible(stack: ItemStack): Boolean = stack.getSubNbt("scribing_tools")?.run{ if(contains("ink"))getInt("ink") < stack.maxInk else null }?:false
    override fun getItemBarStep(stack: ItemStack): Int = round(stack.ink * 13.0 / stack.maxInk).toInt()

    override fun getItemBarColor(stack: ItemStack): Int {
        val f = max(0.0, (stack.ink.toDouble() / stack.maxInk)).toFloat()
        return MathHelper.hsvToRgb(f / 3.0f, 1.0f, 1.0f)
    }


    companion object{
        var ItemStack.maxInk get() = getSubNbt("scribing_tools")?.run{ if(contains("max_ink")) getInt("max_ink") else null }?:200
            set(value) {getOrCreateSubNbt("scribing_tools").putInt("max_ink", value)}
        var ItemStack.ink get() = getSubNbt("scribing_tools")?.run { if(contains("ink")) getInt("ink") else null }?:200
            set(value) {getOrCreateSubNbt("scribing_tools").putInt("ink", value)}

        fun ItemStack.drainInk(amt: Int): Boolean{
            if(this.item !is ScribingToolsItem)return false
            if(ink < amt)return false
            ink -= amt
            return true
        }

    }
}