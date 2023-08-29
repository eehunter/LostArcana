package com.oyosite.ticon.lostarcana.item

import com.oyosite.ticon.lostarcana.research.ResearchCategory
import com.oyosite.ticon.lostarcana.research.ResearchCategoryRegistry
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.core.animation.AnimatableManager
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.math.round

class ResearchNotesItem(val type: ResearchNotesType, settings: FabricItemSettings.()->Unit = {}): Item(FabricItemSettings().apply(settings)) {

    override fun isItemBarVisible(stack: ItemStack): Boolean = !stack.researchComplete
    override fun getItemBarStep(stack: ItemStack): Int = round(13.0 - stack.researchProgress * 13.0 / stack.maxResearchProgress).toInt()




    companion object{
        val ItemStack.researchComplete get() = researchProgress >= maxResearchProgress
        var ItemStack.researchProgress get() = getSubNbt("research")?.getInt("progress") ?: 0
        set(value) = getOrCreateSubNbt("research").putInt("progress", value)
        var ItemStack.maxResearchProgress get() = getSubNbt("research")?.run{ if (contains("maxProgress")) getInt("maxProgress") else null }
        ?: 100
        set(value) = getOrCreateSubNbt("research").putInt("maxProgress", value)

        var ItemStack.researchCategory: ResearchCategory? get() = ResearchCategoryRegistry.CATEGORIES[Identifier(getSubNbt("research")?.getString("category")?:"")]
        set(value) = getOrCreateSubNbt("research").putString("category", value?.id?.toString())


    }


}