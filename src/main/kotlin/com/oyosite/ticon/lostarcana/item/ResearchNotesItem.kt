package com.oyosite.ticon.lostarcana.item

import com.oyosite.ticon.lostarcana.research.ResearchCategory
import com.oyosite.ticon.lostarcana.research.ResearchCategoryRegistry
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import kotlin.math.max
import kotlin.math.round

class ResearchNotesItem(val type: ResearchNotesType, settings: FabricItemSettings.()->Unit = {}): Item(FabricItemSettings().apply(settings)) {


    override fun isItemBarVisible(stack: ItemStack): Boolean = !stack.researchComplete
    override fun getItemBarStep(stack: ItemStack): Int = round(stack.researchProgress * 13.0 / stack.maxResearchProgress).toInt()

    override fun getItemBarColor(stack: ItemStack): Int {
        val f = max(0.0, (stack.researchProgress.toDouble() / stack.maxResearchProgress)).toFloat()
        return MathHelper.hsvToRgb(f / 3.0f, 1.0f, 1.0f)
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        val category = stack.researchCategory?.id ?:return
        tooltip+=Text.translatable("research_category.${category.namespace}.${category.path}.name")
    }



    companion object{
        val ItemStack.researchComplete get() = (researchProgress >= maxResearchProgress).also{getOrCreateSubNbt("research").putBoolean("complete", it)}
        var ItemStack.researchProgress get() = getSubNbt("research")?.getInt("progress") ?: 0
        set(value) = getOrCreateSubNbt("research").putInt("progress", value)
        var ItemStack.maxResearchProgress get() = getSubNbt("research")?.run{ if (contains("maxProgress")) getInt("maxProgress") else null }
        ?: 100
        set(value) = getOrCreateSubNbt("research").putInt("maxProgress", value)

        var ItemStack.researchCategory: ResearchCategory? get() = ResearchCategoryRegistry.CATEGORIES[Identifier(getSubNbt("research")?.getString("category")?:"")]
        set(value) = getOrCreateSubNbt("research").putString("category", value?.id?.toString())


    }


}