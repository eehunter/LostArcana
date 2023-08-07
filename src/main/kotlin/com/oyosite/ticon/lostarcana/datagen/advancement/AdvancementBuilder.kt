package com.oyosite.ticon.lostarcana.datagen.advancement

import com.oyosite.ticon.lostarcana.LostArcana
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.advancement.criterion.CriterionConditions
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class AdvancementBuilder(block: AdvancementBuilder.()->Unit) {
    var item: ItemStack? = null
    var group: String? = null
    var name: String? = null
    var nameText: Text? = null; get() = field?:Text.translatable("advancement.lostarcana.$group.$name")
    var descText: Text? = null; get() = field?:Text.translatable("advancement.lostarcana.$group.$name.desc")
    var background = Identifier("textures/gui/advancements/backgrounds/adventure.png")
    var frame = AdvancementFrame.TASK

    var showToast = true
    var announceToChat = true
    var hidden = true

    var id: Identifier? = null; get() = field?:LostArcana.id("$group/$name")

    var parent: Any? = null


    val criteria: MutableList<Pair<String, AdvancementCriterion>> = mutableListOf()

    fun criterion(name: String, criterion: AdvancementCriterion) = criteria.add(name to criterion)
    fun criterion(name: String, criterion: CriterionConditions) = criterion(name, AdvancementCriterion(criterion))



    val build get() = Advancement.Builder.create().display(item, nameText, descText, background, frame, showToast, announceToChat, hidden).apply{criteria.forEach { this.criterion(it.key, it.value) }; }.apply{
        when(parent){
            null -> Unit
            is Advancement -> parent(parent as Advancement)
            is Identifier -> parent(parent as Identifier)
        }
    }


}