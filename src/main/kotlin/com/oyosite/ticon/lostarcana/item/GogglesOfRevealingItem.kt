package com.oyosite.ticon.lostarcana.item

import com.google.common.collect.ImmutableMultimap
import com.google.common.collect.Multimap
import com.oyosite.ticon.lostarcana.LostArcana
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents

class GogglesOfRevealingItem: ArmorItem(Material, Type.HELMET, FabricItemSettings().maxCount(1)) {



    override fun getAttributeModifiers(slot: EquipmentSlot): Multimap<EntityAttribute, EntityAttributeModifier> = ImmutableMultimap.builder<EntityAttribute, EntityAttributeModifier>().also{
        if(slot == EquipmentSlot.HEAD)it.put(LostArcana.AURA_VISION, AURA_VISION_MOD)
    }.build()
    companion object{
        val AURA_VISION_MOD = EntityAttributeModifier("thaumometer_vision", 0.5, EntityAttributeModifier.Operation.ADDITION)
    }
    object Material: ArmorMaterial{
        override fun getDurability(type: Type): Int = 500

        override fun getProtection(type: Type?): Int = 2

        override fun getEnchantability(): Int = 25

        override fun getEquipSound(): SoundEvent = SoundEvents.ITEM_ARMOR_EQUIP_LEATHER

        override fun getRepairIngredient(): Ingredient = Ingredient.ofItems(Items.LEATHER)

        override fun getName(): String = "vis_goggles"

        override fun getToughness(): Float = 0f

        override fun getKnockbackResistance(): Float = 0f

    }
}