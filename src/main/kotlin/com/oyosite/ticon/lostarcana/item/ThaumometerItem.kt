package com.oyosite.ticon.lostarcana.item

import com.google.common.collect.ImmutableMultimap
import com.google.common.collect.Multimap
import com.oyosite.ticon.lostarcana.LostArcana
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.EquipmentSlot.*
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.item.Item

class ThaumometerItem: Item(FabricItemSettings().maxCount(1)) {

    override fun getAttributeModifiers(slot: EquipmentSlot): Multimap<EntityAttribute, EntityAttributeModifier> = ImmutableMultimap.builder<EntityAttribute, EntityAttributeModifier>().also {
        if(slot in listOf(MAINHAND, OFFHAND)) it.put(LostArcana.AURA_VISION, AURA_VISION_MOD)
    }.build()

    companion object{
        val AURA_VISION_MOD = EntityAttributeModifier("thaumometer_vision", 1.0, EntityAttributeModifier.Operation.ADDITION)
    }
}